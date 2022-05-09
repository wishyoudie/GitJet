package gitjet;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-window-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("GitJet");
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop() {
        File clonePath = new File("clones");
        clonePath.deleteOnExit();
    }

    public static void main(String[] args) {
        launch();
    }
}