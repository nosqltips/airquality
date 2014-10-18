package life.utahairquality.util;

import life.utahairquality.enums.Wildcard;

/**
 *
 * @author cbrown
 */
public class ParseUtil {
    
    /**
     * Clean some of the bad stuff out of our term.
     * 
     * @param term
     * @return 
     */
    public static String cleanTerm(String term) {
        if (term == null) { return term; }
        
        return term
                .trim()
                .toLowerCase()
                .replace(",", "")
                .replace("\\\"", "")
                .replace("'", "")
                .replace("(", "")
                .replace(")", "");
    }

    /**
     * Check to see if this term has a wildcard character in it- * or ?
     * @param term
     * @return 
     */
    public static Wildcard isWildcard(String term) {
        if (term.contains("?") || term.contains("*")) {
            if (term.endsWith("*")) {
                return Wildcard.PREFIX;
            } else {
                return Wildcard.FULL;
            }
        } else {
            return Wildcard.NONE;
        }       
    }
 }
