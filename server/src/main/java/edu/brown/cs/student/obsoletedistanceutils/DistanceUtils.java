package edu.brown.cs.student.obsoletedistanceutils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DistanceUtils {
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
  public DistanceUtils() {
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
      URL url = new URL(String.format("https://api.positionstack.com/v1/forward" +
          "?access_key=%s&query=%s,02912&limit=1", PS_ACCESS_KEY, dorm));
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.connect();

      int responseCode = conn.getResponseCode();

      if (responseCode != 200) {
        throw new RuntimeException("HttpResponseCode: " + responseCode);
      } else {
        StringBuilder requestData = new StringBuilder();
        Scanner urlScanner = new Scanner(url.openStream());
        while (urlScanner.hasNext()) {
          requestData.append(urlScanner.nextLine());
        }
        JSONObject data = new JSONObject(requestData.toString());
        double latitude = data.getDouble("latitude");
        double longitude = data.getDouble("longitude");
        double[] location = new double[] {latitude, longitude};
        dormLocations.put(dorm, location);
        return location;
      }
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
}
