package gitjet.model.collectinfo;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class CheckTests {

    public static boolean isTestsInProject(File file) {
        File pathToCheck = new File(file.getPath() + "/test");
        return pathToCheck.exists() && Objects.requireNonNull(pathToCheck.list()).length != 0;
    }

    public static int getNumberOfLinesInTests(File file) throws IOException {

        if (!isTestsInProject(file)) {
            return 0;
        }

        return getAmountOfLines(file);
    }
}