package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import logic.Word;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DictionaryController extends DictionaryCommandline {

    @FXML
    private TextField searchField;

    @FXML
    private TextField wordField;

    @FXML
    private TextArea meaningArea;

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

    // This method initializes the controller and sets up the mouse click event for the ListView
    public void initialize() {

        wordsList.setOnMouseClicked(this::handleMouseClick);
        handleButton1Click();
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
    }

    @FXML
    private void handleButton2Click() {
        // Hiển thị GridPane khi nhấp vào nút 2
        vBox1.setVisible(false);
        vBox1.setManaged(false);
        gridPane2.setVisible(true);
        gridPane2.setManaged(true);
    }

    @FXML
    public void handleSearch() {
        wordsList.getItems().clear();
        var results = search(searchField.getText().trim());
        wordsList.getItems().addAll(results);
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
}
