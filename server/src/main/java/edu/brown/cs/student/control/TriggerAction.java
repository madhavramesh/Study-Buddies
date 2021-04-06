package edu.brown.cs.student.control;

import java.sql.SQLException;

/**
 * triggeraction interface.
 */
public interface TriggerAction {
  /**
   * @return name of trigger action
   */
  String name();
  /**
   * method that gets triggered.
   * @param args an array of strings containing the command line arguments
   * @return true if action succeeded, false if errored
   */
  boolean action(String[] args) throws SQLException;
}
