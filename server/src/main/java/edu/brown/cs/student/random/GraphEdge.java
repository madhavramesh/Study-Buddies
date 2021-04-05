package edu.brown.cs.student.graph;

/** Edge in a Graph.
 *
 * @param <N> Type of Node in Graph
 * @param <E> Type of Edge in Graph
 */
public interface GraphEdge<N extends GraphNode<N, E>, E extends GraphEdge<N, E>>
        extends Comparable<E> {

  /**
   * Returns the node the edge comes from.
   * @return the node the edge comes from.
   */
  N from();

  /**
   * Returns the node the edge goes to.
   * @return the node the edge goes to.
   */
  N to();

  /**
   * Returns the weight of the edge.
   * @return the weight of the edge.
   */
  double weight();
}
