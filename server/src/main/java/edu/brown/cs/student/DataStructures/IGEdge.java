package edu.brown.cs.student.DataStructures;

public class IGEdge implements Edge<IGNode, IGEdge> {
  private final IGNode startNode;
  private final IGNode endNode;

  private final double weight;

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
}
