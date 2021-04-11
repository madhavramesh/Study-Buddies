package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.DataStructures.Pair;
import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.ClassInfo;
import edu.brown.cs.student.groups.DBCode;
import edu.brown.cs.student.groups.PersonInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * REPL testing command to get person info.
 */
public class GetPersonInfoCommand implements TriggerAction {
  /**
   * Get the name of the command
   *
   * @return the command's name
   */
  @Override
  public String name() {
    return "get_person_info";
  }

  /**
   * Execute the command.
   *
   * @param args an array of strings containing the command line arguments
   * @return true only if the status code is 0 (success)
   * @throws SQLException if an error occurs while connecting to the database.
   */
  @Override
  public boolean action(String[] args) throws SQLException {
    if (args.length != 2) {
      throw new IllegalArgumentException("ERROR: Illegal format! To use `get_person_info`, use " +
          "the following format: `get_person_info <id>`");
    }
    int id = Integer.parseInt(args[1]);
    Pair<DBCode, PersonInfo> result = Main.getGroupsDatabase().getPersonInfo(id);
    List<ClassInfo> enrollments = Main.getGroupsDatabase().getEnrollments(id);
    DBCode code = result.getFirst();
    System.out.printf("status: %d; message: %s%n", code.getCode(), code.getMessage());
    if (code.getCode() == 0) {
      System.out.println(result.getSecond());
    }
    System.out.println("Enrollments: ");
    for (ClassInfo ci : enrollments) {
      System.out.println("- " + ci);
    }
    return code.getCode() == 0;
  }
}
