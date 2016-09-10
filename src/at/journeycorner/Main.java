package at.journeycorner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    private final static String dictionaryPath = "de_neu.dic";
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Wieviele Buchstaben hat das mögliche Lösungswort?");
        int numberOfChars = getIntInput();

        Character[][] possibleChars = new Character[numberOfChars][];
        for (int i = 0; i < numberOfChars; i++) {
            System.out.println("Welche möglichen Buchstaben hat der " + (i + 1) + ". Buchstabe? (Bsp.: xyz)");
            possibleChars[i] = getCharInput();
        }

        System.out.println("Mögliche Antworten:");
        getPossibleAnswers(numberOfChars, possibleChars).forEach(System.out::println);
    }

    private static int getIntInput() {
        while (sc.hasNext()) {
            if (sc.hasNextInt())
                return sc.nextInt();
            System.out.println("Eingabe nicht korrekt, bitte nochmal:");
            sc.nextLine(); //  skip invalid input line
        }
        return 0;
    }

    private static Character[] getCharInput() {
        while (sc.hasNext()) {
            Set<Character> possibleChars = new HashSet<>();
            String line = sc.nextLine();
            boolean correct = false;
            for (int i = 0; i < line.length(); i++) {
                if (!Character.isLetter(line.charAt(i))) {
                    System.out.println("Eingabe nicht korrekt, bitte nochmal:");
                    break;
                }
                possibleChars.add(line.charAt(i));
                correct = true;
            }

            if (correct) return possibleChars.toArray(new Character[possibleChars.size()]);
        }
        return null;
    }

    // TODO simplify
    private static List<String> getPossibleAnswers(int numberOfChars, Character[][] possibleChars) {
        List<String> result = new ArrayList<>();

        try (Stream<String> lines = Files.lines(new File(dictionaryPath).toPath(), Charset.defaultCharset())) {

            // filter for words with the correct size
            lines
                    .filter(line -> line.length() == numberOfChars)
                    .forEachOrdered(line -> {
                        // check every character o4f the dictionary word
                        for (int i = 0; i < line.length(); i++) {
                            boolean matchChar = false;

                            // check against any possible character
                            for (int j = 0; j < possibleChars[i].length; j++) {
                                if (Character.toUpperCase(line.charAt(i)) == Character.toUpperCase(possibleChars[i][j])) {
                                    matchChar = true;
                                    break;
                                }
                            }

                            // skip if no match
                            if (!matchChar) break;

                            // match: add to possible words
                            if (i == line.length() - 1) result.add(line);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
