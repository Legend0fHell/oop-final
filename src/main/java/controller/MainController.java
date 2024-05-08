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
    public static Scene dictionaryScene;
    public static Scene translateScene;

    @Override
    public void start(Stage stage) throws Exception {
        dictionaryScene = new Scene(FXMLLoader.load(getClass().getResource("/Dictionary/DictionaryInterface.fxml")));
        translateScene = new Scene(FXMLLoader.load(getClass().getResource("/Dictionary/TranslateInterface.fxml")));
        stage.setScene(dictionaryScene);
        stage.setTitle("English Learning Application");
        stage.show();
    }

}

