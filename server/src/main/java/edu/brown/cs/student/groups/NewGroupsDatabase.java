package edu.brown.cs.student.groups;

import edu.brown.cs.student.DataStructures.Pair;
import edu.brown.cs.student.encryption.PasswordEncryption;
import edu.brown.cs.student.input.CodeGenerator;
import org.apache.commons.validator.routines.EmailValidator;
import org.sqlite.SQLiteConfig;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static edu.brown.cs.student.groups.DBCode.*;

public class NewGroupsDatabase {
  private static Connection conn = null;

  public NewGroupsDatabase(String fileName) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + fileName;
    if (!fileName.contains("sqlite")) {
      throw new IllegalArgumentException("ERROR: Not a valid SQL database/file!");
    }
    try {
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      conn = DriverManager.getConnection(urlToDB, config.toProperties());
    } catch (Exception e) {
      throw new SQLException("ERROR: SQL database not found!" + e.getMessage());
    }
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON;");

    PreparedStatement prep;
    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS logins(" +
        "id INTEGER NOT NULL PRIMARY KEY, " +
        "first_name TEXT, " +
        "last_name TEXT, " +
        "email TEXT UNIQUE, " +
        "pass_token TEXT);");
    prep.executeUpdate();

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS classes(" +
        "class_id INTEGER NOT NULL PRIMARY KEY, " +
        "class_name TEXT, " +
        "class_number TEXT, " +
        "class_description TEXT, " +
        "class_term TEXT, " +
        "class_code TEXT, " +
        "owner_id INTEGER, " +
        "FOREIGN KEY (owner_id) REFERENCES logins(id) " +
        "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS enrollments(" +
        "person_id INTEGER, " +
        "class_id INTEGER, " +
        "FOREIGN KEY (person_id) REFERENCES logins(id), " +
        "FOREIGN KEY (class_id) REFERENCES classes(class_id) " +
        "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS class(" +
        "class_id INTEGER, " +
        "person_id INTEGER, " +
        "times TEXT, " +
        "dorm TEXT, " +
        "preferences TEXT, " +
        "group_id INTEGER, " +
        "FOREIGN KEY (class_id) REFERENCES classes(class_id), " +
        "FOREIGN KEY (person_id) REFERENCES logins(id) " +
        "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();

    prep.close();
  }

