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
import java.util.List;

public class Controller {
    public final ObservableList<Repo> reposData = FXCollections.observableArrayList();

    /**
     * Table of repositories.
     */
    @FXML
    private TableView<Repo> tableRepos;

    /**
     * Repository name column in table.
     */
    @FXML
    private TableColumn<Repo, String> nameColumn;

    /**
     * Total number of contributors into repository column.
     */
    @FXML
    private TableColumn<Repo, Integer> contributorsColumn;

    /**
     * Total number of commits in repository column.
     */
    @FXML
    private TableColumn<Repo, Integer> commitsColumn;

    /**
     * Total number of lines in repository column.
     */
    @FXML
    private TableColumn<Repo, Integer> linesColumn;

    /**
     * Total number of lines in tests in repository column.
     */
    @FXML
    private TableColumn<Repo, Integer> testLinesColumn;

    /**
     * Repository has a readme file column.
     */
    @FXML
    private TableColumn<Repo, Boolean> readMeColumn;

    /**
     * Total number of repository dependencies column.
     */
    @FXML
    private TableColumn<Repo, Integer> dependenciesColumn;

    @FXML
    protected void initialize() {
        if (tableRepos != null) {
            initData();

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            contributorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfContributors"));
            commitsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCommits"));
            linesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInProject"));
            testLinesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInTests"));
            readMeColumn.setCellValueFactory(new PropertyValueFactory<>("readmeInProject"));
            dependenciesColumn.setCellValueFactory(new PropertyValueFactory<>("mavenDependencies"));

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
        if (file != null) {
            List<Repo> repos = new ReposHandler().handleTextFile(file);
            for (Repo repo : repos) {
                addData(repo);
            }
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