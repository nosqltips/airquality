package life.utahairquality.service;

import life.utahairquality.enums.AggregationField;
import life.utahairquality.enums.SearchField;
import life.utahairquality.model.SelectableFacet;
import life.utahairquality.util.AggregationUtil;
import life.utahairquality.util.QueryUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Interval;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * 
 * @author cbrown
 */
public class AggregationService implements Serializable {
    private static final Logger log = Logger.getLogger(AggregationService.class.getName());
    private final Client client = ClientService.getClient();

    /**
     * Trending tags last hour.
     * 
     * @return 
     */
    public List<SelectableFacet> trending() {
        SearchRequestBuilder builder = client.prepareSearch(ClientService.INDEX)
            .setSearchType(SearchType.QUERY_THEN_FETCH)
            .setQuery(QueryUtil.getRangeQueryBuilder(AggregationField.CREATED_AT.getName(), DateTime.now().minusHours(2).toString(ISODateTimeFormat.dateTime()), null))
            //.setFilter(QueryUtil.getRangeFilterBuilder(Field.CREATED_AT.getName(), DateTime.now().minusHours(1).toString(ISODateTimeFormat.dateTime()), null))
            .setFrom(0)
            .setSize(0)
            .addAggregation(AggregationUtil.getSignificantTerms("tags", AggregationField.TEXT.getName(), 10));
        
        SearchResponse response = builder.execute().actionGet();
        if (response.getAggregations() != null) {
            return (AggregationUtil.parseSingleAggregation(response.getAggregations().get("tags"), AggregationField.USER_SCREEN_NAME));
        }
        
        return null;
    }

    /**
     * Top tags from the entire index.
     * 
     * @return 
     */
    public List<SelectableFacet> topTags() {
        SearchRequestBuilder builder = client.prepareSearch(ClientService.INDEX)
            .setSearchType(SearchType.QUERY_THEN_FETCH)
            .setQuery(QueryUtil.getRangeQueryBuilder(AggregationField.CREATED_AT.getName(), DateTime.now().minusHours(24).toString(ISODateTimeFormat.dateTime()), null))
            //.setFilter(QueryUtil.getRangeFilterBuilder(Field.CREATED_AT.getName(), DateTime.now().minusHours(24).toString(ISODateTimeFormat.dateTime()), null))
            .setFrom(0)
            .setSize(0)
            .addAggregation(AggregationUtil.getSignificantTerms("tags", AggregationField.TEXT.getName(), 10));
        
        SearchResponse response = builder.execute().actionGet();
        if (response.getAggregations() != null) {
            return (AggregationUtil.parseSingleAggregation(response.getAggregations().get("tags"), AggregationField.USER_SCREEN_NAME));
        }
        
        return null;
    }
    
    /**
     * Top tweeters from the entire index.
     * 
     * @return 
     */
    public List<SelectableFacet> topTweeters() {
        SearchRequestBuilder builder = client.prepareSearch(ClientService.INDEX)
            .setSearchType(SearchType.QUERY_THEN_FETCH)
            .setPostFilter(QueryUtil.getMatchAllFilter())
            .setFrom(0)
            .setSize(0)
            .addAggregation(AggregationUtil.getTerms("tweeters", AggregationField.USER_SCREEN_NAME.getName(), 10));
        
        SearchResponse response = builder.execute().actionGet();
        if (response.getAggregations() != null) {
            return (AggregationUtil.parseSingleAggregation(response.getAggregations().get("tweeters"), AggregationField.USER_SCREEN_NAME));
        }
        
        return null;
    }

    /**
     * Top tweeters from the last 24 hours.
     * 
     * @return 
     */
    public List<SelectableFacet> todaysTopTweeters() {
        SearchRequestBuilder builder = client.prepareSearch(ClientService.INDEX)
            .setSearchType(SearchType.QUERY_THEN_FETCH)
            .setQuery(QueryUtil.getRangeQueryBuilder(SearchField.CREATED_AT.getName(), DateTime.now().minusHours(24).toString(ISODateTimeFormat.dateTime()), null))
            .setFrom(0)
            .setSize(0)
            .addAggregation(AggregationUtil.getTerms("tweeters", AggregationField.USER_SCREEN_NAME.getName(), 10));
        
        SearchResponse response = builder.execute().actionGet();
        if (response.getAggregations() != null) {
            return (AggregationUtil.parseSingleAggregation(response.getAggregations().get("tweeters"), AggregationField.USER_SCREEN_NAME));
        }
        
        return null;
    }

    /**
     * Number of tweets per hour
     * 
     * @return 
     */
    public List<SelectableFacet> tweetsByHour() {
        SearchRequestBuilder builder = client.prepareSearch(ClientService.INDEX)
            .setSearchType(SearchType.QUERY_THEN_FETCH)
            .setQuery(QueryUtil.getRangeQueryBuilder(SearchField.CREATED_AT.getName(), DateTime.now().minusHours(24).toString(ISODateTimeFormat.dateTime()), null))
            .setFrom(0)
            .setSize(0)
            .addAggregation(AggregationUtil.getDateHistogram("tweets", AggregationField.CREATED_AT.getName(), Interval.HOUR));
        
        SearchResponse response = builder.execute().actionGet();
        if (response.getAggregations() != null) {
            return (AggregationUtil.parseSingleAggregation(response.getAggregations().get("tweets"), AggregationField.CREATED_AT));
        }
        
        return null;
    }
}
