package gitjet.model.clonerepo;

import gitjet.model.Errors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CloneProjects {

    public static List<String> setUpLinks(File file) throws IOException { // for .txt file
        List<String> repos = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        while (line != null) {
            repos.add(line);
            line = reader.readLine();
        }
        return repos;
    }

    public static void runCloning(String repo) throws GitCloningException {
//        for (String repo: repos) {
        String repoName = repoNameFromLink(repo);
        File cloneDirectory = new File("clones/" + repoName);
        cloneDirectory.mkdirs();

        try {
            System.out.println("Cloning " + repo + " into " + cloneDirectory);
            Git.cloneRepository()
                    .setURI(repo)
                    .setDirectory(Paths.get(String.valueOf(cloneDirectory)).toFile())
                    .call();
            System.out.println("Completed Cloning");
        } catch (GitAPIException e) {
            throw new GitCloningException(Errors.CLONE_ERROR.getMessage());
        }
//        }
    }

    private static String repoNameFromLink(String link) {
        List<String> linkSplited = Arrays.asList(link.split("/"));
        return linkSplited.get(linkSplited.size() - 1);
    }
}
