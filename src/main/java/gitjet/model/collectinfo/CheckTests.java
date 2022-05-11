package gitjet.model.collectinfo;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class CheckTests {

    public static boolean isTestsInProject(File file) {
        File pathToCheck = new File(file + File.separator + "test");

        boolean result = pathToCheck.exists() && Objects.requireNonNull(pathToCheck.list()).length != 0;
        System.out.println("Tests in project: " + result);
        return result;
    }

    public static int getNumberOfLinesInTests(File file) throws IOException {

        if (!isTestsInProject(file)) {
            return 0;
        }

        int result = getAmountOfLines(new File(file + File.separator + "test"));
        System.out.println("Tests size: " + result);
        return result;
    }
}