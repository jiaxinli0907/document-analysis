package de.dbvis;

import db.dbvis.exercise7.nlpTagger;
import de.dbvis.similarity.TfCosineSimilarity;
import de.dbvis.similarity.TfIdfCosineSimilarity;
import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;
import de.dbvis.structured.Word;
import de.dbvis.structured.WordImpl;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.tartarus.snowball.ext.PorterStemmer;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public class Exercise9 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exercise9.class);

    public static void main(String[] args) throws URISyntaxException, IOException {
        //term --> document --> frequency
        final Map<String, Map<String, Integer>> dataStructure = new HashMap<>();
        final Map<String, Map<String, Integer>> dataStructure2 = new HashMap<>();
        /*
         * The goal of this assignment is to get a feeling of how document similarity can be defined.
         * Please, use the poems from Assignment 7
         * + 2 new poems which were added to the ChristmasPoems folder.
         * Remember, one poem represents one document.
         */
        /*
         * TASK 1 (5 pts)
         * Fill the given data structure from the 8 text files (poems). See the documentation in the code.
         */
                
        final URI[] poem = readPoem();
        ArrayList<ArrayList<String>> p = new ArrayList<>();
        for(URI i:poem){
            p.add(poemString(i));
        }
        
        ArrayList<ArrayList<Sentence>> token = new ArrayList<>();
        for(ArrayList<String> j:p){
            token.add(tokenizer(j));
        }
    

        nlpTagger tagger = new nlpTagger();
        for(ArrayList<Sentence> m:token){
            tagger.tag(m);
            
        }
        
        ArrayList<ArrayList<Sentence>> cleaned = new ArrayList<>();
        
        for(ArrayList<Sentence> m:token){
            cleaned.add(clean(m));
            
        }
        
        int flag=0;
        for (ArrayList<Sentence> tk : cleaned) {
            flag++;            
            for(Sentence s:tk){
                for(Word w:s){
                    
                    HashMap<String,Integer> hm = new HashMap<>();
                    if(dataStructure.containsKey(w.getToken())){
                    hm.putAll(dataStructure.get(w.getToken()));
                    }
                    hm.putIfAbsent("Poem " + flag, 0);
                    hm.put("Poem " + flag, hm.get("Poem " + flag) + 1);
                    dataStructure.put(w.getToken(), hm);
                }
            }     
        }   


        /*
         * TASK 2 (5 pts)
         * Implement the interface de.dbvis.utils.VectorMath and its methods.
         * Implement the methods on your own! Do not use libraries!
         */
        


        /*
         * TASK 3 (10 pts)
         * Create two classes 1) TfCosineSimilarity and 2) TfIdfCosineSimilarity.
         * Both classes should extend from the AbstractCosineSimilarity.
         * Hint: Take use of your data structure from Task 1.
         *
         * Create two similarity matrices where you compare the documents pairwise.
         * Store the TfCosineSimilarity in one matrix and TfIdfCosineSimilarity in another.
         * Include the result matrices in your PDF file and discuss the results.
         */
        TfCosineSimilarity tf = new TfCosineSimilarity(dataStructure);
        TfIdfCosineSimilarity tfidf = new TfIdfCosineSimilarity(dataStructure);
        double[][] tfa = new double[8][8];
        double[][] tfidfa = new double[8][8];
        String s = "Poem ";
        for(int i=1;i<=8;i++){
            for(int j=1;j<=8;j++){
                tfa[i-1][j-1]= tf.similarity(s+i, s+j);
                tfidfa[i-1][j-1]= tfidf.similarity(s+i, s+j);
            }
        }
        System.out.println("tf cosine similarity");
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(String.format("%.3f", tfa[i][j]) + ",");
            }
            System.out.println();
        }

        System.out.println("tf-idf cosine similarity");
        
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(String.format("%.3f",tfidfa[i][j] ) + ",");
            }
            System.out.println();
        }
       
        /*
         * TASK 4 (10 pts)
         * Repeat Task 1 and apply the following operations from the NLP pipeline:
         *  1. Stemming
         *  2. Stop word removal
         *
         * Create again two similarity matrices and compare these two matrices to the ones of Task 3.
         * Include the result matrices in your PDF file and discuss the results.
         * HINT: Simply copy the above dataStructure and fill it again.
         */
        int f=0;
        ArrayList<String> stopWord = readStopword();
        for (ArrayList<Sentence> tk : cleaned) {
            f++;            
            for(Sentence s2:tk){
                for(Word w2:s2){
                    boolean flag2 = false;
                    for(String stop : stopWord){
                        if(w2.getToken().equals(stop)){
                            flag2 = true;
                            break;
                        }
                    }

                    if(flag2){
                        break;      
                    }   
                    HashMap<String,Integer> hm = new HashMap<>();
                    if(dataStructure2.containsKey(w2.getStem())){
                    hm.putAll(dataStructure2.get(w2.getStem()));
                    }
                    hm.putIfAbsent("Poem " + f, 0);
                    hm.put("Poem " + f, hm.get("Poem " + f) + 1);
                    dataStructure2.put(w2.getStem(), hm);
                }
            }     
        } 
        
        TfCosineSimilarity tf2 = new TfCosineSimilarity(dataStructure2);
        TfIdfCosineSimilarity tfidf2 = new TfIdfCosineSimilarity(dataStructure2);
        double[][] tfa2 = new double[8][8];
        double[][] tfidfa2 = new double[8][8];
        for(int i=1;i<=8;i++){
            for(int j=1;j<=8;j++){
                tfa2[i-1][j-1]= tf2.similarity(s+i, s+j);
                tfidfa2[i-1][j-1]= tfidf2.similarity(s+i, s+j);
            }
        }
        System.out.println("tf cosine similarity2");
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(String.format("%.3f", tfa2[i][j]) + ",");
            }
            System.out.println();
        }

        System.out.println("tf-idf cosine similarity2");
        
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(String.format("%.3f",tfidfa2[i][j] ) + ",");
            }
            System.out.println();
        }
        

    }
    
        private static URI[] readPoem() throws URISyntaxException, IOException {
        URI[] db = new URI[8];
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
        uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem7.txt").toURI();
        db[6] = uri;
        uri = Exercise7.class.getClassLoader().getResource("data/ChristmasPoems/poem8.txt").toURI();
        db[7] = uri;
        return db;
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

    private static ArrayList<String> readStopword() throws IOException, URISyntaxException {
        URI uri = Exercise9.class.getClassLoader().getResource("stop-words.txt").toURI();
        ArrayList<String> p = new ArrayList<>();
        Iterator<String> it = Files.lines(Paths.get(uri)).iterator();
        while (it.hasNext()) {

            p.add(it.next());

        }
        return p;
    }
    
}
