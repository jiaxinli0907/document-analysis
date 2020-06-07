package de.dbvis;


import de.dbvis.utils.WordCloudFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.json.simple.JSONObject;

/**
 * Created by Wolfgang Jentner.
 */
public class Exercise1 {

    public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException, ParseException {
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
        //final String loremipsum = readExample();
        
        // xml parse
	final URI[] debate = readDebate();
        final Map<String, Integer> wordFrequencies = new HashMap<>();
        DocumentBuilderFactory maker = DocumentBuilderFactory.newInstance();
        DocumentBuilder construct = maker.newDocumentBuilder();
        Document db1 = construct.parse(new File(debate[0]));
        String dbs1 = "";
        Document db2 = construct.parse(new File(debate[1]));
        String dbs2 = "";
        Document db3 = construct.parse(new File(debate[2]));
        String dbs3 = "";
        
        
        NodeList x = db1.getElementsByTagName("utterance");
        for (int i = 0; i < x.getLength(); i++) {
                    Node node = x.item(i);
                    dbs1 = dbs1 + " " + node.getChildNodes().item(0).toString();
                }
        x = db2.getElementsByTagName("utterance");
        for (int i = 0; i < x.getLength(); i++) {
                    Node node = x.item(i);
                    dbs2 = dbs2 + " " + node.getChildNodes().item(0).toString();
                }
        x = db3.getElementsByTagName("utterance");
        for (int i = 0; i < x.getLength(); i++) {
                    Node node = x.item(i);
                    dbs3 = dbs3 + " " + node.getChildNodes().item(0).toString();
                }
        String dbf = dbs1 + dbs2 + dbs3;
        Arrays.stream(dbf.split("\\W+")).forEach(w -> wordFrequencies.merge(w, 1, (a, b) -> a + b));
        WordCloudFactory.create(wordFrequencies, "debate.png");
        
        
        // Json parse
        final URI reddit = readReddit();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(new File(reddit)));
        JSONArray jsonArray = (JSONArray) obj;
        int size = jsonArray.size();
        String text="";
        for(int i=0; i<size;i++){
            text += findBody((JSONObject)jsonArray.get(i));
        }
        
        final Map<String, Integer> wordFrequencies1 = new HashMap<>();
        Arrays.stream(text.split("\\W+")).forEach(w -> wordFrequencies1.merge(w, 1, (a, b) -> a + b));
        WordCloudFactory.create(wordFrequencies1, "reddit.png");
        
        
        final String tv = readTv();
        String tmp2 = tv.replaceAll("transcript","");
        final Map<String, Integer> wordFrequencies2 = new HashMap<>();
        Arrays.stream(tmp2.split("\\W+")).forEach(w -> wordFrequencies2.merge(w, 1, (a, b) -> a + b));
        WordCloudFactory.create(wordFrequencies2, "tv.png");
		
	

        /*
         * Part 2
         * Count the words in a map. Do so for each dataset.
         */
        //Example:
        //final Map<String, Integer> wordFrequencies = new HashMap<>();
        //Arrays.stream(loremipsum.split(" ")).forEach(w -> wordFrequencies.merge(w, 1, (a, b) -> a + b));

        /*
         * Part 3
         * Create a word cloud for the dataset.
         */
        //Example:
        //WordCloudFactory.create(wordFrequencies, "example.png");
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
    
    private static String findBody(JSONObject obj) {
        String tmp="";
        JSONObject jobj;
        JSONArray jarray;
        if(obj.containsKey("data")){
            jobj = (JSONObject)obj.get("data");
            if(jobj.containsKey("body")){
            tmp =jobj.get("body").toString();
            }
            if(jobj.containsKey("children")){
             jarray = (JSONArray) jobj.get("children");
             for(int i = 0; i<jarray.size();i++ ){
                 if(!(jarray.get(i) instanceof String)){
                 tmp += " " + findBody((JSONObject) jarray.get(i));
             }
             }
            
        }
        }
        return tmp;
    }
    
    
    private static URI[] readDebate() throws URISyntaxException, IOException {
        URI[] db = new URI[3];
        URI uri = Exercise1.class.getClassLoader().getResource("data/debates/first.turns.xml").toURI();
        db[0] = uri;
	uri = Exercise1.class.getClassLoader().getResource("data/debates/second.turns.xml").toURI();
        db[1] = uri;
	uri = Exercise1.class.getClassLoader().getResource("data/debates/third.turns.xml").toURI();
        db[2] = uri;
        return db;
    }
    
    private static URI readReddit() throws URISyntaxException, IOException {
 //       final StringBuilder s = new StringBuilder();
        URI uri = Exercise1.class.getClassLoader().getResource("data/reddit/redditdump.json").toURI();
 //       Files.lines(Paths.get(uri)).forEach(s::append);
        return uri;
    }
    private static String readTv() throws URISyntaxException, IOException {
        String s = "";
        URI[] p = new URI[7]; 
        p[0] =  Exercise1.class.getClassLoader().getResource("data/tv/2016_09_26_first_presidential_full_cable_1476407859_raw.csv").toURI();
        p[1] = Exercise1.class.getClassLoader().getResource("data/tv/2016_09_26_first_presidential_full_local_1476408773_raw.csv").toURI();
        p[2] = Exercise1.class.getClassLoader().getResource("data/tv/2016_09_26_first_presidential_late_1476407823_raw.csv").toURI();
        p[3] = Exercise1.class.getClassLoader().getResource("data/tv/2016_10_09_second_presidential_full_cable_1476215812_raw.csv").toURI();
        p[4] = Exercise1.class.getClassLoader().getResource("data/tv/2016_10_09_second_presidential_full_local_1476216430_raw.csv").toURI();
        p[5] = Exercise1.class.getClassLoader().getResource("data/tv/2016_10_09_second_presidential_late_1476112573_raw.csv").toURI();
        p[6] = Exercise1.class.getClassLoader().getResource("data/tv/2016_10_19_third_presidential_late_1477000238_raw.csv").toURI();
        for (int i = 0; i < 7; i++){
        Reader read = Files.newBufferedReader(Paths.get(p[i]));
        CSVParser csvParser = new CSVParser(read, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) { 
                s+= " " + csvRecord.get(12);

            }
        }

        return s;
    }
}

