package edu.brown.cs.student.DataStructures;

import java.util.Collection;
import java.util.List;

public class IGNode implements Node<IGNode, IGEdge> {
  private final int value;
  private List<IGEdge> connectedEdges;

  IGNode(int id, List<IGEdge> connectedEdges) {
    this.value = id;
    this.connectedEdges = connectedEdges;
  }

  public int getValue() {
    return value;
  }

  @Override
  public Collection getOutEdges() {
    return connectedEdges;
  }

  @Override
  public void addEdge(IGEdge edge) {
    this.connectedEdges.add(edge);
  }

  @Override
  public void removeEdge(IGEdge edge) {
    this.connectedEdges.remove(edge);
  }
}
