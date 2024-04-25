class TrieNode {
    private TrieNode[] TrieNode = new TrieNode[26];
    private boolean end;

    private Word word;

    public TrieNode[] getAllNodes() {
        return TrieNode;
    }

    public void setAllNodes(TrieNode[] trieNode) {
        TrieNode = trieNode;
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
    // add constructor
    public TrieNode() {
        for (int i = 0; i < 26; i++) {
            TrieNode[i] = null;
        }
        end = false;
        word.setName("");
        word.setMeaning("");
    }

}
