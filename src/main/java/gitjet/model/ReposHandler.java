package gitjet.model;

import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.CheckTests;
import gitjet.model.collectinfo.CommitsHistory;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.AnalyzePom.getDependencies;
import static gitjet.model.collectinfo.AnalyzePom.isMavenRepository;
import static gitjet.model.collectinfo.CheckReadme.isReadmeInProject;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;
import static gitjet.model.Repo.getNameFromLink;

public class ReposHandler {

    public Repo handle(String link) throws GitCloningException, GitAPIException, IOException {

        String repoName = getNameFromLink(link);

        System.out.println("Starting cloning process");
        File clone = runCloning(link, repoName);

        if (!isMavenRepository(clone)) {
            System.out.println("Not Maven project");
            deleteClone(clone);
            return new Repo(null, 0, 0, 0, false, 0, false, new HashSet<>());
        }

        CommitsHistory commitsHistory = new CommitsHistory();
        commitsHistory.commitsStats(clone);

        int numberOfContributors = commitsHistory.getNumberOfContributors();
        int numberOfCommits = commitsHistory.getNumberOfCommits();
        double commitsPerContributor = (numberOfCommits * 1.0) / numberOfContributors;

        int numberOfLinesInProject = getAmountOfLines(clone);

        CheckTests checkTests = new CheckTests();
        int numberOfLinesInTests = checkTests.getNumberOfLinesInTests(clone);

        boolean readmeInProject = isReadmeInProject(clone);

        Set<String> mavenDependencies = getDependencies(clone);

        deleteClone(clone);
        System.out.println("Deleted");

        return new Repo(repoName, numberOfContributors, numberOfCommits, numberOfLinesInProject, (numberOfLinesInTests != 0), numberOfLinesInTests, readmeInProject, mavenDependencies);
    }

    public List<Repo> handleSearchedRepos(int requiredNumber) throws IOException, GitAPIException, GitCloningException {
        RepositoryService repositoryService = new RepositoryService();
        int page = 1;
        List<Repo> results = new ArrayList<>();

        while (results.size() < 100) {
            List<SearchRepository> repos = repositoryService.searchRepositories("size:>0", "java", page);

            for (SearchRepository repo : repos) {
                Repo result = handle("https://github.com/" + repo.toString());
                if (!Objects.equals(result, new Repo(null, 0, 0, 0, false, 0, false, new HashSet<>()))) {
                    results.add(result);
                }
            }

            page++;
        }

        return results;
    }

    public List<Repo> handleTextFile(File file) {
        try {
            List<Repo> repos = new ArrayList<>();
            List<String> links = setUpLinks(file);
            System.out.println(links);
            for (String link : links) {
                repos.add(handle(link));
            }
            return repos;
        } catch (GitAPIException | GitCloningException | IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static List<String> setUpLinks(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> repos = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                repos.add(line);
            }
            return repos;
        }
    }

    public List<Repo> readData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<Repo> result = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                result.add(new Repo(line));
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException("No data");
        }
    }
}