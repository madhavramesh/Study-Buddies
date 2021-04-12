package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.groups.PersonPreferences;
import org.json.JSONException;

import java.io.IOException;

public class HeuristicUtils {
  /**
   * Empty constructor.
   */
  public HeuristicUtils() {}

  /**
   * Find the heuristic "preference" value between two people, determined by finding the metrics
   * for each of time overlaps (40% weight), their preferences for each other (40% weight), and
   * dorm distance (20% weight).
   * @param p1 the first person's preferences
   * @param p2 the second person's preferences
   * @return the heuristic value between two people
   */
  public static double findHeuristic(PersonPreferences p1, PersonPreferences p2)
      throws IOException, JSONException {
    double timePrefMetric = PreferenceUtils.getTimesOverlap(p1, p2);
    double personPrefMetric = PreferenceUtils.getLevelOfPreference(p1, p2);
    double distanceMetric = PreferenceUtils.getDistanceMetric(p1, p2);
    return timePrefMetric * 0.4 + personPrefMetric * 0.4 + distanceMetric * 0.2;
  }
}
