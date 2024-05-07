package controller;

import database.Database;
import logic.Trie;
import logic.Word;

import java.util.List;

public class DictionaryManagement {
    private Trie trie;
    private Database database;

    public DictionaryManagement() {
        trie = new Trie();
        try {
            database = new Database();
            List<Word> words = database.getAllWordsName();
            int wordCnt = 0;
            for (Word word : words) {
                if (trie.insertWord(word)) wordCnt++;
            }
            System.out.printf("Database loaded successfully!\n%d words in the DB, %d words in the Trie.\n", words.size(), wordCnt);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public List<String> search(String name) {
        return trie.getAllWordNames(name);
    }

    public void insertWord(Word word) {
        trie.insertWord(word);
        database.insertWord(word);
    }

    public void deleteWord(Word word) {
        trie.deleteWord(word);
        database.deleteWord(word);
    }

    public void updateWord(Word word_before, Word word_after) {
        trie.updateWord(word_before, word_after);
        database.updateWord(word_before, word_after);
    }

    public Word displayWordMeaning(String name) {
        return database.getMeaning(name);
    }
}
