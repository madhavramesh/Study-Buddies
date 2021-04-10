package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.DataStructures.Pair;
import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.DBCode;
import edu.brown.cs.student.groups.NewGroupsDatabase;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

/**
 * REPL testing command for validating a user's login info.
 */
public class ValidateUserCommand implements TriggerAction {
  /**
   * Gets the name of a command.
   *
   * @return the command's name
   */
  @Override
  public String name() {
    return "validate_user";
  }

  /**
   * Executes the command.
   *
   * @param args an array of strings containing the command line arguments
   * @return true only if the status code is 0 (success)
   * @throws SQLException             if an error occurs while connecting to the database.
   * @throws NoSuchAlgorithmException if the SHA1PRNG algorithm is not loaded
   * @throws InvalidKeySpecException  if the PBKDF2WithHmacSHA1 algorithm is not loaded
   */
  @Override
  public boolean action(String[] args)
      throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
    NewGroupsDatabase groupsDatabase = Main.getGroupsDatabase();
    if (args.length != 3) {
      throw new IllegalArgumentException(
          "ERROR: Invalid usage! To use `validate_user`, adhere to " +
              "the following format: `validate_user \"<email>\" \"<password>\"`");
    }
    String email = args[1];
    String password = args[2];
    Pair<Integer, DBCode> code = groupsDatabase.validateUser(email, password);
    System.out.printf("status: %d; message: %s; user ID: %d%n",
        code.getSecond().getCode(), code.getSecond().getMessage(), code.getFirst());
    return code.getSecond().getCode() == 0;
  }
}
