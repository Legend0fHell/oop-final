package controller;

import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logic.Word;

import java.sql.SQLException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        //Parent root = FXMLLoader.load(getClass().getResource("/HangmanGame/HangmanGameController.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/Dictionary/DictionaryInterface.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//        stage.setTitle("Hangman Game");
        stage.setTitle("Dictionary");
        stage.show();

    }

}

