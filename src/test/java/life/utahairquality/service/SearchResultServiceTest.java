package life.utahairquality.service;

import org.junit.Test;

/**
 *
 * @author cbrown
 */
public class SearchResultServiceTest {

    /**
     * Test of mangle method, of class SearchResultService.
     */
    @Test
    public void testMangle() {
        String text = "@TARGIT I couldn't agree more!   It's also about having vision for information mgmt  http://ow.ly/vMhmS ” #bigdata #analytics #infochaos";
        String mangle = SearchResultService.mangle(text);
        
        System.out.println("text=" + text);
        System.out.println("mangle=" + mangle);
    }
}
