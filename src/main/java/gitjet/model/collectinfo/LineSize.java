package gitjet.model.collectinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import static gitjet.Utils.getFilesArray;

public class LineSize {
    public static Integer getAmountOfLines(File file) throws IOException {

        Integer stringCounter = 0;
        Integer result = iterateStrings(stringCounter, file);
        System.out.println("Line size is: " + result);

        return result;
    }


    private static Integer iterateStrings(Integer counter, File path) throws IOException {
        File[] filesList = getFilesArray(path);
        for (File file : filesList) {
            if (file.isDirectory()) {
                counter += iterateStrings(0, new File(String.valueOf(file)));
            } else {
                String fileExtension = getExtensionByStringHandling(file.getName());
                if (Set.of("java", "kt").contains(fileExtension)) {
                    counter += countStrings(file);
                }
            }
        }
        return counter;
    }

    private static Integer countStrings(File filesList) throws IOException {
        int counter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filesList))) {
            while (reader.readLine() != null) {
                counter++;
            }
            return counter;
        }
    }

    private static String getExtensionByStringHandling(String file) {
        String[] fileNameSplit = file.split("\\.");
        return fileNameSplit[fileNameSplit.length - 1];
    }
}