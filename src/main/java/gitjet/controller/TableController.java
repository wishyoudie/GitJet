package gitjet.controller;

import gitjet.model.Repo;
import gitjet.model.ReposHandler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Map;

/**
 * Controller of 'View repositories' window, which appears after click on the eye button in start menu.
 */
public class TableController {

    /**
     * List of analyzed repositories.
     */
    public final ObservableList<Repo> reposData = FXCollections.observableArrayList();

    public final ObservableList<Map.Entry<String, Integer>> dependenciesData = FXCollections.observableArrayList();

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
     * Repository author column in table.
     */
    @FXML
    private TableColumn<Repo, String> authorColumn;
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


    @FXML
    private TableView<Map.Entry<String, Integer>> tableDependencies;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> dependencyNameColumn;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> dependencyUsagesColumn;

    /**
     * Initialize controller function.
     */
    @FXML
    protected void initialize() {
        initData();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        contributorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfContributors"));
        commitsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCommits"));
        linesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInProject"));
        hasTestsColumn.setCellValueFactory(new PropertyValueFactory<>("hasTests"));
        testLinesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInTests"));
        readMeColumn.setCellValueFactory(new PropertyValueFactory<>("hasReadMe"));
        dependenciesColumn.setCellValueFactory(new PropertyValueFactory<>("mavenDependencies"));
        tableRepos.setItems(reposData);

        dependencyNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        dependencyUsagesColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));
        tableDependencies.setItems(dependenciesData);

    }

    /**
     * Initialize contents of repository table.
     */
    private void initData() {
        ReposHandler reposHandler = new ReposHandler();
        reposData.addAll(reposHandler.readData("data.dat"));
        dependenciesData.addAll(reposHandler.catalogDependencies());
    }
}
