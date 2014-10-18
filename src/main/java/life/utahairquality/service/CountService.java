package life.utahairquality.service;

import life.utahairquality.util.QueryUtil;
import java.io.Serializable;
import java.util.logging.Logger;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.action.count.CountRequestBuilder;

/**
 * 
 * @author cbrown
 */
public class CountService implements Serializable {
    private static final Logger log = Logger.getLogger(CountService.class.getName());
    
    public long countAll() {
        Client client = ClientService.getClient();
        
        // TODO: can be a filter instead
        CountRequestBuilder builder = client.prepareCount(ClientService.INDEX)
                .setQuery(QueryUtil.getMatchAllQuery());
        
        CountResponse response = builder.execute().actionGet();
        return response.getCount();
    }
}
