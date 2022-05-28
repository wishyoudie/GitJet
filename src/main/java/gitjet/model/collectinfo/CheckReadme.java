package gitjet.model.collectinfo;

import java.io.File;

/**
 * A class to analyze README.md file of a repository.
 */
public class CheckReadme {

    /**
     * Check if README.md file is present in a repository.
     *
     * @param file Directory of a repository.
     * @return True if and only if repository has a README.md file.
     */
    public static boolean isReadmeInProject(File file) {
        File fileToCheckLower = new File(file + File.separator + "readme.md");
        File fileToCheckCaps = new File(file + File.separator + "README.md");
        return fileToCheckCaps.exists() || fileToCheckLower.exists();
    }
}
