package gitjet.model.collectinfo;

import java.io.File;

public class CheckReadme {

    public static boolean isReadmeInProject(File file) {
        File fileToCheckLower = new File(file + File.separator + "readme.md");
        File fileToCheckCaps = new File(file + File.separator + "README.md");
        boolean result = fileToCheckCaps.exists() || fileToCheckLower.exists();
        System.out.println("Readme in project: " + result);
        return result;
    }
}
