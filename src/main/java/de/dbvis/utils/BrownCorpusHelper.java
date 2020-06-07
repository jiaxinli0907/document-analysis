package de.dbvis.utils;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;
import de.dbvis.structured.Word;
import de.dbvis.structured.WordImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Wolfgang Jentner on 11/10/2017.
 */
public class BrownCorpusHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrownCorpusHelper.class);

    public static List<Sentence> readFileFromCorpus(String path) throws IOException, URISyntaxException {
        return readFileFromCorpus(Resources.getResource(path));
    }

    public static List<Sentence> readFileFromCorpus(Path path) throws IOException {
        try (Stream<String> s = Files.lines(path)) {
            return s.filter(line -> line.length() > 5)
                    .map(BrownCorpusHelper::parseLineToSentence)
                    .collect(Collectors.toList());
        }
    }

    public static Sentence parseLineToSentence(final String line) {
        return parseLineToSentence(line, "/");
    }

    public static Sentence parseLineToSentence(final String line, final String tagTokenDelimiter) {
        String[] tokenTags = line.split(" ");
        return parseLineToSentence(tokenTags, tagTokenDelimiter);
    }

    public static Sentence parseLineToSentence(final String[] line, final String tagTokenDelimiter) {
        final Sentence sentence = new SentenceImpl();
        Arrays.stream(line).forEach(tokenTag -> {
            Word w = getWordFromTokenTag(tokenTag, tagTokenDelimiter);
            if(w != null) {
                sentence.add(w);
            }
        });
        return sentence;
    }

    public static Word getWordFromTokenTag(String tokenTag, String delimiter) {
        int lastIndex = tokenTag.lastIndexOf(delimiter);
        if(lastIndex > -1) {
            String token = tokenTag.substring(0, lastIndex);
            String tag = tokenTag.substring(lastIndex+1);

            int idx = tag.indexOf("-");
            if(idx > -1) {
                tag = tag.substring(0, idx).trim();
            }

            Word word = new WordImpl(token.trim());
            word.setTag(tag);
            return word;
        }
        return null;
    }
}
