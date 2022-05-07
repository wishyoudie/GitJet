package gitjet;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    private MenuItem analyzeNewMenuItem;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void analyzeNewMenuItemClick() {

    }
}