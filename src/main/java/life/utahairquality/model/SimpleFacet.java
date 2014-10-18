package life.utahairquality.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class SimpleFacet implements Comparable<SimpleFacet> {
    private String name;
    private Long count;
    private Double value;

    public SimpleFacet() { }
    
    public SimpleFacet(String name, Long count) {
        this.name = name;
        this.count = count;
    }
    
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public SimpleFacet setName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("count")
    public Long getCount() {
        return count;
    }

    @JsonProperty("count")
    public SimpleFacet setCount(Long count) {
        this.count = count;
        return this;
    }

    @JsonProperty("value")
    public Double getValue() {
        return value;
    }

    @JsonProperty("value")
    public SimpleFacet setValue(Double value) {
        this.value = value;
        return this;
    }
    
    @Override
    public int compareTo(SimpleFacet o) {
        return (o.getCount().compareTo(count));
    }
}
