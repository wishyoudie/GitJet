package gitjet.controller;

import gitjet.Application;
import gitjet.model.ReposHandler;
import gitjet.model.Repo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
        initData();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contributorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfContributors"));
        linesColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfLines"));

        tableRepos.setItems(reposData);
    }

    /**
     * Initialize contents of repository table.
     */
    private void initData() {
        ReposHandler reposHandler = new ReposHandler();
        // repos.add from file
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
}