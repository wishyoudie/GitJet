package gitjet;

import gitjet.controller.ErrorController;
import gitjet.controller.ProgressController;
import gitjet.controller.WarningController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * A class that contains utility functions to open/close certain windows.
 */
public class WindowsUtils {

    /**
     * Creates new error window with custom text.
     *
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
     * Creates a new warning window and provides it with given controller.
     *
     * @param controller Controller instance to bind to window.
     */
    public static void createWarningWindow(WarningController controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("warning-view.fxml"));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 300, 200);
            Stage warningStage = new Stage();
            warningStage.setTitle("Refresh info");
            warningStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/warning.png"))));
            warningStage.setScene(scene);
            warningStage.initModality(Modality.APPLICATION_MODAL);
            warningStage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't find FXML preset for warning message.");
        }
    }

    /**
     * Create progress window.
     *
     * @param controller Controller instance to bind to window.
     */
    public static void createProgressWindow(ProgressController controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("progress-view.fxml"));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage progressStage = new Stage();
            progressStage.setTitle("Progress");
            progressStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/icon.png"))));
            progressStage.setScene(scene);
            progressStage.initModality(Modality.WINDOW_MODAL);
            progressStage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't find FXML preset for progress window.");
        }
    }

    /**
     * Kill window of control element.
     *
     * @param ctrl Control element.
     */
    public static void closeWindow(Control ctrl) {
        Stage stage = (Stage) ctrl.getScene().getWindow();
        stage.close();
    }
}
