package gitjet.model;

import java.io.File;

public class FilesList {

    public static File[] getFilesList(File file) {
        if (!file.isDirectory()) {
            return new File[] {file};
        }

        File[] filesList = file.listFiles();
        if (filesList == null) {
            throw new IllegalArgumentException("");
        }

        return filesList;
    }
}