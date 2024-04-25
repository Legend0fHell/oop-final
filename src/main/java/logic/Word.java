package logic;

public class Word {
    private String name;

    private String meaning;

    public Word() {
    }

    public Word(String name, String meaning) {
        this.name = name;
        this.meaning = meaning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public boolean trieCompliant() {
        for (char c : name.toCharArray()) {
            if (Helper.convertToIndex(c) == -1) {
                return false;
            }
        }
        return true;
    }
}
