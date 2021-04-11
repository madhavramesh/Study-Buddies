package edu.brown.cs.student.DataStructures;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class GraphTest {
  Graph testGraph;

  IGNode testNode0;
  IGNode testNode1;

  IGEdge testEdge0;

  @Before
  public void setUp() {
    testGraph = new Graph();

    testNode0 = new IGNode(0, new HashSet<>(Lists.newArrayList(testEdge0)));
    testNode1 = new IGNode(1, new HashSet<>(Lists.newArrayList(testEdge0)));
    testGraph.addNode(testNode0);
    testGraph.addNode(testNode1);

    testEdge0 = new IGEdge(testNode0, testNode1, 10);
    testGraph.addEdge(testEdge0);
  }

  @After
  public void tearDown() {
    testGraph = null;
    testNode0 = null;
    testNode1 = null;
    testEdge0 = null;
  }

  @Test
  public void getNodesTest() {
    setUp();

    Set<IGNode> nodesInGraph = testGraph.getNodes();

    // Test same references
    Set<IGNode> answer = new HashSet<>(Lists.newArrayList(testNode0, testNode1));
    assertTrue(nodesInGraph.equals(answer));

    // TODO: Consider if you should create a custom equals method
    /*
    // Test different copies
    IGNode node0Copy = new IGNode(0, new ArrayList<>());
    IGNode node1Copy = new IGNode(1, new ArrayList<>());
    IGEdge edge0Copy = new IGEdge(node0Copy, node1Copy, 10);
    node0Copy.addEdge(edge0Copy);
    node1Copy.addEdge(edge0Copy);

    List<IGNode> answerCopy = Lists.newArrayList(node0Copy, node1Copy);
    assertTrue(nodesInGraph.equals(answerCopy));
    */

    tearDown();
  }

  @Test
  public void getEdgesTest() {
    setUp();

    Set<IGEdge> edgesInGraph = testGraph.getEdges();
    Set<IGEdge> answer = new HashSet<>(Lists.newArrayList(testEdge0));
    assertTrue(edgesInGraph.equals(answer));

    tearDown();
  }

  @Test
  public void addNodeTest() {
    setUp();

    // Create new nodes
    IGNode newNode2 = new IGNode(2, new HashSet<>());
    IGNode newNode3 = new IGNode(3, new HashSet<>());
    IGEdge newEdge1 = new IGEdge(testNode0, newNode3, -5);
    newNode3.addEdge(newEdge1);

    testGraph.addNode(newNode2);
    assertTrue(testGraph.getNodes().equals(
            new HashSet<>(Lists.newArrayList(testNode0, testNode1, newNode2))
    ));

    testGraph.addNode(newNode3);
    assertTrue(testGraph.getNodes().equals(
            new HashSet<>(Lists.newArrayList(testNode0, testNode1, newNode2, newNode3))
    ));

    tearDown();
  }

  @Test
  public void addEdgeTest() {
    setUp();

    // Create new edges
    IGNode newNode2 = new IGNode(2, new HashSet<>());
    IGNode newNode3 = new IGNode(3, new HashSet<>());

    IGEdge newEdge1 = new IGEdge(testNode0, newNode3, -5);
    IGEdge newEdge2 = new IGEdge(newNode2, newNode3, 0);

    // Test adding one edge
    testGraph.addEdge(newEdge2);

    assertTrue(testGraph.getEdges().equals(
            new HashSet<>(Lists.newArrayList(testEdge0, newEdge2))
    ));
    assertTrue(newNode2.getEdges().equals(
            new HashSet<>(Lists.newArrayList(newEdge2))
    ));
    assertTrue(newNode3.getEdges().equals(
            new HashSet<>(Lists.newArrayList(newEdge2))
    ));
    assertTrue(testNode0.getEdges().equals(
       new HashSet<>(Lists.newArrayList(testEdge0))
    ));

    // Test adding another edge
    testGraph.addEdge(newEdge1);

    assertTrue(testGraph.getEdges().equals(
            new HashSet<>(Lists.newArrayList(testEdge0, newEdge2, newEdge1))
    ));
    assertTrue(testNode0.getEdges().equals(
            new HashSet<>(Lists.newArrayList(newEdge1, testEdge0))
    ));
    assertTrue(newNode3.getEdges().equals(
            new HashSet<>(Lists.newArrayList(newEdge2, newEdge1))
    ));

    tearDown();
  }

  @Test
  public void removeNodeTest() {
    setUp();

    testGraph.removeNode(testNode0);
    assertTrue(testGraph.getNodes().equals(
            new HashSet<>(Lists.newArrayList(testNode1))
    ));

    testGraph.removeNode(testNode0);
    testGraph.removeNode(testNode1);
    assertTrue(testGraph.getNodes().equals(
            Collections.emptySet()
    ));


    tearDown();
  }

  @Test
  public void removeEdgeTest() {
    setUp();

    testGraph.removeEdge(testEdge0);

    assertTrue(testGraph.getEdges().equals(
            Collections.emptySet()
    ));

    tearDown();
  }

  @Test
  public void getNodeTest() {
    setUp();

    assertTrue(testGraph.getNode(testNode0).equals(testNode0));

    testGraph.removeNode(testNode0);

    assertTrue(testGraph.getNode(testNode0) == null);

    tearDown();
  }
}
