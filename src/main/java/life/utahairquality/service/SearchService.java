package life.utahairquality.service;

import life.utahairquality.enums.AggregationField;
import life.utahairquality.enums.SearchField;
import life.utahairquality.model.FacetRequest;
import life.utahairquality.model.SearchQuery;
import life.utahairquality.model.SearchResult;
import life.utahairquality.util.AggregationUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Interval;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * 
 * @author cbrown
 */
public class SearchService implements Serializable {
    private static final Logger log = Logger.getLogger(SearchService.class.getName());
    private final Client client = ClientService.getClient();
    
    /**
     * Stock search method.
     * 
     * @param searchQuery
     * @return 
     */
    public SearchResult search(SearchQuery searchQuery) {
        searchQuery = QueryBuilderService.setPaging(searchQuery);

        // Set interval based on search type
        Interval interval = Interval.HOUR;
        if (searchQuery.getPlace() != null || 
                (searchQuery.getEndDate() != null && searchQuery.getStartDate() == null) || 
                (searchQuery.getEndDate() != null && searchQuery.getStartDate() != null) || 
                searchQuery.getTerms() != null) {
            interval = Interval.DAY;
        }
        
        SortBuilder sort = new FieldSortBuilder(SearchField.CREATED_AT.getName())
            .order(SortOrder.DESC);

        SearchRequestBuilder builder = client.prepareSearch(ClientService.INDEX)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilderService.buildQuery(searchQuery))
                .setFrom(searchQuery.getPageFrom())
                .setSize(searchQuery.getPageSize())
                .addSort(sort);
        
        // Add aggregations
        builder.addAggregation(AggregationUtil.getDateHistogram("TWEETSBYHOUR", AggregationField.CREATED_AT.getName(), interval));
//        builder.addAggregation(AggregationUtil.getSignificantTerms(AggregationField.TAGS.toString(), AggregationField.TEXT.getName(), 10));
//        builder.addAggregation(AggregationUtil.getTerms(AggregationField.TWEETERS.toString(), AggregationField.USER_SCREEN_NAME.getName(), 10));
        
        System.out.println("Builder=" + builder.toString());
        
        SearchResponse response = builder.execute().actionGet();
        List<FacetRequest> requests = null;
        if (response.getAggregations() != null) {
            requests = AggregationUtil.parseAggregations(response.getAggregations());
        }
        
        // Update the SearchQuery results
        SearchHits h = response.getHits();
        searchQuery.setTimeMillis(Long.toString(response.getTookInMillis()));
        searchQuery.setTotalResults(h.getTotalHits());
        searchQuery.setAvailableResults(h.getHits().length);
        
        System.out.println("SearchService " + response.getHits().getTotalHits());
        return new SearchResult(searchQuery, SearchResultService.generateSearchOutput(h.getHits(), searchQuery.getSentiment()), requests);
    }
}
