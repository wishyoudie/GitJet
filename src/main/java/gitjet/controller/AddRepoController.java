package gitjet.controller;

import gitjet.model.Errors;
import gitjet.model.Repo;
import gitjet.model.ReposHandler;
import gitjet.model.clonerepo.GitCloningException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.util.List;

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
     * 'Submit (link)' button in adding new repository window.
     */
    @FXML
    private Button newRepoOpenFileButton;

    /**
     * Add every repository from a list to storage.
     * @param repos List of repositories to be added.
     */
    private void addData(List<Repo> repos) {
        try (Writer writer = new BufferedWriter(new FileWriter("data.dat", true))) {
            for (Repo repo : repos)
                writer.append(repo.toString()).append(System.lineSeparator());
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }
    }

    /**
     * 'Submit' button in adding new repository window pressing handler.
     * @throws GitAPIException todoo throws change and descriptions
     */
    @FXML
    protected void newRepoSubmit() throws GitAPIException, GitCloningException, IOException {
        String textFieldValue = newRepoField.getText();
        ReposHandler reposHandler = new ReposHandler();
        if (isLink(textFieldValue)) {
            reposHandler.update(textFieldValue);
        } else if (isNumber(textFieldValue)) {
            addData(reposHandler.handleSearchedRepos(Integer.parseInt(textFieldValue)));
        } else {
            System.out.println("Warning");
        }
        killWindow(newRepoField);
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
            addData(new ReposHandler().handleTextFile(file));
        }
        killWindow(newRepoOpenFileButton);
    }
}
