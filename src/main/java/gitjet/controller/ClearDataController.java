package gitjet.controller;

import gitjet.Utils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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
        try (Writer writer = new BufferedWriter(new FileWriter("data.dat"))) {
            writer.write("");
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Utils.killWindow(clearProceedButton);
    }

    /**
     * 'No' button in clearing data window pressing handler.
     */
    @FXML
    protected void clearCancel() {
        Utils.killWindow(clearCancelButton);
    }
}
