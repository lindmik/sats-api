package com.gmail.lindmik.satsapi.json;

import java.util.List;
import java.util.stream.Collectors;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;


public class ClassesJson {
  
  public static class Classes extends GenericJson {
    
    @Key
    private List<Class1> classes;
    
    public List<Class1> getClasses() {
      return classes;
    }
    
  }
  
  public static class Class1 extends GenericJson {
    
    @Key
    private String id;
    
    @Key
    private String name;
    
    @Key
    private String classTypeId;
    
    @Key
    @JsonString
    private Integer centerFilterId;
    
    @Key
    private String regionId;
    
    @Key
    private String instructorId;
    
    @Key
    @JsonString
    private DateTime startTime;
    
    @Key
    private Integer durationInMinutes;
    
    @Key
    private Integer bookedPersonsCount;
    
    @Key
    private Integer maxPersonsCount;
    
    @Key
    @JsonString
    private List<Integer> classCategoryIds;
    
    @Key
    private Integer waitingListCount;
    
    public String getId() {
      return id;
    }
    
    public String getName() {
      return name;
    }
    
    public String getClassTypeId() {
      return classTypeId;
    }
    
    public Integer getCenterFilterId() {
      return centerFilterId;
    }
    
    public String getRegionId() {
      return regionId;
    }
    
    public String getInstructorId() {
      return instructorId;
    }
    
    public DateTime getStartTime() {
      return startTime;
    }
    
    public Integer getDurationInMinutes() {
      return durationInMinutes;
    }
    
    public Integer getBookedPersonsCount() {
      return bookedPersonsCount;
    }
    
    public Integer getMaxPersonsCount() {
      return maxPersonsCount;
    }
    
    public List<Integer> getClassCategoryIds() {
      return classCategoryIds;
    }
    
    public Integer getWaitingListCount() {
      return waitingListCount;
    }
    
  }
  
  public static class ClassesUrl extends GenericUrl {
    
    public ClassesUrl(String encodedUrl) {
      super(encodedUrl);
    }
    
    public static ClassesUrl listClasses(List<Integer> centers) {
      return new ClassesUrl("https://www.sats.fi/sats-api/fi/classes?centers="
          + centers.stream().map(String::valueOf).collect(Collectors.joining(";")));
    }
    
  }
  
}
