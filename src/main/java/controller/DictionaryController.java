package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import logic.API;
import logic.Word;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import java.io.IOException;

public class DictionaryController extends DictionaryManagement implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TextField wordField;

    @FXML
    private TextArea meaningArea;

    @FXML
    private ObservableList<String> filteredWordsObsList;
    private ObservableList<String> allWordsCacheObsList;

    @FXML
    private ListView<String> wordsList;

    @FXML
    private TextField wordInputField;

    @FXML
    private WebView webView;

    @FXML
    private VBox vBox1;

    @FXML
    private GridPane gridPane2;

    @FXML
    private VBox vBox3;

    @FXML
    private TextArea textField;

    @FXML
    private TextArea translated;

    public DictionaryController() throws Exception {
        super();
        filteredWordsObsList = FXCollections.observableArrayList();
        List<String> words = search("");
        allWordsCacheObsList = FXCollections.observableArrayList(words);
        wordsList = new ListView<>(filteredWordsObsList);
    }

    // This method initializes the controller and sets up the mouse click event for the ListView
    public void initialize(URL url, ResourceBundle resourceBundle) {
// make sure the listview updates when the filtered list changes, and handle mouse clicks
        wordsList.setItems(filteredWordsObsList);
        wordsList.setOnMouseClicked(this::handleMouseClick);
        handleButton1Click();

// Add a listener to the search field to update the filtered list when the search field changes
        filteredWordsObsList.addAll(allWordsCacheObsList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String prefix = newValue.toLowerCase().trim();
            filteredWordsObsList.clear();

            if (prefix.isEmpty()) {
                filteredWordsObsList.addAll(allWordsCacheObsList);
            } else {
                try {
                    List<String> words = search(prefix);
                    filteredWordsObsList.addAll(words);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });
    }

    // Event handler for mouse click on ListView
    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 1) {  // Checks if it is a single click
            String selectedWord = wordsList.getSelectionModel().getSelectedItem();
            if (selectedWord != null && !selectedWord.isEmpty()) {
                handleDefinition(selectedWord);
            }
        }
    }

    @FXML
    private void handleButton1Click() {
        // Hiển thị VBox khi nhấp vào nút 1
        vBox1.setVisible(true);
        vBox1.setManaged(true);
        gridPane2.setVisible(false);
        gridPane2.setManaged(false);

        vBox3.setVisible(false);
        vBox3.setManaged(false);
    }

    @FXML
    private void handleButton2Click() {
        // Hiển thị GridPane khi nhấp vào nút 2
        vBox1.setVisible(false);
        vBox1.setManaged(false);
        gridPane2.setVisible(true);
        gridPane2.setManaged(true);

        vBox3.setVisible(false);
        vBox3.setManaged(false);
    }

    @FXML
    private void handleButton3Click() {
        // Hiển thị GridPane khi nhấp vào nút 2
        vBox1.setVisible(false);
        vBox1.setManaged(false);
        gridPane2.setVisible(false);
        gridPane2.setManaged(false);

        vBox3.setVisible(true);
        vBox3.setManaged(true);
    }

    @FXML
    public void handleSearch() {
        String prefix = searchField.getText().toLowerCase().trim();
        filteredWordsObsList.clear();

        if (prefix.isEmpty()) {

            filteredWordsObsList.addAll(allWordsCacheObsList);
        } else {
            List<String> words = search(prefix);
            filteredWordsObsList.addAll(words);
        }
    }

    @FXML
    public void handleAdd() {
        insertWord(new Word(wordField.getText(), meaningArea.getText()));
        handleSearch(); // Refresh the list
    }

    @FXML
    public void handleUpdate() {
        updateWord(new Word(wordField.getText(), ""), new Word(wordField.getText(), meaningArea.getText()));
        handleSearch(); // Refresh the list
    }

    @FXML
    public void handleDelete() {
        deleteWord(new Word(wordField.getText(), ""));
        handleSearch(); // Refresh the list
    }

    // Modified handleDefinition to accept a word as parameter
    private void handleDefinition(String wordToDefine) {
        WebEngine webEngine = webView.getEngine();
        if (!wordToDefine.isEmpty()) {
            Word word = super.displayWordMeaning(wordToDefine.toLowerCase());
            if (word != null) {
                String htmlContent = "<html><body>" + word.getMeaning() + "</body></html>";
                webEngine.loadContent(htmlContent, "text/html");
            } else {
                webEngine.loadContent("<html><body>Word not found.</body></html>", "text/html");
            }
        } else {
            webEngine.loadContent("<html><body>Please enter a word.</body></html>", "text/html");
        }
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
        String translatedText = API.translate(textToTranslate, API.ENGLISH_US, API.VIETNAMESE);
        translated.setText(translatedText);
    }

    @FXML
    public void handleSpeech() throws Exception {
        String translatedText = translated.getText().trim();
        API.speech(translatedText, API.VIETNAMESE, 1.0f);
    }
    @FXML
    private void handleUSspeech(ActionEvent event) throws Exception {
        String selectedWord = wordsList.getSelectionModel().getSelectedItem();
        API.speech(selectedWord, API.ENGLISH_US, 1.0f);
    }

    @FXML
    private void handleUKspeech(ActionEvent event) throws Exception {
        String selectedWord = wordsList.getSelectionModel().getSelectedItem();
        API.speech(selectedWord, API.ENGLISH_UK, 1.0f);
    }


}
