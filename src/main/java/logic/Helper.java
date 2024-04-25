package logic;

public class Helper {
    static public int convertToIndex(char c) {
        int index = -1;
        if (c >= 'a' && c <= 'z') index = c - 'a';
        else if (c == '-') index = 26;
        else if (c == ' ') index = 27;
        else if (c == '.') index = 28;
        else if (c == '\'') index = 29;
        else if (c == '\n') index = 30;
        return index;
    }
}
