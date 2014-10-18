package life.utahairquality.util;

import java.util.regex.Pattern;

/**
 *
 * @author cbrown
 */
public class RegexUtil {
    public static Pattern URL = Pattern.compile("(\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])");
    public static Pattern AT_REF = Pattern.compile("(@(\\b\\w+))");
    public static Pattern HASH_REF = Pattern.compile("(#(\\b\\w+))");
    public static Pattern RT = Pattern.compile("rt|RT");
    public static Pattern RE = Pattern.compile("re|RE");
    public static Pattern OH = Pattern.compile("oh|OH");
    public static Pattern NON_ASCII = Pattern.compile("[^\\x20-\\x7E]");
    
    public static String removeGarbage(String s) {
        s = URL.matcher(s).replaceAll("");
        s = AT_REF.matcher(s).replaceAll("");
        s = HASH_REF.matcher(s).replaceAll("");
        s = RT.matcher(s).replaceAll("");
        s = RE.matcher(s).replaceAll("");
        s = OH.matcher(s).replaceAll("");
        s = NON_ASCII.matcher(s).replaceAll("");
        s = s.trim();
        
        return s;
    }
}
