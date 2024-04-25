package database;

import logic.Word;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    private static final String url = "jdbc:sqlite:src\\main\\java\\database\\data.sqlite3";
    public Database() throws SQLException {
        connect();
    }
    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public ArrayList<Word> getAllWordsName() {
        connect();

        ArrayList<Word> words = new ArrayList<>();
        try {
            String query = "SELECT word FROM av";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Word word = new Word(resultSet.getString("word"), "");
                words.add(word);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return words;
    }

    public Word getMeaning(String name) {
        connect();

        Word word = new Word(name, "");
        try {
            String query = "SELECT html FROM av WHERE word = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                word.setMeaning(resultSet.getString("html"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return word;
    }

    public Word getRandomWord() {
        connect();

        Word word = new Word();
        try {
            String query = "SELECT word, html FROM av ORDER BY RANDOM() LIMIT 1";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                word.setName(resultSet.getString("word"));
                word.setMeaning(resultSet.getString("html"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return word;
    }

    public void insertWord(Word word) {
        connect();

        try {
            String query = "INSERT INTO av (word, html) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, word.getName());
            preparedStatement.setString(2, word.getMeaning());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public void deleteWord(Word word) {
        connect();

        try {
            String query = "DELETE FROM av WHERE word = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, word.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public void updateWord(Word word_before, Word word_after) {
        deleteWord(word_before);
        insertWord(word_after);
    }
}
