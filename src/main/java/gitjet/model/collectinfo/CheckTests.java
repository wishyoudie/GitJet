package gitjet.model.collectinfo;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import static gitjet.Utils.getFilesArray;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class CheckTests {

    private File testsDirectory;

    private void findTests(File file) {
        File[] filesArray = getFilesArray(file);

        for (File currentFile : filesArray) {
            if (currentFile.isDirectory()) {
                if (currentFile.getName().toLowerCase().contains("test")) {
                    testsDirectory = currentFile;
                } else {
                    findTests(currentFile);
                }
            }
        }

        testsDirectory = null;
    }

    public int getNumberOfLinesInTests(File file) throws IOException {
        findTests(file);

        if (testsDirectory == null) {
            return 0;
        }

        int result = getAmountOfLines(testsDirectory);
        System.out.println("Tests size: " + result);
        return result;
    }
}