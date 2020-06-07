package de.dbvis.utils.tagset;

import java.util.EnumSet;

/**
 * Tag set
 *
 * @author Florian Stoffel &lt;florian.stoffel@uni-konstanz.de&gt;
 */
public interface Tagset {

    /**
     * Returns the name of the tag set
     *
     * @return the name of the tag set
     */
    String getName();

    /**
     * Checks whether the given tag is of the given standard tag family
     *
     * @param toCheck the tag to check
     * @return the standard tag the tag to check can be associated with
     */
    EnumSet<StandardTag> getTag(String toCheck);
}
