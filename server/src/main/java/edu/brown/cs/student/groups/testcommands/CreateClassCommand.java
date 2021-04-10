package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.DBCode;

import java.sql.SQLException;

/**
 * REPL testing command for creating a class.
 */
public class CreateClassCommand implements TriggerAction {
  /**
   * Gets the name of the command.
   *
   * @return the command's name
   */
  @Override
  public String name() {
    return "create_class";
  }

  /**
   * Executes the command.
   *
   * @param args an array of strings containing the command line arguments
   * @return true only if the status code of the command is 0 (success)
   * @throws SQLException
   */
  @Override
  public boolean action(String[] args)
      throws SQLException {
    if (args.length != 7) {
      throw new IllegalArgumentException("ERROR: Invalid format! To use `create_class`, use the " +
          "following format: `create_class \"<class_name>\" \"<class_number>\" " +
          "\"<class_description>\" \"<class_term>\" \"<class_code>\" <owner_id>`");
    }
    String className = args[1];
    String classNumber = args[2];
    String classDescription = args[3];
    String classTerm = args[4];
    String classCode = args[5];
    int ownerId = Integer.parseInt(args[6]);
    DBCode code = Main.getGroupsDatabase().createClass(className, classNumber, classDescription,
        classTerm, classCode, ownerId);
    System.out.printf("status: %d; message: %s%n", code.getCode(), code.getMessage());
    return code.getCode() == 0;
  }
}
