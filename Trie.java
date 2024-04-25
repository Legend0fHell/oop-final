import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Trie {
    private static TrieNode root = new TrieNode();
    public Trie(){
    }

    public void insertWord(Word word){
        try {
            TrieNode now = root;
            String name = word.getName();
            word.setName(name.toLowerCase());

            for(char i: name.toCharArray()){
                int num = i - 'a';
                if(now.getAllNodes()[num] == null){
                    now.getAllNodes()[num] = new TrieNode();
                }
                now = now.getAllNodes()[num]; // chuyen now sang đầu root mới
            }
            now.setWord(word);
            now.setEnd(true);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    // check null cho deleteWord
    private boolean isNull(TrieNode trieNode) {
        for(TrieNode trie : trieNode.getAllNodes()){
            if(trie == null) return true;
        }
        return false;
    }

    public void deleteWord(Word word){
        if(isExist(word.getName())){
            deleteWord(root, word, 0);
        }
    }

    // có vẻ sẽ sai ?
    private void deleteWord(TrieNode trieNode, Word word, int JonnyDeep){
        if(JonnyDeep == word.getName().length()) {
            if (trieNode.getEnd()) {
                trieNode.setEnd(false);
                trieNode.setWord(null);
            }
            return;
        }
        int num = word.getName().charAt(JonnyDeep) - 'a';
        TrieNode now = trieNode.getAllNodes()[num];
        if(now == null){
            return;
        }
        deleteWord(now, word, JonnyDeep+1);
        if(isNull(trieNode)){
            trieNode.getAllNodes()[num] = null;
        }
    }

    public boolean isExist(String name){
        TrieNode now = root;
        try {
            for(char i : name.toCharArray()){
                int num = i - 'a';
                if(now.getAllNodes()[num] == null){
                    return false;
                }
                now = now.getAllNodes()[num]; // still like up there, but no need new Trie anymore.
            }
        } catch (Exception e){
            System.out.println(e.toString());
        }
        return now.getEnd(); // no need now != null cuz now is always have end = false in the default constructor. i think :))))))
    }

    public void updateWord(Word word_before, Word word_after){
        deleteWord(word_before);
        insertWord(word_after);
    }

    public String getWordMeaning(String name){
        if(!isExist(name)){
            return "";
        }
        TrieNode now = root;
        int JonnyDeep = 0;
        while(JonnyDeep != name.length() - 1) {
            int num = name.charAt(JonnyDeep++) - 'a';
            if(!now.getEnd()) {
                now = now.getAllNodes()[num];
            }
        }
        return now.getWord().getMeaning();
    }

    public List<String> getAllWordNames(){
        return helperGetAllWordNames(root);
    }

    // do toi ko de y la p truyen List vao mà tôi lại nhìn thành list là kiểu dữ liệu của hàm nên lỡ code như này>
    public List<String> helperGetAllWordNames(TrieNode node){
        List<String> listName = new ArrayList<>();
        for(TrieNode trieNode: node.getAllNodes()){
            if(!trieNode.getEnd()){
                helperGetAllWordNames(trieNode);
            }
            else listName.add(trieNode.getWord().getName());
        }
        return listName;
    }

    public TrieNode findPrefixNode(String prefix_name){
        TrieNode now = root;
        for (char c : prefix_name.toCharArray()){
            int num = c - 'a';
            try {
                if(now.getAllNodes()[num] == null){
                    return null;
                }
            } catch (Exception e){
                System.out.println(e.toString());
            }
            now = now.getAllNodes()[num];
        }
        return now;
    }
}
