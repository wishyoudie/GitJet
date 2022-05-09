package gitjet.model.collectinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static gitjet.model.FilesList.getFilesList;

public class LineSize {
    public static Integer getAmountOfLines() throws IOException {
        File clonesDirectory = new File("clones");
        File[] allClones = getFilesList(clonesDirectory);

        int averageSize = 0;

        for (File file : allClones) {

            if (file.isHidden()) {
                continue;
            }

            Integer stringCounter = 0;
            Integer result = iterateStrings(stringCounter, file);
            averageSize += result;
            System.out.println(file.getName() + " size is: " + result + " lines");
        }

        return averageSize / allClones.length;

    }

    private static Integer iterateStrings(Integer counter, File path) throws IOException {
        File[] filesList = getFilesList(path);
        for (File file : filesList) {
            if (file.isDirectory()) {
                counter += iterateStrings(0, new File(String.valueOf(file)));
            } else {
                String fileExtension = getExtensionByStringHandling(file.getName());
                if (Objects.equals(fileExtension, "java") || Objects.equals(fileExtension, "kt")) {
                    counter += countStrings(file);
                }
            }
        }
        return counter;
    }

    private static Integer countStrings(File filesList) throws IOException {
        int counter = 1;
        BufferedReader reader = new BufferedReader(new FileReader(filesList));
        while (reader.readLine() != null) counter++;
        reader.close();
        return counter;
    }

    private static String getExtensionByStringHandling(String file) {
        String[] fileNameSplited = file.split("\\.");
        return fileNameSplited[fileNameSplited.length - 1];
    }
}
