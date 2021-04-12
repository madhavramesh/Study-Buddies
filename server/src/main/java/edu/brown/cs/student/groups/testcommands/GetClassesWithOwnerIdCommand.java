package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.ClassInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * REPL testing command for getting classes owned by someone.
 */
public class GetClassesWithOwnerIdCommand implements TriggerAction {
  /**
   * Gets the name of the command.
   *
   * @return the command's name.
   */
  @Override
  public String name() {
    return "get_classes_with_owner_id";
  }

  /**
   * Executes the command.
   *
   * @param args an array of strings containing the command line arguments
   * @return true
   * @throws SQLException if an error occurs while connecting to the database
   */
  @Override
  public boolean action(String[] args) throws SQLException {
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "ERROR: Illegal format! To use `get_classes_with_owner_id`," +
              "adhere to the following format: `get_classes_with_owner_id <owner_id>`");
    }
    int ownerId = Integer.parseInt(args[1]);
    List<ClassInfo> classes = Main.getGroupsDatabase().getClassesByOwnerId(ownerId);
    System.out.printf("Classes owned by %d:%n", ownerId);
    for (ClassInfo ci : classes) {
      System.out.println("- " + ci);
    }
    return true;
  }
}
