/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dbvis;

import de.dbvis.structured.Sentence;
import de.dbvis.structured.SentenceImpl;
import de.dbvis.structured.Word;
import de.dbvis.structured.WordImpl;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author thech
 */
public class reddit {

    ArrayList<wordGroup> postMap = new ArrayList<>();
    ArrayList<wordGroup> x;
    List<Sentence> corpus = new ArrayList<>();
    String[] token;
    String[] stem;

    public reddit() throws URISyntaxException, IOException, ParseException {

        final URI reddit = readReddit();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(new File(reddit)));
        JSONArray jsonArray = (JSONArray) obj;
        int size = jsonArray.size();
        String text = "";
        for (int i = 0; i < size; i++) {
            findBody((JSONObject) jsonArray.get(i));
        }

    }

    private void findBody(JSONObject obj) throws IOException, URISyntaxException {
        String tmp = "";
        String author = "";
        JSONObject jobj;
        JSONArray jarray;
        if (obj.containsKey("data")) {
            jobj = (JSONObject) obj.get("data");
            if (jobj.containsKey("body")) {
                author = (String) jobj.get("author");
                Reader tmpRead = new StringReader(jobj.get("body").toString());
                DocumentPreprocessor dp = new DocumentPreprocessor(tmpRead);
                for (List<HasWord> sentence : dp) {
                    ArrayList<String> tmps = new ArrayList<>();
                    Sentence sent = new SentenceImpl();
                    for (HasWord word : sentence) {

                        String tmpo = "";
                        tmpo = word.word().toLowerCase().replaceAll("-rrb-", "").replaceAll("-lrb-", "").replaceAll("#text", "").replaceAll("-rsb-", "").replaceAll("-lsb-", "");
                        if (!tmpo.equals("")) {
                            Word tmpw = new WordImpl(tmpo);
                            sent.add(tmpw);

                        }
                        tmps.add(word.word());

                    }
                    corpus.add(sent);
                    wordGroup tmpW = new wordGroup(tmps, author);
                    postMap.add(tmpW);

                }
                tmpRead.close();
            }
            if (jobj.containsKey("children")) {
                jarray = (JSONArray) jobj.get("children");
                for (int i = 0; i < jarray.size(); i++) {
                    if (!(jarray.get(i) instanceof String)) {
                        findBody((JSONObject) jarray.get(i));

                    }
                }

            }
        }

        this.x = new ArrayList<>(postMap);
        clean();

    }

    private static URI readReddit() throws URISyntaxException, IOException {
        //       final StringBuilder s = new StringBuilder();
        URI uri = reddit.class.getClassLoader().getResource("data/reddit/redditdump.json").toURI();
        //       Files.lines(Paths.get(uri)).forEach(s::append);
        return uri;
    }

    public void clean() throws URISyntaxException, IOException {
        ArrayList<String> stopWord = readStopword();
        ArrayList<String> tk = new ArrayList<String>();
        int i = 0;
        for (wordGroup key : x) {
            int in = 0;
            ArrayList<String> tmpsarr2 = new ArrayList<>();
            for (String val : key.Sentence) { 

                tmpsarr2.add(val.toLowerCase().replaceAll("-rrb-", "").replaceAll("-lrb-", "").replaceAll("#text", "").replaceAll("-rsb-", "").replaceAll("-lsb-", "").replaceAll("[^a-zA-Z1-9' ]", ""));
                boolean stopo = false;
                for (String stop : stopWord) {
                    if (tmpsarr2.get(in).contains(stop)) {
                        tmpsarr2.set(in, "");
                        stopo = true;
                        break;
                    }
                }
                if (tmpsarr2.get(in).length() > 1) {
                    tk.add(tmpsarr2.get(in));
                }

                in++;


            }
            
            x.set(i, key);
            i++;
        }
        token = new String[tk.size()];
        stem = new String[tk.size()];
        int indx = 0;
        for (String f : tk) {

            token[indx] = f;
            indx++;
        }

    }

    private ArrayList<String> readStopword() throws URISyntaxException, IOException {
        URI uri = Exercise1.class.getClassLoader().getResource("stop-words.txt").toURI();
        ArrayList<String> p = new ArrayList<>();
        Iterator<String> it = Files.lines(Paths.get(uri)).iterator();
        while (it.hasNext()) {

            p.add(it.next());

        }
        return p;

    }

}
