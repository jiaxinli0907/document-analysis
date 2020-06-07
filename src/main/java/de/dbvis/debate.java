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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author jiaxinli
 */
public class debate {

    ArrayList<wordGroup> presMap = new ArrayList<>();
    ArrayList<wordGroup> x;
    List<Sentence> corpus = new ArrayList<>();
    String[] token;
    String[] stem;

    public debate() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {

        final URI[] debate = readDebate();
        DocumentBuilderFactory maker = DocumentBuilderFactory.newInstance();
        DocumentBuilder construct = maker.newDocumentBuilder();
        Document db1 = construct.parse(new File(debate[0]));
        Document db2 = construct.parse(new File(debate[1]));
        Document db3 = construct.parse(new File(debate[2]));

        NodeList x = db1.getElementsByTagName("utterance");
        for (int i = 0; i < x.getLength(); i++) {
            Node node = x.item(i);
            String speaker = "";
            for (int c = 0; c < node.getAttributes().getLength(); c++) {
                if (node.getAttributes().item(c).getNodeName() == "name") {
                    speaker = node.getAttributes().item(c).getNodeValue();
                }

            }

            Reader tmpRead = new StringReader(node.getChildNodes().item(0).toString());
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
                wordGroup tmpW = new wordGroup(tmps, speaker);
                presMap.add(tmpW);

            }
            tmpRead.close();

        }

        x = db2.getElementsByTagName("utterance");
        for (int i = 0; i < x.getLength(); i++) {
            Node node = x.item(i);
            String speaker = "";
            for (int c = 0; c < node.getAttributes().getLength(); c++) {
                if (node.getAttributes().item(c).getNodeName() == "name") {
                    speaker = node.getAttributes().item(c).getNodeValue();
                }

            }

            Reader tmpRead = new StringReader(node.getChildNodes().item(0).toString());
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
                wordGroup tmpW = new wordGroup(tmps, speaker);
                presMap.add(tmpW);

            }
            tmpRead.close();

        }
        x = db3.getElementsByTagName("utterance");
        for (int i = 0; i < x.getLength(); i++) {
            Node node = x.item(i);
            String speaker = "";
            for (int c = 0; c < node.getAttributes().getLength(); c++) {
                if (node.getAttributes().item(c).getNodeName() == "name") {
                    speaker = node.getAttributes().item(c).getNodeValue();
                }

            }

            Reader tmpRead = new StringReader(node.getChildNodes().item(0).toString());
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
                wordGroup tmpW = new wordGroup(tmps, speaker);
                presMap.add(tmpW);

            }
            tmpRead.close();

        }

        this.x = new ArrayList<>(presMap);
        clean();

    }

    private static URI[] readDebate() throws URISyntaxException, IOException {
        URI[] db = new URI[3];
        URI uri = debate.class.getClassLoader().getResource("data/debates/first.turns.xml").toURI();
        db[0] = uri;
        uri = debate.class.getClassLoader().getResource("data/debates/second.turns.xml").toURI();
        db[1] = uri;
        uri = debate.class.getClassLoader().getResource("data/debates/third.turns.xml").toURI();
        db[2] = uri;
        return db;
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
