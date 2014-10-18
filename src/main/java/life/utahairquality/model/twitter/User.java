package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class User {
    private long id;
    private String location;
    private String description;
    private String name;
    private String screenName;
    private String profile_image_url;
    private String profile_image_url_https;
    
    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("screen_name")
    public String getScreenName() {
        return screenName;
    }

    @JsonProperty("screen_name")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @JsonProperty("profile_image_url")
    public String getProfileImageUrl() {
        return profile_image_url;
    }

    @JsonProperty("profile_image_url")
    public void setProfileImageUrl(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    @JsonProperty("profile_image_url_https")
    public String getProfileImageUrlHttps() {
        return profile_image_url_https;
    }

    @JsonProperty("profile_image_url_https")
    public void setProfileImageUrlHttps(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }
}
