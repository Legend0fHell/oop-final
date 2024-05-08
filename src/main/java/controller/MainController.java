package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MainController extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Image image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/Dictionary/icon.png")));

        Parent root = FXMLLoader.load(getClass().getResource("/Dictionary/DictionaryInterface.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(image);
        stage.setTitle("English Learning Application");
        stage.show();
    }

}

