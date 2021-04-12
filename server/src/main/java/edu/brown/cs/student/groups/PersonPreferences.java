package edu.brown.cs.student.groups;

public class PersonPreferences {
  private final int personId;
  private String times;
  private String dorm;
  private String preferences;
  private int groupId;

  /**
   * Constructor for a person's preferences.
   * @param personId the person' id
   * @param times the person's times
   * @param dorm the person's dorm
   * @param preferences the person's preferences
   * @param groupId the person's group
   */
  public PersonPreferences(int personId, String times, String dorm, String preferences,
                           int groupId) {
    this.personId = personId;
    this.times = times;
    this.dorm = dorm;
    this.preferences = preferences;
    this.groupId = groupId;
  }

  /**
   * String representation of a Person's preferences.
   * @return the person's preferences as a String
   */
  @Override
  public String toString() {
    return String.format("[personId: %d, text: %s, dorm: %s, preferences: %s, groupId: %d]",
        personId, times, dorm, preferences, groupId);
  }
}
