package gitjet;

import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.File;

public class Utils {

    /**
     * Kill window of control element.
     * @param ctrl Control element.
     */
    public static void killWindow(Control ctrl) {
        Stage stage = (Stage) ctrl.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns an array of files of the received directory
     */
    public static File[] getFilesArray(File file) {
        if (!file.isDirectory()) {
            return new File[] {file};
        }

        File[] filesList = file.listFiles();
        if (filesList == null) {
            throw new IllegalArgumentException("");
        }

        return filesList;
    }
}
