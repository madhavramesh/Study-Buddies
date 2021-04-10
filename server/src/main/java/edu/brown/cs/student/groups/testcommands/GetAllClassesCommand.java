package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.ClassInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * REPL testing command for getting all classes.
 */
public class GetAllClassesCommand implements TriggerAction {
  /**
   * Get the name of the command.
   *
   * @return the command's name
   */
  @Override
  public String name() {
    return "get_all_classes";
  }

  /**
   * Execute command's action.
   *
   * @param args an array of strings containing the command line arguments
   * @return true
   * @throws SQLException if an error occurs while connecting to the database.
   */
  @Override
  public boolean action(String[] args)
      throws SQLException {
    List<ClassInfo> classes = Main.getGroupsDatabase().getAllClasses();
    System.out.println("Classes: ");
    for (ClassInfo ci : classes) {
      System.out.println("- " + ci);
    }
    return true;
  }
}
