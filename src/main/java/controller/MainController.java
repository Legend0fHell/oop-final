package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Dictionary/DictionaryInterface.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("English Learning Application");
        stage.show();
    }

}

