package gitjet.controller;

import gitjet.model.Repo;
import gitjet.model.ReposHandler;
import gitjet.model.clonerepo.GitCloningException;
import gitjet.Utils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.util.List;

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
     * Add repository to storage.
     * @param repo Repository to be added.
     */
    private void addData(Repo repo) {
        try (Writer writer = new BufferedWriter(new FileWriter("data.dat", true))) {
            writer.append(repo.toString()).append("\n"); // change to line end (different op systems)
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't open 'data.dat' file.");
        }
    }

    /**
     * 'Submit' button in adding new repository window pressing handler.
     * @throws GitAPIException todoo throws change and descriptions
     */
    @FXML
    protected void newRepoSubmit() throws GitAPIException, GitCloningException, IOException {
        String newRepoTextUrl = newRepoField.getText();
        Repo repo = new ReposHandler().handle(newRepoTextUrl);
        addData(repo);
        Utils.killWindow(newRepoField);
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
            List<Repo> repos = new ReposHandler().handleTextFile(file);
            for (Repo repo : repos) {
                addData(repo);
            }
        }
        Utils.killWindow(newRepoOpenFileButton);
    }
}
