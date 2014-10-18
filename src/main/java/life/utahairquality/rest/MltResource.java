package life.utahairquality.rest;

import life.utahairquality.model.SearchQuery;
import life.utahairquality.model.SearchResult;
import life.utahairquality.service.MoreLikeThisService;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author cbrown
 */
@Path("mlt")
public class MltResource {
    private static final Logger log = Logger.getLogger(MltResource.class.getName());

    @Context
    private UriInfo context;
    
    private final MoreLikeThisService mlt = new MoreLikeThisService();
    
    @GET
    @Produces("application/json")
    public SearchResult search(@QueryParam("docId") String docId) {
        SearchQuery query = new SearchQuery();
        query.setDocId(docId);
        return mlt.search(query);    }
}

