package life.utahairquality.rest;

import life.utahairquality.model.Count;
import life.utahairquality.service.CountService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * @author cbrown
 */
@Path("count")
public class CountResource {

    @Context
    private UriInfo context;

    private final CountService countService = new CountService();
    
    @GET
    @Produces("application/json")
    public Count getCount() {
        long count = countService.countAll();
        return new Count().setCount(count);
    }
}
