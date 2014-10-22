package life.utahairquality.service;

import life.utahairquality.enums.SearchField;
import life.utahairquality.model.Result;
import life.utahairquality.model.twitter.Location;
import life.utahairquality.model.twitter.Place;
import life.utahairquality.util.DateUtil;
import life.utahairquality.util.RegexUtil;
import life.utahairquality.util.SentimentCallable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.search.SearchHit;

/**
 *
 * @author cbrown
 */
public class SearchResultService {
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
    /**
     * Generate the search results to send to the client.
     * 
     * @param hits
     * @return 
     */
    public static List<Result> generateSearchOutput(SearchHit[] hits, Boolean sentiment)  {
        //create an arraylist the hold the searchoutput
        List<Result> values = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            //DisplayValue item = mapper.readValue((String) fields.get(Field.DISPLAY_VALUES.getName()), Result.class);
            Map<String, Object> fields = hit.sourceAsMap();
            //Map<String, String> display = (HashMap) fields.get(Field.DISPLAY_VALUES.getName());
            Result result = new Result();
            Location location = new Location();
            Place place = new Place();
            Map<String,Object> place1 = (HashMap)fields.get("place");
            Map<String,Object> geo1 = (HashMap)fields.get("geo");
            ArrayList<Double> latLong = null;
            if (geo1 != null) {
                 latLong = (ArrayList<Double>) geo1.get("coordinates");
            }
        
            
         //  ArrayList<double> latLong = geo1.get("coordinates");
            String placeName = "null";
            if( place1!=null) {
                 placeName = (String) place1.get("name");
            }
            place.setName(placeName);
            if (latLong !=null) {
                double[] temp = {latLong.get(0), latLong.get(1)};
                location.setGeo(temp);
            }
            result.setPlace(place);
            result.setLocation(location);
            result.setDocId(hit.getId());
            String text = (String) fields.get(SearchField.TEXT.getName());
            result.setText(mangle(text));
            result.setCreatedAt((String) fields.get("created_at"));
            result.setSource((String) fields.get(SearchField.SOURCE.getName()));
            result.setRelativeTime(DateUtil.formatRelativeTime((String) fields.get(SearchField.CREATED_AT.getName())));

            // USER
            Map<String, Object> user = (HashMap) fields.get(SearchField.USER.getName());
            result.setUserId(safeUserId(user.get(SearchField.ID.getName())));
            result.setScreenName((String) user.get(SearchField.SCREEN_NAME.getName()));
            
            // Sentiment
            result.setSentiment(2);

            result.setScore(hit.getScore());
            values.add(result);           
        }
        
        if (sentiment != null && sentiment) {
            values = runSentiment(values);
        }
        
        return values;
    }

    private static List<Result> runSentiment(List<Result> results) {
        List<Future> futures = new ArrayList<>();
        
        int size = 10;
        int length = results.size() < 100 ? results.size() : 100;
        for (int i = 0; i <= length; i++) {
            futures.add(threadPool.submit(new SentimentCallable(results, i, size)));
            i += size;
        }
        
        // Wait for all threads to complete.
        for (Future future: futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(SearchResultService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return results;
    }
    
    public static String mangle(String text)  {
        // Replace URLS with HREFs
        text = RegexUtil.URL.matcher(text)
                .replaceAll("<a href=\"\" target=\"_blank\" onmouseout=\"this.className=''\" onmouseover=\"this.className='ui-state-hover'\"></a>");
        // Replace @Refs with HREFS
        text = RegexUtil.AT_REF.matcher(text)

                .replaceAll("<a href=\"\" class=\"tweeter\" ng-click=\"searchUser(\'$2\')\">$1</a>");
//                .replaceAll("<a href=\"http://www.twitter.com/$2\" target=\"_blank\" onmouseout=\"this.className=''\" onmouseover=\"this.className='ui-state-hover'\">$1</a>");

        text = RegexUtil.HASH_REF.matcher(text)
//                .replaceAll("<a href=\"http://www.twitter.com/search?q=$2\" target=\"_blank\" onmouseout=\"this.className=''\" onmouseover=\"this.className='ui-state-hover'\">$1</a>");
                .replaceAll("<a href=\"\" class=\"tweeter\" ng-click=\"searchOther(\'$2\')\">$1</a>");
        
        return text;
    }

    private static Long safeUserId(Object userId) {
        if (userId instanceof Integer) {
            Integer intUserId = (Integer) userId;
            return intUserId.longValue();
        } else if (userId instanceof Long) {
            return (Long) userId;
        } else {
            return Long.parseLong(userId.toString());
        }
    }
}
