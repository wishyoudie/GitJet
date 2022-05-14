package gitjet.controller;

import gitjet.model.Repo;
import gitjet.model.ReposHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller of 'View repositories' window, which appears after click on the eye button in start menu.
 */
public class TableController {

    /**
     * List of analyzed repositories.
     */
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
     * Repository has tests column.
     */
    @FXML
    private TableColumn<Repo, String> hasTestsColumn;

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

    /**
     * Initialize controller function.
     */
    @FXML
    protected void initialize() {
        if (tableRepos != null) {
            initData();

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            contributorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfContributors"));
            commitsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCommits"));
            linesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInProject"));
            hasTestsColumn.setCellValueFactory(new PropertyValueFactory<>("hasTests"));
            testLinesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInTests"));
            readMeColumn.setCellValueFactory(new PropertyValueFactory<>("hasReadMe"));
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
}
