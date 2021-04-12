package edu.brown.cs.student.DataStructures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Graph {

  private final Set<IGNode> nodes;
  private final Set<IGEdge> edges;

  /**
   * Constructor for the graph class, initializes the nodes and edges ArrayLists.
   */
  public Graph() {
    nodes = new HashSet<>();
    edges = new HashSet<>();
  }

  public Graph(Graph otherGraph) {
    this.nodes = new HashSet<>(otherGraph.getNodes());
    this.edges = new HashSet<>(otherGraph.getEdges());
  }


  /**
   * adds a node.
   * @param n node to be added
   */
  public void addNode(IGNode n) {
    nodes.add(n);
  }

  /**
   * adds an edge.
   * @param e the edge to be added
   */
  public void addEdge(IGEdge e) {
    edges.add(e);
    e.getStart().addEdge(e);
    e.getEnd().addEdge(e);
  }

  /**
   * removes a node.
   * @param n node to be removed.
   */
  public void removeNode(IGNode n) {

    nodes.remove(n);

  }

  /**
   * removes an edge.
   * @param e edge to be removed.
   */
  public void removeEdge(IGEdge e) {
    edges.remove(e);
    e.getStart().removeEdge(e);
    e.getEnd().removeEdge(e);
  }

  /**
   * @return list of nodes
   */
  public Set<IGNode> getNodes() {
    return nodes;
  }

  /**
   * @return list of edges
   */
  public Set<IGEdge> getEdges() {
    return edges;
  }

  /**
   * @return specific node or null if non-existent
   */
  public IGNode getNode(IGNode node) {
    for(IGNode n: nodes) {
      if (n.equals(node)) {
        return n;
      }
    }
    return null;
  }
}
