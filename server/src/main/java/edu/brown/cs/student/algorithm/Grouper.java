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
  private final Graph<IGNode, IGEdge> graph;
  private static Random r = new Random();

  /**
   * Constructor for the Grouper class
   * @param graph Graph to be referenced
   */
  public Grouper(Graph<IGNode, IGEdge> graph) {
    this.graph = graph;
  }

  /**
   * Finds a single group with max edge weights of size n.
   * @param g Graph to be referenced
   * @param groupSize Group size to find
   */
  public Graph<Node, IGEdge> findGroup(Graph<IGNode, IGEdge> g, int groupSize) throws Exception {
    // 0)
    // Change the value of contributions of each node from "null" to an actual value.
    Graph initializedGraph = setContributions(g);

    // 1)
    // Create an initial candidate solution graph by removing the
    // lowest contribution nodes to get a graph of size groupSize.
    Graph candidateSolution = deconstruct(initializedGraph, graph.getNodes().size() - groupSize);


    // 2)
    // (Optional)
    // localSearch()


    // 3)
    // Have a variable that stores the best-so-far candidate graph.
    Graph bestSoFar = candidateSolution;


    // 4)
    // Loop through while the termination condition hasn't been met.
    // Termination condition ie iterations or computation time.
    // - termination condition for now will be 100 run throughs -
    // TODO: talk about termination condition and decide if it makes sense
    int runCount = 0;
    int maxRuns  = 100;
    // Also, initialize the randomDropNum. For each run through, a random number of nodes will be dropped.
    // TODO: Research/test ideal random drop number
    // TODO: Consider if it should be fixed or variable over the run throughs!!
    // Temporary solution is assuming groups of 5 and we'll drop 2 each time.
    int randomDropNum;
    if (groupSize > 2) {
      randomDropNum = 2;
    } else {
      randomDropNum = 1;
    }

    // Enter the while-loop.
    // Basically, it first deconstructs the partial solution randomly.
    // Then, it reconstructs the solution through a greedy solution.
    // It then checks if the greedy solution is better than the previous solution
    // and always stores the best-so-far solution.
    while (!(runCount >= maxRuns)) {
      runCount += 1;

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

  private Graph randomDrop(Graph g, int randomDropNum) {
    Graph graphCopy = new Graph(g);
    List<IGNode> nodesCopy = graphCopy.getNodes();

    Collections.shuffle(nodesCopy);
    nodesCopy.subList(0, nodesCopy.size() - randomDropNum).clear();
    return graphCopy;
  }

  private Graph setContributions(Graph<IGNode, IGEdge> g) {
    Graph graphCopy = new Graph(g);
    List<IGNode> nodes = graphCopy.getNodes();
    for (IGNode node: nodes) {
      double contribution = calculateContribution(g, node);
      node.setContribution(contribution);
    }
    return graphCopy;
  }

  /**
   * Runs the construction algorithm "numToAdd" times.
   * @param g the reference graph/graph to modify
   * @param numToAdd how many elements to add to the partial graph
   * @param counter the counter that tracks the recursion
   * @return
   */
  private Graph<IGNode, IGEdge> greedyAdd(Graph<IGNode, IGEdge> g, int numToAdd, int counter) throws Exception {
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
  private Graph<IGNode, IGEdge> construct(Graph<IGNode, IGEdge> g) throws Exception {

    // TODO: See if you can potentially make this more efficient by not recalculating everytime.
    // For each node not in the graph, calculate its contribution to the given subgraph g
    // and store the result in a map from the node to the contribution.
    List<IGNode> nodesNotInG = nodesNotInGraph(g);
    Map<Node, Double> nodeContributionMap = new HashMap<>();
    for (IGNode n: nodesNotInG) {
      double contribution = calculateContribution(g, n);
      nodeContributionMap.put(n, contribution);
    }

    // Find the node with the maximum contribution
    Map.Entry<Node, Double> maxNode = null;
    for (Map.Entry<Node, Double> entry: nodeContributionMap.entrySet()) {
      if (maxNode == null) {
        // if there hasn't been a maxNode found yet, assign the entry to it
        maxNode = entry;
      } else if (maxNode.getValue() < entry.getValue()) {
        // if the entry has a greater contribution key, replace the current maxNode with it
        maxNode = entry;
      } else if (maxNode.getValue() == entry.getValue()) {
        // if there is a tie, randomly choose one
        List<Map.Entry<Node, Double>> potentialMaxes = Lists.newArrayList(maxNode, entry);
        maxNode = potentialMaxes.get(r.nextInt(2));
      }
    }

    // Add that node to the graph and return it and also update the contributions of each node
    // in the graph
    if (!(maxNode == null)) {
      Graph newGraph = new Graph(g);
      List<IGNode> newGraphNodes = newGraph.getNodes();
      double maxContribution = maxNode.getValue();

      // Update the contributions of the nodes already in the subgraph by adding the new contribution to them
      for (IGNode node: newGraphNodes) {
        node.setContribution(node.getContribution() + maxContribution);
      }

      newGraph.addNode(maxNode.getKey());
      return newGraph;
    } else {
      // If maxNode is still null, that means the input graph g and the original large graph were the same size.
      // This shouldn't happen because we always deconstruct first, so we throw an error.
      throw new Exception("Input graph and original graph are same size.");
    }
  }

  /**
   * Removes the n lowest contribution elements from the graph.
   * @param g the graph to be referenced
   * @param n how many elements to remove
   */
  private Graph<IGNode, IGEdge> deconstruct(Graph<IGNode, IGEdge> g, int n) throws Exception {
    List<IGNode> nodes = g.getNodes();

    // Find the minimum contribution node
    IGNode minNode = null;
    for (IGNode node: nodes) {
      double oldNodeContribution = minNode.getContribution();
      double newNodeContribution = node.getContribution();

      if (minNode == null) {
        minNode = node;
      } else if (newNodeContribution < oldNodeContribution) {
        minNode = node;
      } else if (newNodeContribution == oldNodeContribution) {
        // Since they are equally minimum, randomly choose one.
        List<IGNode> randomList = Lists.newArrayList(minNode, node);
        minNode = randomList.get(r.nextInt(1));
      }
    }

    // Remove it and update the contributions of the other nodes
    if (!(minNode == null)) {
      double minContribution = minNode.getContribution();

      Graph graphCopy = new Graph(g);
      graphCopy.removeNode(minNode);

      List<IGNode> nodesCopy = graphCopy.getNodes();
      for (IGNode node: nodesCopy) {
        node.setContribution(node.getContribution() - minContribution);
      }

      return graphCopy;
    } else {
      // If minNode is still null, that means the input graph was empty.
      // Therefore, we should throw an error
      throw new Exception("Input graph was empty");
    }
  }

  /**
   * Determines the contribution of a node.
   * The contribution of a node is the sum of the edge weights that contain that node in the graph.
   * @return the contribution
   */
  private double calculateContribution(Graph g, IGNode n) {
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
   * Returns the graph with the greater contribution values.
   * @param g1
   * @param g2
   * @return
   */
  private Graph<IGNode, IGEdge> bestGraph(Graph<IGNode, IGEdge> g1, Graph<IGNode, IGEdge> g2) {
    double g1Score = graphHeuristic(g1);
    double g2Score = graphHeuristic(g2);

    if (g1Score > g2Score) {
      return g1;
    } else if (g2Score > g1Score) {
      return g2;
    } else {
      // in case of a tie, randomly choose
      List<Graph> randomList = Lists.newArrayList(g1, g2);
      return randomList.get(r.nextInt(2));
    }
  }

  /**
   * Given a graph with IGNodes, it sums all the contribution values and divides by 2.
   * This gives a good estimate of how well a graph is composed of high contributing nodes.
   * @return Sum of all contributions of the nodes divided by 2
   */
  private double graphHeuristic(Graph<IGNode, IGEdge> g) {
    double heuristic = 0;
    List<IGNode> nodes = g.getNodes();

    for (IGNode node: nodes) {
      heuristic = heuristic + node.getContribution();
    }

    return heuristic / 2;
  }

  /**
   * Returns the nodes that are not in the full original complete graph
   * @param g The subset graph
   * @return The list of nodes that are not in the subset graph
   */
  private List<IGNode> nodesNotInGraph(Graph<IGNode, IGEdge> g) {
    List<IGNode> allNodes = new ArrayList<IGNode>(graph.getNodes());
    List<IGNode> nodesInSubgraph = g.getNodes();
    allNodes.removeAll(nodesInSubgraph);
    return allNodes;
  }

}
