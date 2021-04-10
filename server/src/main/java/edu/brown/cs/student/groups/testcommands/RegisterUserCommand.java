package edu.brown.cs.student.groups.testcommands;

import edu.brown.cs.student.control.Main;
import edu.brown.cs.student.control.TriggerAction;
import edu.brown.cs.student.groups.DBCode;
import edu.brown.cs.student.groups.NewGroupsDatabase;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

/**
 * REPL testing command for registering a user.
 */
public class RegisterUserCommand implements TriggerAction {
  /**
   * Gets the name of the command.
   *
   * @return the command's name
   */
  @Override
  public String name() {
    return "register_user";
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
      throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
    NewGroupsDatabase groupsDatabase = Main.getGroupsDatabase();
    // get data fields
    if (args.length != 6) {
      throw new IllegalArgumentException("ERROR: Invalid usage! To use `register_user`, adhere to" +
          "the following format: `register_user \"<first_name>\" \"<last_name>\" \"<email>\" " +
          "\"<password>\" \"<confirm_password>\"`");
    }
    String firstName = args[1];
    String lastName = args[2];
    String email = args[3];
    String password = args[4];
    String password2 = args[5];
    DBCode code = password.equals(password2)
        ? groupsDatabase.registerUser(firstName, lastName, email, password)
        : DBCode.PASSWORD_MISMATCH;
    System.out.printf("status: %d; message: %s%n", code.getCode(), code.getMessage());
    return code.getCode() == 0;
  }
}
