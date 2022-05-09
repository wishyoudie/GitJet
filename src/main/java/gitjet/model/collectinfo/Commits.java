package gitjet.model.collectinfo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Commits {

    int numberOfContributors = 0;
    int numberOfCommits = 0;

    public void commitsStats(File file) {

        try (Git git = Git.open(file)) {
            Iterable<RevCommit> commits = git.log().all().call();
            Map<String, Integer> commitsPerContributors = new HashMap<>();

            // System.out.println("Checking " + file.getName() + " project");

            for (RevCommit commit : commits) {
                String contributorName = commit.getAuthorIdent().getName();
                if (!commitsPerContributors.containsKey(contributorName)) {
                    commitsPerContributors.put(contributorName, 1);
                } else {
                    Integer newNumber = commitsPerContributors.get(contributorName) + 1;
                    commitsPerContributors.put(contributorName, newNumber);
                }

                numberOfCommits += 1;
            }

            System.out.println("Number of contributors: " + commitsPerContributors.size() + "\n");
            numberOfContributors += commitsPerContributors.size();
            System.out.println("Commits per contributors: ");

            for (Map.Entry<String, Integer> entry : commitsPerContributors.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            // System.out.println(file.getName() + " project checked\n");
        } catch (IOException | GitAPIException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public int getNumberOfCommits() {
        return numberOfCommits;
    }

    public int getNumberOfContributors() {
        return numberOfContributors;
    }
}