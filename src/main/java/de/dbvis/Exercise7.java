package de.dbvis;

import db.dbvis.exercise7.nlpTagger;
import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;
import de.dbvis.structured.Word;
import de.dbvis.structured.WordImpl;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import org.tartarus.snowball.ext.PorterStemmer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import static java.lang.Math.abs;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Exercise7 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise7.class);

    public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {

        //Merry Christmas!!!

        /*
         * TASK 1:
         * - Write a reader to read the poems (you will find them in resources/data/ChristmasPoems).
         * - Implement an efficient data structure to store the poems.
         * - Use Stanford NLP English Tokenizer to separate text into tokens.
         * - Use one of the taggers (you can use the provided Stanford Tagger or OpenNLP Tagger from exercise 4)
         * to extract POS tags.
         * - Clean the data!
         */
        ArrayList<String> label = new ArrayList<>();
        label.add("Poem 1 V Poem 2");
        label.add("Poem 1 V Poem 3");
        label.add("Poem 1 V Poem 4");
        label.add("Poem 1 V Poem 5");
        label.add("Poem 1 V Poem 6");
        label.add("Poem 2 V Poem 3");
        label.add("Poem 2 V Poem 4");
        label.add("Poem 2 V Poem 5");
        label.add("Poem 2 V Poem 6");
        label.add("Poem 3 V Poem 4");
        label.add("Poem 3 V Poem 5");
        label.add("Poem 3 V Poem 6");
        label.add("Poem 4 V Poem 5");
        label.add("Poem 4 V Poem 6");
        label.add("Poem 5 V Poem 6");

        final URI[] poem = readPoem();
        ArrayList<String> p0 = poemString(poem[0]);
        ArrayList<String> p1 = poemString(poem[1]);
        ArrayList<String> p2 = poemString(poem[2]);
        ArrayList<String> p3 = poemString(poem[3]);
        ArrayList<String> p4 = poemString(poem[4]);
        ArrayList<String> p5 = poemString(poem[5]);

        ArrayList<Sentence> token0 = tokenizer(p0);
        ArrayList<Sentence> token1 = tokenizer(p1);
        ArrayList<Sentence> token2 = tokenizer(p2);
        ArrayList<Sentence> token3 = tokenizer(p3);
        ArrayList<Sentence> token4 = tokenizer(p4);
        ArrayList<Sentence> token5 = tokenizer(p5);

        nlpTagger tagger0 = new nlpTagger();
        tagger0.tag(token0);
        tagger0.tag(token1);
        tagger0.tag(token2);
        tagger0.tag(token3);
        tagger0.tag(token4);
        tagger0.tag(token5);

        token0 = clean(token0);
        token1 = clean(token1);
        token2 = clean(token2);
        token3 = clean(token3);
        token4 = clean(token4);
        token5 = clean(token5);

        /*
         * TASK 2:
         * - Create a data structure to store the following features:
         *    o	Word (token) frequencies
         *    o	POS tag frequencies
         * - Extract at least 3 additional features which you will use for your similarity measure algorithm.
         * (The features might be on a word, on sentence, and poem level!
         * What can describe the similarity of two poems? ;) Be creative!)
         */
        // get the word frequency
        HashMap<String, Integer> wordFreq0 = createStructure(token0, 1);
        HashMap<String, Integer> wordFreq1 = createStructure(token1, 1);
        HashMap<String, Integer> wordFreq2 = createStructure(token2, 1);
        HashMap<String, Integer> wordFreq3 = createStructure(token3, 1);
        HashMap<String, Integer> wordFreq4 = createStructure(token4, 1);
        HashMap<String, Integer> wordFreq5 = createStructure(token5, 1);
        // get the tag frequency
        HashMap<String, Integer> tagFreq0 = createStructure(token0, 2);
        HashMap<String, Integer> tagFreq1 = createStructure(token1, 2);
        HashMap<String, Integer> tagFreq2 = createStructure(token2, 2);
        HashMap<String, Integer> tagFreq3 = createStructure(token3, 2);
        HashMap<String, Integer> tagFreq4 = createStructure(token4, 2);
        HashMap<String, Integer> tagFreq5 = createStructure(token5, 2);

        double wordPer01 = getPer(wordFreq0, wordFreq1);
        double wordPer02 = getPer(wordFreq0, wordFreq2);
        double wordPer03 = getPer(wordFreq0, wordFreq3);
        double wordPer04 = getPer(wordFreq0, wordFreq4);
        double wordPer05 = getPer(wordFreq0, wordFreq5);
        double wordPer12 = getPer(wordFreq1, wordFreq2);
        double wordPer13 = getPer(wordFreq1, wordFreq3);
        double wordPer14 = getPer(wordFreq1, wordFreq4);
        double wordPer15 = getPer(wordFreq1, wordFreq5);
        double wordPer23 = getPer(wordFreq2, wordFreq3);
        double wordPer24 = getPer(wordFreq2, wordFreq4);
        double wordPer25 = getPer(wordFreq2, wordFreq5);
        double wordPer34 = getPer(wordFreq3, wordFreq4);
        double wordPer35 = getPer(wordFreq3, wordFreq5);
        double wordPer45 = getPer(wordFreq4, wordFreq5);

        ArrayList<Double> wordPer = new ArrayList<>();
        wordPer.add(wordPer01);
        wordPer.add(wordPer02);
        wordPer.add(wordPer03);
        wordPer.add(wordPer04);
        wordPer.add(wordPer05);
        wordPer.add(wordPer12);
        wordPer.add(wordPer13);
        wordPer.add(wordPer14);
        wordPer.add(wordPer15);
        wordPer.add(wordPer23);
        wordPer.add(wordPer24);
        wordPer.add(wordPer25);
        wordPer.add(wordPer34);
        wordPer.add(wordPer35);
        wordPer.add(wordPer45);

        double tagPer01 = prePro(tagFreq0, tagFreq1);
        double tagPer02 = prePro(tagFreq0, tagFreq2);
        double tagPer03 = prePro(tagFreq0, tagFreq3);
        double tagPer04 = prePro(tagFreq0, tagFreq4);
        double tagPer05 = prePro(tagFreq0, tagFreq5);
        double tagPer12 = prePro(tagFreq1, tagFreq2);
        double tagPer13 = prePro(tagFreq1, tagFreq3);
        double tagPer14 = prePro(tagFreq1, tagFreq4);
        double tagPer15 = prePro(tagFreq1, tagFreq5);
        double tagPer23 = prePro(tagFreq2, tagFreq3);
        double tagPer24 = prePro(tagFreq2, tagFreq4);
        double tagPer25 = prePro(tagFreq2, tagFreq5);
        double tagPer34 = prePro(tagFreq3, tagFreq4);
        double tagPer35 = prePro(tagFreq3, tagFreq5);
        double tagPer45 = prePro(tagFreq4, tagFreq5);

        ArrayList<Double> tagPer = new ArrayList<>();
        tagPer.add(tagPer01);
        tagPer.add(tagPer02);
        tagPer.add(tagPer03);
        tagPer.add(tagPer04);
        tagPer.add(tagPer05);
        tagPer.add(tagPer12);
        tagPer.add(tagPer13);
        tagPer.add(tagPer14);
        tagPer.add(tagPer15);
        tagPer.add(tagPer23);
        tagPer.add(tagPer24);
        tagPer.add(tagPer25);
        tagPer.add(tagPer34);
        tagPer.add(tagPer35);
        tagPer.add(tagPer45);

        // get the average words per line
        double wordPerLine0 = getAvgWpl(token0);
        double wordPerLine1 = getAvgWpl(token1);
        double wordPerLine2 = getAvgWpl(token2);
        double wordPerLine3 = getAvgWpl(token3);
        double wordPerLine4 = getAvgWpl(token4);
        double wordPerLine5 = getAvgWpl(token5);

        ArrayList<Double> AvgWpl = new ArrayList<>();
        AvgWpl.add(abs(wordPerLine0 - wordPerLine1));
        AvgWpl.add(abs(wordPerLine0 - wordPerLine2));
        AvgWpl.add(abs(wordPerLine0 - wordPerLine3));
        AvgWpl.add(abs(wordPerLine0 - wordPerLine4));
        AvgWpl.add(abs(wordPerLine0 - wordPerLine5));
        AvgWpl.add(abs(wordPerLine1 - wordPerLine2));
        AvgWpl.add(abs(wordPerLine1 - wordPerLine3));
        AvgWpl.add(abs(wordPerLine1 - wordPerLine4));
        AvgWpl.add(abs(wordPerLine1 - wordPerLine5));
        AvgWpl.add(abs(wordPerLine2 - wordPerLine3));
        AvgWpl.add(abs(wordPerLine2 - wordPerLine4));
        AvgWpl.add(abs(wordPerLine2 - wordPerLine5));
        AvgWpl.add(abs(wordPerLine3 - wordPerLine4));
        AvgWpl.add(abs(wordPerLine3 - wordPerLine5));
        AvgWpl.add(abs(wordPerLine4 - wordPerLine5));

        double linePerVerse0 = getAvgLpv(token0);
        double linePerVerse1 = getAvgLpv(token1);
        double linePerVerse2 = getAvgLpv(token2);
        double linePerVerse3 = getAvgLpv(token3);
        double linePerVerse4 = getAvgLpv(token4);
        double linePerVerse5 = getAvgLpv(token5);

        ArrayList<Double> AvgLpv = new ArrayList<>();
        AvgLpv.add(abs(linePerVerse0 - linePerVerse1));
        AvgLpv.add(abs(linePerVerse0 - linePerVerse2));
        AvgLpv.add(abs(linePerVerse0 - linePerVerse3));
        AvgLpv.add(abs(linePerVerse0 - linePerVerse4));
        AvgLpv.add(abs(linePerVerse0 - linePerVerse5));
        AvgLpv.add(abs(linePerVerse1 - linePerVerse2));
        AvgLpv.add(abs(linePerVerse1 - linePerVerse3));
        AvgLpv.add(abs(linePerVerse1 - linePerVerse4));
        AvgLpv.add(abs(linePerVerse1 - linePerVerse5));
        AvgLpv.add(abs(linePerVerse2 - linePerVerse3));
        AvgLpv.add(abs(linePerVerse2 - linePerVerse4));
        AvgLpv.add(abs(linePerVerse2 - linePerVerse5));
        AvgLpv.add(abs(linePerVerse3 - linePerVerse4));
        AvgLpv.add(abs(linePerVerse3 - linePerVerse5));
        AvgLpv.add(abs(linePerVerse4 - linePerVerse5));

        // get the avarage syllabel per line
        double SylPerLine0 = getAvgSpl(token0);
        double SylPerLine1 = getAvgSpl(token1);
        double SylPerLine2 = getAvgSpl(token2);
        double SylPerLine3 = getAvgSpl(token3);
        double SylPerLine4 = getAvgSpl(token4);
        double SylPerLine5 = getAvgSpl(token5);

        ArrayList<Double> AvgSpl = new ArrayList<>();
        AvgSpl.add(abs(SylPerLine0 - SylPerLine1));
        AvgSpl.add(abs(SylPerLine0 - SylPerLine2));
        AvgSpl.add(abs(SylPerLine0 - SylPerLine3));
        AvgSpl.add(abs(SylPerLine0 - SylPerLine4));
        AvgSpl.add(abs(SylPerLine0 - SylPerLine5));
        AvgSpl.add(abs(SylPerLine1 - SylPerLine2));
        AvgSpl.add(abs(SylPerLine1 - SylPerLine3));
        AvgSpl.add(abs(SylPerLine1 - SylPerLine4));
        AvgSpl.add(abs(SylPerLine1 - SylPerLine5));
        AvgSpl.add(abs(SylPerLine2 - SylPerLine3));
        AvgSpl.add(abs(SylPerLine2 - SylPerLine4));
        AvgSpl.add(abs(SylPerLine2 - SylPerLine5));
        AvgSpl.add(abs(SylPerLine3 - SylPerLine4));
        AvgSpl.add(abs(SylPerLine3 - SylPerLine5));
        AvgSpl.add(abs(SylPerLine4 - SylPerLine5));

        /*
         * TASK 3:
         * - Think about how to combine all features that you extracted into a single similarity measure.
         * Some features might be more important for the similarity measure than others. Transform this
         * importance into weights.
         * - Use all previously weighted features to implement a similarity measure.
         * - Make sure your measure is normalized to [0,1].
         * - Apply this measure to each pair of poems and store the similarity scores in a matrix.
         */
        AvgWpl = norm(AvgWpl);
        AvgLpv = norm(AvgLpv);
        AvgSpl = norm(AvgSpl);
        tagPer = norm(tagPer);
        wordPer = norm(wordPer);

        String[][] print = new String[2][15];

        for (int i = 0; i < label.size(); i++) {
            print[0][i] = label.get(i);
        }
        for (int i = 0; i < label.size(); i++) {
            print[1][i] = weigh(AvgWpl, AvgLpv, AvgSpl, tagPer, wordPer).get(i).toString();
        }

        for (int i = 0; i < 15; i++) {

            System.out.print(print[0][i] + ": ");
            System.out.println(print[1][i]);

        }


        /*
         * TASK 4:
         * - Which two poems are the most similar?
         * - Why? Explain the influence of the individual features.
         * - Which other features could be used to improve the quality of your similarity measure?
         * (you don't have to implement them!)
         */
    }

    private static URI[] readPoem() throws URISyntaxException, IOException {
        URI[] db = new URI[6];
        URI uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem1.txt").toURI();
        db[0] = uri;
        uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem2.txt").toURI();
        db[1] = uri;
        uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem3.txt").toURI();
        db[2] = uri;
        uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem4.txt").toURI();
        db[3] = uri;
        uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem5.txt").toURI();
        db[4] = uri;
        uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem6.txt").toURI();
        db[5] = uri;
        return db;
    }

    private static ArrayList<Double> weigh(ArrayList<Double> AvgWpl, ArrayList<Double> AvgLpv, ArrayList<Double> AvgSpl, ArrayList<Double> wordPer, ArrayList<Double> tagPer) {

        ArrayList<Double> ret = new ArrayList<>();

        for (int i = 0; i < AvgWpl.size(); i++) {

            ret.add(AvgWpl.get(i) * 0.15 + AvgLpv.get(i) * 0.15 + AvgSpl.get(i) * 0.2 + wordPer.get(i) * 0.3 + tagPer.get(i) * 0.2);

        }

        return ret;
    }

    private static ArrayList<String> poemString(URI uri) throws URISyntaxException, IOException {
        final ArrayList<String> tmp = new ArrayList<>();
        Files.lines(Paths.get(uri)).forEach(s -> tmp.add(s));
        return tmp;
    }

    private static ArrayList<Sentence> tokenizer(ArrayList<String> s) throws FileNotFoundException {

        ArrayList<Sentence> tmps = new ArrayList<>();
        for (String sentence : s) {
            Sentence sent = new SentenceImpl();
            Reader st = new StringReader(sentence);
            PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(st, new CoreLabelTokenFactory(), "");
            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();
                Word tempWord = new WordImpl(label.word());
                sent.add(tempWord);
            }
            tmps.add(sent);
        }
        return tmps;
    }

    private static double getPer(HashMap<String, Integer> a, HashMap<String, Integer> b) {
        double k = 0.0;
        HashMap<String, Integer> tmp = new HashMap<>();
        tmp.putAll(a);
        tmp.putAll(b);

        for (Map.Entry<String, Integer> entry : tmp.entrySet()) {
            if (b.containsKey(entry.getKey()) && a.containsKey(entry.getKey())); else if (b.containsKey(entry.getKey()) && !a.containsKey(entry.getKey())) {
                k += 1;
            } else if (!b.containsKey(entry.getKey()) && a.containsKey(entry.getKey())) {
                k += 1;
            }
        }
        return k;
    }

    private static double getAvgWpl(ArrayList<Sentence> s) {

        double tot = 0.0;
        int lineN = 0;

        for (Sentence sent : s) {
            for (Word wor : sent) {
                tot++;
            }
            if (!sent.isEmpty()) {
                lineN++;
            }
        }

        return tot / lineN;
    }

    private static Double Max(ArrayList<Double> s) {
        Double max = -9999999.0;
        for (Double d : s) {
            if (d > max) {
                max = d;
            }
        }
        return max;
    }

    private static Double Min(ArrayList<Double> s) {
        Double min = 9999999.0;
        for (Double d : s) {
            if (d < min) {
                min = d;
            }
        }
        return min;
    }

    private static ArrayList<Double> norm(ArrayList<Double> s) {
        ArrayList<Double> tm = new ArrayList<>();
        for (Double d : s) {

            tm.add((Max(s) - d) / (Max(s) - Min(s)));

        }
        return tm;
    }

    private static double getAvgLpv(ArrayList<Sentence> s) {
        double tot = 0.0;
        int verseN = 1;
        for (Sentence sent : s) {
            if (sent.isEmpty()) {
                verseN++;
            } else {
                tot++;
            }
        }

        return tot / verseN;
    }

    private static double getAvgSpl(ArrayList<Sentence> s) {

        double tot = 0.0;
        int lineN = 0;

        for (Sentence sent : s) {
            for (Word wor : sent) {
                String tmp = wor.getToken();
                int k = syl(tmp);
                if (k == 0) {
                    k++;
                }
                tot += k;

            }
            if (!sent.isEmpty()) {
                lineN++;
            }
        }

        return tot / lineN;
    }

    private static HashMap<String, Integer> createStructure(ArrayList<Sentence> s, int flag) {
        HashMap<String, Integer> freq = new HashMap<>();
        for (Sentence i : s) {
            for (Word j : i) {
                if (flag == 1) {
                    freq.putIfAbsent(j.getToken(), 0);
                    freq.put(j.getToken(), freq.get(j.getToken()) + 1);
                } else {
                    freq.putIfAbsent(j.getTag(), 0);
                    freq.put(j.getTag(), freq.get(j.getTag()) + 1);
                }
            }
        }
        return freq;
    }

    private static int tot(HashMap<String, Integer> a) {
        int to = 0;
        for (Map.Entry<String, Integer> entry : a.entrySet()) {

            to += entry.getValue();

        }
        return to;
    }

    private static HashMap<String, Double> tagPer(HashMap<String, Integer> a) {
        HashMap<String, Double> ret = new HashMap<>();
        int to = tot(a);
        for (Map.Entry<String, Integer> entry : a.entrySet()) {
            ret.put(entry.getKey(), (double) entry.getValue()/to);
        }
        return ret;
    }
    
     private static double prePro(HashMap<String, Integer> a, HashMap<String, Integer> b){
        HashMap<String, Double> aa = tagPer(a);
        HashMap<String, Double> bb = tagPer(b);
        HashMap<String, Double> tmp = new HashMap<>();
        tmp.putAll(aa);
        tmp.putAll(bb);
        double tot = 0.0;
         
        for (Map.Entry<String, Double> entry : tmp.entrySet()) {
            if(aa.containsKey(entry.getKey()) && bb.containsKey(entry.getKey())){
            
                tot += abs(aa.get(entry.getKey()) - bb.get(entry.getKey()));
            
            }
            if(!aa.containsKey(entry.getKey()) && bb.containsKey(entry.getKey())){
            
                tot += abs(0 - bb.get(entry.getKey()));
            
            }
            if(aa.containsKey(entry.getKey()) && !bb.containsKey(entry.getKey())){
            
                tot += abs(aa.get(entry.getKey())- 0);
            
            }
        } 
         
     return tot/tmp.size();
     }
    

    public static int syl(String s) {
        final Pattern pat = Pattern.compile("([ayeiou]+)");
        final String lc = s.toLowerCase();
        final Matcher o = pat.matcher(lc);
        int ct = 0;
        while (o.find()) {
            ct++;
        }
        if (lc.endsWith("e")) {
            ct--;
        }
        return ct < 0 ? 1 : ct;
    }

    private static ArrayList<Sentence> clean(ArrayList<Sentence> s) {
        ArrayList<Sentence> tmps = new ArrayList<>();
        PorterStemmer st = new PorterStemmer();
        for (int i = 0; i < s.size(); i++) {
            Sentence t = new SentenceImpl();
            //if(!s.get(i).isEmpty()){
            for (Word word : s.get(i)) {
                if (word.getTag().length() > 1) {
                    st.setCurrent(word.getToken());
                    if (st.stem()) {
                        word.setStem(st.getCurrent());
                    }
                    t.add(word);
                }
                //}

            }
            tmps.add(t);
        }
        return tmps;
    }

}
