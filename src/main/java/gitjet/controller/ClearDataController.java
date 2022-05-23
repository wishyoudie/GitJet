package gitjet.controller;

import static gitjet.Utils.killWindow;

import gitjet.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller of 'Clear data' window, which appears after click on the 'x' button in start menu.
 */
public class ClearDataController {

    /**
     * 'Yes' button in clearing data window.
     */
    @FXML
    private Button clearProceedButton;

    /**
     * 'No' button in clearing data window.
     */
    @FXML
    private Button clearCancelButton;

    /**
     * 'Yes' button in clearing data window pressing handler.
     */
    @FXML
    protected void clearProceed() {
        Utils.cleanFile("data.dat");
        killWindow(clearProceedButton);
    }

    /**
     * 'No' button in clearing data window pressing handler.
     */
    @FXML
    protected void clearCancel() {
        killWindow(clearCancelButton);
    }
}
