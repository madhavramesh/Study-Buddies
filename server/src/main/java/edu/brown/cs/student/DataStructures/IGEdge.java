package edu.brown.cs.student.DataStructures;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class IGEdge implements Edge<IGNode, IGEdge> {
  private final IGNode startNode;
  private final IGNode endNode;

  private final Double weight;

  /**
   * Constructor for IGEdge.
   * @param s the starting node
   * @param e the ending node
   * @param weight
   */
  public IGEdge(IGNode s, IGNode e, double weight) {
    this.startNode = s;
    this.endNode = e;
    this.weight = weight;
  }

  @Override
  public IGNode getStart() {
    return this.startNode;
  }

  @Override
  public IGNode getEnd() {
    return this.endNode;
  }

  @Override
  public double getWeight() {
    return this.weight;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IGEdge) || (o == null)) {
      return false;
    }
    IGEdge otherEdge = (IGEdge) o;
    int oStartValue = otherEdge.getStart().getValue();
    int oEndValue   = otherEdge.getEnd().getValue();
    boolean sameNodes =
            (startNode.getValue() == oStartValue && endNode.getValue() == oEndValue ||
                    startNode.getValue() == oEndValue && endNode.getValue() == oStartValue);
    boolean sameWeight = weight == (otherEdge.getWeight());
    return sameNodes && sameWeight;
  }

  @Override
  public int hashCode() {
    Set<Integer> nodeValues = new HashSet<>(Lists.newArrayList(startNode.getValue(), endNode.getValue()));
    return Objects.hash(nodeValues, weight);
    /*
    // This depends on each node's value being UNIQUE, which is true because
    // each value is the unique ID of a person
    int result = 17;
    result = 31 * result + startNode.getValue() + endNode.getValue();
    result = 31 * result + weight.hashCode();

    return result;
     */
  }
}
