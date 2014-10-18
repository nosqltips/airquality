package life.utahairquality.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import life.utahairquality.enums.AggregationField;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cbrown
 */
public class FacetRequest implements Serializable {
    // The field name we are faceting on.
    private AggregationField field;
    // How many facets to return.
    private int size = 10;
    // All of the rerned facets the user can choose from.
    private List<SelectableFacet> selectables;
    
    public FacetRequest() {}

    public FacetRequest(AggregationField field) {
        this.field = field;
    }

    public FacetRequest(AggregationField field, int size) {
        this.field = field;
        this.size = size;
    }
    
    @JsonProperty("field")
    public AggregationField getField() {
        return field;
    }

    @JsonProperty("field")
    public FacetRequest setField(AggregationField field) {
        this.field = field;
        return this;
    }

    @JsonIgnore
    public FacetRequest setField(String fieldName) {
        this.field = AggregationField.valueOf(fieldName);
        return this;
    }

    @JsonProperty("size")
    public int getSize() {
        return size;
    }

    @JsonProperty("size")
    public FacetRequest setSize(int size) {
        this.size = size;
        return this;
    }

    @JsonProperty("selectables")
    public List<SelectableFacet> getSelectables() {
        return selectables;
    }

    @JsonProperty("selectables")
    public FacetRequest setSelectables(List<SelectableFacet> selectables) {
        this.selectables = selectables;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj instanceof FacetRequest) {
            FacetRequest other = (FacetRequest) obj;
            if (this.getField().equals(other.getField())) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
