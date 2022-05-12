package gitjet.model.collectinfo;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class CheckTests {

    public static boolean isTestsInProject(File file) {

        File pathToCheck = getFirstPath(file);

        if (!pathToCheck.exists()) {
            pathToCheck = getSecondPath(file);
        }

        boolean result = pathToCheck.exists() && Objects.requireNonNull(pathToCheck.list()).length != 0;
        System.out.println("Tests in project: " + result);
        return result;
    }

    private static File getFirstPath(File file) {
        return new File(file + File.separator + "test");
    }

    private static File getSecondPath(File file) {
        return new File(file + File.separator + "src" + File.separator + "test");

    }

    public static int getNumberOfLinesInTests(File file) throws IOException {

        if (!isTestsInProject(file)) {
            return 0;
        }

        File pathToCheck = new File(file + File.separator + "test");

        if (!pathToCheck.exists()) {
            pathToCheck = new File(file + File.separator + "src" + File.separator + "test");
        }

        int result = getAmountOfLines(pathToCheck);
        System.out.println("Tests size: " + result);
        return result;
    }
}