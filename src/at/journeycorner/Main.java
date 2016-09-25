package at.journeycorner;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {

    private final static String DICTIONARY_PATH = "de_neu.dic";
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Wieviele Buchstaben hat das mögliche Lösungswort?");
        int numberOfChars = getIntInput();

        List<Set<Character>> possibleChars = new ArrayList<>(numberOfChars);
        for (int i = 0; i < numberOfChars; i++) {
            System.out.println("Welche möglichen Buchstaben hat der " + (i + 1) + ". Buchstabe? (Bsp.: xyz)");
            possibleChars.add(getCharInput());
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

    private static Set<Character> getCharInput() {
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            if (!line.chars().allMatch(Character::isLetter)) {
                System.out.println("Eingabe nicht korrekt, bitte nochmal:");
                continue;
            }
            return line.toLowerCase().chars()
                    .distinct()
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toSet());
        }
        return null;
    }

    private static List<String> getPossibleAnswers(int numberOfChars, List<Set<Character>> possibleChars)
            throws IOException {
        List<String> results = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(DICTIONARY_PATH), Charset.defaultCharset())) {
            String line;
            SCAN_LINES: while ((line = reader.readLine()) != null) {
                if (line.length() < numberOfChars) {
                    continue;
                }
                if (line.length() > numberOfChars) {
                    break;
                }
                for (int i = 0; i < line.length(); i++) {
                    if (!possibleChars.get(i).contains(Character.toLowerCase(line.charAt(i)))) {
                        continue SCAN_LINES;
                    }
                }
                results.add(line);
            }
        }
        return results;
    }
}
