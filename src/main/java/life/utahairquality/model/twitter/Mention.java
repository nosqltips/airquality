package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class Mention {
    private long id;
    private long start;
    private String name;
    private String screenName;
    private long end;

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("start")
    public long getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(long start) {
        this.start = start;
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

    @JsonProperty("end")
    public long getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(long end) {
        this.end = end;
    }
}
