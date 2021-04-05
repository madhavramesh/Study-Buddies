package edu.brown.cs.student.graph;

import java.util.Collection;

/**
 * Node in a Graph.
 *
 * @param <N> Type of Node in Graph
 * @param <E> Type of Edge in Graph
 */
public interface GraphNode<N extends GraphNode<N, E>, E extends GraphEdge<N, E>> {

  /**
   * Returns all the outgoing edges.
   * @return a collection of all the outgoing edges.
   */
  Collection<E> getOutEdges();
}
