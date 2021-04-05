package edu.brown.cs.student.DataStructures;

import java.util.Collection;
import java.util.List;

public class IGNode implements Node<IGNode, IGEdge> {
  private final int value;
  private List<IGEdge> connectedEdges;
  private double contribution;

  /**
   * Constructor for an IGNode
   * @param id The value of the node that corresponds to each person's unique ID.
   * @param connectedEdges The edges that are connected to this node.
   */
  IGNode(int id, List<IGEdge> connectedEdges) {
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
  public Collection getEdges() {
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
  public double getContribution() {
    return contribution;
  }
}
