package edu.brown.cs.student.map;

import edu.brown.cs.student.graph.GraphNode;
import edu.brown.cs.student.kdtree.KDTreeable;
import edu.brown.cs.student.util.Check;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Specific type of GraphNode containing an ID, latitude, and longitude.
 */
public class LocationNode implements GraphNode<LocationNode, Way>, KDTreeable<Double> {

  private final int dimensionCount = 2;
  private final String iD;
  private final double latitude;
  private final double longitude;
  private double distance;
  private static final int EARTH_RADIUS = 6371;

  /**
   * Initializes node using ID, latitude, and longitude.
   *
   * @param i ID
   * @param lat Latitude of node
   * @param lon Longitude of node
   */
  public LocationNode(String i, double lat, double lon) {
    iD = i;
    latitude = lat;
    longitude = lon;
  }

  /**
   * @return the latitude and longitude of a node
   */
  @Override
  public Double[] getFeatures() {
    return new Double[]{latitude, longitude};
  }

  /**
   * @return Number of possible coordinate values node has
   */
  @Override
  public int getDimensions() {
    return dimensionCount;
  }

  /**
   * @return Latitude of node
   */
  public double getLatitude() {
    return this.latitude;
  }

  /**
   * @return Longitude of node
   */
  public double getLongitude() {
    return this.longitude;
  }

  /**
   * @return ID of node
   */
  public String getiD() {
    return iD;
  }

  /**
   * Calculates haversine distance between two Nodes.
   *
   * @param a LocationNode 1
   * @param b LocationNode 2
   * @return Distance as a decimal
   */
  public static double haversineDist(LocationNode a, LocationNode b) {
    final double r = 6371;
    double lat1 = Math.toRadians(a.getLatitude());
    double lat2 = Math.toRadians(b.getLatitude());
    double long1 = Math.toRadians(a.getLongitude());
    double long2 = Math.toRadians(b.getLongitude());

    double havLat = Math.pow(Math.sin((lat2 - lat1) / 2), 2);
    double havLong = Math.pow(Math.sin((long2 - long1) / 2), 2);
    double cosMult = Math.cos(lat1) * Math.cos(lat2);

    return 2 * r * Math.asin(Math.sqrt(havLat + (cosMult * havLong)));
  }

  /**
   * Retrieves all edges coming out of node from cache and calculates them otherwise.
   *
   * @return All edges coming out of node
   */
  @Override
  public Collection<Way> getOutEdges() {
    try {
      return SQLQuery.getFromEdgesCache(iD);
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  /**
   * @return New hashcode made up of values for ID, latitude, and longitude
   */
  @Override
  public int hashCode() {
    return Objects.hash(iD, latitude, longitude);
  }

  /**
   * 2 LocationNodes are equal if they have the same ID, latitude, and longitude.
   *
   * @param obj Other LocationNode to compare to
   * @return true if both are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof LocationNode)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    LocationNode ln = (LocationNode) obj;
    boolean equal = iD.equals(ln.getiD())
            && Double.compare(latitude, ln.getLatitude()) == 0
            && Double.compare(longitude, ln.getLongitude()) == 0;

    return equal;
  }

  /**
   * @return Returns the ID of node
   */
  @Override
  public String toString() {
    return iD;
  }

  @Override
  public int compareTo(KDTreeable<Double> other, int dimension) {
    double d = this.getFeatures()[dimension] - other.getFeatures()[dimension];
    if (d < 0) {
      return -1;
    } else if (d == 0) {
      return 0;
    } else {
      return 1;
    }
  }

  @Override
  public Double distanceTo(Double[] other) {
    Double[] features = this.getFeatures();
    double phi = (Check.toRadians(other[0]) - Check.toRadians(features[0])) / 2;
    double lambda = (Check.toRadians(other[1]) - Check.toRadians(features[1])) / 2;
    double phisinsquared = Math.pow(Math.sin(phi), 2);
    double lambdasinsquared = Math.pow(Math.sin(lambda), 2);
    double cosphi1 = Math.cos(Check.toRadians(other[0]));
    double cosphi2 = Math.cos(Check.toRadians(features[0]));
    double insidesquareroot = phisinsquared + cosphi1 * cosphi2 * lambdasinsquared;
    double squareroot = Math.sqrt(insidesquareroot);
    return (2 * EARTH_RADIUS * Math.asin(squareroot));
  }

  @Override
  public Double distanceTo(Double[] other, int axis) {
    //return Math.abs(this.getFeatures()[axis] - other[axis]);
    Double[] features = this.getFeatures();
    double phi = (Check.toRadians(other[0]) - Check.toRadians(features[0])) / 2;
    double lambda = (Check.toRadians(other[1]) - Check.toRadians(features[1])) / 2;
    double phisinsquared = Math.pow(Math.sin(phi), 2);
    double lambdasinsquared = Math.pow(Math.sin(lambda), 2);
    double cosphi1 = Math.cos(Check.toRadians(other[0]));
    double insidesquareroot;
    if (axis == 0) {
      insidesquareroot = phisinsquared;
    } else {
      insidesquareroot = cosphi1 * cosphi1 * lambdasinsquared;
    }
    double squareroot = Math.sqrt(insidesquareroot);
    return (2 * EARTH_RADIUS * Math.asin(squareroot));
  }

  @Override
  public Double getDistance() {
    return distance;
  }

  @Override
  public void setDistance(Double distance) {
    this.distance = distance;
  }

  @Override
  public String getID() {
    return iD;
  }
}
