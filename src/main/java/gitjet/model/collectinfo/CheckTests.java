package gitjet.model.collectinfo;

import org.eclipse.jgit.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import static gitjet.Utils.getFilesArray;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class CheckTests {

    @Nullable
    private File findTests(File file) {
        File[] filesArray = getFilesArray(file);

        File testDirectory = null;

        for (File currentFile : filesArray) {
            if (currentFile.isDirectory()) {
                if (currentFile.getName().toLowerCase().contains("test") || currentFile.getName().equals("test")) {
                    testDirectory = currentFile;
                    return testDirectory;
                } else {
                   testDirectory = findTests(currentFile);
                }
            }
        }

        return testDirectory;
    }

    public int getNumberOfLinesInTests(File file) throws IOException {
        File testsDirectory = findTests(file);

        if (testsDirectory == null) {
            return 0;
        }

        int result = getAmountOfLines(testsDirectory);
        System.out.println("Tests size: " + result);
        return result;
    }
}