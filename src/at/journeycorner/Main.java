package at.journeycorner;

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

    private static List<String> getPossibleAnswers(int numberOfChars, List<Set<Character>> possibleChars) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(DICTIONARY_PATH), Charset.defaultCharset())) {
            return takeWhile(lines, line -> line.length() <= numberOfChars)
                    // filter length
                    .filter(line -> line.length() == numberOfChars)
                    // every character of the dictionary word must be contained in possible characters of current position
                    .filter(line -> IntStream.range(0, line.length()).allMatch(i ->
                            possibleChars.get(i).contains(Character.toLowerCase(line.charAt(i))))
                    )
                    .collect(Collectors.toList());
        }
    }

    private static <T> Spliterator<T> takeWhile(
            Spliterator<T> splitr, Predicate<? super T> predicate) {
        return new Spliterators.AbstractSpliterator<T>(splitr.estimateSize(), 0) {
            boolean stillGoing = true;

            @Override
            public boolean tryAdvance(Consumer<? super T> consumer) {
                if (stillGoing) {
                    boolean hadNext = splitr.tryAdvance(elem -> {
                        if (predicate.test(elem)) {
                            consumer.accept(elem);
                        } else {
                            stillGoing = false;
                        }
                    });
                    return hadNext && stillGoing;
                }
                return false;
            }
        };
    }

    private static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }
}
