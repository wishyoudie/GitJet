package gitjet.controller;

import gitjet.Utils;
import gitjet.model.Errors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField tokenField;

    @FXML
    private Button saveButton;

    private final Map<String, String> settings = new HashMap<>();

    @FXML
    protected void initialize() {
        readSettings();
        usernameField.setText(settings.get("username"));
        tokenField.setText(settings.get("token"));
    }

    @FXML
    protected void saveButtonPressed() {
        settings.replace("username", usernameField.getText());
        settings.replace("token", tokenField.getText());
        changeSettings();
        Utils.closeWindow(saveButton);
    }

    private void readSettings() {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> parts = Arrays.asList(line.split(":"));
                settings.put(parts.get(0), parts.get(1));
            }

        } catch (IOException e) {
            Utils.createErrorWindow(Errors.SETTINGS_ERROR.getMessage());
            throw new IllegalStateException(Errors.SETTINGS_ERROR.getMessage());
        }
    }

    private void changeSettings() {
        try (FileWriter writer = new FileWriter("settings.dat", true)) {
            Utils.cleanFile("settings.dat");
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                writer.append(entry.getKey()).append(":").append(entry.getValue()).append(System.lineSeparator());
            }
        } catch (IOException e) {
            Utils.createErrorWindow(Errors.SETTINGS_ERROR.getMessage());
            throw new IllegalStateException(Errors.SETTINGS_ERROR.getMessage());
        }
    }
}