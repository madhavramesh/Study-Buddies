package edu.brown.cs.student.algorithm;

import com.google.common.collect.Lists;
import edu.brown.cs.student.DataStructures.*;

import java.util.*;

/**
 * This is the class that executes the algorithm to form groups of size n.
 * It implements an "iterated greedy search" algorithm to solve a version
 * of the "maximum diversity problem."
 * Pseudo code and logic was burrowed from
 * https://sci2s.ugr.es/sites/default/files/ficherosPublicaciones/1410_10.1016@j.ejor_.2011.04.018.pdf
 */
public class Grouper {
  private final Graph<Node, IGEdge> graph;
  private static Random r = new Random();

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
      Graph incompleteSolution = randomDrop(candidateSolution, randomDropNum);


      // 6)
      // Greedily add n variables to the candidate solution
      Graph completeSolution = greedyAdd(incompleteSolution, randomDropNum, 0);


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

    // For each node not in the graph, calculate its contribution to the given subgraph g
    // and store the result in a map from the node to the contribution.
    List<IGNode> nodesNotInG = nodesNotInGraph(g);
    Map<Node, Double> nodeContributionMap = new HashMap<>();
    for (IGNode n: nodesNotInG) {
      double contribution = contribution(g, n);
      nodeContributionMap.put(n, contribution);
    }

    // Find the node with the maximum contribution
    Map.Entry<Node, Double> maxNode = null;
    for (Map.Entry<Node, Double> entry: nodeContributionMap.entrySet()) {
      if (maxNode == null) {
        // if there hasn't been a maxNode found yet, assign the entry to it
        maxNode = entry;
      } else if (maxNode.getValue() == entry.getValue()) {
        // if there is a tie, randomly choose one
        List<Map.Entry<Node, Double>> potentialMaxes = Lists.newArrayList(maxNode, entry);
        maxNode = potentialMaxes.get(r.nextInt(2));
      } else if (maxNode.getValue() < entry.getValue()) {
        // if the entry has a greater contribution key, replace the current maxNode with it
        maxNode = entry;
      }
    }

    // Add that node to the graph and return it
    if (!(maxNode == null)) {
      Graph newGraph = new Graph(g);
      newGraph.ad
    }
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
  private double contribution(Graph g, IGNode n) {
    int nodeValue = n.getValue();
    double totalContribution = 0;
    List<IGEdge> edges = g.getEdges();
    
    for (IGEdge edge: edges) {
      int startValue = edge.getStart().getValue();
      int endValue   = edge.getEnd().getValue();
      if (startValue == nodeValue || endValue == nodeValue) {
        totalContribution += edge.weight();
      }
    }

    return totalContribution;
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
