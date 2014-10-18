package life.utahairquality.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author cbrown
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(life.utahairquality.rest.CountResource.class);
        resources.add(life.utahairquality.rest.CrossOriginResourceSharingFilter.class);
        resources.add(life.utahairquality.rest.DeleteResource.class);
        resources.add(life.utahairquality.rest.FacetResource.class);
        resources.add(life.utahairquality.rest.MltResource.class);
        resources.add(life.utahairquality.rest.SearchResource.class);
    }
}
