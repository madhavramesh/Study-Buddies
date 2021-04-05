package edu.brown.cs.student.groups;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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
    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS persons(" +
        "id INTEGER," +
        "name TEXT," +
        "times TEXT," +
        "preferred TEXT," +
        "dorm TEXT," +
        "group_id INTEGER" +
        "PRIMARY KEY (id));");
    prep.executeUpdate();
    prep.close();
  }

  Person getPerson(int id) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM persons WHERE id=?;");
    prep.setInt(1, id);
    ResultSet rs = prep.executeQuery();
    int columnCount = rs.getMetaData().getColumnCount();
    int i = 1;
    int personId = rs.getInt(i++);
    String name = rs.getString(i++);
    int groupId = rs.getInt(columnCount);
    List<String> preferences = new LinkedList<>();
    while (i++ < columnCount && rs.next()) {
      preferences.add(rs.getString(i));
    }
    prep.close();
    return new Person(personId, name, preferences, groupId);
  }

  void insertPerson(Person person) throws SQLException {
    int numPreferences = person.getPreferences().size();
    String values = "?, ".repeat(numPreferences + 1) + "?";
    PreparedStatement prep = conn.prepareStatement(String.format("INSERT INTO persons VALUES (%s)",
        values));
    prep.setInt(1, person.getId());
    for (int i = 0; i < numPreferences; ++i) {
      prep.setString(i + 2, person.getPreferences().get(i));
    }
    prep.setInt(numPreferences + 2, person.getGroupId());
    prep.executeUpdate();
    prep.close();
  }
}
