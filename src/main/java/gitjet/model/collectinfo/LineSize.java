package gitjet.model.collectinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LineSize {
    public static void main(String[] args) throws IOException {
        File cloneDirectory = new File("clones/KotlinAsFirst2020/src/");
        Integer stringCounter = 0;
        System.out.println(iterateString(stringCounter, cloneDirectory));
    }

    public static Integer iterateString(Integer counter, File path) throws IOException {
        File[] filesList = getFilesList(path);
        for (File file : filesList) {
            if (file.isDirectory()) {
                counter += iterateString(0, new File(String.valueOf(file)));
            } else {
                counter += countStrings(path, file);
            }
        }
        return counter;
    }

    public static Integer countStrings(File path, File filesList) throws IOException {
        int counter = 1;
        BufferedReader reader = new BufferedReader(new FileReader(filesList));
        while (reader.readLine() != null) counter++;
        reader.close();
        return counter;
    }

    public static File[] getFilesList(File inputFile) {
        if (!inputFile.isDirectory()) {
            return new File[] {inputFile};
        }

        File[] filesList = inputFile.listFiles();
        if (filesList == null) {
            throw new IllegalArgumentException("");
        }

        return filesList;
    }
}
