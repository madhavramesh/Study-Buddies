package edu.brown.cs.student.DataStructures;

import edu.brown.cs.student.DataStructures.Edge;
import edu.brown.cs.student.DataStructures.IGEdge;
import edu.brown.cs.student.DataStructures.IGNode;
import edu.brown.cs.student.DataStructures.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class IGNodeTest {
  private IGNode testNode;

  @Before
  public void setUp() {
    testNode = new IGNode(13, new ArrayList<>());
  }

  @After
  public void tearDown() {
    testNode = null;
  }

  @Test
  public void getValueTest() {
    setUp();
    assertTrue(testNode.getValue() == 13);
    tearDown();
  }

  @Test
  public void edgesTest() {
    setUp();

    // Confirm edges are empty
    assertTrue(testNode.getEdges().isEmpty());

    // Confirm add edges works
    IGNode newNode  = new IGNode(0, new ArrayList<>());
    IGEdge newEdge1 = new IGEdge(newNode, testNode, 0);
    IGEdge newEdge2 = new IGEdge(newNode, testNode, 100);
    testNode.addEdge(newEdge1);
    assertTrue(testNode.getEdges().equals(Arrays.asList(newEdge1)));
    testNode.addEdge(newEdge2);
    assertTrue(testNode.getEdges().equals(Arrays.asList(newEdge1, newEdge2)));

    // Confirm removing edges works
    testNode.removeEdge(newEdge1);
    assertTrue(testNode.getEdges().equals(Arrays.asList(newEdge2)));
    testNode.removeEdge(newEdge2);
    assertTrue(testNode.getEdges().isEmpty());

    tearDown();
  }

  @Test
  public void contributionTest() {
    setUp();

    // Confirm initial contribution is null
    assertTrue(testNode.getContribution() == null);

    // Confirm setting contribution works
    testNode.setContribution(0.0);
    assertTrue(testNode.getContribution() == 0.0);
    testNode.setContribution(-100.5);
    assertTrue(testNode.getContribution() == -100.5);

    tearDown();
  }
}
