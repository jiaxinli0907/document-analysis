package de.dbvis.utils.tagset;

/**
 * A very limited tag set
 *
 * @author Florian Stoffel &lt;florian.stoffel@uni-konstanz.de&gt;
 */
public enum StandardTag {
    /** adjectives like {@code good, bad, first, ...} */
    adjective,
    /** adverbs like {@code occasionally, fiscally, swiftly, ...} */
    adverb,
    /** conjunctions like {@code for, and, nor, ...} */
    conjunction,
    /** determiners like {@code all, another, each, many, much, ...} */
    determiner,
    /** interjection like {@code Enough!..., Indeed!...} */
    interjection,
    /** list item marker */
    list_item_marker,
    /** modals like {@code can, may, must, will, ...} */
    modal,
    /** negation like {@code not, n't} */
    negation,
    /** nouns like {@code slick, cabbage, house, window, ...} */
    noun,
    /** numbers like {@code mid-1890, 79, zero, two, ...}  */
    number,
    /** parenthesis like {@code {, }, [, ], ...} */
    parenthesis,
    /** prepositions like {@code astride, among, aside, out, inside, ... } */
    preposition,
    /** pronouns like {@code her, herself, our, us, ...} */
    pronoun,
    /** punctuation symbols */
    punctuation,
    /** quotation symbols */
    quotation,
    /** other symbols */
    symbol,
    /** verbs like {@code ask, assemble, bomb, brace, ...} */
    verb,
    /** n/a */
    unavailable;
}
