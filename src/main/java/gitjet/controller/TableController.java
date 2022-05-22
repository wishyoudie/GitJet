package gitjet.controller;

import gitjet.model.Repo;
import gitjet.model.ReposHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controller of 'View repositories' window, which appears after click on the eye button in start menu.
 */
public class TableController {

    /**
     * List of analyzed repositories.
     */
    public final ObservableList<Repo> reposData = FXCollections.observableArrayList();

    /**
     * List of analyzed dependencies.
     */
    public final ObservableList<Map.Entry<String, Integer>> dependenciesData = FXCollections.observableArrayList();

    /**
     * List of summarized repository data.
     */
    public final ObservableList<Repo> summaryData = FXCollections.observableArrayList();

    /**
     * Table of repositories.
     */
    @FXML
    private TableView<Repo> mainTable;

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
    private TableColumn<Repo, String> dependenciesColumn;

    /**
     * Summary table of repositories.
     */
    @FXML
    private TableView<Repo> summaryTable;

    /**
     * Repository name column in summary table.
     */
    @FXML
    private TableColumn<Repo, String> summaryNameColumn;

    /**
     * Repository author column in summary table.
     */
    @FXML
    private TableColumn<Repo, String> summaryAuthorColumn;
    /**
     * Total number of contributors into repository column in summary table.
     */
    @FXML
    private TableColumn<Repo, Integer> summaryContributorsColumn;

    /**
     * Total number of commits in repository column in summary table.
     */
    @FXML
    private TableColumn<Repo, Integer> summaryCommitsColumn;

    /**
     * Total number of lines in repository column in summary table.
     */
    @FXML
    private TableColumn<Repo, Integer> summaryLinesColumn;

    /**
     * Repository has tests column in summary table.
     */
    @FXML
    private TableColumn<Repo, String> summaryHasTestsColumn;

    /**
     * Total number of lines in tests in repository column in summary table.
     */
    @FXML
    private TableColumn<Repo, Integer> summaryTestLinesColumn;

    /**
     * Repository has a readme file column in summary table.
     */
    @FXML
    private TableColumn<Repo, Boolean> summaryReadMeColumn;

    /**
     * Total number of repository dependencies column in summary table.
     */
    @FXML
    private TableColumn<Repo, String> summaryDependenciesColumn;

    /**
     * Table of dependencies.
     */
    @FXML
    private TableView<Map.Entry<String, Integer>> dependenciesTable;

    /**
     * Dependencies names column.
     */
    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> dependencyNameColumn;

    /**
     * Dependencies usages column.
     */
    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> dependencyUsagesColumn;

    /**
     * Initialize controller function.
     */
    @FXML
    protected void initialize() {
        initData();
        initMainTable();
        initSummaryTable();
        initDependenciesTable();
    }

    /**
     * Initialize contents of repository table.
     */
    private void initData() {
        ReposHandler reposHandler = new ReposHandler();
        List<Repo> repos = reposHandler.readData("data.dat");
        reposData.addAll(repos);
        summaryData.addAll(reposHandler.calculateSummary(repos));
        dependenciesData.addAll(reposHandler.catalogDependencies(repos));
    }

    /**
     * Initialize main table.
     */
    private void initMainTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        contributorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfContributors"));
        commitsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCommits"));
        linesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInProject"));
        hasTestsColumn.setCellValueFactory(new PropertyValueFactory<>("hasTests"));
        testLinesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInTests"));
        readMeColumn.setCellValueFactory(new PropertyValueFactory<>("hasReadMe"));
        dependenciesColumn.setCellValueFactory(p -> new SimpleStringProperty(String.join(", ", p.getValue().getMavenDependencies())));

        mainTable.setItems(reposData);
    }

    /**
     * Initialize summary table.
     */
    private void initSummaryTable() {
        summaryTable.widthProperty().addListener((ov, t, t1) -> {
            Pane header = (Pane)summaryTable.lookup("TableHeaderRow");
            if (header != null && header.isVisible()) {
                header.setMaxHeight(0);
                header.setMinHeight(0);
                header.setPrefHeight(0);
                header.setVisible(false);
                header.setManaged(false);
            }
        });

        summaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        summaryAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        summaryContributorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfContributors"));
        summaryCommitsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCommits"));
        summaryLinesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInProject"));
        summaryHasTestsColumn.setCellValueFactory(new PropertyValueFactory<>("hasTests"));
        summaryTestLinesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLinesInTests"));
        summaryReadMeColumn.setCellValueFactory(new PropertyValueFactory<>("hasReadMe"));
        summaryDependenciesColumn.setCellValueFactory(p -> new SimpleStringProperty(String.join(", ", p.getValue().getMavenDependencies())));

        summaryTable.setItems(summaryData);
    }

    /**
     * Initialize dependencies table.
     */
    private void initDependenciesTable() {
        dependencyNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        dependencyUsagesColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));
        dependenciesTable.setItems(dependenciesData);
    }
}
