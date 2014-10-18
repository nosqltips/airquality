package life.utahairquality.enums;

/**
 *
 * @author cbrown
 */
public enum AggregationField {
    TEXT("text"),
    SOURCE("source"),
    CREATED_AT("@timestamp"),

    USER("user"),
    ID("id"),
    SCREEN_NAME("screen_name"),
    
    USER_ID("user.id"),
    USER_SCREEN_NAME("user.screen_name"),

    TAGS("text"),
    TWEETERS("user.screen_name"),
    TWEETSBYHOUR("@timestamp");
    
    private String name;
    AggregationField(String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
