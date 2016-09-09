package com.gmail.lindmik.satsapi;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.gmail.lindmik.satsapi.json.CentersJson.Center;
import com.gmail.lindmik.satsapi.json.CentersJson.CentersUrl;
import com.gmail.lindmik.satsapi.json.CentersJson.Region;
import com.gmail.lindmik.satsapi.json.CentersJson.Regions;
import com.gmail.lindmik.satsapi.json.ClassesJson.Class1;
import com.gmail.lindmik.satsapi.json.ClassesJson.Classes;
import com.gmail.lindmik.satsapi.json.ClassesJson.ClassesUrl;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Main {
  
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  
  private HttpRequestFactory requestFactory;
  
  public Main() {
    requestFactory = HTTP_TRANSPORT
        .createRequestFactory(request -> request.setParser(new JsonObjectParser(JSON_FACTORY)));
  }
  
  private static class CenterPrinter {
    
    private Map<String, String> regionNamesById;
    
    public CenterPrinter(Map<String, String> regionNamesById) {
      this.regionNamesById = regionNamesById;
    }
    
    public void printCenter(Center center) {
      System.out.println(String.format("%s - %s (%d)", regionNamesById.get(center.getRegionId()),
          center.getName(), center.getId()));
    }
    
  }
  
  private static class ClassPrinter {
    
    private Map<String, String> regionNamesById;
    private Map<Integer, String> centerNamesById;
    
    public ClassPrinter(Map<String, String> regionNamesById, Map<Integer, String> centerNamesById) {
      this.regionNamesById = regionNamesById;
      this.centerNamesById = centerNamesById;
    }
    
    public void printClass(Class1 class1) {
      System.out.println(
          String.format("%s - %s: %s %s %s (%d/%d +%d)", regionNamesById.get(class1.getRegionId()),
              centerNamesById.get(class1.getCenterFilterId()),
              ZonedDateTime.parse(class1.getStartTime().toString())
                  .withZoneSameInstant(ZoneId.systemDefault())
                  .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
              class1.getName(), class1.getInstructorId(),
              class1.getBookedPersonsCount(),
              class1.getMaxPersonsCount(),
              class1.getWaitingListCount()));
    }
    
  }
  
  private void run() throws IOException {
    Regions regions = sendAndReceive(CentersUrl.listCenters()).parseAs(Regions.class);
    
    Map<String, String> regionNamesById =
        regions.getRegions().stream().collect(Collectors.toMap(Region::getId, Region::getName));
    
    regions.getRegions().stream().map(Region::getCenters).flatMap(Collection::stream)
        .forEach(new CenterPrinter(regionNamesById)::printCenter);
    
    Map<Integer, String> centerNamesById = regions.getRegions().stream().map(Region::getCenters)
        .flatMap(Collection::stream).collect(Collectors.toMap(Center::getId, Center::getName));
    
    Classes classes =
        sendAndReceive(ClassesUrl.listClasses(Arrays.asList(712))).parseAs(Classes.class);
    
    classes.getClasses().forEach(new ClassPrinter(regionNamesById, centerNamesById)::printClass);
  }
  
  private HttpResponse sendAndReceive(GenericUrl url) throws IOException {
    return requestFactory.buildGetRequest(url).execute();
  }
  
  public static void main(String[] args) {
    try {
      new Main().run();
      return;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
  
}
