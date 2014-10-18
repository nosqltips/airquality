package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class Location {
    private double[] geo;

    //location":{"lat":5.29407376,"lon":103.09339893}    

    
    //add constructor
    public Location(){};
    
    @JsonProperty("geo")
    public double[] getGeo() {
        return geo;
    }

    @JsonProperty("geo")
    public void setGeo(double[] geo) {
        this.geo = geo;
    }

   
}
