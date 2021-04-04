package edu.brown.cs.student.DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Directed Graph class with generic extended edge/node parameters.
 * Used to hold MapNodes and MapEdges for AStar/Dijkstra Searches.
 * @param <E> generic for edge type
 * @param <N> generic for node type
 */
public class Graph<E extends Edge, N extends Node> {

  private final List<N> nodes;
  private final List<E> edges;

  /**
   * Constructor for the graph class, initializes the nodes and edges ArrayLists.
   */
  public Graph() {
    nodes = new ArrayList();
    edges = new ArrayList();
  }

  /**
   * adds a node.
   * @param n node to be added
   */
  public void addNode(N n) {
    nodes.add(n);
  }

  /**
   * adds an edge.
   * @param e the edge to be added
   */
  public void addEdge(E e) {
    edges.add(e);
    e.getStart().addEdge(e);
    e.getEnd().addEdge(e);
  }

  /**
   * @return list of nodes
   */
  public List<N> getNodes() {
    return nodes;
  }

  /**
   * @return list of edges
   */
  public List<E> getEdges() {
    return edges;
  }
}
