package edu.brown.cs.student.DataStructures;

/**
 * Class for generic edge types for a directed graph.
 * @param <N> an extension/type of node used for the edge.
 */
public class Edge<N extends Node<N, E>, E extends Edge<N, E>> {

  private final N startNode;
  private final N endNode;

  /**
   * Constructor for the edge.
   * @param s the starting node
   * @param e the ending node
   */
  public Edge(N s, N e) {
    startNode = s;
    endNode = e;
  }

  /**
   * @return the starting node
   */
  public N getStart() {
    return startNode;
  }

  /**
   * @return the ending node
   */
  public N getEnd() {
    return endNode;
  }

}
