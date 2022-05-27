package gitjet.model.clonerepo;

import gitjet.Utils;
import gitjet.model.Errors;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class CloneProjects {

    private final static int NUMBER_OF_RETRIES = Integer.parseInt(Objects.requireNonNull(Utils.getSetting("connection_threshold")));

    public static File runCloning(String repo, String repoName) throws GitCloningException, IOException {

        Path path = Path.of("clones");
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        File localFile = Files.createTempDirectory(path, repoName).toFile();

       /* if (!localFile.delete()) {
            throw new IOException("Could not delete temporary file " + localFile);
        }*/

        int counter = 0;

        while (true) {
            System.out.println("Start cloning: " + repoName);
            try (Git git = Git.cloneRepository()
                    .setURI(repo)
                    .setDirectory(localFile)
                    .call()) {
                System.out.println("Completed cloning: " + repoName);
                return localFile;
            } catch (GitAPIException e) {
                counter++;
                System.err.println("Can't clone project, retrying: " + counter + "/" + NUMBER_OF_RETRIES);
                if (counter == NUMBER_OF_RETRIES) {
                    System.err.println(Errors.CLONE_ERROR.getMessage());
                    System.err.println("Skipping " + repo);
                    return null;
                }
            }
        }
    }

    public static void deleteClone(File localFile) {
        try {
            FileUtils.deleteDirectory(localFile);
        } catch (IOException e) {
            System.err.println("Couldn't delete clone " + localFile);
        }
    }
}