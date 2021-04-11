package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.groups.PersonInfo;

public class Heuristic {

  /**
   * Given two PersonInfos, calculates the total heuristic between them.
   * This represents the compatibility between two people.
   * This also represents the edge weight between two people in our graph representation.
   * @param p1 Person 1
   * @param p2 Person 2
   * @return Final weight metric
   */
  public double calculate(PersonInfo p1, PersonInfo p2) {}

  /**
   * Calculates the metric of people preferring eachother.
   * Represents 35% weight.
   * @param p1 Person 1
   * @param p2 Person 2
   * @return matching metric
   */
  private double matchingMetric(PersonInfo p1, PersonInfo p2) {

  }

  /**
   * Calculates the metric of the overlap of availability between two people.
   * Represents 35% weight.
   * @param p1 Person 1
   * @param p2 Person 2
   * @return overlap metric
   */
  private double hoursOverlapMetric(PersonInfo p1, PersonInfo p2) {}

  /**
   * Calculates the metric of the dorm location distance.
   * @param p1 Person 1
   * @param p2 Person 2
   * @return location distance metric
   */
  private double dormLocationMetric(PersonInfo p1, PersonInfo p2) {}

}
