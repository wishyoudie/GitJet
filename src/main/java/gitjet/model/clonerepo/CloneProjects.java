package gitjet.model.clonerepo;

import gitjet.model.Errors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CloneProjects {
    private String repo;

//    public static void main(String[] args) throws IOException, GitCloningException {
//        cloneProject(setUpLinks(new File("test.txt")));
//    }

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

    public void cloneProject() throws GitCloningException {
//        for (String repo: repos) {
        File cloneDirectory = new File("clones/" + repo.replace("https://github.com/", ""));
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

    public void setRepos(String repo) {
        if (!repo.isEmpty()) {
            this.repo = repo;
        }
    }
}
