package life.utahairquality.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class Count {
    private long count;

    @JsonProperty("count")
    public long getCount() {
        return count;
    }

    @JsonProperty("count")
    public Count setCount(long count) {
        this.count = count;
        return this;
    }
}
