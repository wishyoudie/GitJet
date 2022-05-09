package gitjet.controller;

import gitjet.Application;
import gitjet.model.ReposHandler;
import gitjet.model.Repo;

import gitjet.model.clonerepo.GitCloningException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;

public class Controller {
    public final ObservableList<Repo> reposData = FXCollections.observableArrayList();

    @FXML
    private TableView<Repo> tableRepos;

    @FXML
    private TableColumn<Repo, String> nameColumn;

    @FXML
    private TableColumn<Repo, Integer> contributorsColumn;

    @FXML
    private TableColumn<Repo, Integer> linesColumn;

    @FXML
    protected void initialize() {
        if (tableRepos != null) {
            initData();

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            contributorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfContributors"));
            linesColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfLines"));

            tableRepos.setItems(reposData);
        }
    }

    /**
     * Initialize contents of repository table.
     */
    private void initData() {
        reposData.addAll(new ReposHandler().readData("data.dat"));
    }

    /**
     * Add repository to storage.
     * @param repo Repository to be added.
     */
    private void addData(Repo repo) {
        // Write to file, change view to open table with different controller instance !!! (or add reload button)
        try (Writer writer = new BufferedWriter(new FileWriter("data.dat", true))) {
            writer.append(repo.toString()).append("\n");
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Reload controller.
     */
    private void reload() {
        initialize();
    }

    @FXML
    protected void fileNewMenuItemClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-repo-view.fxml"));
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
        Repo repo = new ReposHandler().handle(newRepoTextUrl);
        addData(repo);
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
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            ReposHandler reposHandler = new ReposHandler();
            while ((line = br.readLine()) != null) {
                addData(reposHandler.handle(line));
            }
        } catch (Exception e) { // Change to IOException
            throw new IllegalArgumentException(e.getMessage());
        }
        killWindow(newRepoOpenFileButton);
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