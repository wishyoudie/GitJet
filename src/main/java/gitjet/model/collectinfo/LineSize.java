package gitjet.model.collectinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import static gitjet.Utils.getFilesArray;

/**
 * Class to calculate number of lines in repository source files.
 */
public class LineSize {

    /**
     * Calculate number of lines in repository source files.
     *
     * @param file Directory of a repository.
     * @return Number of lines in repository source files.
     * @throws IOException Throws if occurred while calculating total number of lines.
     */
    public static Integer getAmountOfLines(File file) throws IOException {
        Integer stringCounter = 0;

        return iterateStrings(stringCounter, file);
    }

    /**
     * Recursive function to calculate total number of lines of a file or all files in a directory.
     *
     * @param counter Initial value.
     * @param file    Directory.
     * @return Number of lines in a directory.
     * @throws IOException Throws if errors occurred while reading directory files.
     */
    private static Integer iterateStrings(Integer counter, File file) throws IOException {
        File[] filesList = getFilesArray(file);
        for (File file1 : filesList) {
            if (file1.isDirectory()) {
                counter += iterateStrings(0, new File(String.valueOf(file1)));
            } else {
                String fileExtension = getExtensionByStringHandling(file1.getName());
                if (Set.of("java", "kt").contains(fileExtension)) {
                    counter += countStrings(file1);
                }
            }
        }
        return counter;
    }

    /**
     * Calculate amount of lines in a file.
     *
     * @param file File.
     * @return Total number of lines in the specified file.
     * @throws IOException Throws if errors occurred while reading the file.
     */
    private static Integer countStrings(File file) throws IOException {
        int counter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                counter++;
            }
            return counter;
        }
    }

    /**
     * Get file extension from file name.
     *
     * @param file File.
     * @return File extension.
     */
    private static String getExtensionByStringHandling(String file) {
        String[] fileNameSplit = file.split("\\.");
        return fileNameSplit[fileNameSplit.length - 1];
    }
}