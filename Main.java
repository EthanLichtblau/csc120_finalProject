import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Dictionary dictionary = new Dictionary();
        Random random = new Random();
        int chooseList = random.nextInt(dictionary.wordList.length);
        String secret = dictionary.wordList[chooseList];

        System.out.println("Do you like to see the secret?: ");
        if (keyboard.next().startsWith("y")) {
            System.out.println("The secret is "+secret);
        }

        ArrayList<String> guesses = new ArrayList<>();
        ArrayList<ArrayList<String>> letters = new ArrayList<>();
        letters.add(new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I")));
        letters.add(new ArrayList<>(Arrays.asList("J", "K", "L", "M", "N", "O", "P", "Q", "R")));
        letters.add(new ArrayList<>(Arrays.asList("S", "T", "U", "V", "W", "X", "Y", "Z")));

        Scanner keyboardGame = new Scanner(System.in);
        for (int i = 1; i <= 6; i++) {
            System.out.println();
            System.out.println("Round " + i + ". Enter guess:");
            String guess = keyboardGame.next();
            guess = guess.toUpperCase();
            guess = verify(guess);
            compare(guess, secret, letters);
            guesses.add(guess + ": " + checker(guess, secret));
            if (guess.equalsIgnoreCase(secret)) {
                winner(args, secret);
            }
            printGuesses(guesses);
            printLetters(letters);
        }
        System.out.println();
        System.out.println("The secret was: " + secret.toUpperCase());
    }

    public static void compare(String guess, String secret, ArrayList<ArrayList<String>> letters) {
        ArrayList<String> matches = new ArrayList<String>();
        ArrayList<String> hits = new ArrayList<String>();
        ArrayList<String> misses = new ArrayList<String>();
        for (int k = 0; k < 5; k++) {
            for (int j = 0; j < 5; j++) {
                if (guess.charAt(k) == secret.charAt(j)) {
                    if (k == j) {
                        matches.add(String.valueOf(guess.charAt(k)));
                        break;
                    } else {
                        hits.add(String.valueOf(guess.charAt(k)));
                    }
                }
            }
        }
        for (int k = 0; k < 5; k++) {
            for (int j = 0; j < 5; j++) {
                if (guess.charAt(k) != secret.charAt(j)) {
                    misses.add(String.valueOf(guess.charAt(k)));
                }
            }
        }
        updateLetters(guess, secret, letters, matches, hits, misses);
    }

    public static ArrayList<ArrayList<String>> updateLetters(String guess, String secret, ArrayList<ArrayList<String>> letters, ArrayList<String> matches, ArrayList<String> hits, ArrayList<String> misses) {
        ColorSelect colors = new ColorSelect();

        for (int i = 0; i < matches.size(); i++) {
            for (int k = 0; k < letters.size(); k++) {
                ArrayList<String> row = letters.get(k);
                for (int j = 0; j < row.size(); j++) {
                    String letter = row.get(j);
                    if (matches.get(i).equals(letter)) {
                        row.set(j, colors.encode(0x00, 0x02, letter));
                    }
                }
            }
        }

        // yellow for right letter wrong position
        for (int i = 0; i < hits.size(); i++) {
            String hit = hits.get(i);
            for (int k = 0; k < letters.size(); k++) {
                ArrayList<String> row = letters.get(k);
                for (int j = 0; j < row.size(); j++) {
                    String letter = row.get(j);
                    if (hit.equals(letter)) {
                        row.set(j, colors.encode(0x00, 0x03, letter));
                    }
                }
            }
        }
        // gray for misses
        for (int i = 0; i < misses.size(); i++) {
            String miss = misses.get(i);
            for (int k = 0; k < letters.size(); k++) {
                ArrayList<String> row = letters.get(k);
                for (int j = 0; j < row.size(); j++) {
                    String letter = row.get(j);
                    if (miss.equals(letter)) {
                        row.set(j, colors.encode(0x00, 0x08, letter));
                    }
                }
            }
        }
        matches.removeAll(matches);
        hits.removeAll(hits);
        misses.removeAll(misses);
        return letters;
    }

    public static void printLetters(ArrayList<ArrayList<String>> letters) {
        int counter = 0;
        for (ArrayList<String> row : letters) {
            for (String letter : row) {
                System.out.print(letter + " ");
                counter++;
                if (counter == 9) {
                    System.out.println();
                    counter = 0;
                }
            }
        }
    }

    public static void printGuesses(ArrayList<String> guesses) {

        System.out.println("Guesses: ");
        for (String guess : guesses) {
            System.out.println(guess + " ");
        }
        System.out.println();
    }

    public static String verify(String guess) {
        EnglishWords englishCheck = new EnglishWords();
        String[] englishWords = englishCheck.englishWordChecker;
        Scanner keyboard = new Scanner(System.in);
        boolean validWord = Arrays.asList(englishWords).contains(guess);
        boolean length_test = (guess.length() == 5);

        while (!length_test) {
            System.out.println("Invalid guess. Please enter a 5 letter word: ");
            guess = keyboard.nextLine();
            guess = guess.toUpperCase();
            length_test = (guess.length() == 5);
            validWord = Arrays.asList(englishWords).contains(guess);
        }

        while (!validWord) {
            System.out.println("Not an English Word. Please enter a new word: ");
            guess = keyboard.nextLine();
            guess = guess.toUpperCase();
            validWord = Arrays.asList(englishWords).contains(guess);
            length_test = (guess.length() == 5);
        }
        return guess.toUpperCase();
    }

    public static void winner(String[] args, String secret) {
        Scanner keyboard = new Scanner(System.in);
        String playAgain;
        System.out.println("You've got it right");
        System.out.println("-------------------------------");
        System.out.println("The secret was: " + secret.toUpperCase());
        System.out.println("Would you like to play again?: ");
        playAgain = keyboard.next();
        if (playAgain.startsWith("y")) {
            main(args);
        } else {
            System.exit(0);
        }
    }
    public static String checker(String guess, String secret) {
        StringBuilder userOutput = new StringBuilder("_____");


        for (int i = 0; i < 5; i++) {
            for (int y = 0; y < 5; y++) {
                if (guess.charAt(i) == secret.charAt(y)) {
                    if (i == y) {
                        userOutput.setCharAt(i, 'H');
                        break;
                    } // end of 2nd if statement
                    else {
                        userOutput.setCharAt(i, 'M');
                    }
                } // end of first if statement

            } // end of 2nd for loop

        } // end of 1st for loop

        return userOutput.toString();
    } // end of checker method


}
