package edu.brown.cs.student.DataStructures;

import java.util.Collection;

/**
 * Node in a Graph.
 *
 * @param <N> Type of Node in Graph
 * @param <E> Type of Edge in Graph
 */
public interface Node<N extends Node<N, E>, E extends Edge<N, E>> {

  /**
   * Returns all the outgoing edges.
   * @return a collection of all the outgoing edges.
   */
  Collection<E> getEdges();

  void addEdge(E edge);

  void removeEdge(E edge);
}
