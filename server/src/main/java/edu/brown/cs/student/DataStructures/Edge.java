package edu.brown.cs.student.DataStructures;

/**
 * Class for generic edge types for a directed graph.
 * @param <N> an extension/type of node used for the edge.
 */
public class Edge<N extends Node> {

  private final N startnode;
  private final N endnode;

  /**
   * Constructor for the edge.
   * @param s the starting node
   * @param e the ending node
   */
  public Edge(N s, N e) {
    startnode = s;
    endnode = e;
  }

  /**
   * @return the starting node
   */
  public N getStart() {
    return startnode;
  }

  /**
   * @return the ending node
   */
  public N getEnd() {
    return endnode;
  }

}
