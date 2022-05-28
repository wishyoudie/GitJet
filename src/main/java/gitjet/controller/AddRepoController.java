package gitjet.controller;

import gitjet.Application;
import gitjet.model.repository.Repository;
import gitjet.model.repository.RepositoriesHandler;
import gitjet.WindowsUtils;

import gitjet.model.clonerepo.GitCloningException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

import static gitjet.Utils.*;

/**
 * Controller of 'Add repository' window, which appears after click on the '+' button in start menu.
 */
public class AddRepoController {

    /**
     * Text field in adding new repository window.
     */
    @FXML
    private TextField newRepoField;

    /**
     * 'Submit' button in adding new repository window.
     */
    @FXML
    private Button newRepoOpenFileButton;

    /**
     * 'Submit' button in adding new repository window pressing handler.
     */
    @FXML
    protected void newRepoSubmit() throws IOException, GitCloningException {
        String textFieldValue = newRepoField.getText();
        RepositoriesHandler repositoriesHandler = new RepositoriesHandler();

        if (isLink(textFieldValue)) {
            Label status = changeToProgressBar(String.format("Analyzing %s", Repository.getNameFromLink(textFieldValue)));
            repositoriesHandler.update(textFieldValue);
            WindowsUtils.closeWindow(status);
        } else if (isNumber(textFieldValue)) {

            int number = Integer.parseInt(textFieldValue);
            if (number > 1000) {
                WindowsUtils.createErrorWindow("Please pick a number between 1-1000.");
            } else {
                Label status = changeToProgressBar(String.format("Searching for %d repositories", number));
                repositoriesHandler.searchRepos(number);
                WindowsUtils.closeWindow(status);
            }
        } else {
            WindowsUtils.createErrorWindow("Unexpected input. Consider using URL to repository or a number of repositories to be found.");
        }
    }

    /**
     * Choose a file which contains a list of URLs to repositories function.
     */
    @FXML
    protected void newRepoFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file with a list of repositories");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("All text files", "*.*"),
                new FileChooser.ExtensionFilter("DAT", "*.dat")
        );
        File file = fileChooser.showOpenDialog(newRepoOpenFileButton.getScene().getWindow());
        if (file != null) {
            try {
                new RepositoriesHandler().handleLinksFile(file);
            } catch (IllegalArgumentException e) {
                WindowsUtils.createErrorWindow(e.getMessage());
            }
        }
        WindowsUtils.closeWindow(newRepoOpenFileButton);
    }

    /**
     * Change view to progress bar stage.
     *
     * @param task Task text to put.
     * @throws IOException Throws if problems occurred while creating new window.
     */
    private Label changeToProgressBar(String task) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("progress-view.fxml"));
        ProgressController progressController = new ProgressController();
        progressController.setTask(task);
        fxmlLoader.setController(progressController);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage progressStage = (Stage) (newRepoField.getScene().getWindow());
        progressStage.setScene(scene);
        progressStage.setTitle("Progress");
        progressStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/icon.png"))));
        progressStage.show();
        return progressController.getStatus();
    }
}
