package gitjet;

import gitjet.model.ReposHandler;
import gitjet.model.clonerepo.GitCloningException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;

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
        Stage stage = (Stage) newRepoField.getScene().getWindow();
        stage.close();
    }
}