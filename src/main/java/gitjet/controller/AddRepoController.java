package gitjet.controller;

import gitjet.model.ReposHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;

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
    protected void newRepoSubmit() {
        String textFieldValue = newRepoField.getText();
        ReposHandler reposHandler = new ReposHandler();
        if (isLink(textFieldValue)) {
            reposHandler.update(textFieldValue);
        } else if (isNumber(textFieldValue)) {
            reposHandler.searchRepos(Integer.parseInt(textFieldValue));
        } else {
            System.out.println("Warning");
        }
        closeWindow(newRepoField);
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
            new ReposHandler().handleLinksFile(file);
        }
        closeWindow(newRepoOpenFileButton);
    }
}
