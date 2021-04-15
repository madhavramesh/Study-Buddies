package edu.brown.cs.student.heuristic;

import edu.brown.cs.student.groups.PersonPreferences;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
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
   *
   * @param classId classId to choose from
   * @return the list of groups generated
   */
  public 
  Pair<
          List<List<Pair<Integer, PersonInfo>>>, 
          Map<Integer, Map<Integer, Double>>> 
  getGroups(int classId, int groupSize) throws Exception {
    List<PersonPreferences> peopleInDatabase = GROUPS_DATABASE.getPersonsPrefsInClass(classId);
    Graph graph = createGraph(peopleInDatabase);

    grouper = new Grouper(graph);
    List<Set<IGNode>> results = grouper.makeGroups(graph, groupSize);

    // Create the data for visualization purposes
    // First integer is start node, second integer is end node, double is weight between them
    Map<Integer, Map<Integer, Double>> nodeToNodeWeights = new HashMap<>();
    for (Set<IGNode> group: results) {
      for (IGNode n : group) {
        Map<Integer, Double> destinations = new HashMap<>();
        for (IGNode n2 : group) {
          if (!(n2.equals(n))) {
            destinations.put(n2.getValue(), n.weightTo(n2));
          }
        }
        nodeToNodeWeights.put(n.getValue(), destinations);
      }
    }

    // Convert to groups of PersonInfos
    List<List<Pair<Integer, PersonInfo>>> resultGroups = new ArrayList<>();
    int groupId = 1;
    for (Set<IGNode> group : results) {
      List<Pair<Integer, PersonInfo>> newGroup = new ArrayList<>();
      for (IGNode n : group) {
        DBCode code = GROUPS_DATABASE.setPersonGroupInClass(n.getValue(), classId, groupId);
        Pair<DBCode, PersonInfo> personInfo = GROUPS_DATABASE.getPersonInfo(n.getValue());
        newGroup.add(new Pair<>(groupId, personInfo.getSecond()));
      }
      resultGroups.add(newGroup);
      ++groupId;
    }
    return new Pair(resultGroups, nodeToNodeWeights);
  }

  public void testGetGroupWithEdgeDisplay(Graph g, int groupSize) throws Exception {

  }

  /**
   * Given a list of person preferences, creates the corresponding graph.
   *
   * @param people list of person preferences
   * @return the graph representing the class
   * @throws Exception if an error occurs somewhere in graph creation
   */
  public Graph createGraph(List<PersonPreferences> people) throws Exception {
    Graph graph = new Graph();

    List<IGNode> nodes = new ArrayList<>();
    List<IGEdge> edges = new ArrayList<>();

    // Create new nodes 
    for (PersonPreferences pp : people) {
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
//        System.out.println(thisNode.getValue() + " " + otherNode.getValue() + " " + newEdge.getWeight());
      }
    }

    // Populate graph with nodes
    for (IGNode n : nodes) {
      graph.addNode(n);
    }

    // Populate graph with edges
    for (IGEdge e : edges) {
      graph.addEdge(e);
    }

    return graph;
  }

  /**
   * Find the heuristic "preference" value between two people, determined by finding the metrics
   * for each of time overlaps (40% weight), their preferences for each other (40% weight), and
   * dorm distance (20% weight).
   *
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
