package de.dbvis.utils.tagset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

/**
 * Penn Treebank tag set, according to <a href="http://www.comp.leeds.ac.uk/amalgam/tagsets/upenn.html">http://www.comp.leeds.ac.uk/amalgam/tagsets/upenn.html</a>
 *
 * @author Florian Stoffel &lt;florian.stoffel@uni-konstanz.de&gt;
 */
public final class PennTreebankTagSet implements Tagset {

    private static final Logger LOGGER = LoggerFactory.getLogger(PennTreebankTagSet.class);

    /** the tag set instance */
    public static final PennTreebankTagSet instance = new PennTreebankTagSet();

    private PennTreebankTagSet() {
    }

    @Override
    public String getName() {
        return "Penn Treebank";
    }

    @Override
    public EnumSet<StandardTag> getTag(String toCheck) {
        EnumSet<StandardTag> ret = EnumSet.noneOf(StandardTag.class);

        String[] tags = toCheck.split("\\+");

        for(String tag : tags) {
            ret.add(this.getSingleTag(tag));
        }

        return ret;
    }

    private StandardTag getSingleTag(String singleTag) {
        singleTag = singleTag.trim();
        switch(singleTag) {
            case "":
                return StandardTag.unavailable;

            case "``":
                return StandardTag.quotation;

            case "''":
                return StandardTag.quotation;

            case "(":
            case "-LRB-":
                return StandardTag.parenthesis;

            case ")":
            case "-RRB-":
                return StandardTag.parenthesis;

            case ",":
                return StandardTag.punctuation;

            case "--":
                return StandardTag.punctuation;

            case ".":
                return StandardTag.punctuation;

            case ":":
                return StandardTag.punctuation;

            case "$":
                return StandardTag.unavailable;

            case "CC":
                return StandardTag.conjunction;

            case "CD":
                return StandardTag.number;

            case "DT":
                return StandardTag.determiner;

            case "EX":
                return StandardTag.determiner;

            case "FW":
                //foreign words --> ?
                return StandardTag.unavailable;

            case "IN":
                return StandardTag.preposition;

            case "JJ":
                return StandardTag.adjective;

            case "JJR":
                return StandardTag.adjective;

            case "JJS":
                return StandardTag.adjective;

            case "LS":
                return StandardTag.list_item_marker;

            case "MD":
                return StandardTag.modal;

            case "NN":
                return StandardTag.noun;

            case "NNP":
                return StandardTag.noun;

            case "NNPS":
                return StandardTag.noun;

            case "NNS":
                return StandardTag.noun;

            case "PDT":
                return StandardTag.determiner;

            case "POS":
                return StandardTag.pronoun;

            case "RP":
                return StandardTag.adverb;

            case "PRP":
                return StandardTag.pronoun;

            case "PRP$":
                return StandardTag.pronoun;

            case "RB":
                return StandardTag.adverb;

            case "RBR":
                return StandardTag.adverb;

            case "RBS":
                return StandardTag.adverb;

            case "SYM":
                return StandardTag.symbol;

            case "TO":
                return StandardTag.preposition;

            case "UH":
                return StandardTag.interjection;

            case "VB":
                return StandardTag.verb;

            case "VBD":
                return StandardTag.verb;

            case "VBG":
                return StandardTag.verb;

            case "VBN":
                return StandardTag.verb;

            case "VBP":
                return StandardTag.verb;

            case "VBZ":
                return StandardTag.verb;

            case "WDT":
                return StandardTag.determiner;

            case "WP":
                return StandardTag.pronoun;

            case "WP$":
                return StandardTag.pronoun;

            case "WRB":
                return StandardTag.adverb;

            default:
                LOGGER.warn("Could not find a mapping for tag '{}'", singleTag);
                return StandardTag.unavailable;
        }
    }

}
