package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.groups.PersonPreferences;
import edu.brown.cs.student.DataStructures.*;
import org.junit.Test;
import com.google.common.collect.Lists;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class HeuristicUtilsTest {
  
  HeuristicUtils heuristic;
  
  PersonPreferences p1 = new edu.brown.cs.student.groups.PersonPreferences(1, "001:110:111", "Andrews Hall", "2,3", -1),
  p2 = new edu.brown.cs.student.groups.PersonPreferences(2, "111:010:011", "Caswell Hall", "3", -1),
  p3 = new edu.brown.cs.student.groups.PersonPreferences(3, "000:001:011", "Slater Hall", "1,2", -1),
  p4 = new edu.brown.cs.student.groups.PersonPreferences(4, "111:111:111", "Perkins Hall", "-1,-2,-3", -1);

  @Test
  public void createGraphTest() throws SQLException, Exception {
    heuristic = new HeuristicUtils();
    Graph g = heuristic.createGraph(Lists.newArrayList(p1, p2, p3, p4));
    assertTrue(g.getNodes().size() == 4);
    assertTrue(g.getEdges().size() == 6);
  }

}
