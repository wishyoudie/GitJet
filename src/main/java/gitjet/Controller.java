package gitjet;

import gitjet.model.ReposHandler;
import gitjet.model.clonerepo.GitCloningException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class Controller {
    ReposHandler reposHandler = new ReposHandler();

    @FXML
    private MenuItem fileNewMenuItem;

    @FXML
    private MenuItem fileSaveMenuItem;

    @FXML
    private MenuItem fileClearMenuItem;

    @FXML
    private TextField newRepoField;

    @FXML
    private Button newRepoButton;

    @FXML
    private Button newRepoOpenFileButton;

    @FXML
    private Button clearSaveButton;

    @FXML
    private Button cancelClearSaveButton;

    @FXML
    protected void fileNewMenuItemClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("open-repo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage getRepoStage = new Stage();
        getRepoStage.setTitle("Analyze new repository");
        getRepoStage.setScene(scene);
        getRepoStage.initModality(Modality.APPLICATION_MODAL);
        getRepoStage.show();
    }

    @FXML
    protected void fileSaveMenuItemClick() {
        System.out.println("Saved");
        // Change to saving data
    }

    @FXML
    protected void fileClearMenuItemClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("clear-data-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 200);
        Stage getRepoStage = new Stage();
        getRepoStage.setTitle("Warning");
        getRepoStage.setScene(scene);
        getRepoStage.initModality(Modality.APPLICATION_MODAL);
        getRepoStage.show();
        // Clear data
    }

    @FXML
    protected void newRepoFieldInsert() throws GitAPIException, GitCloningException, IOException {
        String newRepoTextUrl = newRepoField.getText();
        reposHandler.handler(newRepoTextUrl);
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