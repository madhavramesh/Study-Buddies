package edu.brown.cs.student.groups;

import java.util.ArrayList;
import java.util.List;

public class Person {
  private final int id;
  private final String name;
  private List<String> preferences;
  private int groupId;

  private static int idGenerator;

  public Person(int id, String name, List<String> preferences, int groupId) {
    this.id = id;
    this.name = name;
    this.preferences = new ArrayList<>(preferences);
    this.groupId = groupId;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getPreferences() {
    return preferences;
  }

  public void setPreferences(List<String> preferences) {
    this.preferences = preferences;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
}
