package gitjet.model.clonerepo;

import gitjet.model.Errors;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CloneProjects {

    public static File runCloning(String repo, String repoName) throws GitCloningException, IOException {

        Path path = Path.of("clones");
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        File localPath = Files.createTempDirectory(path, repoName).toFile();

        if (!localPath.delete()) {
            throw new IOException("Could not delete temporary file " + localPath);
        }

        int counter = 0;

        while (true) {
            try (Git git = Git.cloneRepository()
                    .setURI(repo)
                    .setDirectory(localPath)
                    .call()) {
                System.out.println("Completed Cloning " + repoName);
                return localPath;
            } catch (GitAPIException e) {
                counter++;
                System.err.println("Can't clone project, retrying: " + counter + "/5");
                if (counter == 5) {
                    System.err.println(Errors.CLONE_ERROR.getMessage());
                    System.err.println("Skipping " + repo);
                    return null;
                }
            }
        }
    }

    public static void deleteClone(File localPath) {
        try {
            FileUtils.deleteDirectory(localPath);
        } catch (IOException e) {
            System.err.println("Couldn't delete clone " + localPath);
        }
    }
}