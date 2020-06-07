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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author thech
 */
public class tv {

    ArrayList<wordGroup> netMap = new ArrayList<>();
    ArrayList<wordGroup> x;
    List<Sentence> corpus = new ArrayList<>();
    String[] token;
    String[] stem;

    public tv() throws URISyntaxException, IOException {

        readTv("data/tv/2016_09_26_first_presidential_full_cable_1476407859_raw.csv");
        readTv("data/tv/2016_09_26_first_presidential_full_local_1476408773_raw.csv");
        readTv("data/tv/2016_09_26_first_presidential_late_1476407823_raw.csv");
        readTv("data/tv/2016_10_09_second_presidential_full_cable_1476215812_raw.csv");
        readTv("data/tv/2016_10_09_second_presidential_full_local_1476216430_raw.csv");
        readTv("data/tv/2016_10_09_second_presidential_late_1476112573_raw.csv");
        readTv("data/tv/2016_10_19_third_presidential_late_1477000238_raw.csv");

    }

    public void readTv(String loc) throws URISyntaxException, IOException {

        URI add = tv.class.getClassLoader().getResource(loc).toURI();
        Reader read = Files.newBufferedReader(Paths.get(add));
        CSVParser csvParser = new CSVParser(read, CSVFormat.DEFAULT);
        int i = 0;
        String info = "";
        String network = "";
        for (CSVRecord csvRecord : csvParser) {
            if (i != 0) {
                network = csvRecord.get(3);
                info = csvRecord.get(12);

                Reader tmpRead = new StringReader(info);
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
                    wordGroup tmpW = new wordGroup(tmps, network);
                    netMap.add(tmpW);

                }
                tmpRead.close();
            }
            i++;

        }

        this.x = new ArrayList<>(netMap);
        clean();

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
