package edu.brown.cs.student.heuristic;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PreferenceUtilsTest {

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