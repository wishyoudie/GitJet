package gitjet.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import static gitjet.Utils.closeWindow;

public class ErrorController {

    /**
     * Error text (as JavaFX Text).
     */
    @FXML
    private Text errorMessageText;

    /**
     * Error text (as String).
     */
    private final String errorMessage;

    /**
     * 'OK' button in error window.
     */
    @FXML
    private Button errorOKButton;

    /**
     * Constructor from error message.
     * @param errorMessage Error message.
     */
    public ErrorController(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Initializer.
     */
    @FXML
    void initialize() {
        errorMessageText.setText(errorMessage);
        errorOKButton.setOnAction(actionEvent -> closeWindow(errorOKButton));
    }
}
