package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.groups.PersonPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PreferenceUtils {

  private final static String PS_ACCESS_KEY = "aa0de4affe32c197eb1588c968a538d0";
  private final static Map<String, double[]> dormLocations = new HashMap<>(Map.of(
      "Andrews Hall", new double[] {41.83075075870588, -71.40253155966681},
      "Metcalf Hall", new double[] {41.83045317087116, -71.40272981548961},
      "Miller Hall", new double[] {41.83038684934773, -71.40220743453294},
      "Champlin Hall", new double[] {41.830037063689375, -71.40166390730937},
      "Morriss Hall", new double[] {41.830041060859024, -71.40164781405619},
      "Emery-Woolley Dormitory", new double[] {41.82948143173206, -71.4017882258766},
      "Perkins Hall", new double[] {41.823754901339704, -71.39614328665444},
      "Keeney Quadrangle", new double[] {41.82391739134503, -71.403446169186},
      "Gregorian Quad B", new double[] {41.82322376113421, -71.3997473639765},
      "Caswell Hall", new double[] {41.826251315538, -71.40063454446076}
  ));

  /**
   * Empty constructor.
   */
  public PreferenceUtils() {
  }

  /**
   * Returns the level of preferences between two people.
   *
   * @param p1 the first person's preferences
   * @param p2 the second person's preferences
   * @return If either is uncomfortable with the other, return 0; if both are indifferent, return
   * 0.5; if one prefers another, but isn't reciprocated, return 0.75; if both prefer, return 1.
   */
  public static double getLevelOfPreference(PersonPreferences p1, PersonPreferences p2) {
    int p1Id = p1.getPersonId(), p2Id = p2.getPersonId();
    boolean p1PrefersP2 = Arrays.stream(p1.getPreferences()).anyMatch(p -> p == p2Id);
    boolean p2PrefersP1 = Arrays.stream(p2.getPreferences()).anyMatch(p -> p == p1Id);
    boolean p1NotPrefersP2 = Arrays.stream(p1.getPreferences()).anyMatch(p -> -p == p2Id);
    boolean p2NotPrefersP1 = Arrays.stream(p2.getPreferences()).anyMatch(p -> -p == p1Id);
    if (p1NotPrefersP2 || p2NotPrefersP1) {
      return 0;
    } else {
      return 0.5 + (p1PrefersP2 ? 0.25 : 0) + (p2PrefersP1 ? 0.25 : 0);
    }
  }

  /**
   * Gets the overlap of times between two people.
   *
   * @param p1 the first person's preferences
   * @param p2 the second person's preferences
   * @return the overlap
   */
  public static int getTimesOverlap(PersonPreferences p1, PersonPreferences p2) {
    int overlap = 0;
    Integer[][] p1Times = p1.getTimes(), p2Times = p2.getTimes();
    for (int i = 0; i < p1Times.length; ++i) {
      for (int j = 0; j < p1Times[i].length; ++j) {
        overlap += p1Times[i][j].equals(p2Times[i][j]) ? 1 : 0;
      }
    }
    return overlap;
  }

  /**
   * Gets the normalized distance between two dorms.
   * @param p1 the first person's dorm
   * @param p2 the second person's dorm
   * @return the haversine distance between the two dorms, divided by the max haversine distance
   * overall.
   * @throws IOException if an error occurs in the HTML GET request
   * @throws JSONException if an error occurs while parsing JSON
   */
  public static double getDistancePreference(PersonPreferences p1, PersonPreferences p2)
      throws IOException, JSONException {
    double[] p1Loc = getLatLong(p1.getDorm()), p2Loc = getLatLong(p2.getDorm());
    return haversineDistance(p1Loc, p2Loc) / maxDistance();
  }

  /**
   * Fetches the latitude and longitude of the specified dorm at a certain zip.
   *
   * @param dorm the properly formatted dorm name (e.g. "Andrews Hall")
   * @return an array consisting of the [latitude, longitude] of the dorm
   * @throws IOException   if an error occurs during the HTTP connection
   * @throws JSONException if an error occurs while parsing the GET request JSON
   */
  public static double[] getLatLong(String dorm) throws IOException, JSONException {
    if (dormLocations.containsKey(dorm)) {
      return dormLocations.get(dorm);
    } else {
      String formattedDorm = dorm.replace(" ", "%20");
      JSONObject data;
      while (true) {
        URL url = new URL(String.format(
            "http://api.positionstack.com/v1/forward?access_key=%s&query=%s,02912&limit=1",
            PS_ACCESS_KEY, formattedDorm));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        StringBuilder requestData = new StringBuilder();
        Scanner urlScanner = new Scanner(url.openStream());
        while (urlScanner.hasNext()) {
          requestData.append(urlScanner.nextLine());
        }
        try {
          data = new JSONObject(requestData.toString()).getJSONArray("data").getJSONObject(0);
          break;
        } catch (Exception e) {
        }
      }
      double latitude = data.getDouble("latitude");
      double longitude = data.getDouble("longitude");
      double[] location = new double[] {latitude, longitude};
      dormLocations.put(dorm, location);
      return location;
    }
  }

  /**
   * Calculates the haversine distance between two nodes.
   *
   * @param loc1 the lat/long of the first location
   * @param loc2 the lat/long of the second location
   * @return the haversine distance between them
   */
  public static double haversineDistance(double[] loc1, double[] loc2) {
    final double earthRadius = 6371;
    return 2 * earthRadius * Math.asin(
        Math.sqrt(Math.pow(Math.sin((loc2[0] - loc1[0]) / 2), 2)
            + Math.cos(loc1[0]) * Math.cos(loc2[0])
            * Math.pow(Math.sin((loc2[1] - loc1[1]) / 2), 2)));
  }

  /**
   * Calculates the max distance of the current dorm locations.
   *
   * @return the max distance of the dorms
   */
  public static double maxDistance() {
    Collection<double[]> locations = dormLocations.values();
    double maxDistance = 0;
    for (double[] loc : locations) {
      for (double[] otherLoc : locations) {
        double distance = haversineDistance(loc, otherLoc);
        maxDistance = Math.max(distance, maxDistance);
      }
    }
    return maxDistance;
  }
}
