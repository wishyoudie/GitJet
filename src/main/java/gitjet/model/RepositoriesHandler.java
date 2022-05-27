package gitjet.model;

import gitjet.Utils;
import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.AnalyzePom;
import gitjet.model.collectinfo.CommitsHistory;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.*;
import java.util.*;

import static gitjet.model.Repository.getAuthorFromLink;
import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.CheckReadme.isReadmeInProject;
import static gitjet.model.collectinfo.CheckTests.getNumberOfLinesInTests;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;
import static gitjet.model.Repository.getNameFromLink;

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
        } catch (IOException e) {
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
        try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
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
     */
    public void update(String link) throws IOException, GitCloningException {
//        ExecutorService executor = Executors.newFixedThreadPool(1);
        String name = getNameFromLink(link);
        try (FileWriter writer = new FileWriter("data.dat", true)) {
            if (alreadyHandled(name)) {
                List<Repository> data = readData("data.dat");
                Utils.cleanFile("data.dat");
                for (Repository repository : data) {
                    if (Objects.equals(repository.getName(), name)) {
                        writer.append(handle(link).toString()).append(System.lineSeparator());
                    } else {
                        writer.append(repository.toString()).append(System.lineSeparator());
                    }
                }
            } else {
                writer.append(handle(link).toString()).append(System.lineSeparator());
            }
        }
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
     */
    public void searchRepos(int requiredNumber) throws GitCloningException, IOException {
//        ExecutorService executor = Executors.newFixedThreadPool(1);
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
                            return;
                        }
                    }
                }

                page++;
            }
        } catch (IOException | GitCloningException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Handle multiple URLs taken from a text file.
     *
     * @param file A file which contains URLs to repositories on GitHub.
     */
    public void handleLinksFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            AnalyzePom analyzePom = new AnalyzePom();
            while ((line = reader.readLine()) != null) {
                if (analyzePom.isMavenRepository(line)) {
                    update(line);
                }
            }
        } catch (IOException | GitCloningException e) {
            throw new IllegalArgumentException("Couldn't open file " + file);
        }
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
}