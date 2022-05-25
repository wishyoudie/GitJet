package gitjet;

import gitjet.controller.ErrorController;
import gitjet.controller.RefreshController;
import gitjet.controller.WarningController;
import gitjet.model.Errors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static gitjet.model.Errors.DIRECTORY_ERROR;

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
        Stage warningStage = new Stage();
        warningStage.setTitle("Refresh info");
        warningStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/warning.png"))));
        warningStage.setScene(scene);
        warningStage.initModality(Modality.APPLICATION_MODAL);
        warningStage.show();
    }

    /**
     * Creates new error window with custom text.
     * @param text Error text message.
     */
    public static void createErrorWindow(String text) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("error-view.fxml"));
            fxmlLoader.setController(new ErrorController(text));
            Scene scene = new Scene(fxmlLoader.load(), 400, 400);
            Stage errorStage = new Stage();
            errorStage.setTitle("Error");
            errorStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/warning.png"))));
            errorStage.setScene(scene);
            errorStage.initModality(Modality.APPLICATION_MODAL);
            errorStage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't find FXML preset for error message.");
        }
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
            throw new IllegalArgumentException(DIRECTORY_ERROR.getMessage());
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
            String msg = "Couldn't open file " + fileName;
            createErrorWindow(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Get setting from settings file by its name.
     * @param parameterName Name of setting.
     * @return Setting value for given name.
     */
    public static String getSetting(String parameterName) {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> parts = Arrays.asList(line.split(":"));
                if (Objects.equals(parts.get(0), parameterName)) {
                    return parts.get(1);
                }
            }
        } catch (IOException e) {
            Utils.createErrorWindow("Couldn't find setting " + parameterName);
            throw new IllegalStateException(Errors.SETTINGS_ERROR.getMessage());
        }
        return null;
    }
}
