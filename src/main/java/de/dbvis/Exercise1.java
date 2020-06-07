package de.dbvis;


import de.dbvis.utils.WordCloudFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfgang Jentner.
 */
public class Exercise1 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        /*
         * Part 1
         * Read all the files in the data directory (and subdirectories)
         * You may use any available library here but please add it via maven (pom.xml).
         * Creata Java files to represent your data. For the beginning, the simple text body should be sufficient.
         * However, if you want to add other metadata you are free to do so.
         * Regard each folder as one dataset. So in Part 3 you should generate 3 word clouds.
         *
         * Hint: You do _not_ need to represent the thread structure of the reddit data with your Java Objects.
         */
        //Example:
        final String loremipsum = readExample();

        /*
         * Part 2
         * Count the words in a map. Do so for each dataset.
         */
        //Example:
        final Map<String, Integer> wordFrequencies = new HashMap<>();
        Arrays.stream(loremipsum.split(" ")).forEach(w -> wordFrequencies.merge(w, 1, (a, b) -> a + b));

        /*
         * Part 3
         * Create a word cloud for the dataset.
         */
        //Example:
        WordCloudFactory.create(wordFrequencies, "example.png");
    }

    /**
     * Read in lorem ipsum text
     * @return the text as a single string
     * @throws URISyntaxException if the path to the resource is wrong
     * @throws IOException if the file cannot be read
     */
    private static String readExample() throws URISyntaxException, IOException {
        final StringBuilder s = new StringBuilder();
        URI uri = Exercise1.class.getClassLoader().getResource("data/example.txt").toURI();
        Files.lines(Paths.get(uri)).forEach(s::append);
        return s.toString();
    }
}
