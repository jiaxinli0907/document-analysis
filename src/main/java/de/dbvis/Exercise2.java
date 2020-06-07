package de.dbvis;

import de.dbvis.stemmer.tokenstem;
import de.dbvis.utils.LineChartFactory;
import de.dbvis.utils.LoremIpsum;
import de.dbvis.utils.WordCloudFactory;
import de.dbvis.utils.ZipfsLaw;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import net.mintern.primitive.Primitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

/**
 * Created by Wolfgang Jentner.
 */
public class Exercise2 {
    private static Logger LOGGER = LoggerFactory.getLogger(Exercise2.class);

    public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
        /*
         * Task 1 - Create tokens
         * Create a list of tokens for each dataset
         */
        PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<>(new StringReader(LoremIpsum.get()), new CoreLabelTokenFactory(), "");
        while(tokenizer.hasNext()) {
            CoreLabel label = tokenizer.next();
            LOGGER.debug(label.toString());
        }
        int i = 0;

        tokenstem stem = new tokenstem();
        debate co1 = new debate();
        tv co2 = new tv();
        reddit co3 = new reddit();
        /*
         * Task 2 - Filter data
         * a) Remove unwanted data (<br>, ., ...)
         * b) Lowercase all words
         * c) Read in stop-words.txt and remove the tokens from the list
         */       
        
         /*
         * Task 3 - Stemming
         * Write 10 rules and implement Stemmer.stem(..)
         * Apply your method to your list of cleaned tokens
         * Create word clouds just like you did in Exercise 1.
         */
        
        int m=0;
        for(String tk : co1.token){
            
            co1.stem[m]=stem.stem(tk);
            //System.out.println(co2.stem[m]);
            m++;
           
        }

        m=0;
        for(String tk : co2.token){
            
            co2.stem[m]=stem.stem(tk);
            //System.out.println(co2.stem[m]);
            m++;
           
        }
        m=0;
        for(String tk : co3.token){
            
            co3.stem[m]=stem.stem(tk);
            //System.out.println(co3.stem[m]);
            m++;
           
        }
        final Map<String, Integer> wordFrequencies1 = new HashMap<>();
        Arrays.stream(co1.stem).forEach(w -> wordFrequencies1.merge(w, 1, (a, b) -> a + b));
        WordCloudFactory.create(wordFrequencies1, "debate2.png");

        final Map<String, Integer> wordFrequencies2 = new HashMap<>();
        Arrays.stream(co2.stem).forEach(w -> wordFrequencies2.merge(w, 1, (a, b) -> a + b));
        WordCloudFactory.create(wordFrequencies2, "reddit2.png");
        
        final Map<String, Integer> wordFrequencies3 = new HashMap<>();
        Arrays.stream(co3.stem).forEach(w -> wordFrequencies3.merge(w, 1, (a, b) -> a + b));
        WordCloudFactory.create(wordFrequencies3, "tv2.png");

        
        
        //Create a class implementing the de.dbvis.stemmer.Stemmer Interface
        //Stemmer stemmer = new MyStemmer();
        //String stem = stemmer.stem("children");


        /*
         * Task 4 - Zipf's Law
         * For each dataset, use your word frequencies, calculate the relative frequency (count/total).
         * Create a double[] array and sort it in a descending order.
         * Use the LineChartFactory to plot it, the line chart will also contain the frequency estimated by Zipf'sLaw
         */
        double total = 0.0;
        ArrayList<Integer> count = new ArrayList<>();
        for(Map.Entry<String,Integer> key : wordFrequencies1.entrySet()) {
            total+=wordFrequencies1.get(key.getKey());
            count.add(wordFrequencies1.get(key.getKey()));
            
        }
        
        double[] frequencies1 = new double[count.size()];
        for(int n=0;n<frequencies1.length;n++){
            frequencies1[n]=(double)count.get(n)/total;
        }
        Primitive.sort(frequencies1, (d1, d2) -> Double.compare(d2, d1), true);
        LineChartFactory.createZipfsLaw("debate_zipf's_law", frequencies1);
    
        
        total = 0.0;
        count = new ArrayList<>();
        for(Map.Entry<String,Integer> key : wordFrequencies2.entrySet()) {
            total+=wordFrequencies2.get(key.getKey());
            count.add(wordFrequencies2.get(key.getKey()));
            
        }
        
        double[] frequencies2 = new double[count.size()];
        for(int n=0;n<frequencies2.length;n++){
            frequencies2[n]=(double)count.get(n)/total;
        }
        Primitive.sort(frequencies2, (d1, d2) -> Double.compare(d2, d1), true);
        LineChartFactory.createZipfsLaw("reddit_zipf's_law", frequencies2);
    
        
        total = 0.0;
        count = new ArrayList<>();
        for(Map.Entry<String,Integer> key : wordFrequencies3.entrySet()) {
            total+=wordFrequencies3.get(key.getKey());
            count.add(wordFrequencies3.get(key.getKey()));
            
        }
        
        double[] frequencies3 = new double[count.size()];
        for(int n=0;n<frequencies3.length;n++){
            frequencies3[n]=(double)count.get(n)/total;
        }
        Primitive.sort(frequencies3, (d1, d2) -> Double.compare(d2, d1), true);
        LineChartFactory.createZipfsLaw("tv_zipf's_law", frequencies3);
    
    }
    
    
    /**
     *
     * @param x
     */

}