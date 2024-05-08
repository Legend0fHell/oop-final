package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import logic.API;
import logic.DictionaryManagement;
import logic.Word;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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
    private int currentOperation = -1;

    @FXML
    private ListView<String> wordsList;

    @FXML
    private TextField wordInputField;

    @FXML
    private WebView webView;

    @FXML
    private VBox vBox1;

    @FXML
    private VBox vBox2;

    @FXML
    private VBox vBox3;

    @FXML
    private TextArea textField;

    @FXML
    private TextArea translated;

    @FXML
    private Button actionButton;

    @FXML
    private ImageView loadingIndicator;

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
        vBox2.setVisible(false);
        vBox2.setManaged(false);

        vBox3.setVisible(false);
        vBox3.setManaged(false);
    }

    @FXML
    private void handleButton2Click() {
        // Hiển thị GridPane khi nhấp vào nút 2
        vBox1.setVisible(false);
        vBox1.setManaged(false);
        vBox2.setVisible(true);
        vBox2.setManaged(true);

        vBox3.setVisible(false);
        vBox3.setManaged(false);
    }

    @FXML
    private void handleButton3Click() {
        // Hiển thị GridPane khi nhấp vào nút 2
        vBox1.setVisible(false);
        vBox1.setManaged(false);
        vBox2.setVisible(false);
        vBox2.setManaged(false);

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
        handlePreAction(1);
    }

    @FXML
    public void handleUpdate() {
        handlePreAction(2);
    }

    @FXML
    public void handleDelete() {
        handlePreAction(3);
    }

    private void handlePreAction(int operation) {
        currentOperation = operation;
        if (currentOperation == 1) {
            actionButton.setText("Add");
        }
        if (currentOperation == 2) {
            actionButton.setText("Update");
            String selectedWord = wordsList.getSelectionModel().getSelectedItem();
            System.out.println(selectedWord);
            if (selectedWord == null || selectedWord.isEmpty()) {
                handleButton2Click();
                return;
            }
            Word word = getWordMeaning(selectedWord);
            wordField.setText(word.getName());
            meaningArea.setText(word.getMeaning());
        }
        if(currentOperation == 3) {
            String selectedWord = wordsList.getSelectionModel().getSelectedItem();
            if (selectedWord == null || selectedWord.isEmpty()) {
                return;
            }
            deleteWord(new Word(selectedWord, ""));
            handlePostAction();
            return;
        }
        handleButton2Click();
    }
    private void handlePostAction() {
        if(currentOperation == 1) {
            String newWord = wordField.getText();
            searchField.setText(newWord);
            handleDefinition(newWord);
        }
        if (currentOperation == 2) {
            String updatedWord = wordField.getText();
            searchField.setText(updatedWord);
            handleDefinition(updatedWord);
        }
        if (currentOperation == 3) {
            searchField.setText("");
            handleDefinition("");
        }
        handleActionCancel();
    }
    public void handleAction() {
        if (currentOperation == 1) {
            insertWord(new Word(wordField.getText(), meaningArea.getText()));
        }
        if (currentOperation == 2) {
            updateWord(new Word(wordField.getText(), ""), new Word(wordField.getText(), meaningArea.getText()));
        }
        handlePostAction();
    }

    public void handleActionCancel() {
        currentOperation = -1;
        wordField.clear();
        meaningArea.clear();
        handleButton1Click();
        handleSearch(); // Refresh the list
        wordsList.getSelectionModel().selectFirst();
        wordsList.getFocusModel().focus(0);
    }

    // Modified handleDefinition to accept a word as parameter
    private void handleDefinition(String wordToDefine) {
        WebEngine webEngine = webView.getEngine();
        if (!wordToDefine.isEmpty()) {
            Word word = super.getWordMeaning(wordToDefine.toLowerCase());
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
            Image image = new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/HangmanGame/HangmanIcons/img_7.png")));
            // Load the FXML file for the Hangman game
            Parent root = FXMLLoader.load(getClass().getResource("/HangmanGame/HangmanGameController.fxml"));
            // Create a new stage for the Hangman game
            Stage hangmanStage = new Stage();
            hangmanStage.setTitle("Hangman Game"); // Set the title for the Hangman game window
            hangmanStage.getIcons().add(image);
            hangmanStage.setScene(new Scene(root)); // Set the scene for this stage
            hangmanStage.show(); // Display the Hangman game window
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
    }

    @FXML
    private void handleQuizgameLaunch() {
        try {
            Image image = new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/EnglishQuizzGame/Icons/ideas.png")));
            // Load the FXML file for the Hangman game
            Parent root = FXMLLoader.load(getClass().getResource("/EnglishQuizzGame/QuizGameController.fxml"));
            // Create a new stage for the Hangman game
            Stage hangmanStage = new Stage();
            hangmanStage.setTitle("Quiz Game"); // Set the title for the Hangman game window
            hangmanStage.getIcons().add(image);
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
        CompletableFuture.supplyAsync(() -> {
            handleLoadingIndicator(true);
            return null;
        }).thenRun(() -> {
            try {
                API.speech(selectedWord, API.ENGLISH_US, 1.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {
            handleLoadingIndicator(false);
        });
    }

    @FXML
    private void handleUKspeech(ActionEvent event) throws Exception {
        String selectedWord = wordsList.getSelectionModel().getSelectedItem();
        CompletableFuture.supplyAsync(() -> {
            handleLoadingIndicator(true);
            return null;
        }).thenRun(() -> {
            try {
                API.speech(selectedWord, API.ENGLISH_UK, 1.0f);
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
