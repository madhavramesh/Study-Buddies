package edu.brown.cs.student.DataStructures;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;

public class IGEdgeTest {
  IGEdge testEdge;
  IGNode testStart;
  IGNode testEnd;

  @Before
  public void setUp() {
    testStart = new IGNode(0, new HashSet<>(Lists.newArrayList(testEdge)));
    testEnd   = new IGNode(1, new HashSet<>(Lists.newArrayList(testEdge)));
    testEdge  = new IGEdge(testStart, testEnd, 10);
  }

  @After
  public void tearDown() {
    testEdge = null;
  }

  @Test
  public void getStartTest() {
    setUp();
    assertTrue(testEdge.getStart() == testStart);
    tearDown();
  }

  @Test
  public void getEndTest() {
    setUp();
    assertTrue(testEdge.getEnd() == testEnd);
    tearDown();
  }

  @Test
  public void getWeightTest() {
    setUp();
    assertTrue(testEdge.getWeight() == 10);
    tearDown();
  }

}
