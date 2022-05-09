package gitjet.model.clonerepo;

import gitjet.model.Errors;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CloneProjects {

    public static void runCloning(String repo, String repoName) throws GitCloningException {
        File cloneDirectory = new File("clones" + File.separator + repoName);

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
    }

    public static void deleteClone() throws GitCloningException {
        /*try {
            File clonePath = new File("clones");
            clonePath.deleteOnExit();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
            //throw new GitCloningException(Errors.DELETE_ERROR.getMessage());
        }*/
        File clonePath = new File("clones");
        clonePath.deleteOnExit();
    }
}