package de.dbvis.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * Created by Wolfgang Jentner.
 */
public class LoremIpsum {

    /**
     * Read in lorem ipsum text
     * @return the text as a single string
     * @throws URISyntaxException in case the path is wrong
     * @throws IOException in case the file cannot be read
     */
    public static String get() throws URISyntaxException, IOException {
        final StringBuilder s = new StringBuilder();
        Files.lines(Resources.getResource("data/example.txt")).forEach(s::append);
        return s.toString();
    }
}
