package gitjet.model;

import gitjet.model.clonerepo.DeleteClones;
import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.Commits;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class ReposHandler {
    int numberOfCommits, numberofContributors, numberOfStringsInProject, testsInProject,
            numberOfStringsInTests, readmeInProject;
    double commitsPerContributor;
    List<String> mavenDependencies;

    public Repo handle(String link) throws GitCloningException, GitAPIException, IOException {
        System.out.println("Starting cloning process");
        runCloning(link);
        System.out.println("All repos cloned");

        Commits commits = new Commits();
        commits.commitsStats();

        numberofContributors = commits.getNumberOfContributors();
        numberOfCommits = commits.getNumberOfCommits();
        commitsPerContributor = (numberOfCommits * 1.0) / (numberofContributors * 1.0);

        numberOfStringsInProject = getAmountOfLines();

        // other classes

        System.out.println("Starting deleting process");
        DeleteClones.delete();
        System.out.println("All clones deleted");

        return new Repo(link, numberofContributors, numberOfStringsInProject, numberOfCommits); // Saves URL as link name
    }

    public List<Repo> handleTextFile(File file) throws IOException, GitAPIException, GitCloningException {
        List<String> links = setUpLinks(file);
        List<Repo> repos = new ArrayList<>();
        for (String link : links) {
            repos.add(handle(link));
        }
        return repos;
    }

    public static List<String> setUpLinks(File file) throws IOException {
        List<String> repos = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        while (line != null) {
            repos.add(line);
            line = reader.readLine();
        }
        return repos;
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
