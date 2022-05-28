package gitjet.controller;

import gitjet.model.repository.RepositoriesHandler;
import gitjet.WindowsUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static gitjet.Utils.*;

/**
 * Controller of 'Add repository' window, which appears after click on the '+' button in start menu.
 */
public class AddRepoController {

    /**
     * Text field in adding new repository window.
     */
    @FXML
    private TextField repositoryURLField;

    /**
     * Number field in adding new repository window.
     */
    @FXML
    private TextField numberOfRepositoriesField;

    /**
     * Add repository by URL pane.
     */
    @FXML
    private TitledPane urlPane;

    /**
     * Search for repositories pane.
     */
    @FXML
    private TitledPane numberPane;

    /**
     * 'Submit' button in adding new repository window pressing handler.
     */
    @FXML
    protected void newRepoSubmit() {
        String textFieldValue = repositoryURLField.getText();
        String numberLabelValue = numberOfRepositoriesField.getText();
        RepositoriesHandler repositoriesHandler = new RepositoriesHandler();

        if (urlPane.isExpanded()) {
            if (isLink(textFieldValue)) {
                repositoriesHandler.runUpdatingThread(textFieldValue);
                WindowsUtils.closeWindow(repositoryURLField);
            } else {
                WindowsUtils.createErrorWindow("Please input a link to GitHub repository");
            }
        } else if (numberPane.isExpanded()) {
            if (isNumber(numberLabelValue)) {
                int number = Integer.parseInt(numberLabelValue);
                if (number > 1000 || number < 1) {
                    WindowsUtils.createErrorWindow("Please pick a number between 1-1000");
                } else {
                    repositoriesHandler.runSearchingThread(number);
                    WindowsUtils.closeWindow(repositoryURLField);
                }
            } else {
                WindowsUtils.createErrorWindow("Please pick a number between 1-1000");
            }
        } else {
            WindowsUtils.createErrorWindow("Please choose the way to add a repository");
        }
    }
}
