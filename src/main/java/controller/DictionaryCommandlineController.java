package controller;

import logic.DictionaryManagement;
import logic.Word;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DictionaryCommandlineController extends DictionaryManagement {
    public static void main(String[] args) {
        DictionaryManagement dm = new DictionaryManagement();

        boolean check = true;

        while (check) {
            // menu display
            System.out.println("[0]  Exit\n[1]  Add\n[2]  Remove\n[3]  Update\n[4]  Count\n[5]  Definition\n" +
                    "[6]  Search");
            System.out.println("Your action : ");

            // user selection
            Scanner sc = new Scanner(System.in);
            int sl = -1;

            try {
                sl = sc.nextInt();
            } catch (InputMismatchException e) {

            }

            sc.nextLine(); // remove down line

            if (sl == 0) // Quit
            {
                System.out.print("QUIT!");
                check = false;
            } else if (sl == 1) // Add
            {
                System.out.println("Word you want to add :");
                String wt = sc.nextLine();
                System.out.println("Meaning :");
                String we = sc.nextLine();
                dm.insertWord(new Word(wt, we));
            } else if (sl == 2) // Remove
            {
                System.out.println("Word you want to remove :");
                String rm = sc.nextLine();
                dm.deleteWord(new Word(rm,""));
            } else if (sl == 3) // Update
            {
                System.out.println("Word you want to fix :");
                String tg = sc.nextLine();
                System.out.println("Edit meaning to :");
                String ep = sc.nextLine();
                dm.updateWord(new Word(tg, ""), new Word(tg, ep));
            } else if (sl == 4) {
                List<String> words = dm.search("");
                System.out.println(words.size() + " words found");
            } else if (sl == 5) {
                System.out.println("Lookup : ");
                String search = sc.nextLine().trim().toLowerCase();
                Word word = dm.getWordMeaning(search);
                if (word != null) {
                    System.out.println(word.getMeaning());
                } else {
                    System.out.println("Word not found");
                }
            } else if (sl == 6) {
                System.out.println("Search : ");
                String sr = sc.nextLine();
                System.out.println("Result : ");
                List<String> words = dm.search(sr);
                System.out.println(words.size() + " words found");
                for (String word : words) {
                    System.out.print(word + ' ');
                }
                System.out.println();
            } else {
                System.out.println("Action not supported");
            }
        }
    }


}

