package gitjet.model.collectinfo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Commits {


    public static void main(String[] args) throws GitAPIException, IOException {
        String repoURL = "https://github.com/Kotlin-Polytech/KotlinAsFirst2020";
        File cloneDirectory = new File("clones/KotlinAsFirst2020");
        // ВОВА РАЗКОМЕНТИ ЭТУ ТЕМУ СНИЗУ ЧТОБЫ КЛОНИРОВАТЬ СЕБЕ РЕПУ
//        cloneDirectory.mkdirs();
//
//        System.out.println("Cloning KotlinAsFirst2020 into " + cloneDirectory);
//        Git.cloneRepository()
//                .setURI(repoURL)
//                .setDirectory(Paths.get(String.valueOf(cloneDirectory)).toFile())
//                .call();
//        System.out.println("Completed Cloning");

        Git git = Git.open(cloneDirectory);
        Repository repo = git.getRepository();

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
        }

        System.out.println("Number of contributors: " + commitsPerContributors.size() + "\n");
        System.out.println("Commits per contributors \n" +
                "");

        for (Map.Entry<String, Integer> entry : commitsPerContributors.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

    }


}