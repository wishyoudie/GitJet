package gitjet;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private MenuItem analyzeNewMenuItem;

    @FXML
    private TextField newRepoField;

    @FXML
    private Button newRepoButton;

    @FXML
    protected void analyzeNewMenuItemClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("open-repo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage getRepoStage = new Stage();
        getRepoStage.setTitle("Analyze new repository");
        getRepoStage.setScene(scene);
        getRepoStage.initModality(Modality.APPLICATION_MODAL);
        getRepoStage.show();
    }

    @FXML
    protected void newRepoFieldInsert() {
        String newRepoTextUrl = newRepoField.getText();

        System.out.println(newRepoTextUrl);
        /*
        check and pass to model
        */
        Stage stage = (Stage) newRepoField.getScene().getWindow();
        stage.close();
    }
}