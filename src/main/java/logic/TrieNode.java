package logic;

public class TrieNode {
    private TrieNode[] TrieNode = new TrieNode[31];
    private boolean end;
    private Word word;

    public TrieNode() {
        for (int i = 0; i < 31; i++) {
            TrieNode[i] = null;
        }
        this.end = false;
        this.word = null;
    }

    public TrieNode[] getAllNodes() {
        return TrieNode;
    }

    public boolean getEnd() {
        return end;
    }

    public Word getWord() {
        return word;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
