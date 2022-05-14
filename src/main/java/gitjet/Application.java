package gitjet;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * The main program entry class.
 */
public class Application extends javafx.application.Application {
    /**
     * Start program handler.
     * @param stage Primary stage.
     * @throws IOException Throws if problems occurred while creating new window.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("GitJet");
        stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/icon.png"))));
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Close/finish program handler. Used to clean up.
     */
    @Override
    public void stop() {
        File clonePath = new File("clones");
        clonePath.deleteOnExit();
    }

    /**
     * The main program entry point. Launches JavaFX.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch();
    }
}