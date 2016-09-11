package at.journeycorner;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private final static String DICTIONARY_PATH = "de_neu.dic";
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Wieviele Buchstaben hat das mögliche Lösungswort?");
        int numberOfChars = getIntInput();

        char[][] possibleChars = new char[numberOfChars][];
        for (int i = 0; i < numberOfChars; i++) {
            System.out.println("Welche möglichen Buchstaben hat der " + (i + 1) + ". Buchstabe? (Bsp.: xyz)");
            possibleChars[i] = getCharInput();
        }

        System.out.println("Mögliche Antworten:");
        getPossibleAnswers(numberOfChars, possibleChars).forEach(System.out::println);
    }

    private static int getIntInput() {
        while (sc.hasNext()) {
            if (sc.hasNextInt()) {
                return sc.nextInt();
            }
            System.out.println("Eingabe nicht korrekt, bitte nochmal:");
            sc.nextLine(); // skip invalid input line
        }
        return 0;
    }

    private static char[] getCharInput() {
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            if (!line.chars().allMatch(Character::isLetter)) {
                System.out.println("Eingabe nicht korrekt, bitte nochmal:");
                continue;
            }
            return line.toLowerCase().toCharArray();
        }
        return null;
    }

    private static List<String> getPossibleAnswers(int numberOfChars, char[][] possibleChars) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(DICTIONARY_PATH), Charset.defaultCharset())) {
            return lines
                    // filter for words with the correct size
                    .filter(line -> line.length() == numberOfChars)
                    // every character of the dictionary word must be contained in possible characters of current position
                    .filter(line -> IntStream.range(0, line.length())
                            .allMatch(i -> CharBuffer.wrap(possibleChars[i]).chars()
                                    .anyMatch(c -> c == Character.toLowerCase(line.charAt(i)))))
                    .collect(Collectors.toList());
        }
    }
}
