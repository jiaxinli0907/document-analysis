package de.dbvis.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class SentimentLexicon {

    private static Set<String> positive;
    private static Set<String> negative;

    public static Set<String> positiveWords() {
        if(positive == null) {
            try {
                positive = readFile("./sentiment/positive-words.txt");
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return positive;
    }

    public static Set<String> negativeWords() {
        if(negative == null) {
            try {
                negative = readFile("./sentiment/negative-words.txt");
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return negative;
    }

    private static Set<String> readFile(String str) throws IOException, URISyntaxException {
        try (Stream<String> s = Files.lines(Resources.getResource(str), StandardCharsets.ISO_8859_1)) {
            return s.filter(line -> line.trim().length() > 1)
                    .filter(line -> !line.startsWith(";"))
                    .map(String::trim)
                    .collect(Collectors.toSet());
        }
    }
}
