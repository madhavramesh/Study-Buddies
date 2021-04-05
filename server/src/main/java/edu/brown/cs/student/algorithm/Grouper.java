package edu.brown.cs.student.algorithm;

import edu.brown.cs.student.DataStructures.Edge;
import edu.brown.cs.student.DataStructures.Graph;
import edu.brown.cs.student.DataStructures.IGEdge;
import edu.brown.cs.student.DataStructures.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class that executes the algorithm to form groups of size n.
 * It implements an "iterated greedy search" algorithm to solve a version
 * of the "maximum diversity problem."
 * Pseudo code and logic was burrowed from
 * https://sci2s.ugr.es/sites/default/files/ficherosPublicaciones/1410_10.1016@j.ejor_.2011.04.018.pdf
 */
public class Grouper {
  private final Graph<Node, IGEdge> graph;

  /**
   * Constructor for the Grouper class
   * @param graph Graph to be referenced
   */
  public Grouper(Graph<Node, IGEdge> graph) {
    this.graph = graph;
  }

  /**
   * Finds a single group with max edge weights of size n.
   * @param g Graph to be referenced
   * @param groupSize Group size to find
   */
  public Graph<Node, IGEdge> findGroup(Graph<Node, IGEdge> g, int groupSize) {

    // 1)
    // Create an initial candidate solution graph by removing the
    // lowest contribution nodes to get a graph of size groupSize.
    Graph candidateSolution = deconstruct(g, graph.getNodes().size() - groupSize);


    // 2)
    // (Optional)
    // localSearch()


    // 3)
    // Have a variable that stores the best-so-far candidate graph.
    Graph bestSoFar = candidateSolution;


    // 4)
    // Loop through while the termination condition hasn't been met.
    // Termination condition ie iterations or computation time.
    while (!terminationCondition) {

      // 5)
      // Randomly drop n variables from the candidate solution
      Graph<Node, IGEdge> incompleteSolution = randomDrop(candidateSolution, randomDropNum);


      // 6)
      // Greedily add n variables to the candidate solution
      Graph<Node, IGEdge> completeSolution = greedyAdd(incompleteSolution, randomDropNum, 0);


      // 7)
      // (Optional) Local search


      // 8)
      // If the new complete solution is better than the bestSoFar solution, replace it
      bestSoFar = bestGraph(bestSoFar, completeSolution);


      // 9)
      // Update the candidate solution which is the solution we are iterating through
      candidateSolution = bestGraph(candidateSolution, completeSolution);
    }

    return bestSoFar;
  }

  /**
   * Runs the construction algorithm "numToAdd" times.
   * @param g the reference graph/graph to modify
   * @param numToAdd how many elements to add to the partial graph
   * @param counter the counter that tracks the recursion
   * @return
   */
  private Graph<Node, IGEdge> greedyAdd(Graph<Node, IGEdge> g, int numToAdd, int counter) {
    if (counter < numToAdd) {
      return greedyAdd(construct(g), numToAdd, counter + 1);
    } else {
      return g;
    }
  }

  /**
   * Adds the highest contribution element that is not already in the graph
   * @param g graph to be referenced
   */
  private Graph<Node, IGEdge> construct(Graph g) {

  }

  /**
   * Removes the n lowest contribution elements from the graph.
   * @param g the graph to be referenced
   * @param n how many elements to remove
   */
  private Graph<Node, IGEdge> deconstruct(Graph<Node, IGEdge> g, int n) {

  }

  /**
   * Determines the contribution of a node.
   * The contribution of a node is the sum of the edge weights that contain that node in the graph.
   * @return the contribution
   */
  private double contribution(Graph g, Node<Integer, Double> n) {
    Node nodeValue = n.getValue();
    double totalContribution = 0;
    List<IGEdge> edges = g.getEdges();
    
    for (IGEdge edge: edges) {
      Integer startValue = edge.getStart().getValue();
      Integer endValue   = edge.getEnd().getValue();
      if ()
    }
  }

  /**
   * Returns the graph with the greater average weights.
   * @param g1
   * @param g2
   * @return
   */
  private Graph<Node, IGEdge> bestGraph(Graph<Node, IGEdge> g1, Graph<Node, IGEdge> g2) {

  }

  /**
   * Returns the nodes that are not in the full original complete graph
   * @param g The subset graph
   * @return The list of nodes that are not in the subset graph
   */
  private List<Node> nodesNotInGraph(Graph<Node, IGEdge> g) {
    List<Node> allNodes = new ArrayList<>(graph.getNodes());
    List<Node> nodesInSubgraph = g.getNodes();
    allNodes.removeAll(nodesInSubgraph);
    return allNodes;
  }

}
