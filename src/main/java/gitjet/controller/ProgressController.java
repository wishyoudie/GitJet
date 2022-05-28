package gitjet.controller;

import gitjet.WindowsUtils;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class ProgressController {
    private String task = "Default";

    @FXML
    protected ImageView statusSpinner;

    @FXML
    protected Label statusText;

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

    public void setTask(String task) {
        this.task = task;
    }

    public Label getStatus() {
        return this.statusText;
    }

    public ProgressController(String task) {
        this.task = task;
    }

    public void close() {
        WindowsUtils.closeWindow(statusText);
    }
}
