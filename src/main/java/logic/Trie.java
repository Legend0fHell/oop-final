package logic;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private static TrieNode root = new TrieNode();

    public Trie() {
    }

    public boolean insertWord(Word word) {
        try {
            TrieNode now = root;

            String name = word.getName();
            name = name.toLowerCase();
            word.setName(name);
            if(!word.trieCompliant()) {
                return false;
            }

            for (char c : name.toCharArray()) {
                int num = Helper.convertToIndex(c);
                if (now.getAllNodes()[num] == null) {
                    now.getAllNodes()[num] = new TrieNode();
                }
                now = now.getAllNodes()[num]; // chuyen now sang đầu root mới
            }
            now.setWord(word);
            now.setEnd(true);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    // check null cho deleteWord
    private boolean isNull(TrieNode trieNode) {
        for (TrieNode node : trieNode.getAllNodes()) {
            if (node != null) return false;
        }
        return true;
    }

    public void deleteWord(Word word) {
        if (isExist(word.getName())) {
            helperDeleteWord(root, word, 0);
        }
    }
    // có vẻ sẽ sai ?
    private boolean helperDeleteWord(TrieNode trieNode, Word word, int depth) {
        if (depth == word.getName().length()) {
            if (trieNode.getEnd()) {
                trieNode.setEnd(false);
                trieNode.setWord(null);
            }
            return isNull(trieNode);
        }
        int num = Helper.convertToIndex(word.getName().charAt(depth));
        TrieNode now = trieNode.getAllNodes()[num];
        if (now == null) {
            return false;
        }
        if (helperDeleteWord(now, word, depth + 1)) {
            trieNode.getAllNodes()[num] = null;
            return isNull(trieNode);
        }
        return false;
    }

    public boolean isExist(String name) {
        TrieNode node = findPrefixNode(name);
        return node != null && node.getEnd();
    }

    public void updateWord(Word word_before, Word word_after) {
        deleteWord(word_before);
        insertWord(word_after);
    }

    public List<String> getAllWordNames(String prefix_name) {
        List<String> list = new ArrayList<>();
        TrieNode node = root;
        if (prefix_name != null && !prefix_name.isEmpty()) {
            node = findPrefixNode(prefix_name);
            if (node == null) {
                return list;
            }
        }
        helperGetAllWordNames(node, list);
        return list;
    }

    // do toi ko de y la p truyen List vao mà tôi lại nhìn thành list là kiểu dữ liệu của hàm nên lỡ code như này>
    private void helperGetAllWordNames(TrieNode node, List<String> listName) {
        if (node.getEnd()) {
            listName.add(node.getWord().getName());
        }
        for (TrieNode trieNode : node.getAllNodes()) {
            if(trieNode != null) helperGetAllWordNames(trieNode, listName);
        }
    }

    public TrieNode findPrefixNode(String prefix_name) {
        TrieNode now = root;
        for (char c : prefix_name.toCharArray()) {
            int num = Helper.convertToIndex(c);
            try {
                if (now.getAllNodes()[num] == null) {
                    return null;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            now = now.getAllNodes()[num];
        }
        return now;
    }
}
