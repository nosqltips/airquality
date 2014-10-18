package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class Tweet {
    private String id;
    private String text;
    private String source;
    private String language;
    private Location location;
    private Link[] link;
    private HashTag[] hashtag;
    private long retweetCount;
    private Date createdAt;
    private Mention[] mention;
    private InReply inReply;
    private boolean truncated;
    private Place place;
    private User user;
    private Retweet retweet;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("link")
    public Link[] getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(Link[] link) {
        this.link = link;
    }

    @JsonProperty("hashtag")
    public HashTag[] getHashtag() {
        return hashtag;
    }

    @JsonProperty("hashtag")
    public void setHashtag(HashTag[] hashtag) {
        this.hashtag = hashtag;
    }

    @JsonProperty("retweet_count")
    public long getRetweetCount() {
        return retweetCount;
    }

    @JsonProperty("retweet_count")
    public void setRetweetCount(long retweetCount) {
        this.retweetCount = retweetCount;
    }

    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("mention")
    public Mention[] getMention() {
        return mention;
    }

    @JsonProperty("mention")
    public void setMention(Mention[] mention) {
        this.mention = mention;
    }

    @JsonProperty("in_reply")
    public InReply getInReply() {
        return inReply;
    }

    @JsonProperty("in_reply")
    public void setInReply(InReply inReply) {
        this.inReply = inReply;
    }

    @JsonProperty("truncated")
    public boolean isTruncated() {
        return truncated;
    }

    @JsonProperty("truncated")
    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    @JsonProperty("place")
    public Place getPlace() {
        return place;
    }

    @JsonProperty("place")
    public void setPlace(Place place) {
        this.place = place;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }

    @JsonProperty("retweet")
    public Retweet getRetweet() {
        return retweet;
    }

    @JsonProperty("retweet")
    public void setRetweet(Retweet retweet) {
        this.retweet = retweet;
    }
}
