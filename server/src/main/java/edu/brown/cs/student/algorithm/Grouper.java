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
  private final Graph graph;
  private Graph decrementGraph;
  private static Random r = new Random();

  /**
   * Constructor for the Grouper class
   * @param inputGraph Graph to be referenced
   */
  public Grouper(Graph inputGraph) {
    this.graph = new Graph(inputGraph);
    this.decrementGraph = new Graph(inputGraph);
    for (IGNode node: graph.getNodes()) {
      node.setWeightMap();
    }
  }

  public List<Set<IGNode>> makeGroups(Graph g, int groupSize) throws Exception {
    List<Set<IGNode>> groups = new ArrayList<>();

    Graph workingGraph = new Graph(g);

    // Make groups while there are still enough people left
    while (groupSize <= workingGraph.getNodes().size()) {

      // Add the newest group to the groups list
      Graph newGroup = findGroup(workingGraph, groupSize);
      groups.add(newGroup.getNodes());
      // Remove the nodes from that group from the working graph
      for (IGNode n: newGroup.getNodes()) {
        workingGraph.removeNode(n);
      }

      // Remove the edges
      Set<IGEdge> edgesToRemove = new HashSet<>(newGroup.getEdges());
      Set<IGNode> nodesLeft = workingGraph.getNodes();
      for (IGEdge e: edgesToRemove) {
        if (!(nodesLeft.contains(e.getStart()) || nodesLeft.contains(e.getEnd()))) {
          workingGraph.removeEdge(e);
        }
      }
      decrementGraph = workingGraph;
    }

    // Assign groups to the leftovers

    /*
    // TODO: Assign leftover to groups that would have most value added
    for (IGNode leftOverNode: workingGraph.getNodes()) {
      Set<IGNode> bestGroup = null;
      for (Set<IGNode> group: groups) {
        if (group.size() == groupSize) {
          if (bestGroup == null) {
            bestGroup = group;
          } else {

          }
        }
      }
    }
     */
    Set<IGNode> leftOverNodes = workingGraph.getNodes();
    if (groups.size() <= leftOverNodes.size()) {
      groups.add(leftOverNodes);
    } else {
      int count = 0;
      for (IGNode n: leftOverNodes) {
        groups.get(groups.size() - 1 - count).add(n);
        count += 1;
      }
    }
    
    System.out.println("----------------------------");
    System.out.println("----------------------------");
    System.out.println("Created groups of size " + groupSize + " are");
    for (Set<IGNode> group: groups) {
      for (IGNode n: group) {
        System.out.print(n.getValue() + " ");
      }
      System.out.println();
      for (IGNode n: group) {
        for (IGNode n2: group) {
          if (!(n2.equals(n))) {
            System.out.println("weight from " + n.getValue() + " to " + n2.getValue() + " is " + n.weightTo(n2));
          }
        }
      }
      System.out.println();
    }
    System.out.println("----------------------------");
    System.out.println("----------------------------");


    return groups;
  }

  /**
   * Finds a single group with max edge weights of size n.
   * @param g Graph to be referenced
   * @param groupSize Group size to find
   */
  public Graph findGroup(Graph g, int groupSize) throws Exception {

    // make a copy
    Graph workingGraph = new Graph(g);


    // 0)
    // Change the value of contributions of each node from "null" to an actual value.
    Graph initializedGraph = setContributions(workingGraph);


    // 1)
    // Create an initial candidate solution graph by removing the
    // lowest contribution nodes to get a graph of size groupSize.
    Graph candidateSolution = deconstruct(initializedGraph, initializedGraph.getNodes().size() - groupSize);


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
    // TODO:
    int maxRuns  = 20 * g.getEdges().size();
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
    while (runCount <= maxRuns) {
      runCount += 1;

      // 5)
      // Randomly drop n variables from the candidate solution
      Graph incompleteSolution = randomDrop(candidateSolution, randomDropNum);


      // 6)
      // Greedily add n variables to the candidate solution
      Graph completeSolution = greedyAdd(incompleteSolution, randomDropNum);


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
   * Randomly removes n nodes from the graph's node list. Note: leaves edges untouched.
   * Important: mutates original graph nodes list.
   * @param g Graph to remove nodes from
   * @param randomDropNum How many nodes to randomly remove.
   * @return
   */
  protected Graph randomDrop(Graph g, int randomDropNum) {
    Graph graphCopy = new Graph(g);
    Set<IGNode> nodesCopy = graphCopy.getNodes();
    List<IGNode> toRemove = new ArrayList<>(nodesCopy).subList(0, randomDropNum);
    for (IGNode nodeToRemove: toRemove) {
      // Remove the node
      nodesCopy.remove(nodeToRemove);

      // Update the contributions of the other nodes
      for (IGNode n: nodesCopy) {
        n.setContribution(n.getContribution() - n.weightTo(nodeToRemove));
      }

    }

    return graphCopy;
  }

  /**
   * Sets the contributions of each node in the graph.
   * @param g Graph to set contributions for.
   * @return
   */
  protected Graph setContributions(Graph g) {
    Graph graphCopy = new Graph(g);
    Set<IGNode> nodes = graphCopy.getNodes();
    for (IGNode node: nodes) {
      double contribution = calculateContribution(g, node);
      node.setContribution(contribution);
    }
    return graphCopy;
  }

  /**
   * Determines the contribution of a node.
   * The contribution of a node is the sum of the edge weights that contain that node in the graph.
   * @return the contribution
   */
  protected double calculateContribution(Graph g, IGNode n) {
    int nodeValue = n.getValue();
    double totalContribution = 0;
    Set<IGEdge> edges = g.getEdges();
    Set<IGNode> nodes = g.getNodes();

    for (IGEdge edge: edges) {
      IGNode start   = edge.getStart();
      IGNode end     = edge.getEnd();
      int startValue = start.getValue();
      int endValue   = end.getValue();
      if ((startValue == nodeValue && nodes.contains(end)) || (endValue == nodeValue && nodes.contains(start))) {
        totalContribution += edge.getWeight();
      }
    }

    return totalContribution;
  }

  /**
   * Removes the n lowest contribution elements from the graph.
   * @param g the graph to be referenced
   * @param n how many elements to remove
   */
  protected Graph deconstruct(Graph g, int n) throws Exception {
    Graph finalCopy = new Graph(g);

    for (int i = 0; i < n; i++) {
      Set<IGNode> nodes = finalCopy.getNodes();

      // Find the minimum contribution node
      IGNode minNode = null;
      for (IGNode node : nodes) {
        if (minNode == null) {
          minNode = node;
        } else {
          double oldNodeContribution = minNode.getContribution();
          double newNodeContribution = node.getContribution();

          if (newNodeContribution < oldNodeContribution) {
            // If the new one is less, update the minNode
            minNode = node;
          } else if (newNodeContribution == oldNodeContribution) {
            // Since they are equally minimum, randomly choose one.
            List<IGNode> randomList = Lists.newArrayList(minNode, node);
            int randIndex = r.nextInt(2);
            minNode = randomList.get(randIndex);
          }
        }
      }

      // Remove it and update the contributions of the other nodes
      if (!(minNode == null)) {
        // remove node
        Graph graphCopy = new Graph(finalCopy);
        graphCopy.removeNode(minNode);
        Set<IGNode> graphCopyNodes = graphCopy.getNodes();
        graphCopyNodes.remove(minNode);

        // Update other contributions.
        // We only want to update the nodes that are in the subgraph in graphCopyNodes.
        // Note that connected edges contains edges to ALL nodes (not just the ones in subgraph).
        for (IGNode node: graphCopyNodes) {
          node.setContribution(node.getContribution() - node.weightTo(minNode));
        }
        finalCopy =  graphCopy;
      } else {
        // If minNode is still null, that means the input graph was empty.
        // Therefore, we should throw an error
        throw new Exception("Input graph was empty");
      }
    }

    return finalCopy;
  }

  /**
   * Runs the construction algorithm "numToAdd" times.
   * @param g the reference graph/graph to modify
   * @param numToAdd how many elements to add to the partial graph
   * @return
   */
  protected Graph greedyAdd(Graph g, int numToAdd) throws Exception {
    Graph finalAnswer = new Graph(g);
    for (int i = 0; i < numToAdd; i++) {
      Graph newAnswer = construct(finalAnswer);
      finalAnswer = newAnswer;
    }
    return finalAnswer;
  }

  /**
   * Adds the highest contribution element that is not already in the graph
   * @param g graph to be referenced
   */
  protected Graph construct(Graph g) throws Exception {

    // TODO: See if you can potentially make this more efficient by not recalculating everytime.
    // For each node not in the graph, calculate its contribution to the given subgraph g
    // and store the result in a map from the node to the contribution.
    Graph gCopy = new Graph(g);
    Set<IGNode> nodesNotInG = nodesNotInGraph(g);
    Map<IGNode, Double> nodeContributionMap = new HashMap<>();
    for (IGNode n: nodesNotInG) {
      double contribution = calculateContribution(g, n);
      nodeContributionMap.put(n, contribution);
    }

    // Find the node with the maximum contribution
    Map.Entry<IGNode, Double> maxNodeMap = null;
    for (Map.Entry<IGNode, Double> entry: nodeContributionMap.entrySet()) {
      if (maxNodeMap == null) {
        // if there hasn't been a maxNode found yet, assign the entry to it
        maxNodeMap = entry;
      } else if (maxNodeMap.getValue() < entry.getValue()) {
        // if the entry has a greater contribution key, replace the current maxNode with it
        maxNodeMap = entry;
      } else if (maxNodeMap.getValue().equals(entry.getValue())) {
        // if there is a tie, randomly choose one
        List<Map.Entry<IGNode, Double>> potentialMaxes = Lists.newArrayList(maxNodeMap, entry);
        maxNodeMap = potentialMaxes.get(r.nextInt(2));
      }
    }

    // Add that node to the graph and return it and also update the contributions of each node
    // in the graph
    if (!(maxNodeMap == null)) {
      Graph newGraph = new Graph(g);
      Set<IGNode> newGraphNodes = newGraph.getNodes();
      IGNode maxNode = maxNodeMap.getKey();

      // Update the contributions of the nodes already in the subgraph by adding the new contribution to them
      for (IGNode node: newGraphNodes) {
        node.setContribution(node.getContribution() + maxNode.weightTo(node));
      }

      newGraph.addNode(maxNode);
      return newGraph;
    } else {
      // If maxNode is still null, that means the input graph g and the original large graph were the same size.
      // This shouldn't happen because we always deconstruct first, so we throw an error.
      throw new Exception("Input graph and original graph are same size.");
    }
  }

  /**
   * Returns the graph with the greater contribution values.
   * @param g1
   * @param g2
   * @return
   */
  private Graph bestGraph(Graph g1, Graph g2) {
    double g1Score = graphScore(g1);
    double g2Score = graphScore(g2);

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
  protected double graphScore(Graph g) {
    double score = 0;
    Set<IGNode> nodes = g.getNodes();

    for (IGNode node: nodes) {
      score = score + node.getContribution();
    }

    return score / 2;
  }

  /**
   * Returns the nodes that are not in the full original complete graph
   * @param g The subset graph
   * @return The list of nodes that are not in the subset graph
   */
  protected Set<IGNode> nodesNotInGraph(Graph g) {
    Set<IGNode> allNodes = new HashSet<IGNode>(decrementGraph.getNodes());
    Set<IGNode> nodesInSubgraph = g.getNodes();
    allNodes.removeAll(nodesInSubgraph);
    return allNodes;
  }

}
