package edu.brown.cs.student.groups;

import edu.brown.cs.student.obsoletedistanceutils.DistanceUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Person {
  private final int id;
  private final String firstName;
  private final String lastName;
  private char[] times;
  private String dorm;
  private String preferences;
  private int groupId;

  private static int idGenerator;

  public Person(int id, String firstName, String lastName, String times, String dorm,
                String preferences,
                int groupId) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.times = times.toCharArray();
    this.dorm = dorm;
    this.preferences = preferences;
    this.groupId = groupId;
  }

  int numIntersections(Person person) {
    Set<Character> otherTimes = new HashSet<>();
    for (char c : person.getTimes()) {
      otherTimes.add(c);
    }
    int counter = 0;
    for (char c : this.times) {
      if (otherTimes.contains(c)) {
        ++counter;
      }
    }
    return counter;
  }

  double distance(Person person) throws IOException, JSONException {
    double[] loc1 = DistanceUtils.getLatLong(this.dorm);
    double[] loc2 = DistanceUtils.getLatLong(person.getDorm());
    return DistanceUtils.haversineDistance(loc1, loc2);
  }

  public int getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public char[] getTimes() {
    return times;
  }

  public void setTimes(char[] times) {
    this.times = times;
  }

  public String getDorm() {
    return dorm;
  }

  public void setDorm(String dorm) {
    this.dorm = dorm;
  }

  public String getPreferences() {
    return preferences;
  }

  public void setPreferences(String preferences) {
    this.preferences = preferences;
  }

  public static int getIdGenerator() {
    return idGenerator;
  }

  public static void setIdGenerator(int idGenerator) {
    Person.idGenerator = idGenerator;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
}
