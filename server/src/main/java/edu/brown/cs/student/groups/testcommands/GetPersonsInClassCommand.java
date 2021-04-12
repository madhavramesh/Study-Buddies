package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.PersonPreferences;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;

public class GetPersonsInClassCommand implements TriggerAction {
  @Override
  public String name() {
    return "get_persons_in_class";
  }

  @Override
  public boolean action(String[] args)
      throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
    if (args.length != 2) {
      throw new IllegalArgumentException("ERROR: Invalid format! To use `get_persons_in_class`, " +
          "use the following format: `get_persons_in_class <class_id>`");
    }
    int classId = Integer.parseInt(args[1]);
    List<PersonPreferences> personPreferences = Main.getGroupsDatabase().getPersonsInClass(classId);
    System.out.println("Person preferences: ");
    for (PersonPreferences pp : personPreferences) {
      System.out.println("- " + pp);
    }
    return true;
  }
}
