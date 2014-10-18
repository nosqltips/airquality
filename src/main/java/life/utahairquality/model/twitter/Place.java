package life.utahairquality.model.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author cbrown
 */
@JsonIgnoreProperties
public class Place {
//    private String id;
    private String name;
//    private String type;
//    private String countryCode;
//    private String url;
//    private String fullName;
//    private String country;
 //  private String streetAddress;
    
   
   //add constructor
    public Place(){};

//    @JsonProperty("id")
//    public String getId() {
//        return id;
//    }
//
//    @JsonProperty("id")
//    public void setId(String id) {
//        this.id = id;
//    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
//
//    @JsonProperty("type")
//    public String getType() {
//        return type;
//    }
//
//    @JsonProperty("type")
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    @JsonProperty("country_code")
//    public String getCountryCode() {
//        return countryCode;
//    }
//
//    @JsonProperty("country_code")
//    public void setCountryCode(String countryCode) {
//        this.countryCode = countryCode;
//    }

//    @JsonProperty("url")
//    public String getUrl() {
//        return url;
//    }
//
//    @JsonProperty("url")
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    @JsonProperty("full_name")
//    public String getFullName() {
//        return fullName;
//    }
//
//    @JsonProperty("full_name")
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    @JsonProperty("country")
//    public String getCountry() {
//        return country;
//    }
//
//    @JsonProperty("country")
//    public void setCountry(String country) {
//        this.country = country;
//    }

//    @JsonProperty("street_address")
//    public String getStreetAddress() {
//        return streetAddress;
//    }
//
//    @JsonProperty("street_address")
//    public void setStreetAddress(String streetAddress) {
//        this.streetAddress = streetAddress;
//    }
}
