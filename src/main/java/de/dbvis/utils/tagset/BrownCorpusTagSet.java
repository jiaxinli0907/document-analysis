package de.dbvis.utils.tagset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

/**
 * Created by Wolfgang Jentner (University of Konstanz) [wolfgang.jentner@uni.kn].
 */
public final class BrownCorpusTagSet implements Tagset {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrownCorpusTagSet.class);

    public static final BrownCorpusTagSet instance = new BrownCorpusTagSet();

    private BrownCorpusTagSet() {}

    @Override
    public String getName() {
        return "Brown Corpus Tagset";
    }

    @Override
    public EnumSet<StandardTag> getTag(String toCheck) {
        EnumSet<StandardTag> ret = EnumSet.noneOf(StandardTag.class);

        String[] tags = toCheck.split("[+]");

        for(String tag : tags) {
            if(tag.endsWith("*")) {
                ret.add(StandardTag.negation);
                ret.add(this.getSingleTag(tag.substring(0, tag.length()-1)));
            } else {
                ret.add(this.getSingleTag(tag));
            }
        }

        return ret;
    }

    private StandardTag getSingleTag(String singleTag) {
        singleTag = singleTag.trim();
        switch(singleTag) {
            case "":
                return StandardTag.unavailable;

            case "2":
            case "4":
            case "4''":
                return StandardTag.unavailable;

            case "'":
            case "``":
            case "''":
                return StandardTag.quotation;

            case "(":
            case ")":
                return StandardTag.parenthesis;

            case ",":
            case "--":
            case ".":
            case ":":
                return StandardTag.punctuation;

            case "abl":
            case "abn":
            case "abx":
                return StandardTag.unavailable;

            case "ap":
            case "at":
            case "ap$":
                return StandardTag.determiner;

            case "be":
            case "bed":
            case "bed*":
            case "bedz":
            case "beg":
            case "bem":
            case "ben":
            case "ber":
            case "ber*":
            case "bez":
            case "bez*":
            case "bedz*":
                return StandardTag.verb;

            case "cc":
                return StandardTag.conjunction;

            case "cd":
            case "cd$":
                return StandardTag.number;

            case "cs":
                return StandardTag.conjunction;

            case "do":
            case "do*":
            case "dod":
            case "dod*":
            case "doz":
            case "doz*":
                return StandardTag.verb;

            case "dt":
            case "dti":
            case "dts":
            case "dtx":
            case "dt$":
                return StandardTag.determiner;

            case "ex":
                return StandardTag.determiner;

            case "fw":
                //foreign words --> ?
                return StandardTag.unavailable;

            case "hv":
            case "hv*":
            case "hvd":
            case "hvd*":
            case "hvg":
            case "hvn":
            case "hvz":
            case "hvz*":
                return StandardTag.verb;

            case "in":
                return StandardTag.preposition;

            case "jj":
            case "jjr":
            case "jjs":
            case "jjt":
            case "jj$":
                return StandardTag.adjective;

            case "l":
                return StandardTag.unavailable;

            case "ls":
                return StandardTag.list_item_marker;

            case "md":
            case "md*":
                return StandardTag.modal;

            case "nil":
                return StandardTag.unavailable;

            case "nn":
            case "nn$":
            case "nnp":
            case "nnps":
            case "nns":
            case "nns$":
            case "np":
            case "np$":
            case "nps":
            case "nps$":
            case "nr":
            case "nr$":
            case "nrs":
                return StandardTag.noun;

            case "od":
                return StandardTag.number;

            case "or":
                return StandardTag.conjunction;

            case "pn":
            case "pn$":
            case "pp$":
            case "pp$$":
            case "ppl":
            case "ppls":
            case "ppo":
            case "pps":
            case "prp":
            case "prp$":
            case "ppss":
            case "pp":
                return StandardTag.pronoun;

            case "ql":
            case "qlp":
                return StandardTag.adverb;

            case "rb":
            case "rbr":
            case "rbt":
            case "rp":
            case "rb$":
            case "rn":
                return StandardTag.adverb;

            case "to":
                return StandardTag.preposition;

            case "uh":
                return StandardTag.interjection;

            case "vb":
                return StandardTag.verb;

            case "vbd":
                return StandardTag.verb;

            case "vbg":
                return StandardTag.verb;

            case "vbn":
                return StandardTag.verb;

            case "vbp":
                return StandardTag.verb;

            case "vbz":
                return StandardTag.verb;

            case "wdt":
                return StandardTag.determiner;

            case "wp":
            case "wp$":
            case "wpo":
            case "wps":
                return StandardTag.pronoun;


            case "wrb":
            case "wql":
                return StandardTag.adverb;

            case "unknown":
                return StandardTag.unavailable;

            default:
                LOGGER.warn("Could not find a mapping for tag \'{}\'", singleTag);
                return StandardTag.unavailable;
        }
    }
}
