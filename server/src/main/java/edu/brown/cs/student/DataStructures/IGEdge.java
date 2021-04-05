package edu.brown.cs.student.DataStructures;

public class IGEdge implements Edge<IGNode, IGEdge> {
  private final IGNode startNode;
  private final IGNode endNode;

  private final double weight;
  private double contribution;

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
  public double weight() {
    return this.weight;
  }

  @Override
  public int compareTo(IGEdge otherIGEdge) {
    if (this.weight < otherIGEdge.weight()) {
      return -1;
    } else if (this.weight == otherIGEdge.weight()) {
      return 0;
    } else {
      return 1;
    }
  }
}
