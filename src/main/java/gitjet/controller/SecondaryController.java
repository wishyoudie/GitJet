package gitjet.controller;

import gitjet.model.ReposHandler;
import gitjet.model.clonerepo.GitCloningException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class SecondaryController {

    @FXML
    private TextField newRepoField;

    @FXML
    private Button newRepoOpenFileButton;

    @FXML
    private Button clearSaveButton;

    @FXML
    private Button cancelClearSaveButton;

    @FXML
    protected void newRepoFieldInsert() throws GitAPIException, GitCloningException, IOException {
        String newRepoTextUrl = newRepoField.getText();
        new ReposHandler().handle(newRepoTextUrl);
        killWindow(newRepoField);
    }

    @FXML
    protected void newRepoOpenFileClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file with a list of repositories");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("All text files", "*.*"),
                new FileChooser.ExtensionFilter("DAT", "*.dat")
        );
        File file = fileChooser.showOpenDialog(newRepoOpenFileButton.getScene().getWindow());
        if (file != null) {
            System.out.println("Opened file " + file.getName());
            // Pass to model
            killWindow(newRepoOpenFileButton);
        }
    }

    @FXML
    protected void clearSaveButtonClick() {
        System.out.println("Cleared save");
        // Clear save
        killWindow(clearSaveButton);
    }

    @FXML
    protected void cancelClearSaveWarningWindowButtonClick() {
        killWindow(cancelClearSaveButton);
    }

    /**
     * Kill window of control element.
     * @param ctrl Control element.
     */
    private void killWindow(Control ctrl) {
        Stage stage = (Stage) ctrl.getScene().getWindow();
        stage.close();
    }
}
