package life.utahairquality.util;

import java.util.regex.Matcher;
import org.junit.Test;

/**
 *
 * @author cbrown
 */
public class RegexUtilTest {

    @Test
    public void testAt() {
        Matcher m = RegexUtil.AT_REF.matcher(("@something"));        
        System.out.println("at found=" + m.find());

        m = RegexUtil.AT_REF.matcher(("123 @something"));
        System.out.println("at found=" + m.find());

        m = RegexUtil.AT_REF.matcher(("1234 @something #another"));        
        System.out.println("at found=" + m.find());

        m = RegexUtil.AT_REF.matcher(("1234 @something #another"));
        System.out.println("at found=" + m.find());

        m = RegexUtil.AT_REF.matcher(("1234 @something_some #another"));        
        System.out.println("at found=" + m.find());

        m = RegexUtil.AT_REF.matcher(("1234 @KirkDBorne: #another"));        
        System.out.println("at found=" + m.find());
}
    @Test
    public void testHash() {
        Matcher m = RegexUtil.HASH_REF.matcher(("#something"));        
        System.out.println("hash found=" + m.find());

        m = RegexUtil.HASH_REF.matcher(("123 #something"));
        System.out.println("hash found=" + m.find());

        m = RegexUtil.HASH_REF.matcher(("1234 #something #another"));        
        System.out.println("hash found=" + m.find());

        m = RegexUtil.HASH_REF.matcher(("1234 #something #another"));
        System.out.println("hash found=" + m.find());

        m = RegexUtil.HASH_REF.matcher(("1234 #something_some #another"));        
        System.out.println("hash found=" + m.find());

        m = RegexUtil.HASH_REF.matcher(("1234 #KirkDBorne: #another"));        
        System.out.println("hash found=" + m.find());
}
    
    @Test
    public void testURL() {
        Matcher m = RegexUtil.URL.matcher(("http://1234.com"));
        
        System.out.println("url found=" + m.find());
    }
    
    @Test
    public void testRemove() {
        String s = RegexUtil.removeGarbage("RT re OH @test #another http://1234.com leftover");
        System.out.println("remove=" + s);
    }
}
