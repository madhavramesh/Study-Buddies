package edu.brown.cs.student.groups;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class GroupsDatabase {
  private static Connection conn = null;

  public GroupsDatabase(String fileName) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + fileName;
    if (!fileName.contains("sqlite")) {
      throw new IllegalArgumentException("ERROR: Not a valid SQL database/file!");
    }
    try {
      conn = DriverManager.getConnection(urlToDB);
    } catch (Exception e) {
      throw new SQLException("ERROR: SQL database not found!");
    }
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");

    PreparedStatement prep;
    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS logins(" +
        "id INTEGER NOT NULL PRIMARY KEY," +
        "first_name TEXT," +
        "last_name TEXT" +
        "email TEXT UNIQUE," +
        "pass_token TEXT," +
        "FOREIGN KEY (id) REFERENCES persons(id)" +
        "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS persons(" +
        "id INTEGER," +
        "first_name TEXT," +
        "last_name TEXT," +
        "times TEXT," +
        "dorm TEXT," +
        "preferences TEXT," +
        "group_id INTEGER" +
        "FOREIGN KEY (id) REFERENCES logins(id)" +
        "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();
    prep.close();
  }


  public int[] validateUser(String email, String passToken) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM logins WHERE " +
        "email=? AND pass_token=?;");
    prep.setString(1, email);
    prep.setString(2, passToken);
    ResultSet rs = prep.executeQuery();
    int id = 0;
    int status = 0;
    if (rs.next()) {
      status = 1;
      id = rs.getInt("id");
    }
    return new int[] {status, id};
  }

  public boolean registerUser(String firstName, String lastName, String email, String passToken)
      throws SQLException {
    PreparedStatement prep = conn.prepareStatement("INSERT INTO " +
        "logins(first_name, last_name, email, pass_token) values(?, ?, ?, ?);");
    prep.setString(1, firstName);
    prep.setString(2, lastName);
    prep.setString(3, email);
    prep.setString(4, passToken);
    boolean status = true;
    try {
      prep.executeUpdate();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      status = false;
    }
    prep.close();
    return status;
  }

  public Person getPerson(int id) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM persons WHERE id=?;");
    prep.setInt(1, id);
    ResultSet rs = prep.executeQuery();
    ResultSetMetaData rsMetaData = rs.getMetaData();
    int columnCount = rsMetaData.getColumnCount();
    if (columnCount == 0) {
      throw new IllegalArgumentException("ERROR: Person does not exist!");
    }
    int personId = rs.getInt("id");
    String firstName = rs.getString("first_name");
    String lastName = rs.getString("last_name");
    String times = rs.getString("times");
    String dorm = rs.getString("dorm");
    String preferences = rs.getString("preferences");
    int groupId = rs.getInt(columnCount);
    prep.close();
    return new Person(personId, firstName, lastName, times, dorm, preferences, groupId);
  }

  boolean insertPerson(Person person) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("INSERT INTO persons" +
        "VALUES (?, ?, ?, ?, ?, ?, ?);");
    prep.setInt(1, person.getId());
    prep.setString(2, person.getFirstName());
    prep.setString(3, person.getLastName());
    prep.setString(4, String.valueOf(person.getTimes()));
    prep.setString(5, person.getDorm());
    prep.setString(6, person.getPreferences());
    prep.setInt(7, person.getGroupId());
    boolean status = true;
    try {
      prep.executeUpdate();
    } catch (SQLException e) {
      status = false;
      System.err.println(e.getMessage());
    }
    prep.close();
    return status;
  }
}
