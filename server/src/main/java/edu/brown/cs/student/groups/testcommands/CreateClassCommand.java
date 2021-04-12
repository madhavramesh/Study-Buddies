package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.DataStructures.Pair;
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
   * @throws SQLException if an error occurs while connecting to the database
   */
  @Override
  public boolean action(String[] args)
      throws SQLException {
    if (args.length != 6) {
      throw new IllegalArgumentException("ERROR: Invalid format! To use `create_class`, use the " +
          "following format: `create_class \"<class_name>\" \"<class_number>\" " +
          "\"<class_description>\" \"<class_term>\" <owner_id>`");
    }
    String className = args[1];
    String classNumber = args[2];
    String classDescription = args[3];
    String classTerm = args[4];
    int ownerId = Integer.parseInt(args[5]);
    Pair<Pair<Integer, String>, DBCode> result =
        Main.getGroupsDatabase()
            .createClass(className, classNumber, classDescription, classTerm, ownerId);
    DBCode code = result.getSecond();
    if (code.getCode() == 0) {
      System.out.printf("status: %d; message: %s; class id: %d; class code: %s%n",
          0, code.getMessage(), result.getFirst().getFirst(), result.getFirst().getSecond());
    } else {
      System.out.printf("status: %d; message: %s%n", code.getCode(), code.getMessage());
    }
    return code.getCode() == 0;
  }
}
