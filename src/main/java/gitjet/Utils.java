package gitjet;

import gitjet.controller.RefreshController;
import gitjet.controller.WarningController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {

    /**
     * Kill window of control element.
     * @param ctrl Control element.
     */
    public static void closeWindow(Control ctrl) {
        Stage stage = (Stage) ctrl.getScene().getWindow();
        stage.close();
    }

    /**
     * Creates a new warning window and provides it with given controller.
     * @param controller Controller instance to bind to window.
     */
    public static void createWarningWindow(WarningController controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("warning-view.fxml"));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 300, 200);
        Stage refreshStage = new Stage();
        refreshStage.setTitle("Refresh info");
        refreshStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/warning.png"))));
        refreshStage.setScene(scene);
        refreshStage.initModality(Modality.APPLICATION_MODAL);
        refreshStage.show();
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
