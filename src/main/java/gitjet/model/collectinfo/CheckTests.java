package gitjet.model.collectinfo;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static gitjet.model.collectinfo.LineSize.getAmountOfLines;

public class CheckTests {

    public static boolean isTestsInProject(String repoName) {
        File pathToCheck = new File("clones/" + repoName + "/test");
        return pathToCheck.exists() && Objects.requireNonNull(pathToCheck.list()).length != 0;
    }

    public static int getNumberOfLinesInTests(String repoName) throws IOException {

        if (!isTestsInProject(repoName)) {
            return 0;
        }

        return getAmountOfLines(repoName + "/test");
    }
}