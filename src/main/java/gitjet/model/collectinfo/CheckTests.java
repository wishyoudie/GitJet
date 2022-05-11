package gitjet.model.collectinfo;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class CheckTests {

    public static File pathToTests(File file) {
        File pathToCheck = new File(file + File.separator + "test");
        if (pathToCheck.exists()) {
            return pathToCheck;
        } else {
            pathToCheck = new File(file + File.separator + "src" + File.separator + "test");
            if (pathToCheck.exists()) {
                return pathToCheck;
            } else {
                return null;
            }
        }
    }

    public static int getNumberOfLinesInTests(File file) throws IOException {

        File path = pathToTests(file);
        if (path == null) {
            return 0;
        }

        int result = getAmountOfLines(path);
        System.out.println("Tests size: " + result);
        return result;
    }
}