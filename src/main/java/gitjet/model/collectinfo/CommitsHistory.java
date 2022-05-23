package gitjet.model.collectinfo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CommitsHistory {

    private int numberOfContributors = 0;
    private int numberOfCommits = 0;

    public CommitsHistory(File file) {

        try (Git git = Git.open(file)) {
            Iterable<RevCommit> commits = git.log().all().call();
            Map<String, Integer> commitsPerContributors = new HashMap<>();

            for (RevCommit commit : commits) {
                String contributorName = commit.getAuthorIdent().getName();
                if (!commitsPerContributors.containsKey(contributorName)) {
                    commitsPerContributors.put(contributorName, 1);
                } else {
                    Integer newNumber = commitsPerContributors.get(contributorName) + 1;
                    commitsPerContributors.put(contributorName, newNumber);
                }

                numberOfCommits++;
            }

            System.out.println("Number of contributors: " + commitsPerContributors.size() + "\n");
            numberOfContributors += commitsPerContributors.size();
            System.out.println("Commits per contributors: ");

            for (Map.Entry<String, Integer> entry : commitsPerContributors.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        } catch (IOException | GitAPIException e) { // maybe handle gitapiex
            e.printStackTrace();
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