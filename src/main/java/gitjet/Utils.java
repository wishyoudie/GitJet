package gitjet;

import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.*;
import java.util.regex.Pattern;

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

    /**
     * Check if a string is a correct URL link.
     * @param text String to check.
     * @return Result of check.
     */
    public static boolean isLink(String text) {
        return Pattern.compile("[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)").matcher(text).find();
    }

    /**
     * Check if a string is a number.
     * @param text String to check.
     * @return Result of check.
     */
    public static boolean isNumber(String text) {
        return Pattern.compile("^[0-9]+$").matcher(text).find();
    }

    /**
     * Delete all contents of a file.
     * @param fileName File to be cleaned.
     */
    public static void cleanFile(String fileName) {
        try (Writer cleaner = new BufferedWriter(new FileWriter(fileName))) {
            cleaner.write("");
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't clean file " + fileName);
        }
    }


}
