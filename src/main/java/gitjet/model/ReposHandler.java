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

import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class ReposHandler {
    int numberOfCommits, numberofContributors, numberOfLinesInProject, testsInProject,
            numberOfStringsInTests, readmeInProject;
    double commitsPerContributor;
    List<String> mavenDependencies;

    public Repo handle(String link) throws GitCloningException, GitAPIException, IOException {

        String repoName = getRepoName(link);

        System.out.println("Starting cloning process");
        runCloning(link, repoName);

        Commits commits = new Commits();
        commits.commitsStats(repoName);

        numberofContributors = commits.getNumberOfContributors();
        numberOfCommits = commits.getNumberOfCommits();
        commitsPerContributor = (numberOfCommits * 1.0) / (numberofContributors * 1.0);

        numberOfLinesInProject = getAmountOfLines(repoName);

        // other classes

        System.out.println("Starting deleting process");
        deleteClone();

        return new Repo(link, numberofContributors, numberOfLinesInProject, numberOfCommits); // Saves URL as link name
    }

    public List<Repo> handleTextFile(File file) throws IOException, GitAPIException, GitCloningException {
        List<String> links = setUpLinks(file);
        List<Repo> repos = new ArrayList<>();
        for (String link : links) {
            repos.add(handle(link));
        }
        return repos;
    }

    private static List<String> setUpLinks(File file) throws IOException {
        List<String> repos = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        while (line != null) {
            repos.add(line);
            line = reader.readLine();
        }
        return repos;
    }

    private static String getRepoName(String link) {
        List<String> linkSplited = Arrays.asList(link.split("/"));
        return linkSplited.get(linkSplited.size() - 1);
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
            throw new IllegalArgumentException("NO data");
        }
    }
}
