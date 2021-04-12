package edu.brown.cs.student.DataStructures;

import java.util.*;

public class IGNode implements Node<IGNode, IGEdge> {
  private final int value;
  private Set<IGEdge> connectedEdges;
  private Double contribution;
  private Map<IGNode, Double> weightMap;

  /**
   * Constructor for an IGNode
   * @param id The value of the node that corresponds to each person's unique ID.
   * @param connectedEdges The edges that are connected to this node.
   */
  public IGNode(int id, Set<IGEdge> connectedEdges) {
    this.value = id;
    this.connectedEdges = connectedEdges;
  }

  /**
   * @return The value of the node.
   */
  public int getValue() {
    return value;
  }

  /**
   * @return The edges connected to the node.
   */
  @Override
  public Set<IGEdge> getEdges() {
    return connectedEdges;
  }

  /**
   * Adds an edge to the node.
   * TODO: Check if the reverse edge has already been added (because undirected) or already added
   * @param edge edge to potentially be added
   */
  @Override
  public void addEdge(IGEdge edge) {
    this.connectedEdges.add(edge);
  }

  /**
   * Removes an edge from the node.
   * TODO: Check if the reverse exists
   * @param edge edge to be removed
   */
  @Override
  public void removeEdge(IGEdge edge) {
    this.connectedEdges.remove(edge);
  }

  /**
   * Sets the contribution of this node.
   * @param newContribution new contribution
   */
  public void setContribution(double newContribution) {
    this.contribution = newContribution;
  }

  /**
   * Returns the contribution of a node.
   * @return
   */
  public Double getContribution() {
    return contribution;
  }

  /**
   * Populates the node -> weight map.
   * This is to increase efficiency when updating the contribution values
   * in the algorithm. Assumes weight values do not change.
   */
  public void setWeightMap() {
    weightMap = new HashMap<>();
    for (IGEdge edge: connectedEdges) {
      IGNode start  = edge.getStart();
      IGNode end    = edge.getEnd();
      double weight = edge.getWeight();
      if (start.equals(this)) {
        weightMap.put(end, weight);
      } else if (end.equals(this)) {
        weightMap.put(start, weight);
      }
    }
  }

  /**
   * Returns the weight to a certain node.
   * @param targetNode Node to get weight to
   * @return Weight of the edge connecting to that node
   */
  public double weightTo(IGNode targetNode) {
    return weightMap.get(targetNode);
  }

  public Map<IGNode, Double> getWeightMap() {
    return weightMap;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IGNode) || (o == null)) {
      return false;
    }
    IGNode otherNode = (IGNode) o;
    boolean sameValue = value == otherNode.getValue();
    return sameValue;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
    /*
    int result = 17;
    result = result * 31 + value;
    result = result * 31 + connectedEdges.hashCode();
    if (contribution == null) {
      return result;
    } else {
      return result * 31 + contribution.hashCode();
    }
     */
  }
}
