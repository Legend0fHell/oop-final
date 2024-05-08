package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.API;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class TranslateController implements Initializable {

    @FXML
    private TextArea textField;

    @FXML
    private TextArea translated;

    @FXML
    private ComboBox<String> fromLang;

    @FXML
    private ComboBox<String> toLang;

    @FXML
    private ImageView loadingIndicator;

    private static final String VIETNAMESE_DISPLAY = "Vietnamese";
    private static final String ENGLISH_DISPLAY = "English";
    private static final String AUTO_DISPLAY = "Auto-detect";

    public TranslateController() throws Exception {
        super();
    }

    // This method initializes the controller and sets up the mouse click event for the ListView
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fromLang.getItems().addAll(AUTO_DISPLAY, ENGLISH_DISPLAY, VIETNAMESE_DISPLAY);
        toLang.getItems().addAll(ENGLISH_DISPLAY, VIETNAMESE_DISPLAY);
        fromLang.setValue(AUTO_DISPLAY);
        toLang.setValue(VIETNAMESE_DISPLAY);
    }

    @FXML
    private void handleButton1Click() {
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.setScene(MainController.dictionaryScene);
        stage.show();
    }

    @FXML
    private void handleHangmanLaunch() {
        try {
            // Load the FXML file for the Hangman game
            Parent root = FXMLLoader.load(getClass().getResource("/HangmanGame/HangmanGameController.fxml"));
            // Create a new stage for the Hangman game
            Stage hangmanStage = new Stage();
            hangmanStage.setTitle("Hangman Game"); // Set the title for the Hangman game window
            hangmanStage.setScene(new Scene(root)); // Set the scene for this stage
            hangmanStage.show(); // Display the Hangman game window
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
    }

    @FXML
    private void handleQuizgameLaunch() {
        try {
            // Load the FXML file for the Hangman game
            Parent root = FXMLLoader.load(getClass().getResource("/EnglishQuizzGame/QuizGameController.fxml"));
            // Create a new stage for the Hangman game
            Stage hangmanStage = new Stage();
            hangmanStage.setTitle("Quiz Game"); // Set the title for the Hangman game window
            hangmanStage.setScene(new Scene(root)); // Set the scene for this stage
            hangmanStage.show(); // Display the Hangman game window
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
    }

    @FXML
    public void handleTranslate() throws Exception {
        String textToTranslate = textField.getText().trim();
        if (textToTranslate.isEmpty()) {
            return;
        }
        String fromLangStr = switch (fromLang.getValue()) {
            case AUTO_DISPLAY -> API.AUTO;
            case ENGLISH_DISPLAY -> API.ENGLISH_US;
            case VIETNAMESE_DISPLAY -> API.VIETNAMESE;
            default -> API.AUTO;
        };
        String toLangStr = switch (toLang.getValue()) {
            case ENGLISH_DISPLAY -> API.ENGLISH_US;
            case VIETNAMESE_DISPLAY -> API.VIETNAMESE;
            default -> API.VIETNAMESE;
        };
        CompletableFuture.supplyAsync(() -> {
            handleLoadingIndicator(true);
            return null;
        }).thenRun(() -> {
            try {
                String translatedText = API.translate(textToTranslate, fromLangStr, toLangStr);
                translated.setText(translatedText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {
            handleLoadingIndicator(false);
        });

    }

    @FXML
    public void handleSpeech() throws Exception {
        if (translated.getText().trim().isEmpty()) {
            return;
        }
        String translatedText = translated.getText().trim();
        String toLangStr = switch (toLang.getValue()) {
            case ENGLISH_DISPLAY -> API.ENGLISH_US;
            case VIETNAMESE_DISPLAY -> API.VIETNAMESE;
            default -> API.VIETNAMESE;
        };
        CompletableFuture.supplyAsync(() -> {
            handleLoadingIndicator(true);
            return null;
        }).thenRun(() -> {
            try {
                API.speech(translatedText, toLangStr, 1.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {
            handleLoadingIndicator(false);
        });
    }

    public void handleLoadingIndicator(boolean state) {
        loadingIndicator.setVisible(state);
    }

}
