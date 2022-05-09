package gitjet.model.collectinfo;

import java.io.File;

public class CheckReadme {

    public static boolean isReadmeInProject(String repoName) {
        File fileToCheckLower = new File("clones/" + repoName + "/readme.md");
        File fileToCheckCaps = new File("clones/" + repoName + "/README.md");
        return fileToCheckCaps.exists() || fileToCheckLower.exists();
    }
}
