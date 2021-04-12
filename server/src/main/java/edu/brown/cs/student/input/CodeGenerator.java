package edu.brown.cs.student.input;

import java.util.Random;

/**
 * Code generation utility class. Code gracefully plundered from https://www.baeldung
 * .com/java-random-string.
 */
public class CodeGenerator {
  /**
   * Generates a random alphanumeric (uppercase) code of specified length.
   * @param length the code's length
   * @return an alphanumeric uppercase code
   */
  public static String generateCode(int length) {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 90; // letter 'Z'
    return new Random().ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && i <= 90)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString()
        .toUpperCase();
  }
}
