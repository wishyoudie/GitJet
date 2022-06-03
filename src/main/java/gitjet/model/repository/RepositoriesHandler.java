package gitjet.model.repository;

import gitjet.WindowsUtils;
import gitjet.controller.ProgressController;
import gitjet.model.DataWriter;
import gitjet.model.Errors;
import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.AnalyzePom;
import gitjet.model.collectinfo.CommitsHistory;
import javafx.application.Platform;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.*;
import java.util.*;

import static gitjet.Utils.getSetting;
import static gitjet.model.repository.Repository.getAuthorFromLink;
import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.CheckReadme.isReadmeInProject;
import static gitjet.model.collectinfo.CheckTests.getNumberOfLinesInTests;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;
import static gitjet.model.repository.Repository.getNameFromLink;

/**
 * Retrieve data from GitHub and turn it into Repo instances class.
 */
public class RepositoriesHandler {

    /**
     * Initialize already collected data from storage.
     *
     * @param fileName A file which contains result of handling.
     * @return List of data translated into Repo instances.
     */
    public List<Repository> readData(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<Repository> result = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                result.add(new Repository(line));
            }
            return result;
        } catch (IOException | NullPointerException e) {
            throw new IOException(Errors.DATA_ERROR.getMessage());
        }
    }

    /**
     * Check if repository with given name has already been handled. A repository has already been handled
     * if the main database file contains its data.
     *
     * @param name A name of repository.
     * @return True if this repository has already been handled.
     */
    public boolean alreadyHandled(String name) throws IOException {
        List<String> scannedNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Objects.requireNonNull(getSetting("storage"))))) {
            String line;
            while ((line = br.readLine()) != null) {
                scannedNames.add(Arrays.asList(line.split(" ")).get(0));
            }
            return scannedNames.contains(name);
        } catch (IOException e) {
            throw new IOException(Errors.DATA_ERROR.getMessage());
        }
    }

    /**
     * Update repository by its URL. Updates information on repository without changing its position if it has
     * already been handled, handles it otherwise.
     *
     * @param link URL to repository on GitHub.
     * @return True if and only if successful.
     */
    public boolean update(String link) throws IOException, GitCloningException {
        String name = getNameFromLink(link);
        if (alreadyHandled(name)) {
            StringBuilder lineBuffer = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(Objects.requireNonNull(getSetting("storage"))));
            String line;
            while ((line = br.readLine()) != null) {
                String lineName = Arrays.asList(line.split(" ")).get(0);
                if (Objects.equals(lineName, name)) {
                    lineBuffer.append(handle(link));
                } else {
                    lineBuffer.append(line);
                }
                lineBuffer.append(System.lineSeparator());
            }
            br.close();
            DataWriter.getInstance().write(lineBuffer.toString(), false);
        } else {
            DataWriter.getInstance().write(handle(link).toString(), true);
        }
        return true;
    }

    /**
     * Handle link to repository. Turns data into Repo.
     *
     * @param link URL to repository on GitHub.
     * @return Repo instance of provided repository.
     */
    public Repository handle(String link) throws GitCloningException, IOException {
        File clone = null;

        try {
            String repoName = getNameFromLink(link);
            clone = runCloning(link, repoName);
            int numberOfLinesInTests = getNumberOfLinesInTests(clone);
            CommitsHistory commitsHistory = new CommitsHistory(clone);
            AnalyzePom analyzePom = new AnalyzePom();
            return new Repository(repoName,
                    getAuthorFromLink(link),
                    commitsHistory.getNumberOfContributors(),
                    commitsHistory.getNumberOfCommits(),
                    getAmountOfLines(clone) - numberOfLinesInTests,
                    (numberOfLinesInTests != 0),
                    numberOfLinesInTests,
                    isReadmeInProject(clone),
                    analyzePom.getDependencies(clone));
        } catch (GitCloningException | IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            deleteClone(clone);
        }
    }

    /**
     * Search Maven repositories on GitHub and handle them.
     *
     * @param requiredNumber A number of repositories to be found.
     * @return True if and only if successful.
     */
    public boolean searchRepos(int requiredNumber) throws GitCloningException, IOException {
        RepositoryService repositoryService = new RepositoryService();
        int page = 1;
        int counter = 0;
        try {
            while (counter < requiredNumber) {
                List<SearchRepository> repos = repositoryService.searchRepositories("size:>0", "java", page);
                AnalyzePom analyzePom = new AnalyzePom();
                for (SearchRepository repo : repos) {
                    String link = "https://github.com/" + repo.toString();
                    System.out.println("Checking " + link);
                    if (!alreadyHandled(getNameFromLink(link)) && analyzePom.isMavenRepository(link)) {
                        Repository result = handle(link);
                        if (!Objects.equals(result, null)) {
                            counter++;
                        }
                        result.addToStorage();
                        if (counter == requiredNumber) {
                            return true;
                        }
                    }
                }
                page++;
            }
        } catch (IOException | GitCloningException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Gather information on dependencies used in given repositories
     *
     * @param repositories List of repositories.
     * @return A map-like list, keys are unique dependency names, values are numbers of dependency usages.
     */
    public List<Map.Entry<String, Integer>> catalogDependencies(List<Repository> repositories) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        List<String> dependenciesList = new ArrayList<>();
        Set<String> dependenciesSet = new HashSet<>();
        for (Repository repository : repositories) {
            Set<String> dependencies = repository.getMavenDependencies();
            dependenciesSet.addAll(dependencies);
            dependenciesList.addAll(dependencies);
        }
        for (String dependency : dependenciesSet) {
            result.add(Map.entry(dependency, Collections.frequency(dependenciesList, dependency)));
        }
        result.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return result;
    }

    /**
     * Get default branch of a remote repository.
     *
     * @param link URL to repository.
     * @return Name of default branch.
     * @throws IOException Throws if errors occurred while getting default branch.
     */
    public String getDefaultBranch(String link) throws IOException {
        try {
            GitHub gitHub = new GitHubBuilder().withOAuthToken(getSetting("token")).build();
            GHRepository ghRepository = gitHub.getRepository(getAuthorFromLink(link) + "/" + getNameFromLink(link));
            return ghRepository.getDefaultBranch();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(Errors.TOKEN_ERROR.getMessage());
        }
    }

    /**
     * Structure summary into list of 'repositories' to pass to summary table.
     *
     * @param repositories List of repositories to summarize.
     * @return Mean and Total special 'repositories'.
     */
    public List<RepositoryEvaluation> calculateSummary(List<Repository> repositories) {
        List<RepositoryEvaluation> result = new ArrayList<>();
        result.add(new MeanRepository(repositories));
        result.add(new TotalRepository(repositories));
        return result;
    }

    /**
     * Creates a new thread to update repository provided by link.
     *
     * @param link URL to a repository.
     */
    public void runUpdatingThread(String link) {
        String name = Repository.getNameFromLink(link);
        new Thread(() -> {
            ProgressController progressController = new ProgressController(String.format("Handling %s", name));
            try {
                Platform.runLater(() -> WindowsUtils.createProgressWindow(progressController));
                boolean flag = update(link);
                if (!flag) {
                    Platform.runLater(() -> WindowsUtils.createErrorWindow(Errors.HANDLE_ERROR.getMessage() + name));
                }
                Platform.runLater(progressController::close);
            } catch (IOException | GitCloningException e) {
                e.printStackTrace();
                WindowsUtils.createErrorWindow(e.getMessage());
            }
        }).start();
    }

    /**
     * Creates a new thread to search for a number of repositories and handle them.
     *
     * @param requiredNumber A number of repositories to be found.
     */
    public void runSearchingThread(int requiredNumber) {
        new Thread(() -> {
            ProgressController progressController = new ProgressController(String.format("Searching for %d %s", requiredNumber, requiredNumber == 1 ? "repository" : "repositories"));
            try {
                Platform.runLater(() -> WindowsUtils.createProgressWindow(progressController));
                if (!searchRepos(requiredNumber)) {
                    Platform.runLater(() -> WindowsUtils.createErrorWindow(Errors.SEARCH_ERROR.getMessage()));
                }
                Platform.runLater(progressController::close);
            } catch (IOException | GitCloningException e) {
                e.printStackTrace();
                WindowsUtils.createErrorWindow(e.getMessage());
            }
        }).start();
    }
}