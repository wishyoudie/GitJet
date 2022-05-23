package gitjet.controller;

import gitjet.Application;
import gitjet.Utils;
import gitjet.model.Errors;
import gitjet.model.ReposHandler;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Controller of start menu window.
 */
public class StartController {

    /**
     * Whole body of title screen.
     */
    @FXML
    private VBox body;

    /**
     * Text with a hint to all functional buttons which appears on buttons mouse hover.
     */
    @FXML
    private Label hintText;

    /**
     * Mouse hover 'New' button handler.
     */
    @FXML
    protected void newButtonHover() {
        hintText.setText("Add new repository/ies");
    }

    /**
     * Mouse hover 'Look' button handler.
     */
    @FXML
    protected void lookButtonHover() {
        hintText.setText("Open table of analyzed repositories");
    }

    /**
     * Mouse hover 'Refresh' button handler.
     */
    @FXML
    protected void refreshButtonHover() {
        hintText.setText("Refresh info about analyzed repositories");
    }

    /**
     * Mouse hover 'Clear' button handler.
     */
    @FXML
    protected void clearButtonHover() {
        hintText.setText("Clear all gathered information");
    }

    /**
     * Mouse exit any button handler.
     */
    @FXML
    protected void buttonExited() {
        hintText.setText("");
    }

    /**
     * Initialize controller function.
     */
    @FXML
    protected void initialize() {
        FadeTransition fade = new FadeTransition();
        fade.setNode(body);
        fade.setDuration(Duration.millis(1500));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Act after user pressed 'New' button on main screen.
     * @throws IOException Throws if problems occurred while creating new window.
     */
    @FXML
    protected void newButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-repo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage getRepoStage = new Stage();
        getRepoStage.setTitle("Analyze new repository");
        getRepoStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/button_new.png"))));
        getRepoStage.setScene(scene);
        getRepoStage.initModality(Modality.APPLICATION_MODAL);
        getRepoStage.show();
    }

    /**
     * Act after user pressed 'Look' button on main screen.
     * @throws IOException Throws if problems occurred while creating new window.
     */
    @FXML
    protected void lookButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("table-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 650);
        Stage getRepoStage = new Stage();
        getRepoStage.setTitle("Gathered information");
        getRepoStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/icon.png"))));
        getRepoStage.setScene(scene);
        getRepoStage.initModality(Modality.APPLICATION_MODAL);
        getRepoStage.show();
    }

    /**
     * Act after user pressed 'Refresh' button on main screen.
     */
    @FXML
    protected void refreshButtonClick() {
        List<String> repos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                repos.add(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }

        Utils.cleanFile("data.dat");
        ReposHandler reposHandler = new ReposHandler();
        for (String line : repos) {
            reposHandler.handle("https://www.github.com/" + Arrays.asList(line.split(" ")).get(1) + "/" + Arrays.asList(line.split(" ")).get(0));
        }
    }

    /**
     * Act after user pressed 'Clear' button on main screen.
     * @throws IOException Throws if problems occurred while creating new window.
     */
    @FXML
    protected void clearButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("clear-data-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 200);
        Stage clearDataStage = new Stage();
        clearDataStage.setTitle("Warning");
        clearDataStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/warning.png"))));
        clearDataStage.setScene(scene);
        clearDataStage.initModality(Modality.APPLICATION_MODAL);
        clearDataStage.show();
    }
}
