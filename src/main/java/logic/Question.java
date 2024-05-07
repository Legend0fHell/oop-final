package logic;

public class Question {
    private String bigQuestion;
    private String question;
    private char answer;
    public Question() {

    }

    public Question(String bigQuestion, String question, char answer) {
        this.bigQuestion = bigQuestion;
        this.question = question;
        this.answer = answer;
    }

    public String getBigQuestion() {
        return bigQuestion;
    }

    public void setBigQuestion(String bigQuestion) {
        this.bigQuestion = bigQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public char getAnswer() {
        return answer;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }
}
