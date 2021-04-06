package edu.brown.cs.student.groups;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;

import java.sql.SQLException;

public class RegisterUser implements TriggerAction {
  @Override
  public String name() {
    return "register_user";
  }

  @Override
  public boolean action(String[] args) throws SQLException {
    String firstName = args[1];
    String lastName = args[2];
    String email = args[3];
    String passToken = args[4];
    NewGroupsDatabase groupsDatabase = Main.getGroupsDatabase();
    boolean result = groupsDatabase.registerUser(firstName, lastName, email, passToken);
    if (result) {
      System.out.println("Successfully registered user!");
    }
    return true;
  }
}
