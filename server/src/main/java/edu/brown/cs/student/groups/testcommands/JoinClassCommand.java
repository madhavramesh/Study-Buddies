package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.DBCode;

import java.sql.SQLException;

/**
 * Testing command for joining a class.
 */
public class JoinClassCommand implements TriggerAction {
  /**
   * Gets the name of the command.
   *
   * @return the command's name
   */
  @Override
  public String name() {
    return "join_class";
  }

  /**
   * Executes the command.
   *
   * @param args an array of strings containing the command line arguments
   * @return true only if the status code is 0 (success)
   * @throws SQLException
   */
  @Override
  public boolean action(String[] args) throws SQLException {
    if (args.length != 4) {
      throw new IllegalArgumentException("ERROR: Invalid format! To use `join_class`, use the " +
          "following format: `join_class <id> <class_id> \"<class_code>\"");
    }
    int id = Integer.parseInt(args[1]);
    int classId = Integer.parseInt(args[2]);
    String classCode = args[3];
    DBCode code = Main.getGroupsDatabase().joinClass(id, classId, classCode);
    System.out.printf("status: %d; message: %s%n", code.getCode(), code.getMessage());
    return code.getCode() == 0;
  }
}
