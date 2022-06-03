package gitjet.controller;

import gitjet.Application;
import gitjet.Utils;
import gitjet.WindowsUtils;
import gitjet.model.Errors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitjet.Utils.getSettingValueSeparator;

/**
 * Controller of 'Settings' window, which appears after click on the gear button in start menu.
 */
public class SettingsController {

    /**
     * Username field in settings.
     */
    @FXML
    private TextField usernameField;

    /**
     * GitHub token field in settings.
     */
    @FXML
    private TextField tokenField;

    /**
     * Connection threshold field in settings. (maximum number of attempts to establish connection to repository)
     */
    @FXML
    private TextField connectionThresholdField;

    /**
     * Name of the storage file used to save information about handled repositories.
     */
    @FXML
    private TextField storageField;

    /**
     * Save button in settings.
     */
    @FXML
    private Button saveButton;

    /**
     * Settings map.
     */
    private final Map<String, String> settings = new HashMap<>();

    /**
     * Initializer.
     */
    @FXML
    protected void initialize() {
        readSettings();
        usernameField.setText(settings.get("username"));
        tokenField.setText(settings.get("token"));
        connectionThresholdField.setText(settings.get("connection_threshold"));
        storageField.setText(settings.get("storage"));
    }

    /**
     * Save settings in memory button.
     */
    @FXML
    protected void saveButtonPressed() {
        try {
            settings.replace("username", usernameField.getText());
            settings.replace("token", tokenField.getText());
            Integer.parseInt(connectionThresholdField.getText());
            settings.replace("connection_threshold", connectionThresholdField.getText());
            settings.replace("storage", storageField.getText());
            changeSettings();
            WindowsUtils.closeWindow(saveButton);
        } catch (NumberFormatException e) {
            WindowsUtils.createErrorWindow(String.format("Unsupported connection threshold setting value.\n'%s' is not a valid number.", connectionThresholdField.getText()));
        } catch (NullPointerException e) {
            WindowsUtils.createErrorWindow(String.format("Couldn't reach '%s' as storage file.", storageField.getText()));
        }
    }

    /**
     * Read settings from memory.
     */
    private void readSettings() {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> parts = Arrays.asList(line.split(getSettingValueSeparator()));
                settings.put(parts.get(0), parts.get(1));
            }

        } catch (IOException e) {
            WindowsUtils.createErrorWindow(Errors.SETTINGS_ERROR.getMessage());
            throw new IllegalStateException(Errors.SETTINGS_ERROR.getMessage());
        }
    }

    /**
     * Change settings in memory.
     */
    private void changeSettings() {
        try (FileWriter writer = new FileWriter("settings.dat", true)) {
            Utils.cleanFile("settings.dat");
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                writer.append(entry.getKey()).append(getSettingValueSeparator()).append(entry.getValue()).append(System.lineSeparator());
            }
        } catch (IOException e) {
            WindowsUtils.createErrorWindow(Errors.SETTINGS_ERROR.getMessage());
            throw new IllegalStateException(Errors.SETTINGS_ERROR.getMessage());
        }
    }

    /**
     * Select storage file button click handler.
     */
    @FXML
    protected void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select storage file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DAT", "*.dat"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("All text files", "*.*")
        );
        File file = fileChooser.showOpenDialog(saveButton.getScene().getWindow());
        if (file != null) {
            try {
                storageField.setText(Paths.get(file.toURI()).toString());
            } catch (IllegalArgumentException e) {
                WindowsUtils.createErrorWindow(e.getMessage());
            }
        }
    }
}
