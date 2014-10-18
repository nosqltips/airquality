package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
public class SimpleTweet {
    private String id;
    private String screenname;
    private String text;

    public SimpleTweet() {}
    public SimpleTweet(String id, String screenname, String text) {
        this.id = id;
        this.screenname = screenname;
        this.text = text;
    }
    
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("screenname")
    public String getScreenname() {
        return screenname;
    }

    @JsonProperty("screenname")
    public void setScreenname(String source) {
        this.screenname = source;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }
}
