package edu.brown.cs.student.groups;

import edu.brown.cs.student.DataStructures.IGNode;

import java.util.Objects;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent a person's information.
 */
public class PersonInfo {
  private final int id;
  private final String firstName;
  private final String lastName;
  private final String email;
  
  private Set<Map<IGNode, Double>> groupMemberWeights;

  /**
   * Creates a person with the specified information.
   *
   * @param id        the person's id
   * @param firstName the person's first name
   * @param lastName  the person's last name
   * @param email     the person's email
   */
  public PersonInfo(int id, String firstName, String lastName, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  /**
   * Get the person's id.
   *
   * @return the person's id
   */
  public int getId() {
    return id;
  }

  /**
   * Get the person's first name.
   *
   * @return the person's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Get the person's last name.
   *
   * @return the person's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Get the person's email.
   *
   * @return the person's email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get the person info's hash code.
   *
   * @return the generated hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email);
  }

  /**
   * Test for person info equality.
   *
   * @param obj the object to compare
   * @return true only if both are objects, and all fields are equivalent.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof PersonInfo)) {
      return false;
    }

    PersonInfo otherPersonInfo = (PersonInfo) obj;
    return this.id == otherPersonInfo.getId()
        && this.firstName.equals(otherPersonInfo.getFirstName())
        && this.lastName.equals(otherPersonInfo.getLastName())
        && this.email.equals(otherPersonInfo.getEmail());
  }

  /**
   * Returns a String representation of a person's info.
   *
   * @return the string representation
   */
  @Override
  public String toString() {
    return String.format("[id: %d, firstName: %s, lastName: %s, email: %s]",
        id, firstName, lastName, email);
  }

  /**
   * Re-initializes the person's group member weights
   */
  public void clearGroupMemberWeights() {
    groupMemberWeights = new HashSet<>();
  }

  /**
   * Adds a new groupmember -> weight mapping to the set of these mappings.
   * @param newGroupMemberWeight new mapping to be added
   */
  public void addGroupMemberWeight(Map<IGNode, Double> newGroupMemberWeight) {
    groupMemberWeights.add(newGroupMemberWeight);
  }

}
