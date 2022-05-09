package gitjet;

import gitjet.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-window-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        Controller controller = fxmlLoader.getController();

        stage.setTitle("GitJet");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}