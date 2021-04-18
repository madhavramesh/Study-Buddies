package edu.brown.cs.student.algorithm;

import com.google.common.collect.Lists;
import edu.brown.cs.student.DataStructures.Graph;
import edu.brown.cs.student.DataStructures.IGEdge;
import edu.brown.cs.student.DataStructures.IGNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

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

    double contribution0 = grouper.calculateContribution(graph, node0);
    double contribution1 = grouper.calculateContribution(graph, node1);
    double contribution2 = grouper.calculateContribution(graph, node2);
    double contribution3 = grouper.calculateContribution(graph, node3);
    double contribution4 = grouper.calculateContribution(graph, node4);

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

    Graph newGraph = grouper.setContributions(graph);

    assertTrue(newGraph.getNode(node0).getContribution() == 24);
    assertTrue(newGraph.getNode(node1).getContribution() == 23);
    assertTrue(newGraph.getNode(node2).getContribution() == 21);
    assertTrue(newGraph.getNode(node3).getContribution() == 20);
    assertTrue(newGraph.getNode(node4).getContribution() == 20);

    tearDown();
  }

  @Test
  public void deconstructTest() throws Exception {
    // We need to setUp() and tearDown() between each test type because
    // removal modifies the original node.

    Set<IGNode> graphNodes;

    // -------------------------------------------------------
    // Test remove 0 nodes
    setUp();

    graph = grouper.setContributions(graph);

    Graph g5 = grouper.deconstruct(graph, 0);
    assertTrue(g5.getNodes().equals(graph.getNodes()));

    tearDown();

    // -------------------------------------------------------
    // Test remove 1 node
    setUp();
    graph = grouper.setContributions(graph);
    graphNodes = graph.getNodes();

    Graph g4 = grouper.deconstruct(g5, 1);

    // There are two minimum nodes, so two possible answers.
    Set<IGNode> g4ans1 = new HashSet<>(graphNodes);
    g4ans1.remove(node3);
    Set<IGNode> g4ans2 = new HashSet<>(graphNodes);
    g4ans2.remove(node4);

    Set<IGNode> g4Nodes = g4.getNodes();
    assertTrue(
            g4Nodes.equals(g4ans1)
                    || g4Nodes.equals(g4ans2)
    );

    // -------------------------------------------------------
    // Test remove 2 nodes
    // There are 3 possible answers
    setUp();
    graph = grouper.setContributions(graph);
    graphNodes = graph.getNodes();

    Graph g3 = grouper.deconstruct(graph, 2);
    Set<IGNode> g3ans1 = new HashSet<>(graphNodes);
    g3ans1.remove(node3);
    g3ans1.remove(node0);
    Set<IGNode> g3ans2 = new HashSet<>(graphNodes);
    g3ans2.remove(node3);
    g3ans2.remove(node1);
    Set<IGNode> g3ans3 = new HashSet<>(graphNodes);
    g3ans3.remove(node4);
    g3ans3.remove(node3);

    Set<IGNode> g3nodes = g3.getNodes();

    assertTrue(
            (g3nodes.equals(g3ans1) ||
                    g3nodes.equals(g3ans2) ||
                    g3nodes.equals(g3ans3)));

    tearDown();

    // -------------------------------------------------------
    // Test remove 1 nodes then another 2 nodes
    setUp();
    graph = grouper.setContributions(graph);
    graphNodes = graph.getNodes();

    Graph g2 = grouper.deconstruct(g4, 2);
    Set<IGNode> g2ans = new HashSet<>(graphNodes);
    g2ans.remove(node3);
    g2ans.remove(node4);
    g2ans.remove(node2);

    assertTrue(g2.getNodes().equals(g2ans));

    tearDown();

    // -------------------------------------------------------
    // Test remove 4 nodes
    setUp();
    graph = grouper.setContributions(graph);

    Graph g1 = grouper.deconstruct(graph, 4);
    Set<IGNode> g1ans1 = new HashSet<>(Lists.newArrayList(node0));
    Set<IGNode> g1ans2 = new HashSet<>(Lists.newArrayList(node1));
    assertTrue(g1.getNodes().equals(g1ans1) ||
            g1.getNodes().equals(g1ans2));

    tearDown();

    // -------------------------------------------------------
    // Test remove 4 nodes then 1 node
    setUp();
    graph = grouper.setContributions(graph);

    Graph g0 = grouper.deconstruct(g1, 1);
    assertTrue(g0.getNodes().isEmpty());

    tearDown();

    // -------------------------------------------------------
    // Test remove all nodes
    setUp();
    graph = grouper.setContributions(graph);

    assertTrue(grouper.deconstruct(graph, 5).getNodes().isEmpty());

    tearDown();
  }

  @Test
  public void randomDropTest() {
    setUp();
    grouper.setContributions(graph);

    Graph g5 = grouper.randomDrop(graph, 0);
    Graph g4 = grouper.randomDrop(graph, 1);
    Graph g3 = grouper.randomDrop(graph, 2);
    Graph g2 = grouper.randomDrop(graph, 3);
    Graph g1 = grouper.randomDrop(graph, 4);
    //Graph g0 = grouper.randomDrop(graph, 5);

    assertTrue(g5.getNodes().size() == 5);
    assertTrue(g4.getNodes().size() == 4);
    assertTrue(g3.getNodes().size() == 3);
    assertTrue(g2.getNodes().size() == 2);
    assertTrue(g1.getNodes().size() == 1);
    //assertTrue(g0.getNodes().size() == 0);

    tearDown();
  }

  @Test
  public void nodesNotInGraph() {
    setUp();

    Graph smallerGraph = new Graph(graph);

    // Test remove no nodes
    Set<IGNode> result0 = grouper.nodesNotInGraph(smallerGraph);
    Set<IGNode> ans0    = Collections.emptySet();
    assertTrue(result0.equals(ans0));

    // Test remove 1 node
    smallerGraph.removeNode(node0);
    Set<IGNode> result1 = grouper.nodesNotInGraph(smallerGraph);
    Set<IGNode> ans1    = new HashSet<>(Lists.newArrayList(node0));
    assertTrue(result1.equals(ans1));

    // Test remove 2 nodes
    smallerGraph.removeNode(node3);
    Set<IGNode> result2 = grouper.nodesNotInGraph(smallerGraph);
    Set<IGNode> ans2    = new HashSet<>(Lists.newArrayList(node0, node3));
    assertTrue(result2.equals(ans2));

    // Test remove 5 nodes
    smallerGraph.removeNode(node1);
    smallerGraph.removeNode(node2);
    smallerGraph.removeNode(node4);
    Set<IGNode> result5 = grouper.nodesNotInGraph(smallerGraph);
    Set<IGNode> ans5    = new HashSet<>(Lists.newArrayList(node0, node1, node2, node3, node4));
    assertTrue(result5.equals(ans5));

    tearDown();
  }

  @Test
  public void constructTest() throws Exception {
    setUp();
    grouper.setContributions(graph);
    Graph ogGraph = new Graph(graph);

    // start with node 0
    graph.removeNode(node1);
    graph.removeNode(node2);
    graph.removeNode(node3);
    graph.removeNode(node4);

    Graph res1 = grouper.construct(graph);
    graph.addNode(node1);
    assertTrue(res1.getNodes().equals(graph.getNodes()));

    Graph res2 = grouper.construct(res1);
    graph.addNode(node2);
    assertTrue(res2.getNodes().equals(graph.getNodes()));

    Graph res3 = grouper.construct(res2);
    Graph ans1 = new Graph(res3);
    ans1.addNode(node3);
    Graph ans2 = new Graph(res3);
    ans2.addNode(node4);
    assertTrue(
            res3.getNodes().equals(ans1.getNodes()) ||
            res3.getNodes().equals(ans2.getNodes()));

    Graph res4 = grouper.construct(res3);
    assertTrue(res4.getNodes().equals(ogGraph.getNodes()));

    // start with node 3
    graph.removeNode(node0);
    graph.removeNode(node1);
    graph.removeNode(node2);
    graph.addNode(node3);
    Graph ans3 = grouper.construct(graph);
    graph.addNode(node4);
    assertTrue(ans3.getNodes().equals(graph.getNodes()));

    tearDown();
  }

  @Test
  public void greedyAddTest() throws Exception {
    setUp();
    grouper.setContributions(graph);

    Graph graphCopy = new Graph(graph);

    // start with only node 3
    graphCopy.removeNode(node0);
    graphCopy.removeNode(node1);
    graphCopy.removeNode(node2);
    graphCopy.removeNode(node4);

    Graph ans1 = grouper.greedyAdd(graphCopy, 1);
    graphCopy.addNode(node4);
    assertTrue(ans1.getNodes().equals(graphCopy.getNodes()));

    // start with only node 0
    graphCopy.removeNode(node4);
    graphCopy.removeNode(node3);
    graphCopy.addNode(node1);

    Graph ans2 = grouper.greedyAdd(graphCopy, 2);
    assertTrue(ans2.getNodes().equals(new HashSet<>(Lists.newArrayList(node0, node1, node2))));

    tearDown();
  }

  @Test
  public void graphScoreTest() {
    setUp();
    grouper.setContributions(graph);

    // Full graph
    assertTrue(grouper.graphScore(graph) == (54));

    // Subgraph
    graph.removeNode(node0);
    graph.removeNode(node1);
    graph.removeNode(node2);
    // Note, this wouldn't be the actual score if the removal was done by
    // the functions in the algorithm. This is because removeNode doesn't update
    // the contribution.
    assertTrue(grouper.graphScore(graph) == 20);

    // Empty graph
    graph.removeNode(node3);
    graph.removeNode(node4);
    assertTrue(grouper.graphScore(graph) == 0);

    tearDown();
  }

  /*
  @Test
  public void bestGraphTest() {
    setUp();
    tearDown();
  }
   */

  @Test
  public void findGroupTest() throws Exception {
    setUp();
    grouper.setContributions(graph);

    Graph groupOf1 = grouper.findGroup(graph, 1);

    Graph groupOf2 = grouper.findGroup(graph, 2);

    Graph groupOf3 = grouper.findGroup(graph, 3);

    Graph groupOf4 = grouper.findGroup(graph, 4);

    Graph groupOf5 = grouper.findGroup(graph, 5);


    tearDown();
  }

  @Test
  public void makeGroupTest() throws Exception {

    setUp();
    grouper.setContributions(graph);
    grouper.makeGroups(graph, 1);
    tearDown();

    setUp();
    grouper.setContributions(graph);
    grouper.makeGroups(graph, 2);
    tearDown();

    setUp();
    grouper.setContributions(graph);
    grouper.makeGroups(graph, 3);
    tearDown();

    setUp();
    grouper.setContributions(graph);
    grouper.makeGroups(graph, 4);
    tearDown();

    setUp();
    grouper.setContributions(graph);
    grouper.makeGroups(graph, 5);
    tearDown();

    tearDown();
  }

}
