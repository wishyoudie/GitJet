package gitjet.controller;

import static gitjet.Utils.closeWindow;
import static gitjet.Utils.createErrorWindow;

import gitjet.Utils;
import gitjet.model.Errors;
import gitjet.model.ReposHandler;
import gitjet.model.clonerepo.GitCloningException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller of 'Refresh data' window, which appears after click on the arrows button in start menu.
 */
public class RefreshController implements WarningController {

    /**
     * 'Yes' button in refreshing data window.
     */
    @FXML
    private Button warningProceedButton;

    /**
     * 'No' button in refreshing data window.
     */
    @FXML
    private Button warningCancelButton;

    /**
     * Text field in refreshing data window.
     */
    @FXML
    private Text warningText;

    /**.
     * Initializer.
     */
    @FXML
    protected void initialize() {
        warningText.setText("This action will refresh all collected data");
        warningProceedButton.setOnAction(actionEvent -> warningButtonProceed());
        warningCancelButton.setOnAction(actionEvent -> warningButtonCancel());
    }

    /**
     * 'Yes' button in refreshing data window pressing handler.
     */
    @FXML
    public void warningButtonProceed() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
                    List<String> repos = new ArrayList<>();

                    try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            repos.add(line);
                        }
                    } catch (IOException e) {
                        createErrorWindow(Errors.DATA_ERROR.getMessage());
                        throw new IllegalStateException(Errors.DATA_ERROR.getMessage());
                    }

                    Utils.cleanFile("data.dat");
                    ReposHandler reposHandler = new ReposHandler();
                    for (String line : repos) {
                        try {
                            reposHandler.handle("https://www.github.com/" + Arrays.asList(line.split(" ")).get(1) + "/" + Arrays.asList(line.split(" ")).get(0)).addToStorage();
                        } catch (GitCloningException | IOException e) {
                            Utils.createErrorWindow(e.getMessage() + "\nSkipping repository " + Arrays.asList(line.split(" ")).get(0));
                        }
                    }
                });
        executor.shutdown();
        closeWindow(warningProceedButton);
    }

    /**
     * 'No' button in refreshing data window pressing handler.
     */
    @FXML
    public void warningButtonCancel() {
        closeWindow(warningCancelButton);
    }
}