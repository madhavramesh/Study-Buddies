package edu.brown.cs.student.groups;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        "email TEXT, " +
        "pass_token TEXT);");
    prep.executeUpdate();

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS classes(" +
        "class_id INTEGER NOT NULL PRIMARY KEY, " +
        "class_name TEXT, " +
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

  public int[] validateUser(String email, String passToken) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM logins WHERE " +
        "email=? AND pass_token=?;");
    prep.setString(1, email);
    prep.setString(2, passToken);
    ResultSet rs = prep.executeQuery();
    int id = -1;
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
}
