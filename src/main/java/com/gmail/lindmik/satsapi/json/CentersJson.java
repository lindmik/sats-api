package com.gmail.lindmik.satsapi.json;

import java.util.List;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;


public class CentersJson {
  
  public static class Regions extends GenericJson {
    
    @Key
    private List<Region> regions;
    
    public List<Region> getRegions() {
      return regions;
    }
    
  }
  
  public static class Region extends GenericJson {
    
    @Key
    private String id;
    
    @Key
    private String name;
    
    @Key
    private List<Center> centers;
    
    public String getId() {
      return id;
    }
    
    public String getName() {
      return name;
    }
    
    public List<Center> getCenters() {
      return centers;
    }
    
  }
  
  public static class Center extends GenericJson {
    
    @Key
    @JsonString
    private Integer id;
    
    @Key
    @JsonString
    private Integer filterId;
    
    @Key
    private String name;
    
    @Key
    private String regionId;
    
    @Key
    private String url;
    
    @Key("lat")
    private Double latitude;
    
    @Key("long")
    private Double longitude;
    
    @Key
    private String description;
    
    @Key
    private String bookingDescription;
    
    @Key
    private Boolean availableForOnlineBooking;
    
    public Integer getId() {
      return id;
    }
    
    public Integer getFilterId() {
      return filterId;
    }
    
    public String getName() {
      return name;
    }
    
    public String getRegionId() {
      return regionId;
    }
    
    public String getUrl() {
      return url;
    }
    
    public Double getLatitude() {
      return latitude;
    }
    
    public Double getLongitude() {
      return longitude;
    }
    
    public String getDescription() {
      return description;
    }
    
    public String getBookingDescription() {
      return bookingDescription;
    }
    
    public Boolean getAvailableForOnlineBooking() {
      return availableForOnlineBooking;
    }
    
  }
  
  public static class CentersUrl extends GenericUrl {
    
    public CentersUrl(String encodedUrl) {
      super(encodedUrl);
    }
    
    public static CentersUrl listCenters() {
      return new CentersUrl("https://www.sats.fi/sats-api/fi/centers");
    }
    
  }
  
}
