package gitjet.model;

import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.Commits;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.AnalyzePom.getDependencies;
import static gitjet.model.collectinfo.CheckReadme.isReadmeInProject;
import static gitjet.model.collectinfo.CheckTests.getNumberOfLinesInTests;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class ReposHandler {
    int numberOfCommits,
            numberOfContributors,
            numberOfLinesInProject,
            numberOfLinesInTests;
    boolean readmeInProject;
    double commitsPerContributor;
    Set<String> mavenDependencies;

    public Repo handle(String link) throws GitCloningException, GitAPIException, IOException {

        String repoName = getRepoName(link);

        System.out.println("Starting cloning process");
        File clone = runCloning(link, repoName);

        Commits commits = new Commits();
        commits.commitsStats(clone);

        numberOfContributors = commits.getNumberOfContributors();
        numberOfCommits = commits.getNumberOfCommits();
        commitsPerContributor = (numberOfCommits * 1.0) / numberOfContributors;

        numberOfLinesInProject = getAmountOfLines(clone);

        numberOfLinesInTests = getNumberOfLinesInTests(clone);

        readmeInProject = isReadmeInProject(clone);

        mavenDependencies = getDependencies(clone);

        deleteClone(clone);
        System.out.println("Deleted");

        return new Repo(repoName, numberOfContributors, numberOfCommits, numberOfLinesInProject, numberOfLinesInTests, readmeInProject, mavenDependencies);
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

    private static String getRepoName(String link) {
        List<String> linkSplit = Arrays.asList(link.split("/"));
        return linkSplit.get(linkSplit.size() - 1);
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