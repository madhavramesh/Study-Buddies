package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.groups.PersonPreferences;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PreferenceUtilsTest {
  PersonPreferences
      p1 = new PersonPreferences(1, "001:110:111", "Andrews Hall", "2,3", -1),
  p2 = new PersonPreferences(2, "111:010:011", "Caswell Hall", "3", -1),
  p3 = new PersonPreferences(3, "000:001:011", "Slater Hall", "1,2", -1),
  p4 = new PersonPreferences(4, "111:111:111", "Perkins Hall", "-1,-2,-3", -1);

  @Test
  public void testGetLevelOfPreferences() {
    assertEquals(0.75, PreferenceUtils.getLevelOfPreference(p1, p2), 0);
    assertEquals(1, PreferenceUtils.getLevelOfPreference(p2, p3), 0);
    assertEquals(0, PreferenceUtils.getLevelOfPreference(p1, p4), 0);
  }

  @Test
  public void testGetTimesOverlap() {
    assertEquals(4.0 / 35, PreferenceUtils.getTimesOverlap(p1, p2), 0);
    assertEquals(6.0 / 35, PreferenceUtils.getTimesOverlap(p2, p4), 0);
    assertEquals(2.0 / 35, PreferenceUtils.getTimesOverlap(p2, p3), 0);
  }

  @Test
  public void testGetDistanceMetric() throws IOException, JSONException {
    double[] andrews = PreferenceUtils.getLatLong("Andrews Hall");
    double[] caswell = PreferenceUtils.getLatLong("Caswell Hall");
    double[] slater = PreferenceUtils.getLatLong("Slater Hall");

    assertEquals(1 - PreferenceUtils.haversineDistance(andrews, caswell) / PreferenceUtils.maxDistance(),
        PreferenceUtils.getDistanceMetric(p1, p2), 0);
    assertEquals(1 - PreferenceUtils.haversineDistance(caswell, slater) / PreferenceUtils.maxDistance(),
        PreferenceUtils.getDistanceMetric(p2, p3), 0);
    assertEquals(1 - PreferenceUtils.haversineDistance(andrews, slater) / PreferenceUtils.maxDistance(),
        PreferenceUtils.getDistanceMetric(p1, p3), 0);
  }

  @Test
  public void getLatLong() throws IOException, JSONException {
    double[] loc = PreferenceUtils.getLatLong("Andrews Hall");
    assertArrayEquals(loc, new double[] {41.83075075870588, -71.40253155966681}, 0);
    loc = PreferenceUtils.getLatLong("Slater Hall");
    assertArrayEquals(new double[] {41.825775, -71.403866}, loc, 0);
  }

  @Test
  public void maxDistance() {
    double maxDistance = PreferenceUtils.maxDistance();
    System.out.println(maxDistance);
    assertTrue(maxDistance > 0);
  }
}