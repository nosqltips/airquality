package life.utahairquality.enums;

/**
 *
 * @author cbrown
 */
public enum SearchField {
    TEXT("text"),
    SOURCE("source"),
    CREATED_AT("@timestamp"),

    USER("user"),
    ID("id"),
    SCREEN_NAME("screen_name"),
    
    USER_ID("user.id"),
    USER_SCREEN_NAME("user.screen_name"),
    PLACE_NAME("place.name");
    
    private final String name;
    SearchField(String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
