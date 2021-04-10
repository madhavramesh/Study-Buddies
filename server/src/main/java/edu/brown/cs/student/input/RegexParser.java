package edu.brown.cs.student.input;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility command line parser class. Employs regex for more sophisticated parsing. Regex and
 * parsing structure adapted from [here](https://stackoverflow
 * .com/questions/3366281/tokenizing-a-string-but-ignoring-delimiters-within-quotes).
 */
public final class RegexParser {
  /**
   * Empty constructor for CommandLineParser.
   */
  private RegexParser() {
  }

  /**
   * Parses a command from the command line into its individual arguments.
   *
   * @param command a command line to parse
   * @return the command line, parsed into different arguments.
   */
  public static String[] parseCommand(String command) {
    // regex to select 1) characters inside quotes, or 2) non-white space characters
    String regex = "\"([^\"]*)\"|(\\S+)";
    Matcher m = Pattern.compile(regex).matcher(command);
    List<String> arguments = new LinkedList<>();
    while (m.find()) {
      arguments.add(m.group(1) != null ? m.group(1) : m.group(2));
    }
    return arguments.toArray(String[]::new);
  }

  /**
   * Determines whether a string can be parsed to a double. [NB: I made the regex myself, and
   * it's likely very primitive].
   *
   * @param arg the string to check
   * @return true only if the string can be parsed to a double
   */
  public static boolean canParseDouble(String arg) {
    String regex = "^-?\\d*(\\.\\d*)?$";
    Matcher m = Pattern.compile(regex).matcher(arg);
    return m.find();
  }
}
