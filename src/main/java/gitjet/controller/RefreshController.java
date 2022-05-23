package gitjet.controller;

import static gitjet.Utils.killWindow;

import gitjet.Utils;
import gitjet.model.Errors;
import gitjet.model.ReposHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller of 'Clear data' window, which appears after click on the 'x' button in start menu.
 */
public class RefreshController {

    /**
     * 'Yes' button in refreshing data window.
     */
    @FXML
    private Button refreshProceedButton;

    /**
     * 'No' button in refreshing data window.
     */
    @FXML
    private Button refreshCancelButton;

    /**
     * 'Yes' button in refreshing data window pressing handler.
     */
    @FXML
    protected void refreshProceed() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
                    List<String> repos = new ArrayList<>();

                    try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            repos.add(line);
                        }
                    } catch (IOException e) {
                        throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
                    }

                    Utils.cleanFile("data.dat");
                    ReposHandler reposHandler = new ReposHandler();
                    for (String line : repos) {
                        reposHandler.handle("https://www.github.com/" + Arrays.asList(line.split(" ")).get(1) + "/" + Arrays.asList(line.split(" ")).get(0)).addToStorage();
                    }
                });
        executor.shutdown();
        killWindow(refreshProceedButton);
    }

    /**
     * 'No' button in refreshing data window pressing handler.
     */
    @FXML
    protected void refreshCancel() {
        killWindow(refreshCancelButton);
    }
}
