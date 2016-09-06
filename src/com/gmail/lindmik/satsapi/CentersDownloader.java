package com.gmail.lindmik.satsapi;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.JsonString;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;


public class CentersDownloader {
  
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  
  public static class ApiRegions extends GenericJson {
    
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
  
  private static void parseResponse(HttpResponse response) throws IOException {
    ApiRegions apiRegions = response.parseAs(ApiRegions.class);
    
    if (apiRegions.getRegions().isEmpty()) {
      System.out.println("No centers found.");
      return;
    }
    
    Map<String, String> regionNamesById = apiRegions.getRegions().stream().collect(Collectors.toMap(Region::getId, Region::getName));
    
    apiRegions
        .getRegions()
        .stream()
        .map(Region::getCenters)
        .flatMap(Collection::stream)
        .forEach(new CenterPrinter(regionNamesById)::printCenter);
  }
  
  private static class CenterPrinter {
    
    private Map<String, String> _regionNamesById;
    
    public CenterPrinter(Map<String, String> regionNamesById) {
      _regionNamesById = regionNamesById;
    }
    
    public void printCenter(Center center) {
      System.out.println();
      System.out.println("Region: " + _regionNamesById.get(center.getRegionId()));
      System.out.println("Name: " + center.getName());
      System.out.println("ID: " + center.getId());
    }
    
  }
  
  private static void run() throws IOException {
    HttpRequestFactory requestFactory =
        HTTP_TRANSPORT.createRequestFactory(request -> request.setParser(new JsonObjectParser(JSON_FACTORY)));
    CentersUrl url = CentersUrl.listCenters();
    HttpRequest request = requestFactory.buildGetRequest(url);
    parseResponse(request.execute());
  }
  
  public static void main(String[] args) {
    try {
      try {
        run();
        return;
      } catch (HttpResponseException e) {
        System.err.println(e.getMessage());
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
  
}
