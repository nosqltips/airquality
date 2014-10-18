package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class InReply {
    private String userScreenName;
    private long status;
    private long userId;

    @JsonProperty("user_screen_name")
    public String getUserScreenName() {
        return userScreenName;
    }

    @JsonProperty("user_screen_name")
    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    @JsonProperty("status")
    public long getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(long status) {
        this.status = status;
    }

    @JsonProperty("user_id")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
