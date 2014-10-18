package life.utahairquality.urlExpander;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import org.junit.Test;

/**
 *
 * @author wei
 */
public class urlExpanderTest {
    
    @Test
    public void test() throws IOException{
        System.out.println(urlExpander("http://goo.gl/1X29iR"));
        //https://support.twitter.com/articles/78124-posting-links-in-a-tweet
        
    }
    
    public String urlExpander(String address) throws MalformedURLException, IOException{
        URL url = new URL(address);
 
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); //using proxy may increase latency
        connection.setInstanceFollowRedirects(false);
        connection.connect();
        String expandedURL = connection.getHeaderField("Location");
        connection.getInputStream().close();
        return expandedURL;
    }
}
