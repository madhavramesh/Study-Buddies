package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.groups.PersonPreferences;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set; 
import java.util.Map;

import java.sql.SQLException;

import edu.brown.cs.student.DataStructures.Pair;
import edu.brown.cs.student.DataStructures.Graph;
import edu.brown.cs.student.DataStructures.IGEdge;
import edu.brown.cs.student.DataStructures.IGNode;

import edu.brown.cs.student.groups.*;
import edu.brown.cs.student.algorithm.Grouper;

public class HeuristicUtils {
  private static NewGroupsDatabase GROUPS_DATABASE;
  private Grouper grouper;

  /**
   * Empty constructor.
   */
  public HeuristicUtils() throws SQLException, ClassNotFoundException {
    GROUPS_DATABASE = new NewGroupsDatabase("data/groups_db.sqlite3");
  }

  /**
   * Returns the best groups in a certain class. 
   * @param classId classId to choose from 
   * @return
   */
  public List<List<PersonInfo>> getGroups(int classId, int groupSize) throws SQLException, Exception {
    List<PersonPreferences> peopleInDatabase = GROUPS_DATABASE.getPersonsInClass(classId);
    Graph graph = createGraph(peopleInDatabase);
    grouper = new Grouper(graph);
    List<Set<IGNode>> results = grouper.makeGroups(graph, groupSize);
    
    // Convert to groups of personinfos 
    List<List<PersonInfo>> resultGroups = new ArrayList<>();
    for (Set<IGNode> group: results) {
      List<PersonInfo> newGroup = new ArrayList<>();
      for (IGNode n: group) {
        Pair<DBCode, PersonInfo> personInfo = GROUPS_DATABASE.getPersonInfo(n.getValue());
        newGroup.add(personInfo.getSecond());
      }
      resultGroups.add(newGroup);
    }
    
    return resultGroups;
  }

  /**
   * Given a list of person preferences, creates the corresponding graph.
   * @param people list of person preferences
   * @return
   * @throws Exception
   */
  public Graph createGraph(List<PersonPreferences> people) throws Exception {
    Graph graph = new Graph();

    List<IGNode> nodes = new ArrayList<>();
    List<IGEdge> edges = new ArrayList<>();

    // Create new nodes 
    for (PersonPreferences pp: people) {
      IGNode newNode = new IGNode(pp.getPersonId(), new HashSet<>());
      nodes.add(newNode);
    }

    // Add the edges for each node
    int max = people.size();
    for (int i = 0; i < max; i++) {
      IGNode thisNode = nodes.get(i);
      PersonPreferences thisPP = people.get(i);

      for (int j = i + 1; j < max; j++) {
        IGNode otherNode = nodes.get(j);
        PersonPreferences otherPP = people.get(j);
        
        IGEdge newEdge = new IGEdge(thisNode, otherNode, findHeuristic(thisPP, otherPP));
        edges.add(newEdge);
      }
    }
    
    // Populate graph with nodes
    for (IGNode n: nodes) {
      graph.addNode(n);
    }
    
    // Populate graph with edges
    for (IGEdge e: edges) {
      graph.addEdge(e);
    }
    
    return graph;
  }

  /**
   * Find the heuristic "preference" value between two people, determined by finding the metrics
   * for each of time overlaps (40% weight), their preferences for each other (40% weight), and
   * dorm distance (20% weight).
   * @param p1 the first person's preferences
   * @param p2 the second person's preferences
   * @return the heuristic value between two people
   */
  public static double findHeuristic(PersonPreferences p1, PersonPreferences p2)
      throws IOException, JSONException {
    double timePrefMetric = PreferenceUtils.getTimesOverlap(p1, p2);
    double personPrefMetric = PreferenceUtils.getLevelOfPreference(p1, p2);
    double distanceMetric = PreferenceUtils.getDistanceMetric(p1, p2);
    return timePrefMetric * 0.4 + personPrefMetric * 0.4 + distanceMetric * 0.2;
  }
}
