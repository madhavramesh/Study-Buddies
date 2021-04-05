package edu.brown.cs.student.DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Node class for an undirected graph, has type parameter extension of generic edge class.
 * @param <E> extension of edge class.
 */
public class Node<N, E extends Edge> {

  private final N value;
  private final List<E> connectedEdges;

  /**
   * Constructor that initializes an empty value and the incoming/outgoing edges arraylists.
   */
  public Node() {
    value = null;
    connectedEdges = new ArrayList();
  }

  /**
   * Constructor that takes in a node value and an already populated list of connected edges.
   */
  public Node(N value, List<E> connectedEdges){
    this.value = value;
    this.connectedEdges = connectedEdges;
  }

  /**
   * @return Returns the value of the node.
   */
  public N getValue() {
    return value;
  }

  /**
   * @return an ArrayList holding the connected edges of this node.
   */
  public List<E> getConnectedEdges() {
    return connectedEdges;
  }

  /**
   * adds an edge to this node.
   * @param e a generic graph edge.
   */
  public void addEdge(E e) {
    connectedEdges.add(e);
  }

  /**
   * removes an edge from this node.
   * @param e a generic graph edge.
   */
  public void removeEdge(E e) {
    connectedEdges.remove(e);
  }

}
