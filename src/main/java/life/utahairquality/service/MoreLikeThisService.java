package life.utahairquality.service;

import life.utahairquality.enums.AggregationField;
import life.utahairquality.enums.SearchField;
import life.utahairquality.model.FacetRequest;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.action.search.SearchResponse;
import life.utahairquality.model.SearchQuery;
import life.utahairquality.model.SearchResult;
import life.utahairquality.util.AggregationUtil;
import life.utahairquality.util.QueryUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * 
 * @author cbrown
 */
public class MoreLikeThisService implements Serializable {
    private static final Logger log = Logger.getLogger(MoreLikeThisService.class.getName());
    private final Client client = ClientService.getClient();
    
    public SearchResult search(SearchQuery searchQuery) {
        // Set page sizes to passed in values if not already set in query parser
        if (searchQuery.getPageSize() == -1) {
            searchQuery.setPageSize(ClientService.SIZE);
        }
        searchQuery.setPageFrom((searchQuery.getPage() -1)  * searchQuery.getPageSize());

        // Need to perform a get for the original document
        GetRequestBuilder get = client.prepareGet(ClientService.INDEX, ClientService.TYPE, searchQuery.getDocId());
        GetResponse getR = get.execute().actionGet();
        if (getR == null) {
            return null;
        }
        String text = (String)getR.getSource().get(SearchField.TEXT.getName());
        
        // Do a little bit of cleanup of the terms
        String[] terms = text.split("\\s|,");
        StringBuilder sb = new StringBuilder();
        for (String term: terms) {
            if (! (term.startsWith("#") 
                    || term.startsWith("@") 
                    || term.startsWith("http:") 
                    || term.toLowerCase().equals("rt") 
                    || term.toLowerCase().equals("oh"))) {
                sb.append(term).append(" ");
            }
        }
        
        SortBuilder sort = new FieldSortBuilder(SearchField.CREATED_AT.getName())
                .order(SortOrder.DESC);

        SortBuilder rel = new ScoreSortBuilder()
                .order(SortOrder.DESC);

        SearchRequestBuilder builder = client.prepareSearch(ClientService.INDEX)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryUtil.getTextQuery(SearchField.TEXT.getName(), sb.toString()))
                .setPostFilter(QueryUtil.getRangeFilterBuilder(AggregationField.CREATED_AT.getName(), DateTime.now().minusDays(1).toString(ISODateTimeFormat.dateTime()), null))
                .setFrom(searchQuery.getPageFrom())
                .setSize(searchQuery.getPageSize())
                .addSort(rel);
//                .addSort(sort);
        
        // Add aggregations
        builder.addAggregation(AggregationUtil.getDateHistogram("TWEETSBYHOUR", AggregationField.CREATED_AT.getName(), DateHistogram.Interval.DAY));
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
        
        System.out.println("MoreLikeThisService " + response.getHits().getTotalHits());
        return new SearchResult(searchQuery, SearchResultService.generateSearchOutput(h.getHits(), searchQuery.getSentiment()), requests);
    }
}
