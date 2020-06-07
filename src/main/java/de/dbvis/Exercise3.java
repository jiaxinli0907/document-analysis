package de.dbvis;

import de.dbvis.stemmer.tokenstem;
import de.dbvis.structured.Sentence;
import de.dbvis.structured.Word;
import de.dbvis.structured.WordImpl;
import de.dbvis.tagger.NGramTag;
import de.dbvis.tagger.WorstPOSTaggerInHistory;
import de.dbvis.utils.POSTaggerScorer;
import de.dbvis.utils.WordCloudFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

/**
 * Created by Wolfgang Jentner.
 */
public class Exercise3 {

    public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
        /*
         * Task 1
         * Implement a statistical N-Gram tagger that extends the AbstractNGramTagger class.
         * Initialize it for three different N (1 = unigram, 2 = bigram, 3 = trigram). The n is passed to the constructor.
         * You may also use the NGram class for that.
         * @see de.dbvis.tagger.POSTagger
         * Also take a look at the de.dbvis.tagger.WorstPOSTaggerInHistory implementation.
         * Train all of your taggers with the brown corpus provided in data/brown.
         */
        //Example:
        //train your tagger based on the specific n:
        //look into the method to see how to read the brown corpus for training

        //Constructing and tranning the taggers
        //train your tagger based on the specific n:
        //look into the method to see how to read the brown corpus for training
        WorstPOSTaggerInHistory myWorstPosTagger = new WorstPOSTaggerInHistory(1);
        myWorstPosTagger.trainTagger();
        NGramTag tag1 = new NGramTag(1);
        tag1.trainTagger();
        NGramTag tag2 = new NGramTag(2);
        tag2.trainTagger();
        NGramTag tag3 = new NGramTag(3);
        tag3.trainTagger();

        /*
         * Task 2
         * Check all of your taggers with the POSTaggerScorer (see example below)
         * Which one performs better and why?
         * How could you improve your taggers?
         * You don't have to implement your discussed improvements.
         */
        //Scoring and Comparing the taggers
        POSTaggerScorer scorer = new POSTaggerScorer();
        scorer.score(myWorstPosTagger);
        System.out.print("UniGram: ");
        scorer.score(tag1);
        System.out.print("BiGram: ");
        scorer.score(tag2);
        System.out.print("TriGram: ");
        scorer.score(tag3);

        /*
         * Task 3
         * Combine all three datasets (Discussion, Reddit, TV). Use all of your taggers.
         * With the tagged words, filter your dataset for:
         * - Nouns
         * - Verbs
         * - Adjectives
         * - Adverbs
         * And create a word cloud for each.
         * In total you should generate 12 word clouds here.
        
         *
         * Which word cloud is the most meaningful?
         * Are the best-scored taggers generating the best word clouds?
         * How do the word clouds from this assignment compare to the ones you created last time?
         */
        //Construct objects for the three data sets
        debate co1 = new debate();
        tv co2 = new tv();
        reddit co3 = new reddit();

        for (int N = 1; N <= 3; N++) {

            String tagger = String.valueOf(N);

            //Constructs Corpus to append all datasets
            List<Sentence> magnaCorpus = new ArrayList<>();
            
           

            magnaCorpus.addAll(co1.corpus);
            magnaCorpus.addAll(co2.corpus);
            magnaCorpus.addAll(co3.corpus);
            NGramTag tagCloud;
            if (N == 1) {
                tagCloud = tag1;
            } else if (N == 2) {
                tagCloud = tag2;
            } else {
                tagCloud = tag3;
            }
            int dex = 0;
            for (Sentence dyn : magnaCorpus) {

                magnaCorpus.set(dex, tagCloud.assignTags(dyn));
                dex++;

            }
            
            //Sperating by tags

            ArrayList<String> nouns = new ArrayList<>();
            ArrayList<String> verbs = new ArrayList<>();
            ArrayList<String> adjectives = new ArrayList<>();
            ArrayList<String> adverbs = new ArrayList<>();

            for (Sentence dyn : magnaCorpus) {

                for (Word tagW : dyn) {

                    if (tagW.getTag().contains("nn") || tagW.getTag().contains("nnp")) {
                        nouns.add(tagW.getToken());
                    }

                }

            }
            for (Sentence dyn : magnaCorpus) {

                for (Word tagW : dyn) {

                    if (tagW.getTag().contains("vb")) {
                        verbs.add(tagW.getToken());
                    }

                }

            }
            for (Sentence dyn : magnaCorpus) {

                for (Word tagW : dyn) {

                    if (tagW.getTag().contains("jj")) {
                        adjectives.add(tagW.getToken());
                    }

                }

            }
            for (Sentence dyn : magnaCorpus) {

                for (Word tagW : dyn) {

                    if (tagW.getTag().contains("rb")) {
                        adverbs.add(tagW.getToken());
                    }

                }

            }

            // Creating the word clouds
            final Map<String, Integer> wordFrequencies1 = new HashMap<>();
            Arrays.stream(nouns.toArray(new String[nouns.size()])).forEach(w -> wordFrequencies1.merge(w, 1, (a, b) -> a + b));
            WordCloudFactory.create(wordFrequencies1, "nouns" + tagger + ".png");

            final Map<String, Integer> wordFrequencies2 = new HashMap<>();
            Arrays.stream(verbs.toArray(new String[verbs.size()])).forEach(w -> wordFrequencies2.merge(w, 1, (a, b) -> a + b));
            WordCloudFactory.create(wordFrequencies2, "verbs" + tagger + ".png");

            final Map<String, Integer> wordFrequencies3 = new HashMap<>();
            Arrays.stream(adjectives.toArray(new String[adjectives.size()])).forEach(w -> wordFrequencies3.merge(w, 1, (a, b) -> a + b));
            WordCloudFactory.create(wordFrequencies3, "adjs" + tagger + ".png");

            final Map<String, Integer> wordFrequencies4 = new HashMap<>();
            Arrays.stream(adverbs.toArray(new String[adverbs.size()])).forEach(w -> wordFrequencies4.merge(w, 1, (a, b) -> a + b));
            WordCloudFactory.create(wordFrequencies4, "adverbs" + tagger + ".png");

        }
        int stop = 0;

    }
}
