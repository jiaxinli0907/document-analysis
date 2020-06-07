package de.dbvis.wordnet;

import de.dbvis.utils.Resources;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.Pointer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Wolfgang Jentner (University of Konstanz).
 */
public abstract class AbstractWordnet {

    protected static Dictionary dictionary;

    public AbstractWordnet() throws URISyntaxException, IOException {
        if (dictionary == null) {
            File file = Resources.getResource("wordnet").toFile();
            dictionary = new Dictionary(file);
            dictionary.open();
        }
    }

    /**
     * Find all common hypernyms (lemmas) of two given strings.
     * @param str1 word1
     * @param str2 word2
     * @return all common hypernyms
     */
    public abstract List<String> findCommonHypernyms(String str1, String str2);

    /**
     * Find all hypernyms for a given word.
     * Use this: dictionary.getIndexWord(word, POS.NOUN);
     * And go on from there.
     * @param word the word
     * @return a list of all hypernyms
     */
    protected abstract List<ISynsetID> getHypernyms(String word);

    /**
     * Finds all hypernyms recursively
     * @param synsets a list of hypernyms from the first word
     * @param allHypernyms a collection of hypernyms
     */
    protected void findAllHypernymsRecursively(List<ISynsetID> synsets, List<ISynsetID> allHypernyms) {
        synsets.forEach(iSynsetID -> {
            if(!allHypernyms.contains(iSynsetID)) {
                allHypernyms.add(iSynsetID);
            }
            findAllHypernymsRecursively(dictionary.getSynset(iSynsetID).getRelatedSynsets(Pointer.HYPERNYM), allHypernyms);
        });
    }
}
