package edu.brown.cs.student.groups;

import com.google.common.base.Objects;

/**
 * Represent a class's information.
 */
public class ClassInfo {
  private final int classId;
  private final String className;
  private final String classNumber;
  private final String classDescription;
  private final String classTerm;
  private final String classCode;
  private final int ownerId;

  /**
   * Constructs a ClassInfo instance.
   *
   * @param classId          the class's id
   * @param className        the class's name
   * @param classNumber      the class's number
   * @param classDescription the class's description
   * @param classTerm        the class's term
   * @param ownerId          the class owner's id
   */
  public ClassInfo(int classId, String className, String classNumber, String classDescription,
                   String classTerm, String classCode, int ownerId) {
    this.classId = classId;
    this.className = className;
    this.classNumber = classNumber;
    this.classDescription = classDescription;
    this.classTerm = classTerm;
    this.classCode = classCode;
    this.ownerId = ownerId;
  }

  /**
   * Gets the class's ID.
   *
   * @return the class's ID
   */
  public int getClassId() {
    return classId;
  }

  /**
   * Gets the class's name.
   *
   * @return the class's name
   */
  public String getClassName() {
    return className;
  }

  /**
   * Gets the class owner's ID.
   *
   * @return the class owner's ID
   */
  public int getOwnerId() {
    return ownerId;
  }

  /**
   * Get the class's number.
   *
   * @return the class's number
   */
  public String getClassNumber() {
    return classNumber;
  }

  /**
   * Get the class's description.
   *
   * @return the class's description
   */
  public String getClassDescription() {
    return classDescription;
  }

  /**
   * Get the class's term.
   *
   * @return the class's term
   */
  public String getClassTerm() {
    return classTerm;
  }

  /**
   * Get the class's code.
   *
   * @return the class's code
   */
  public String getClassCode() {
    return classCode;
  }

  /**
   * Calculates the hash of a class info object.
   *
   * @return the object's hash
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(classId, className, classNumber, classDescription, classTerm, ownerId);
  }

  /**
   * Updates equality for ClassInfo objects.
   *
   * @param obj the object to compare
   * @return true only if all object fields are the same
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ClassInfo)) {
      return false;
    }
    ClassInfo otherClassInfo = (ClassInfo) obj;
    return this.classId == otherClassInfo.getClassId()
        && this.className.equals(otherClassInfo.getClassName())
        && this.classNumber.equals(otherClassInfo.getClassNumber())
        && this.classDescription.equals(otherClassInfo.getClassDescription())
        && this.classTerm.equals(otherClassInfo.getClassTerm())
        && this.classCode.equals(otherClassInfo.getClassCode())
        && this.ownerId == otherClassInfo.getOwnerId();
  }

  /**
   * String representation of a ClassInfo object.
   *
   * @return the string representation
   */
  @Override
  public String toString() {
    return String.format("[classId: %d, className: %s, classNumber: %s, classDescription: %s, " +
            "classTerm: %s, ownerId: %d]",
        classId, className, classNumber, classDescription, classTerm, ownerId);
  }
}
