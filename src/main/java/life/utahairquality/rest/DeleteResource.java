package life.utahairquality.rest;

import life.utahairquality.model.SearchQuery;
import life.utahairquality.service.DeleteService;
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
@Path("delete")
public class DeleteResource {
    private static final Logger log = Logger.getLogger(DeleteResource.class.getName());

    @Context
    private UriInfo context;
    
    private final DeleteService delete = new DeleteService();
    
    /**
     * 
     * @param terms
     * @param screenName
     * @param docId
     * @return 
     */
    @GET
    @Produces("application/json")    
    public Boolean deleteByQuery(
            @QueryParam("terms") String terms, 
            @QueryParam("docId") String docId) {

        SearchQuery query = new SearchQuery();
        query.setTerms(terms);
        query.setDocId(docId);
        
        return delete.deleteByQuery(query);
    }
}

