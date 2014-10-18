package life.utahairquality.rest;

import life.utahairquality.model.SearchQuery;
import life.utahairquality.model.SearchResult;
import life.utahairquality.service.SearchService;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author cbrown
 */
@Path("search")
public class SearchResource {
    private static final Logger log = Logger.getLogger(SearchResource.class.getName());

    @Context
    private UriInfo context;
    
    private final SearchService search = new SearchService();
    
    /**
     * Standard search method call.
     * 
     * @param terms
     * @param place
     * @param startDate
     * @param endDate
     * @param sentiment
     * @param pageFrom
     * @param pageSize
     * @return 
     */
    @GET
    @Produces("application/json")    
    public SearchResult search(
            @QueryParam("terms") String terms, 
            @QueryParam("place") String place, 
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("sentiment") Boolean sentiment,            
            @QueryParam("pageFrom") Integer pageFrom, 
            @QueryParam("pageSize") Integer pageSize) {

        SearchQuery query = new SearchQuery();
        query.setTerms(cleanValue(terms));
        query.setPlace(cleanValue(place));
        query.setStartDate(cleanValue(startDate));
        query.setEndDate(cleanValue(endDate));
        query.setSentiment(cleanValue(sentiment));
        if (pageFrom != null) { 
            query.setPageFrom(pageFrom);
        }
        if (pageSize != null) {
            query.setPageSize(pageSize);
        }
      
        return search.search(query);
    }
    
    private String cleanValue(String s) {
        if (s != null && s.isEmpty()) {
            return null;
        } else {
            return s;
        }
    }
    
    private Boolean cleanValue(Boolean b) {
        if (b == null) {
            return Boolean.FALSE;
        } else {
            return b;
        }
    }
}

