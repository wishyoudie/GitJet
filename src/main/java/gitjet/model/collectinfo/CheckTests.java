package gitjet.model.collectinfo;

import org.eclipse.jgit.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import static gitjet.Utils.getFilesArray;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

/**
 * Class to analyze tests of a repository.
 */
public class CheckTests {

    /**
     * Find test directory of a repository.
     *
     * @param file Directory of a repository.
     * @return Test directory if a repository has one, null otherwise.
     */
    @Nullable
    private static File findTests(File file) {
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

    /**
     * Calculates the number of lines in test files of a repository.
     *
     * @param file Directory of a repository.
     * @return Number of liens in test files.
     * @throws IOException Throws if errors occurred while reading test files.
     */
    public static int getNumberOfLinesInTests(File file) throws IOException {
        File testsDirectory = findTests(file);

        if (testsDirectory == null) {
            return 0;
        }

        return getAmountOfLines(testsDirectory);
    }
}