package edu.brown.cs.student.groups;

import java.util.Arrays;

public class PersonPreferences {
  private final int personId;
  private final Integer[][] times;
  private final String dorm;
  private final Integer[] preferences;
  private final int groupId;

  /**
   * Constructor for a person's preferences.
   *
   * @param personId    the person' id
   * @param times       the person's times
   * @param dorm        the person's dorm
   * @param preferences the person's preferences
   * @param groupId     the person's group
   */
  public PersonPreferences(int personId, String times, String dorm, String preferences,
                           int groupId) {
    this.personId = personId;
    this.times =
        Arrays.stream(times.split(":"))
            .map(dayPrefs -> Arrays.stream(dayPrefs.split("")).map(Integer::valueOf)
                .toArray(Integer[]::new))
            .toArray(Integer[][]::new);
    this.dorm = dorm;
    this.preferences =
        Arrays.stream(preferences.split(",")).map(Integer::valueOf).toArray(Integer[]::new);
    this.groupId = groupId;
  }

  /**
   * Get person ID.
   *
   * @return the person's ID
   */
  public int getPersonId() {
    return personId;
  }

  /**
   * Get person's time preferences
   *
   * @return the person's time preferences
   */
  public Integer[][] getTimes() {
    return times;
  }

  /**
   * Get person's dorm.
   *
   * @return the person's dorm
   */
  public String getDorm() {
    return dorm;
  }

  /**
   * Get person's preferences for others.
   *
   * @return the person's preferences for others
   */
  public Integer[] getPreferences() {
    return preferences;
  }

  /**
   * Gets person's current group. If none, -1.
   *
   * @return the person's group or -1
   */
  public int getGroupId() {
    return groupId;
  }

  /**
   * String representation of a Person's preferences.
   *
   * @return the person's preferences as a String
   */
  @Override
  public String toString() {
    return String.format("[personId: %d, times: %s, dorm: %s, preferences: %s, groupId: %d]",
        personId, Arrays.deepToString(times), dorm, Arrays.toString(preferences), groupId);
  }
}
