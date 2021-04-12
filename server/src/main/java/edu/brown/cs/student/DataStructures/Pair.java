package edu.brown.cs.student.DataStructures;

/**
 * Barebones Pair class.
 *
 * @param <T> the first element's type
 * @param <U> the second element's type
 */
public class Pair<T, U> {
  private T first;
  private U second;

  /**
   * Default constructor.
   *
   * @param first  the first element
   * @param second the second element
   */
  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Gets the first element.
   *
   * @return the first element
   */
  public T getFirst() {
    return first;
  }

  /**
   * Gets the second element.
   *
   * @return the second element
   */
  public U getSecond() {
    return second;
  }

  /**
   * Sets the first element.
   *
   * @param first the new first element
   */
  public void setFirst(T first) {
    this.first = first;
  }

  /**
   * Sets the second element.
   *
   * @param second the new second element
   */
  public void setSecond(U second) {
    this.second = second;
  }
}
