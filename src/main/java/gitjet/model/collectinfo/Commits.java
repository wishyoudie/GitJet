package gitjet.model.collectinfo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitjet.model.FilesList.getFilesList;

public class Commits {

    public static List<Integer> commitsStats() throws GitAPIException, IOException {
        File clonesDirectory = new File("clones");
        File[] allClones = getFilesList(clonesDirectory);

        int numberOfContributors = 0;
        int numberOfCommits = 0;

        for(File file : allClones) {

            if (file.isHidden()) {
                continue;
            }

            Git git = Git.open(file);
            Iterable<RevCommit> commits = git.log().all().call();
            Map<String, Integer> commitsPerContributors = new HashMap<>();

            System.out.println("Checking " + file.getName() + " project");

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

            System.out.println(file.getName() + " project checked\n");
        }

        List<Integer> results = new ArrayList<>();
        results.add(numberOfContributors / allClones.length);
        results.add(numberOfCommits / allClones.length);
        results.add(numberOfCommits / numberOfContributors);
        return results;
    }
}
