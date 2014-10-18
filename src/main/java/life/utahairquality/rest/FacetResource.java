package life.utahairquality.rest;

import life.utahairquality.model.SelectableFacet;
import life.utahairquality.service.AggregationService;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author cbrown
 */
@Path("facet")
public class FacetResource {

    @Context
    private UriInfo context;

    private final AggregationService service = new AggregationService();
    
    @GET
    @Path("trending")
    @Produces("application/json")
    public List<SelectableFacet> trending() {
        return service.trending();
    }

    @GET
    @Path("topTags")
    @Produces("application/json")
    public List<SelectableFacet> topTags() {
        List<SelectableFacet> f = service.topTags();
        System.out.println("f=" + (f != null ? f.size() : null));
        return f;
    }
    
    @GET
    @Path("topTweeters")
    @Produces("application/json")
    public List<SelectableFacet> topTweeters() {
        return service.topTweeters();
    }

    @GET
    @Path("todaysTopTweeters")
    @Produces("application/json")
    public List<SelectableFacet> todaysTopTweeters() {
        return service.todaysTopTweeters();
    }

    @GET
    @Path("tweetsByHour")
    @Produces("application/json")
    public List<SelectableFacet> tweetsByHour() {
        return service.tweetsByHour();
    }
}
