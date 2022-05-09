package gitjet.model;

import gitjet.model.clonerepo.GitCloningException;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.Commits.commitsStats;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class ReposHandler {
    int numberOfCommits, numberofContributors, commitsPerContributor, numberOfStringsInProject, testsInProject,
            numberOfStringsInTests, readmeInProject;
    List<String> mavenDependencies;

    public Repo handle(String repo) throws GitCloningException, GitAPIException, IOException {
        System.out.println("Starting cloning process");
        runCloning(repo);

        List<Integer> commits = commitsStats();
        numberofContributors = commits.get(0);
        numberOfCommits = commits.get(1);
        commitsPerContributor = commits.get(2);

        numberOfStringsInProject = getAmountOfLines();
        return new Repo(repo, numberofContributors, numberOfStringsInProject, numberOfCommits); // Saves URL as repo name
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
