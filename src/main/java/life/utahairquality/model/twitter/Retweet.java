
package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class Retweet {
    private String id;
    private String userId;
    private String userScreenName;
    private long retweetCount;

//    "retweet":{"id":454732077683191809,"user_id":27922157,"user_screen_name":"AmerMedicalAssn","retweet_count":21}    
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("user_screen_name")
    public String getUserScreenName() {
        return userScreenName;
    }

    @JsonProperty("user_screen_name")
    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    @JsonProperty("retweet_count")
    public long getCount() {
        return retweetCount;
    }

    @JsonProperty("retweet_count")
    public void setCount(long count) {
        this.retweetCount = count;
    }
}
