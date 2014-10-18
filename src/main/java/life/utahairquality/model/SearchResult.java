package life.utahairquality.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author cbrown
 */
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class SearchResult {
    private SearchQuery query;
    private List<Result> results;
    private List<FacetRequest> requests;
    
    public SearchResult() {}
    public SearchResult(SearchQuery query, List<Result> results) {
        this.query = query;
        this.results = results;
    }
    public SearchResult(SearchQuery query, List<Result> results, List<FacetRequest> requests) {
        this.query = query;
        this.results = results;
        this.requests = requests;
    }
    
    @JsonProperty("query")
    public SearchQuery getQuery() {
        return query;
    }

    @JsonProperty("query")
    public void setQuery(SearchQuery query) {
        this.query = query;
    }

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonProperty("requests")
    public List<FacetRequest> getFacets() {
        return requests;
    }

    @JsonProperty("requests")
    public void setFacets(List<FacetRequest> requests) {
        this.requests = requests;
    }
}
