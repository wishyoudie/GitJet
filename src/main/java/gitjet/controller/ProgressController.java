package gitjet.controller;

import gitjet.WindowsUtils;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Controller of 'Progress' window, which appears when handling repositories.
 */
public class ProgressController {
    /**
     * Task container (as String for construction).
     */
    private final String task;

    /**
     * Jet spinner in progress bar.
     */
    @FXML
    protected ImageView statusSpinner;

    /**
     * Task name in progress bar.
     */
    @FXML
    protected Label statusText;

    /**
     * Controller from string stating a task.
     * @param task A task to show progress of.
     */
    public ProgressController(String task) {
        this.task = task;
    }

    /**
     * Initializer.
     */
    @FXML
    protected void initialize() {
        this.statusText.setText(this.task);

        RotateTransition rotate = new RotateTransition();
        rotate.setNode(statusSpinner);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.play();
    }

    /**
     * Closer.
     */
    public void close() {
        WindowsUtils.closeWindow(statusText);
    }
}
