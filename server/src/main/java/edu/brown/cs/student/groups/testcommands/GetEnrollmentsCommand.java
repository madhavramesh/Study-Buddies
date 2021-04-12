package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.ClassInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * REPL testing command for getting enrollments of a person.
 */
public class GetEnrollmentsCommand implements TriggerAction {
  /**
   * Gets the name of the command.
   *
   * @return the command's name
   */
  @Override
  public String name() {
    return "get_enrollments";
  }

  /**
   * Executes the command.
   *
   * @param args an array of strings containing the command line arguments
   * @return true
   * @throws SQLException if an error occurs while connecting to the database
   */
  @Override
  public boolean action(String[] args)
      throws SQLException {
    if (args.length != 2) {
      throw new IllegalArgumentException("ERROR: Illegal format! To use `get_enrollments`," +
          "adhere to the following format: `get_enrollments <id>`");
    }
    int id = Integer.parseInt(args[1]);
    List<ClassInfo> classes = Main.getGroupsDatabase().getEnrollments(id);
    System.out.printf("Person %d's enrollments:%n", id);
    for (ClassInfo ci : classes) {
      System.out.println("- " + ci);
    }
    return true;
  }
}
