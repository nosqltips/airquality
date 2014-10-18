package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class Link {
    private long start;
    private String expandUrl;
    private String displayUrl;
    private String url;
    private long end;

    @JsonProperty("start")
    public long getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(long start) {
        this.start = start;
    }

    @JsonProperty("expand_url")
    public String getExpandUrl() {
        return expandUrl;
    }

    @JsonProperty("expand_url")
    public void setExpandUrl(String expandUrl) {
        this.expandUrl = expandUrl;
    }

    @JsonProperty("display_url")
    public String getDisplayUrl() {
        return displayUrl;
    }

    @JsonProperty("display_url")
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
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
