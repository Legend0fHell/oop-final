package controller;

import database.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import logic.Word;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class HangmanGameController implements Initializable {


    @FXML
    private TextField guessTextField;

    @FXML
    private ImageView imageViewHangman;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text questionNumberText;
    @FXML
    private Text scoreText;
    @FXML
    private Text textForWord;
    @FXML
    private Text endGameText;
    @FXML
    private Text timerText;
    @FXML
    private Text guideText;
    @FXML
    private Text usedWordText;

    @FXML
    private Button enterButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button nextQuestion;
    @FXML
    private Button deleteButton;
    @FXML
    private Button startButton;
    @FXML
    private Button backHomeButton;

    @FXML
    private WebView webView;
    private WebEngine engine;


    private Alert warningAlert;
    private Alert confirmationAlert;
    private Timer timer;
    //private String word;
    private int countDown = 30;
    private int questionNumber = 0;
    private int score = 0;
    private int highestScore;
    private int livePose = 0;

    private boolean isPlaying;

    private List<Character> usedCharacter;

    private Database database;
    private Word word;


    private StringBuilder secretWord = new StringBuilder();

    private ArrayList<String> humanLives = new ArrayList<>(
            Arrays.asList(
                    "/HangmanGame/HangmanPictures/0.jpg",
                    "/HangmanGame/HangmanPictures/1.jpg",
                    "/HangmanGame/HangmanPictures/2.jpg",
                    "/HangmanGame/HangmanPictures/3.jpg",
                    "/HangmanGame/HangmanPictures/4.jpg",
                    "/HangmanGame/HangmanPictures/5.jpg",
                    "/HangmanGame/HangmanPictures/6.jpg",
                    "/HangmanGame/HangmanPictures/7.jpg",
                    "/HangmanGame/HangmanPictures/8.jpg",
                    "/HangmanGame/HangmanPictures/9.jpg",
                    "/HangmanGame/HangmanPictures/10.jpg",
                    "/HangmanGame/HangmanPictures/11.jpg"

            )
    );

    /**
     * setup buttons icon, size.
     * set invisible for every node except start game button.
     * initialize array list.
     * set up guess word for game.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBackGround();
        setIconStartButton();

        Image image;
        ImageView icon;


        // set initial image of hangman when start the game
        image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream(humanLives.getFirst())));
        imageViewHangman.setImage(image);

        // set icon image to backHome button.
        image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/HangmanGame/HangmanIcons/img_5.png")));
        icon = new ImageView(image);
        icon.setFitHeight(30);
        icon.setFitWidth(35);
        backHomeButton.setGraphic(icon);

        // set icon image to reset button
        image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/HangmanGame/HangmanIcons/img_8.png")));
        icon = new ImageView(image);
        icon.setFitHeight(25);
        icon.setFitWidth(25);
        resetButton.setGraphic(icon);

        //set icon image to nextQuestionButton
        image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/HangmanGame/HangmanIcons/img_2.png")));
        icon = new ImageView(image);
        icon.setFitHeight(30);
        icon.setFitWidth(60);
        nextQuestion.setGraphic(icon);



        // setWarningAlert
        warningAlert = new Alert(Alert.AlertType.WARNING);
        image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/HangmanGame/HangmanIcons/img_4.png")));
        icon = new ImageView(image);
        warningAlert.setTitle("Invalid Input");
        warningAlert.setGraphic(icon);

        //setConfirmationAlert.
        confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/HangmanGame/HangmanIcons/img_6.png")));
        icon = new ImageView(image);
        confirmationAlert.setTitle("Are you sure bro");
        confirmationAlert.setContentText("Are you sure want to quit? Your progress will not be saved");
        confirmationAlert.setGraphic(icon);

        // initialize things
        usedCharacter = new ArrayList<>();
        //insert database into a LinkHashMap.
        //count the lineNumber as the number of words.


        try {
            database = new Database();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        word = new Word();



        // set Highest score.

        try {
            setHighestScore();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        engine = webView.getEngine();

        enterButton.setVisible(false);
        deleteButton.setVisible(false);
        resetButton.setVisible(false);
        backHomeButton.setVisible(false);
        nextQuestion.setVisible(false);
        usedWordText.setVisible(false);
        timerText.setVisible(false);
        imageViewHangman.setVisible(false);
        textForWord.setVisible(false);
        guessTextField.setVisible(false);

        guideText.setVisible(false);
        scoreText.setVisible(false);
        questionNumberText.setVisible(false);
        endGameText.setVisible(false);

        webView.setVisible(false);

        startButton.setVisible(true);
    }

    /**
     * set background image for game.
     */
    private void setBackGround() {
        Image  image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/HangmanGame/HangmanIcons/img.png")));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        anchorPane.setBackground(background);
    }

    /**
     * set icon for start button
     */
    private void setIconStartButton() {
        Image  image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/HangmanGame/HangmanIcons/img_1.png")));
        ImageView imageView = new ImageView(image);
        startButton.setGraphic(imageView);
    }

    /**
     * highest score is saved in a txt file, it won't be reset when the program is closed.
     * if  the file is empty, set the highest score 0.
     */
    public void setHighestScore() throws IOException {
        File file = new File("src/main/resources/HangmanGame/highestScore");
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        if (line == null) highestScore = 0;
        else {
            highestScore = Integer.parseInt(line);
        }
        reader.close();
    }

    public void updateHighestScore(int currentScore) throws IOException {
        if (highestScore >= currentScore) return;
        System.out.println("Updated");
        highestScore = currentScore;


        FileWriter file = new FileWriter("src/main/resources/HangmanGame/highestScore");
        file.write(String.valueOf(highestScore));
        file.close();

    }


    /**
     * function is called when start game button is clicked.
     * set visible for button, text, text field and pictures.
     * reset scores, number of question, and guess word.
     */

    @FXML
    protected void startGame() {
        guessTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                try {
                    playTurn();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        enterButton.setVisible(true);
        deleteButton.setVisible(true);
        resetButton.setVisible(false);
        backHomeButton.setVisible(true);

        timerText.setVisible(true);
        imageViewHangman.setVisible(true);
        textForWord.setVisible(true);
        guessTextField.setVisible(true);
        guideText.setVisible(true);
        scoreText.setVisible(true);
        questionNumberText.setVisible(true);
        usedWordText.setVisible(true);

        startButton.setVisible(false);



        reset();

    }

    /**
     * function is called when click home button, go back to the start.
     * set invisible for every node, set visible for start game button.
     * cancel timer if it is running.
     */

    @FXML
    protected void beforeStartGame() {
        if (isPlaying) {
            if (confirmationAlert.showAndWait().get() == ButtonType.CANCEL) return;
        }


        enterButton.setVisible(false);
        deleteButton.setVisible(false);
        resetButton.setVisible(false);
        backHomeButton.setVisible(false);

        timerText.setVisible(false);
        imageViewHangman.setVisible(false);
        textForWord.setVisible(false);
        guessTextField.setVisible(false);
        guideText.setVisible(false);
        scoreText.setVisible(false);
        questionNumberText.setVisible(false);
        endGameText.setVisible(false);
        usedWordText.setVisible(false);

        webView.setVisible(false);

        startButton.setVisible(true);

        if (timer != null)
            timer.cancel();

    }

    /**
     * set the time to 90 seconds, counting down to 0.
     */

    protected void counting() {
        if (timer != null) timer.cancel();
        countDown = 90;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (countDown >= 0) {
                    timerText.setText(countDown + "");
                    countDown--;
                } else {
                    try {
                        runningOutOfTime();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    timer.cancel();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * set up guess word. Guess word is chosen randomly.
     * the number of hints is one third the word's length.
     * the positions of hints are also random.
     */

    protected void setupWord() {
        isPlaying = true;
        int random;

        // delete the guess word before
        this.textForWord.setText("");
        secretWord.setLength(0);

        // new score, question number.
        scoreText.setText("Score " + score);
        questionNumber++;
        questionNumberText.setText("Question number " + questionNumber);

        // set word from database
        word = database.getRandomWord();
        while (word.getName().length() > 10 || word.getName().length() < 2 || word.getName().contains("-")
        || word.getName().contains(" ") || word.getName().contains("'")) {

            word = database.getRandomWord();
        }

        int numberOfHint = word.getName().length() / 3;
        ArrayList<Integer> hintList = new ArrayList<>();
        for (int i = 0; i < numberOfHint; i++) {
            random = (int) (Math.random() * (word.getName().length() - 1));
            hintList.add(random);
        }

        for (int i = 0; i < word.getName().length(); i++) {
            if (!hintList.contains(i)) {
                secretWord.append("*");
            } else {
                secretWord.append(word.getName().charAt(i));
            }

        }

        guessTextField.setEditable(true);
        usedWordText.setText("a b c d e f g h i j k l m n o p q r s t u v w x y z");
        textForWord.setText(secretWord.toString());
        webView.setVisible(false);
        counting();
    }


    // delete text typed in.
    @FXML
    protected void delete() {
        guessTextField.setText("");
    }

    /**
     * function is called when player lost the game.
     * reset score, livePose, question number, hangman image.
     * set up new guess word.
     */
    @FXML
    protected void reset() {
        score = 0;
        livePose = 0;
        questionNumber = 0;
        secretWord.setLength(0);
        usedCharacter.clear();


        Image image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream(humanLives.get(livePose))));
        imageViewHangman.setImage(image);
        guessTextField.clear();
        textForWord.setText("");
        endGameText.setVisible(false);
        enterButton.setDisable(false);
        resetButton.setVisible(false);

        setupWord();


    }

    /**
     * function is called when next question button is pressed.
     * reset livePose, hangman image and guess word.
     */
    @FXML
    protected void nextQuestion() {

        secretWord.setLength(0);
        usedCharacter.clear();
        livePose = 0;


        Image image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream(humanLives.get(livePose))));
        imageViewHangman.setImage(image);
        guessTextField.clear();
        textForWord.setText("");

        endGameText.setVisible(false);
        enterButton.setDisable(false);
        nextQuestion.setVisible(false);

        setupWord();

    }

    /**
     * 90 seconds count down is over. Player lost the game.
     * set reset button visible,
     */

    protected void runningOutOfTime() throws IOException {
        Image image = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream(humanLives.get(11))));
        imageViewHangman.setImage(image);
        endGameText.setText("You run out of time. Play again?\n" + "Your highest score: " + highestScore);
        endGameText.setVisible(true);
        enterButton.setDisable(true);
        resetButton.setVisible(true);
        textForWord.setText(word.getName());
    }

    /**
     * function is called when click answer button.
     * check if the character is correct or used only once
     * delete that word in guess word text field.
     */

    @FXML
    protected void playTurn() throws IOException {

        // warning alert show up if player's answer longer than one word
        if (guessTextField.getText().length() > 1) {
            warningAlert.setContentText("You must type only one word");
            guessTextField.setText("");
            warningAlert.showAndWait();
            return;
        }
        if (guessTextField.getText().isEmpty()) {
            return;
        }

        char guess = this.guessTextField.getText().charAt(0);
        guess = Character.toLowerCase(guess);


        if (!usedCharacter.contains(guess)) {
            StringBuilder temp = new StringBuilder();
            temp.append(usedWordText.getText());
            for (int i = 0; i < temp.length(); i++) if (temp.charAt(i) == guess) {
                temp.deleteCharAt(i);
                break;
            }
            usedWordText.setText(temp.toString());
        }


        // if the answer is correct.
        if (word.getName().contains(guess + "") && !usedCharacter.contains(guess)) {
            usedCharacter.add(guess);
            for (int i = 0; i < word.getName().length(); i++) {
                if (word.getName().charAt(i) == guess) {
                    secretWord.setCharAt(i, guess);
                }
            }
            textForWord.setText(secretWord.toString());

            if (word.getName().equals(this.textForWord.getText())) {

                score += 10;
                updateHighestScore(score);

                scoreText.setText("Score " + this.score);
                endGameText.setText("Correct! Next question?");
                guessTextField.setEditable(false);

                 engine.loadContent(word.getMeaning());
                 webView.setVisible(true);

                endGameText.setVisible(true);
                enterButton.setDisable(true);
                nextQuestion.setVisible(true);
                timer.cancel();
            }

        } else {
            // if the answer is incorrect.
            Image image = new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream(humanLives.get(++livePose))));
            imageViewHangman.setImage(image);
            if (livePose == 10) {
                isPlaying = false;

                image = new Image(Objects.requireNonNull(getClass()
                        .getResourceAsStream(humanLives.get(11))));
                imageViewHangman.setImage(image);
                endGameText.setText("You lose! Play again?\n" + "Your highest score: " + highestScore);
                endGameText.setVisible(true);
                enterButton.setDisable(true);
                resetButton.setVisible(true);
                webView.setVisible(true);

                guessTextField.setEditable(false);

                engine.loadContent(word.getMeaning());



                textForWord.setText(word.getName());
                timer.cancel();

            }
        }

        this.guessTextField.setText("");

    }

}