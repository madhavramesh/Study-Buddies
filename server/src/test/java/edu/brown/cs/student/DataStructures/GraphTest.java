package edu.brown.cs.student.DataStructures;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class GraphTest {
  Graph<IGNode, IGEdge> testGraph;

  IGNode testNode0;
  IGNode testNode1;

  IGEdge testEdge0;

  @Before
  public void setUp() {
    testGraph = new Graph<IGNode, IGEdge>();

    testNode0 = new IGNode(0, Lists.newArrayList(testEdge0));
    testNode1 = new IGNode(1, Lists.newArrayList(testEdge0));
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

    List<IGNode> nodesInGraph = testGraph.getNodes();


    // Test same references
    List<IGNode> answer = Lists.newArrayList(testNode0, testNode1);
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

    List<IGEdge> edgesInGraph = testGraph.getEdges();
    List<IGEdge> answer = Lists.newArrayList(testEdge0);
    assertTrue(edgesInGraph.equals(answer));

    tearDown();
  }

  @Test
  public void addNodeTest() {
    setUp();

    // Create new nodes
    IGNode newNode2 = new IGNode(2, new ArrayList<>());
    IGNode newNode3 = new IGNode(3, new ArrayList<>());
    IGEdge newEdge1 = new IGEdge(testNode0, newNode3, -5);
    newNode3.addEdge(newEdge1);

    testGraph.addNode(newNode2);
    assertTrue(testGraph.getNodes().equals(
            Lists.newArrayList(testNode0, testNode1, newNode2)
    ));
    testGraph.addNode(newNode3);
    assertTrue(testGraph.getNodes().equals(
            Lists.newArrayList(testNode0, testNode1, newNode2, newNode3)
    ));

    tearDown();
  }

  @Test
  public void addEdgeTest() {
    setUp();

    // Create new edges
    IGNode newNode2 = new IGNode(2, new ArrayList<>());
    IGNode newNode3 = new IGNode(3, new ArrayList<>());
    IGEdge newEdge1 = new IGEdge(testNode0, newNode3, -5);
    newNode3.addEdge(newEdge1);
    IGEdge newEdge2 = new IGEdge(newNode2, newNode3, 0);

    testGraph.addEdge(newEdge2);
    assertTrue(testGraph.getEdges().equals(
            Lists.newArrayList(testEdge0, newEdge2)
    ));
    testGraph.addEdge(newEdge1);
    assertTrue(testGraph.getEdges().equals(
            Lists.newArrayList(testEdge0, newEdge2, newEdge1)
    ));

    tearDown();
  }

  @Test
  public void removeNodeTest() {
    setUp();

    testGraph.removeNode(testNode0);
    assertTrue(testGraph.getNodes().equals(
            Lists.newArrayList(testNode1)
    ));

    testGraph.removeNode(testNode0);
    testGraph.removeNode(testNode1);
    assertTrue(testGraph.getNodes().equals(
            Collections.emptyList()
    ));


    tearDown();
  }

  @Test
  public void removeEdgeTest() {
    setUp();

    testGraph.removeEdge(testEdge0);

    assertTrue(testGraph.getEdges().equals(
            Collections.emptyList()
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
