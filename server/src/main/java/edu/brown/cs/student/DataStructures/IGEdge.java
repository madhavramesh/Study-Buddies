package edu.brown.cs.student.DataStructures;

public class IGEdge extends Edge{
  private final double weight;
  private double contribution;

  /**
   * Constructor for IGEdge.
   * @param s the starting node
   * @param e the ending node
   * @param weight
   */
  public IGEdge(Node s, Node e, double weight) {
    super(s, e);
    this.weight = weight;
  }

  /**
   * Sets the contribution
   * @param contribution new contribution to be set
   */
  public void setContribution(double contribution) {
    this.contribution = contribution;
  }

  /**
   * @return Returns the contribution
   */
  public double getContribution() {
    return contribution;
  }

  /**
   * @return Returns the edge weight
   */
  public double getWeight() {
    return weight;
  }
}
