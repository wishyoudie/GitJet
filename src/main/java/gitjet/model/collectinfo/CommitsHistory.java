package gitjet.model.collectinfo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to analyze repository commits and contributors.
 */
public class CommitsHistory {

    /**
     * Number of contributors to repository.
     */
    private final int numberOfContributors;

    /**
     * Total number of commits into a repository.
     */
    private final int numberOfCommits;

    /**
     * Constructor from directory of a repository.
     *
     * @param file Directory of a repository.
     */
    public CommitsHistory(File file) {

        try (Git git = Git.open(file)) {
            Iterable<RevCommit> commits = git.log().all().call();
            Map<String, Integer> commitsPerContributors = new HashMap<>();
            int totalNumberOfCommits = 0;
            for (RevCommit commit : commits) {
                String contributorName = commit.getAuthorIdent().getName();
                if (!commitsPerContributors.containsKey(contributorName)) {
                    commitsPerContributors.put(contributorName, 1);
                } else {
                    Integer newNumber = commitsPerContributors.get(contributorName) + 1;
                    commitsPerContributors.put(contributorName, newNumber);
                }

                totalNumberOfCommits++;
            }
            numberOfContributors = commitsPerContributors.size();
            numberOfCommits = totalNumberOfCommits;

            /* System.out.println("Number of contributors: " + commitsPerContributors.size() + "\n");
            System.out.println("Commits per contributors: ");

            for (Map.Entry<String, Integer> entry : commitsPerContributors.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }*/
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Number of commits getter.
     *
     * @return Number of commits.
     */
    public int getNumberOfCommits() {
        return numberOfCommits;
    }

    /**
     * Number of contributors getter.
     *
     * @return Number of contributors.
     */
    public int getNumberOfContributors() {
        return numberOfContributors;
    }
}