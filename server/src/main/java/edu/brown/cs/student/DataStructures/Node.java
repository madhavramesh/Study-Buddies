package edu.brown.cs.student.DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Node class for an undirected graph, has type parameter extension of generic edge class.
 * @param <E> extension of edge class.
 */
public class Node<E extends Edge> {

  private final List<E> connectedEdges;

  /**
   * The constructor for the node class, initializes the incoming/outgoing edges arraylists.
   */
  public Node() {
    connectedEdges = new ArrayList();
  }

  /**
   * Constructor that takes in an already populated list of connected edges.
   */
  public Node(List<E> connectedEdges){
    this.connectedEdges = connectedEdges;
  }

  /**
   * @return an ArrayList holding the connected edges of this node.
   */
  public List<E> incomingEdges() {
    return connectedEdges;
  }

  /**
   * adds an outgoing edge to this node.
   * @param e an generic directed graph Edge.
   */
  public void addEdge(E e) {
    connectedEdges.add(e);
  }

}
