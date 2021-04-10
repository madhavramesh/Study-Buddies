package edu.brown.cs.student.algorithm;

import com.google.common.collect.Lists;
import edu.brown.cs.student.DataStructures.Graph;
import edu.brown.cs.student.DataStructures.IGEdge;
import edu.brown.cs.student.DataStructures.IGNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * This is the testing class for our algorithm.
 * We made many private functions public for testing purposes.
 * Note: for any defensive copies of graphs, we only edit the nodes list.
 * Therefore, all graphs made while running contain the full original list of all edges,
 * but may not contain all original nodes.
 */
public class GrouperTest {
  Grouper grouper;
  Graph graph;

  IGNode node0;
  IGNode node1;
  IGNode node2;
  IGNode node3;
  IGNode node4;

  IGEdge edge0;
  IGEdge edge1;
  IGEdge edge2;
  IGEdge edge3;
  IGEdge edge4;
  IGEdge edge5;
  IGEdge edge6;
  IGEdge edge7;
  IGEdge edge8;
  IGEdge edge9;


  @Before
  public void setUp() {
    graph = new Graph();

    node0 = new IGNode(0, new HashSet<>());
    node1 = new IGNode(1, new HashSet<>());
    node2 = new IGNode(2, new HashSet<>());
    node3 = new IGNode(3, new HashSet<>());
    node4 = new IGNode(4, new HashSet<>());

    edge0 = new IGEdge(node0, node1, 9);
    edge1 = new IGEdge(node1, node2, 7);
    edge2 = new IGEdge(node2, node3, 5);
    edge3 = new IGEdge(node3, node4, 10);
    edge4 = new IGEdge(node4, node0, 4);
    edge5 = new IGEdge(node0, node3, 3);
    edge6 = new IGEdge(node0, node2, 8);
    edge7 = new IGEdge(node4, node1, 5);
    edge8 = new IGEdge(node4, node2, 1);
    edge9 = new IGEdge(node1, node3, 2);

    graph.addNode(node0);
    graph.addNode(node1);
    graph.addNode(node2);
    graph.addNode(node3);
    graph.addNode(node4);

    graph.addEdge(edge0);
    graph.addEdge(edge1);
    graph.addEdge(edge2);
    graph.addEdge(edge3);
    graph.addEdge(edge4);
    graph.addEdge(edge5);
    graph.addEdge(edge6);
    graph.addEdge(edge7);
    graph.addEdge(edge8);
    graph.addEdge(edge9);

    grouper = new Grouper(graph);
  }

  @After
  public void tearDown() {
    graph = null;

    node0 = null;
    node1 = null;
    node2 = null;
    node3 = null;
    node4 = null;

    edge0 = null;
    edge1 = null;
    edge2 = null;
    edge3 = null;
    edge4 = null;
    edge5 = null;
    edge6 = null;
    edge7 = null;
    edge8 = null;
    edge9 = null;

    grouper = null;
  }

  @Test
  public void weightMapSetUpTest() {
    // Makes sure that the weight map is set up for each node
    setUp();

    assertTrue(node0.weightTo(node1) == 9);
    assertTrue(node0.weightTo(node2) == 8);
    assertTrue(node0.weightTo(node3) == 3);
    assertTrue(node0.weightTo(node4) == 4);

    tearDown();
  }
  @Test
  public void calculateContributionTest() {
    setUp();

    double contribution0 = Grouper.calculateContribution(graph, node0);
    double contribution1 = Grouper.calculateContribution(graph, node1);
    double contribution2 = Grouper.calculateContribution(graph, node2);
    double contribution3 = Grouper.calculateContribution(graph, node3);
    double contribution4 = Grouper.calculateContribution(graph, node4);

    assertTrue(contribution0 == 24);
    assertTrue(contribution1 == 23);
    assertTrue(contribution2 == 21);
    assertTrue(contribution3 == 20);
    assertTrue(contribution4 == 20);

    tearDown();
  }

  @Test
  public void setContributionsTest() {
    setUp();

    Graph newGraph = Grouper.setContributions(graph);

    assertTrue(newGraph.getNode(node0).getContribution() == 24);
    assertTrue(newGraph.getNode(node1).getContribution() == 23);
    assertTrue(newGraph.getNode(node2).getContribution() == 21);
    assertTrue(newGraph.getNode(node3).getContribution() == 20);
    assertTrue(newGraph.getNode(node4).getContribution() == 20);

    tearDown();
  }

  @Test
  public void deconstructTest() throws Exception {
    setUp();
    graph = Grouper.setContributions(graph);
    Set<IGNode> graphNodes = graph.getNodes();

    // Test remove 0 nodes
    Graph g5 = Grouper.deconstruct(graph, 0);
    assertTrue(g5.getNodes().equals(graph.getNodes()));

    // Test remove 1 node
    System.out.println("LINE 172 -------------------------------");
    System.out.println(Grouper.deconstruct(g5, 1));
    Graph g4 = Grouper.deconstruct(g5, 1);
    // There are two minimum nodes, so two possible answers.
    List<IGNode> g4ans1 = new ArrayList<>(graphNodes);
    g4ans1.remove(node3);
    List<IGNode> g4ans2 = new ArrayList<>(graphNodes);
    g4ans2.remove(node4);

    Set<IGNode> g4Nodes = g4.getNodes();
    assertTrue(
            g4Nodes.equals(g4ans1)
                    || g4Nodes.equals(g4ans2)
    );

    // Test remove 2 nodes
    Graph g3 = Grouper.deconstruct(graph, 2);
    List<IGNode> g3ans = new ArrayList<>(graphNodes);
    g3ans.remove(node3);
    g3ans.remove(node4);
    assertTrue(g3.getNodes().equals(g3ans));

    // Test remove 2 nodes then another 2 nodes
    Graph g2 = Grouper.deconstruct(g4, 2);
    List<IGNode> g2ans = new ArrayList<>(graphNodes);
    g2ans.remove(node3);
    g2ans.remove(node4);
    g2ans.remove(node2);
    assertTrue(g2.getNodes().equals(g2ans));

    // Test remove 4 nodes
    Graph g1 = Grouper.deconstruct(graph, 4);
    List<IGNode> g1ans = Lists.newArrayList(node0);
    assertTrue(g1.getNodes().equals(g1ans));

    // Test remove 4 nodes then 1 node
    Graph g0 = Grouper.deconstruct(g1, 1);
    assertTrue(g0.getNodes().isEmpty());

    // Test remove all nodes
    assertTrue(Grouper.deconstruct(graph, 5).getNodes().isEmpty());

    tearDown();
  }

  @Test
  public void randomDropTest() {

    setUp();

    /*
    System.out.println(graph.getNodes().size());
    Graph graphThree = Grouper.randomDrop(graph, 2);
    List<IGNode> nodesThree = graphThree.getNodes();

    System.out.println("---------");
    System.out.println(graph.getNodes().size());
    System.out.println(nodesThree.size());

    assertTrue(nodesThree.size() == 3);
     */

    tearDown();
  }

  @Test
  public void greedyAddTest() {
    setUp();



    tearDown();
  }

  @Test
  public void constructTest() {
    setUp();



    tearDown();
  }

  @Test
  public void bestGraphTest() {
    setUp();



    tearDown();
  }

  @Test
  public void graphScoreTest() {
    setUp();



    tearDown();
  }

  @Test
  public void nodesNotInGraph() {
    setUp();



    tearDown();}
}
