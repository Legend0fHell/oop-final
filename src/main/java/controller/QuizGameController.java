package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class QuizGameController implements Initializable {
    @FXML
    private Button answerA;
    @FXML
    private Button answerB;
    @FXML
    private Button answerC;
    @FXML
    private Button answerD;
    @FXML
    private Button resetButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button startButton;

    @FXML
    private Text bigQuestionTextField;
    @FXML
    private Text endGameText;
    @FXML
    private Text scoreTextField;
    @FXML
    private Text smallQuestionTextField;
    @FXML
    private Text timerTextField;

    Alert alert;

    private int basedTime = 60;
    private int score = 0;
    private int questionNumber = 0;
    private int numberOfQuestion = 0;

    private boolean isGameOver;

    private List<Integer> usedQuestions;
    private HashMap<Integer, Question> questions;
    private Question currentQuestion;

    private Timer timer;

    /**
     * set invisible for everything except for the start button.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resetButton.setVisible(false);
        exitButton.setVisible(false);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);

        smallQuestionTextField.setVisible(false);
        bigQuestionTextField.setVisible(false);
        scoreTextField.setVisible(false);
        timerTextField.setVisible(false);
        endGameText.setVisible(false);

        startButton.setVisible(true);



        try {
            setUpQuestionsAndAnswer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * insert database into a HashMap
     */


    private void setUpQuestionsAndAnswer() throws IOException {
        questions = new HashMap<>();
        List<String> fileNames = new ArrayList<>(
                Arrays.asList(
                        "src/main/java/database/EnglishQuizGame/ClosetMeaning.txt",
                        "src/main/java/database/EnglishQuizGame/Comparison.txt",
                        "src/main/java/database/EnglishQuizGame/ModelVerb.txt",
                        "src/main/java/database/EnglishQuizGame/Pronunciation.txt",
                        "src/main/java/database/EnglishQuizGame/TagQuestion.txt"
                )
        );
        for (String path : fileNames) {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String bigQuestion = bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while (line != null) {
                StringBuilder question = new StringBuilder();
                // small question
                question.append(line + "\n");
                line = bufferedReader.readLine();
                // answer A
                question.append(line + "\n");
                line = bufferedReader.readLine();
                // answer B
                question.append(line + "\n");
                line = bufferedReader.readLine();
                // answer C
                question.append(line + "\n");
                line = bufferedReader.readLine();
                // answer D
                question.append(line + "\n");


                line = bufferedReader.readLine();

                // correct answer
                if (line != null && !line.isEmpty()) {
                    char answer = line.charAt(0);
                    questions.put(numberOfQuestion, new Question(bigQuestion, question.toString(), answer));
                    numberOfQuestion++;
                }

                line = bufferedReader.readLine();
            }
        }

    }

    /**
     * question is chosen randomly.
     * timer is started count down.
     */

    public void setUpNewQuestion() {
        // the game is still ongoing
        isGameOver = false;

        // set every button accessible
        answerA.setDisable(false);
        answerB.setDisable(false);
        answerC.setDisable(false);
        answerD.setDisable(false);

        // check if the following question is already used in previous questions or not.
        scoreTextField.setText("Score " + score);
        int random = (int) (Math.random() * numberOfQuestion - 1);

        while (usedQuestions.contains(random)) {
            random = (int) (Math.random() * numberOfQuestion - 1);
        }
        currentQuestion = new Question(questions.get(random).getBigQuestion(),
                questions.get(random).getQuestion(), questions.get(random).getAnswer());
        bigQuestionTextField.setText(currentQuestion.getBigQuestion());
        smallQuestionTextField.setText("Question " + (++questionNumber) + currentQuestion.getQuestion());

        setTimer();
    }

    /**
     * reset button only available when player finish the game.
     * function called when reset button is clicked.
     * every button is set accessible.
     */

    @FXML
    public void reset() {
        usedQuestions.clear();

        resetButton.setVisible(false);
        endGameText.setVisible(false);

        bigQuestionTextField.setVisible(true);
        smallQuestionTextField.setVisible(true);

        answerA.setDisable(false);
        answerB.setDisable(false);
        answerC.setDisable(false);
        answerD.setDisable(false);

        score = 0;
        questionNumber = 0;


        setUpNewQuestion();
    }

    /**
     * function is called when exit button is clicked.
     * set every button, text invisible.
     * set start button visible.
     * reset score and question number to 0.
     * cancel timer.
     */

    public void exitButtonClicked() {
        if (!isGameOver) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure bro");
            alert.setContentText("Do you want to exit. Your progress won't be save");
            if (alert.showAndWait().get() == ButtonType.CANCEL) return;
        }


        score = 0;
        questionNumber = 0;

        resetButton.setVisible(false);
        exitButton.setVisible(false);
        answerA.setVisible(false);
        answerB.setVisible(false);
        answerC.setVisible(false);
        answerD.setVisible(false);

        smallQuestionTextField.setVisible(false);
        bigQuestionTextField.setVisible(false);
        scoreTextField.setVisible(false);
        timerTextField.setVisible(false);
        endGameText.setVisible(false);

        timer.cancel();

        startButton.setVisible(true);

    }

    /**
     * function is called when start button is clicked.
     * set visible for some button and invisible for start button itself.
     * set up new question.
     */

    public void startButtonClicked() {
        usedQuestions = new ArrayList<>();

        resetButton.setVisible(false);

        exitButton.setVisible(true);
        answerA.setVisible(true);
        answerB.setVisible(true);
        answerC.setVisible(true);
        answerD.setVisible(true);

        setUpNewQuestion();

        smallQuestionTextField.setVisible(true);
        bigQuestionTextField.setVisible(true);
        scoreTextField.setVisible(true);
        timerTextField.setVisible(true);

        startButton.setVisible(false);
    }

    /**
     * 60 second counting down.
     */

    public void setTimer() {
        if (timer != null) timer.cancel();
        timer = new Timer();
        basedTime = 60;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (basedTime >= 0) {
                    timerTextField.setText("Time left: " + basedTime);
                    basedTime--;
                } else {
                    timer.cancel();
                    timerTextField.setText("You just ran out of time");
                    nextQuestion();


                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    /**
     * function is called when an answer is chosen.
     * mark the correct answer with blue and the incorrect ones with red color.
     * Time delay 0.5 second.
     * set up new question if the just finished question is not question number 50.
     */


    public void displayResult() {
        char a = currentQuestion.getAnswer();
        if (a == 'A') answerA.setTextFill(Color.BLUE);
        else answerA.setTextFill(Color.RED);

        if (a == 'B') answerB.setTextFill(Color.BLUE);
        else answerB.setTextFill(Color.RED);

        if (a == 'C') answerC.setTextFill(Color.BLUE);
        else answerC.setTextFill(Color.RED);

        if (a == 'D') answerD.setTextFill(Color.BLUE);
        else answerD.setTextFill(Color.RED);

        answerA.setDisable(true);
        answerB.setDisable(true);
        answerC.setDisable(true);
        answerD.setDisable(true);

        timer.cancel();

        Timeline pause = new Timeline(new KeyFrame(Duration.seconds(0.01)
                , new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                answerA.setTextFill(Color.BLACK);
                answerB.setTextFill(Color.BLACK);
                answerC.setTextFill(Color.BLACK);
                answerD.setTextFill(Color.BLACK);

                if (!isGameOver)
                    setUpNewQuestion();

            }
        }));
        pause.setCycleCount(1);
        pause.play();


    }

    /**
     * function is called after an answer is chosen.
     * display final result if 50 questions are finished.
     * mocking players for low grades.
     * disable every answer button.
     * set visible for reset button.
     */

    public void nextQuestion() {

        if (questionNumber < 50) {
            displayResult();
        } else {
            timer.cancel();

            isGameOver = true;
            displayResult();

            scoreTextField.setText("Score " + score);

            if (score < 100) endGameText.setText("You only scored " + score + " out of 500, what a loser");
            else if (score < 200) endGameText.setText("Your score is " + score +
                    "\nout of 500, my five year old brother can do better than this");
            else if (score < 300)
                endGameText.setText("You only got " + score + " out of 500, \ndo you even learn anything at school");
            else if (score < 350)
                endGameText.setText("Scoring " + score + " out of 500, \nthis is not something you should proud of");
            else if (score < 400)
                endGameText.setText("Your score is " + score + " out of 500, \n is this the best you can do?");
            else if (score < 450)
                endGameText.setText(score + " out of 500,\n there's still a lot for you to learn");
            else if (score < 500)
                endGameText.setText(score + " out of 500, don't be so self-satisfied,\n there next time i won't go easy ");
            else endGameText.setText("500 out of 500, you cheated, right?");

            smallQuestionTextField.setVisible(false);
            bigQuestionTextField.setVisible(false);
            endGameText.setVisible(true);
            resetButton.setVisible(true);

            answerA.setDisable(true);
            answerB.setDisable(true);
            answerC.setDisable(true);
            answerD.setDisable(true);
        }

    }


    public void answerAClicked() {
        if (currentQuestion.getAnswer() == 'A') {
            score += 10;
        }
        nextQuestion();
    }

    public void answerBClicked() {
        if (currentQuestion.getAnswer() == 'B') {
            score += 10;
        }
        nextQuestion();
    }

    public void answerCClicked() {
        if (currentQuestion.getAnswer() == 'C') {
            score += 10;
        }
        nextQuestion();
    }

    public void answerDClicked() {

        if (currentQuestion.getAnswer() == 'D') {
            score += 10;
        }
        nextQuestion();
    }


}