  public Pair<DBCode, PersonInfo> getPersonInfo(int id) throws SQLException {
    PreparedStatement prep;
    ResultSet rs;
    prep = conn.prepareStatement("SELECT * FROM logins WHERE id=?;");
    prep.setInt(1, id);
    rs = prep.executeQuery();
    if (!rs.next()) {
      return new Pair<>(USER_NOT_FOUND, null);
    }
    int personId = rs.getInt("id");
    String firstName = rs.getString("first_name");
    String lastName = rs.getString("last_name");
    String email = rs.getString("email");
    return new Pair<>(RETRIEVE_USER_SUCCESS, new PersonInfo(personId, firstName, lastName, email));
  }
  /**
   * Validates a user's credentials.
   *
   * @param email    the user's email
   * @param password the user's password
   * @return A DBCode representing the status of the registration; see DBCode for info.
   * @throws SQLException             if an error occurs while connecting to the database
   * @throws NoSuchAlgorithmException if the SHA1PRNG algorithm is not loaded
   * @throws InvalidKeySpecException  if the PBKDF2WithHmacSHA1 algorithm is not loaded
   */
  public Pair<Integer, DBCode> validateUser(String email, String password)
      throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
    if (!EmailValidator.getInstance().isValid(email)) {
      return new Pair<>(-1, INVALID_EMAIL);
    }
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM logins WHERE email=?;");
    prep.setString(1, email);
    ResultSet rs = prep.executeQuery();
    int id;
    if (rs.next()) {
      id = rs.getInt("id");
    } else {
      return new Pair<>(-1, USER_NOT_FOUND);
    }
    String encryptedPass = rs.getString("pass_token");
    prep.close();
    rs.close();
    if (PasswordEncryption.validatePBKDF2Password(password, encryptedPass)) {
      return new Pair<>(id, LOGIN_SUCCESS);
    } else {
      return new Pair<>(-1, INVALID_PASSWORD);
    }
  }

  /**
   * Registers a user into the database.
   *
   * @param firstName the first name
   * @param lastName  the last name
   * @param email     the email
   * @param password  the password [RAW TEXT; will be encrypted]
   * @return A DBCode representing the status of the registration; see DBCode for info.
   * @throws SQLException             if an error occurs while connecting to the database
   * @throws NoSuchAlgorithmException if the SHA1PRNG algorithm is not loaded
   * @throws InvalidKeySpecException  if the PBKDF2WithHmacSHA1 algorithm is not loaded
   */
  public DBCode registerUser(String firstName, String lastName, String email, String password)
      throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
    if (!EmailValidator.getInstance().isValid(email)) {
      return INVALID_EMAIL;
    }
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM logins WHERE email=?;");
    prep.setString(1, email);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      rs.close();
      return EMAIL_TAKEN;
    }
    prep = conn.prepareStatement("INSERT INTO " +
        "logins(first_name, last_name, email, pass_token) values(?, ?, ?, ?);");
    prep.setString(1, firstName);
    prep.setString(2, lastName);
    prep.setString(3, email);
    prep.setString(4, PasswordEncryption.getPBKDF2SecurePassword(password));
    prep.executeUpdate();
    prep.close();
    rs.close();
    return REGISTRATION_SUCCESS;
  }

  /**
   * Gets all classes.
   *
   * @return a list of classes
   * @throws SQLException if an error occurs while connecting to the database
   */
  public List<ClassInfo> getAllClasses() throws SQLException {
    List<ClassInfo> classes = new LinkedList<>();
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM classes");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      classes.add(processClassInfo(rs));
    }
    prep.close();
    rs.close();
    return classes;
  }

  /**
   * Gets all classes owned by a person.
   *
   * @param ownerId the owner's ID
   * @return a list of classes owned by a person
   * @throws SQLException if an error occurs while connecting to the database
   */
  public List<ClassInfo> getClassesByOwnerId(int ownerId) throws SQLException {
    List<ClassInfo> classes = new LinkedList<>();
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM classes WHERE owner_id=?");
    prep.setInt(1, ownerId);
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      classes.add(processClassInfo(rs));
    }
    prep.close();
    rs.close();
    return classes;
  }

  /**
   * Gets all enrollments of a person.
   *
   * @param id the person's id
   * @return the person's class enrollments
   * @throws SQLException if an error occurs while connecting to the database
   */
  public List<ClassInfo> getEnrollments(int id) throws SQLException {
    PreparedStatement prep;
    ResultSet rs;
    // select the person's class enrollments
    prep = conn.prepareStatement("SELECT * FROM enrollments WHERE person_id=?");
    prep.setInt(1, id);
    rs = prep.executeQuery();
    // store the person's enrollments
    List<Integer> classIds = new LinkedList<>();
    while (rs.next()) {
      classIds.add(rs.getInt("class_id"));
    }
    int classIdsSize = classIds.size();
    // if no enrollments, return nothing
    if (classIdsSize == 0) {
      return new LinkedList<>();
    }
    // create a PreparedStatement insert depending on number of enrollments
    StringBuilder sb = new StringBuilder("(");
    for (int i = 1; i <= classIdsSize; ++i) {
      // make sure last one doesn't have a comma xd
      sb.append(i == classIdsSize ? "?" : "?,");
    }
    sb.append(")");
    // sb should look something like "(?,?,...,?)"
    prep = conn.prepareStatement("SELECT * FROM classes WHERE class_id IN " + sb.toString());
    // insert actual enrollment class ids
    for (int i = 1; i <= classIdsSize; ++i) {
      prep.setInt(i, classIds.get(i - 1));
    }
    rs = prep.executeQuery();
    List<ClassInfo> classes = new LinkedList<>();
    while (rs.next()) {
      classes.add(processClassInfo(rs));
    }
    // close the PreparedStatements and ResultSets
    prep.close();
    rs.close();
    return classes;
  }

  /**
   * Creates a class with the given information. Owners cannot create classes with the same name
   * or number.
   *
   * @param className        the class's name
   * @param classNumber      the class's number
   * @param classDescription the class's description
   * @param classTerm        the class's term
   * @param ownerId          the class's owner's ID
   * @return a DBCode representing the status of the class creation
   */
  public Pair<Pair<Integer, String>, DBCode> createClass(String className, String classNumber,
                                       String classDescription, String classTerm, int ownerId)
      throws SQLException {
    PreparedStatement prep;
    ResultSet rs;
    // select current classes and see if one with the class number already exists
    prep = conn.prepareStatement("SELECT * FROM classes WHERE class_number=?;");
    prep.setString(1, classNumber);
    rs = prep.executeQuery();
    // check if class with number already exists; if so, indicate an error
    if (rs.next()) {
      prep.close();
      rs.close();
      return new Pair<>(new Pair<>(-1, ""), CLASS_ALREADY_CREATED);
    }
    // create a new class
    String classCode = CodeGenerator.generateCode(6);
    prep = conn.prepareStatement("INSERT INTO classes(class_name, class_number, " +
        "class_description, class_term, class_code, owner_id) values(?,?,?,?,?,?);");
    prep.setString(1, className);
    prep.setString(2, classNumber);
    prep.setString(3, classDescription);
    prep.setString(4, classTerm);
    prep.setString(5, classCode);
    prep.setInt(6, ownerId);
    try {
      prep.executeUpdate();
      // if foreign key owner_id doesn't exist, return an error code
    } catch (SQLException e) {
      prep.close();
      rs.close();
      return new Pair<>(new Pair<>(-1, ""), CLASS_OWNER_DOES_NOT_EXIST);
    }
    // get the class ID, since it's an auto-incrementing primary key
    prep = conn.prepareStatement("SELECT class_id FROM classes WHERE class_name=? AND " +
        "class_number=? AND class_description=? AND class_term=? AND class_code=? AND owner_id=?;");
    prep.setString(1, className);
    prep.setString(2, classNumber);
    prep.setString(3, classDescription);
    prep.setString(4, classTerm);
    prep.setString(5, classCode);
    prep.setInt(6, ownerId);
    rs = prep.executeQuery();
    int classId = -1;
    // we know this exists, since we just inserted it in
    while (rs.next()) {
      classId = rs.getInt("class_id");
    }
    // now, enroll the owner to the class!
    prep = conn.prepareStatement("INSERT INTO enrollments values(?,?);");
    prep.setInt(1, ownerId);
    prep.setInt(2, classId);
    prep.executeUpdate();
    prep.close();
    rs.close();
    return new Pair<>(new Pair<>(classId, classCode), CLASS_CREATION_SUCCESS);
  }

  /**
   * Enroll the person with the specified ID into the class. Must provide correct code.
   *
   * @param id        the person's id
   * @param classId   the class's id
   * @param classCode the class's code
   * @return a code representing the operation's status
   * @throws SQLException if an error occurs while connecting to the database
   */
  public DBCode joinClass(int id, int classId, String classCode) throws SQLException {
    PreparedStatement prep;
    ResultSet rs;
    prep = conn.prepareStatement("SELECT * FROM enrollments WHERE person_id=? AND class_id=?;");
    prep.setInt(1, id);
    prep.setInt(2, classId);
    rs = prep.executeQuery();
    if (rs.next()) {
      return ALREADY_JOINED_CLASS;
    }
    // select class with specified ID
    prep = conn.prepareStatement("SELECT * FROM classes WHERE class_id=? AND class_code=?;");
    prep.setInt(1, classId);
    prep.setString(2, classCode);
    rs = prep.executeQuery();
    // if the class ID and class code do not match (e.g. class code is incorrect), return error code
    if (!rs.next()) {
      return INVALID_CLASS_CODE;
    }
    prep = conn.prepareStatement("INSERT INTO enrollments values(?,?);");
    prep.setInt(1, id);
    prep.setInt(2, classId);
    prep.executeUpdate();
    prep.close();
    rs.close();
    return CLASS_JOIN_SUCCESS;
  }

  /**
   * Processes class information into a ClassInfo object.
   *
   * @param rs the ResultSet of the current entry
   * @return an object representing a class
   */
  private ClassInfo processClassInfo(ResultSet rs) throws SQLException {
    int classId = rs.getInt("class_id");
    String className = rs.getString("class_name");
    String classNumber = rs.getString("class_number");
    String classDescription = rs.getString("class_description");
    String classTerm = rs.getString("class_term");
    String classCode = rs.getString("class_code");
    int ownerId = rs.getInt("owner_id");
    return new ClassInfo(classId, className, classNumber, classDescription, classTerm, classCode,
        ownerId);
  }
}
