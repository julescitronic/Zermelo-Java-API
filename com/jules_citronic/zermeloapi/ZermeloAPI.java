package com.jules_citronic.zermeloapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZermeloAPI {
  String school = "not set";
  
  public void start(String schoolt) {
    this.school = this.school;
  }
  
  public String getToken(String authCode) {
    String token = null;
    if (checkStatus() == "OK") {
      try {
        URL url = new URL("https://" + this.school + ".zportal.nl/api/v3/oauth/token");
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        StringBuilder tokenUri = new StringBuilder("grant_type=");
        tokenUri.append(URLEncoder.encode("authorization_code", "UTF-8"));
        tokenUri.append("&code=");
        tokenUri.append(URLEncoder.encode(authCode, "UTF-8"));
        con.setDoOutput(true);
        con.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(tokenUri.toString());
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
          response.append(inputLine); 
        in.close();
        String responseString = response.toString();
        token = responseString.split("\"")[3];
      } catch (Exception exception) {}
    } else {
      token = checkStatus();
    } 
    return token;
  }
  
  public List<Appointment> getAppointments(long startTime, long endTime, String accessToken) {
    List<Appointment> appointments = new ArrayList<>();
    if (checkStatus() == "OK") {
      String responseJSON;
      URL url = null;
      StringBuffer response = new StringBuffer();
      try {
        url = new URL("https://gsf.zportal.nl/api/v3/appointments?user=~me&start=" + startTime + "&end=" + endTime + "&access_token=" + accessToken);
      } catch (Exception exception) {}
      HttpURLConnection conn = null;
      try {
        conn = (HttpURLConnection)url.openConnection();
        conn.setDoOutput(false);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        int status = conn.getResponseCode();
        if (status != 200)
          throw new IOException("Post failed with error code " + status); 
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
          response.append(inputLine); 
        in.close();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (conn != null)
          conn.disconnect(); 
        responseJSON = response.toString();
      } 
      try {
        JSONObject jsonResponse = (new JSONObject(responseJSON)).getJSONObject("response");
        JSONArray jsonAppointments = jsonResponse.getJSONArray("data");
        for (int i = 0; i < jsonAppointments.length(); i++) {
          JSONObject appointment = jsonAppointments.getJSONObject(i);
          long aStartTime = appointment.getLong("start");
          long aEndTime = appointment.getLong("end");
          JSONArray teachersArray = appointment.getJSONArray("teachers");
          String[] aTeachers = new String[teachersArray.length()];
          for (int x = 0; x < teachersArray.length(); x++)
            aTeachers[x] = teachersArray.getString(x); 
          JSONArray groupsArray = appointment.getJSONArray("groups");
          String[] aGroups = new String[groupsArray.length()];
          for (int j = 0; j < groupsArray.length(); j++)
            aGroups[j] = groupsArray.getString(j); 
          boolean aCancelled = appointment.getBoolean("cancelled");
          boolean aModified = appointment.getBoolean("modified");
          boolean aMoved = appointment.getBoolean("moved");
          boolean aAdded = appointment.getBoolean("new");
          boolean aHidden = appointment.getBoolean("hidden");
          String aChangeDescription = appointment.getString("changeDescription");
          String aRemark = appointment.getString("remark");
          JSONArray locationArray = appointment.getJSONArray("locations");
          String[] aLocations = new String[locationArray.length()];
          for (int k = 0; k < locationArray.length(); k++)
            aLocations[k] = locationArray.getString(k); 
          String aType = appointment.getString("type");
          JSONArray subjectsArray = appointment.getJSONArray("subjects");
          String[] aSubjects = new String[subjectsArray.length()];
          for (int m = 0; m < subjectsArray.length(); m++)
            aSubjects[m] = subjectsArray.getString(m); 
          appointments.add(new Appointment(aStartTime, aEndTime, aTeachers, aGroups, aCancelled, aModified, aMoved, aAdded, aChangeDescription, aRemark, aLocations, aType, aSubjects, aHidden));
        } 
      } catch (Exception exception) {}
    } 
    return appointments;
  }
  
  public List<Appointment> sortAppointments(List<Appointment> unsorted) {
    Collections.sort(unsorted, new Comparator<Appointment>() {
          public int compare(Appointment o1, Appointment o2) {
            if (o1.getStartTime() == o2
              .getStartTime())
              return 0; 
            if (o1.getStartTime() < o2
              .getStartTime())
              return -1; 
            return 1;
          }
        });
    return unsorted;
  }
  
  private String checkStatus() {
    String status = null;
    Boolean error = Boolean.valueOf(false);
    if (this.school == "not set") {
      error = Boolean.valueOf(true);
      status = "NO SCHOOL";
    } 
    if (!error.booleanValue())
      status = "OK"; 
    return status;
  }
}
