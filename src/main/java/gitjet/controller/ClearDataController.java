package gitjet.controller;

import gitjet.WindowsUtils;
import gitjet.model.DataWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Controller of 'Clear data' window, which appears after click on the 'x' button in start menu.
 */
public class ClearDataController implements WarningController {

    /**
     * 'Yes' button in clearing data window.
     */
    @FXML
    private Button warningProceedButton;

    /**
     * 'No' button in clearing data window.
     */
    @FXML
    private Button warningCancelButton;

    /**
     * Text field in clearing data window.
     */
    @FXML
    private Text warningText;

    /**
     * Initializer.
     */
    @FXML
    protected void initialize() {
        warningText.setText("This action will clear all collected information");
        warningProceedButton.setOnAction(actionEvent -> warningButtonProceed());
        warningCancelButton.setOnAction(actionEvent -> warningButtonCancel());
    }

    /**
     * 'Yes' button in clearing data window pressing handler.
     */
    @FXML
    public void warningButtonProceed() {
        try {
            DataWriter.getInstance().write("", false);
        } catch (IOException e) {
            WindowsUtils.createErrorWindow(e.getMessage());
        }
        WindowsUtils.closeWindow(warningProceedButton);
    }

    /**
     * 'No' button in clearing data window pressing handler.
     */
    @FXML
    public void warningButtonCancel() {
        WindowsUtils.closeWindow(warningCancelButton);
    }
}
