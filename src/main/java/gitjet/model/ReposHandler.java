package gitjet.model;

import gitjet.Utils;
import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.CheckTests;
import gitjet.model.collectinfo.CommitsHistory;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static gitjet.model.Repo.getAuthorFromLink;
import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.AnalyzePom.getDependencies;
import static gitjet.model.collectinfo.AnalyzePom.isMavenRepository;
import static gitjet.model.collectinfo.CheckReadme.isReadmeInProject;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;
import static gitjet.model.Repo.getNameFromLink;

/**
 * Retrieve data from GitHub and turn it into Repo instances class.
 */
public class ReposHandler {

    /**
     * Initialize already collected data from storage.
     * @param fileName A file which contains result of handling.
     * @return List of data translated into Repo instances.
     */
    public List<Repo> readData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<Repo> result = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                result.add(new Repo(line));
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }
    }

    /**
     * Check if repository with given name has already been handled. A repository has already been handled
     * if the main database file contains its data.
     * @param name A name of repository.
     * @return True if this repository has already been handled.
     */
    public boolean alreadyHandled(String name) {
        List<String> scannedNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                scannedNames.add(Arrays.asList(line.split(" ")).get(0));
            }
            return scannedNames.contains(name);
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }
    }

    /**
     * Update repository by its URL. Updates information on repository without changing its position if it has
     * already been handled, handles it otherwise.
     * @param link URL to repository on GitHub.
     */
    public void update(String link) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            String name = getNameFromLink(link);
            if (alreadyHandled(name)) {
                List<Repo> data = readData("data.dat");
                Utils.cleanFile("data.dat");
                try (FileWriter writer = new FileWriter("data.dat", true)) {
                    for (Repo repo : data) {
                        if (Objects.equals(repo.getName(), name)) {
                            writer.append(handle(link).toString()).append(System.lineSeparator());
                        } else {
                            writer.append(repo.toString()).append(System.lineSeparator());
                        }
                    }
                } catch (IOException e) {
                    throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
                }
            } else {
                handle(link);
            }
        });
        executor.shutdown();
    }

    /**
     * Handle link to repository. Turns data into Repo.
     * @param link URL to repository on GitHub.
     * @return Repo instance of provided repository.
     */
    public Repo handle(String link) {
        File clone = null;

        try {
            String repoName = getNameFromLink(link);
            clone = runCloning(link, repoName);
            int numberOfLinesInTests = new CheckTests().getNumberOfLinesInTests(clone);
            CommitsHistory commitsHistory = new CommitsHistory(clone);

            return new Repo(repoName,
                    getAuthorFromLink(link),
                    commitsHistory.getNumberOfContributors(),
                    commitsHistory.getNumberOfCommits(),
                    getAmountOfLines(clone),
                    (numberOfLinesInTests != 0),
                    numberOfLinesInTests,
                    isReadmeInProject(clone),
                    getDependencies(clone));
        } catch (GitCloningException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            deleteClone(clone);
        }
    }

    /**
     * Search Maven repositories on GitHub and handle them.
     * @param requiredNumber A number of repositories to be found.
     */
    public void searchRepos(int requiredNumber) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            RepositoryService repositoryService = new RepositoryService();
            int page = 1;
            int counter = 0;
            try {
                while (counter < requiredNumber) {
                    List<SearchRepository> repos = repositoryService.searchRepositories("size:>0", "java", page);

                    for (SearchRepository repo : repos) {
                        String link = "https://github.com/" + repo.toString();
                        System.out.println("Checking " + link);
                        if (isMavenRepository(link) && !alreadyHandled(getNameFromLink(link))) {
                            Repo result = handle(link);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

    /**
     * Handle multiple URLs taken from a text file.
     * @param file A file which contains URLs to repositories on GitHub.
     */
    public void handleLinksFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isMavenRepository(line)) {
                    update(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't open file " + file);
        }
    }

    /**
     * Gather information on dependencies used in given repositories
     * @param repos List of repositories.
     * @return A map-like list, keys are unique dependency names, values are numbers of dependency usages.
     */
    public List<Map.Entry<String, Integer>> catalogDependencies(List<Repo> repos) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        List<String> dependenciesList = new ArrayList<>();
        Set<String> dependenciesSet = new HashSet<>();
        for (Repo repo : repos) {
            Set<String> dependencies = repo.getMavenDependencies();
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
     * @param repos List of repositories to summarize.
     * @return Mean and Total special 'repositories'.
     */
    public List<Repo> calculateSummary(List<Repo> repos) {
        List<Repo> result = new ArrayList<>();
        result.add(evaluateMean(repos));
        result.add(evaluateTotal(repos));
        return result;
    }

    /**
     * Summarize information on repositories: calculate total values.
     * @param repos List of repositories to summarize.
     * @return Total special 'repository'.
     */
    public Repo evaluateTotal(List<Repo> repos) {
        int numberOfRepos = repos.size();

        int numberOfContributors = 0,
                numberOfCommits = 0,
                numberOfLinesInProject = 0,
                numberOfLinesInTests = 0,
                numberOfReadmes = 0,
                numberOfTests = 0;
        Set<String> dependencies = new HashSet<>();

        for (Repo repo : repos) {

            numberOfContributors += repo.getNumberOfContributors();
            numberOfCommits += repo.getNumberOfCommits();
            numberOfLinesInProject += repo.getNumberOfLinesInProject();
            numberOfLinesInTests += repo.getNumberOfLinesInTests();

            if (repo.hasReadMe()) {
                numberOfReadmes += 1;
            }

            if (repo.getNumberOfLinesInTests() > 0) {
                numberOfTests += 1;
            }

            dependencies.addAll(repo.getMavenDependencies());
        }

        String readmesInProject = numberOfReadmes + "/" + numberOfRepos;
        String testsInProject = numberOfTests + "/" + numberOfRepos;
        Set<String> totalNumberOfDependenciesAsSet = new HashSet<>();
        String totalNumberOfDependencies = String.valueOf(dependencies.size());
        totalNumberOfDependenciesAsSet.add(totalNumberOfDependencies);

        return new Repo("Total",
                null,
                numberOfContributors,
                numberOfCommits,
                numberOfLinesInProject,
                testsInProject,
                numberOfLinesInTests,
                readmesInProject,
                totalNumberOfDependenciesAsSet);
    }

    /**
     * Summarize information on repositories: calculate total values.
     * @param repos List of repositories to summarize.
     * @return Mean special 'repository'.
     */
    public Repo evaluateMean(List<Repo> repos) {
        int numberOfRepos = repos.size();

        int numberOfContributors = 0,
                numberOfCommits = 0,
                numberOfLinesInProject = 0,
                numberOfLinesInTests = 0;
        List<Integer> dependenciesUsage = new ArrayList<>();

        for (Repo repo : repos) {

            numberOfContributors += repo.getNumberOfContributors();
            numberOfCommits += repo.getNumberOfCommits();
            numberOfLinesInProject += repo.getNumberOfLinesInProject();
            numberOfLinesInTests += repo.getNumberOfLinesInTests();

            dependenciesUsage.add(repo.getMavenDependencies().size());

        }

        Set<String> meanNumberOfDependenciesAsSet = new HashSet<>();
        String meanNumberOfDependencies = String.valueOf(dependenciesUsage.stream().mapToInt(Integer::intValue).sum() / numberOfRepos);
        meanNumberOfDependenciesAsSet.add(meanNumberOfDependencies);

        return new Repo("Mean",
                "",
                numberOfContributors / numberOfRepos,
                numberOfCommits / numberOfRepos,
                numberOfLinesInProject / numberOfRepos,
                null,
                numberOfLinesInTests / numberOfRepos,
                null,
                meanNumberOfDependenciesAsSet);
    }
}