package gitjet.model.clonerepo;

import java.io.File;
import java.io.IOException;

import gitjet.model.Errors;
import org.apache.commons.io.FileUtils;

import static gitjet.model.FilesList.getFilesList;

public class DeleteClones {

    public static void delete() throws GitCloningException {
        File[] clones = getFilesList(new File("clones"));

        for (File file : clones) {
            if (!file.isHidden()) {
                try {
                    System.out.println("Deleting " + file.getName());
                    FileUtils.deleteDirectory(file);
                    System.out.println(file.getName() + " deleted");
                } catch (IOException e) {
                    throw new GitCloningException(Errors.DELETE_ERROR.getMessage());
                }
            }
        }

    }
}
