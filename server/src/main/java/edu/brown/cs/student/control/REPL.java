package edu.brown.cs.student.control;

import edu.brown.cs.student.input.RegexParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read, Evaluate, Print, Loop: that is what happens when you instantiate
 * this object and call run(). That is the purpose of this class.
 */
public class REPL {
  private final List<TriggerAction> registered = new ArrayList<>();

  /**
   * Class constructor.
   */
  public REPL() {
  }

  /**
   * Method that registers a TriggerAction.
   *
   * @param action A TriggerAction class that represents an action to be performed.
   */
  public void registerAction(TriggerAction action) {
    registered.add(action);
  }

  /**
   * Parses a line of input specifically for this project and returns a string array.
   * The zero index should be a command,
   * the first index should be either the radius or neighbor,
   * and the remaining index should either be coordinate points or a star name.
   *
   * @param inputLine A line of input from the terminal
   * @return An array of the arguments
   */
  public String[] parseLine(String inputLine) {
    List<String> results = new ArrayList<>();
    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(inputLine);
    while (m.find()) {
      results.add(m.group(1));
    }

    String[] in = new String[results.size()];

    for (int i = 0; i < results.size(); i++) {
      in[i] = results.get(i);
    }
    return in;
  }

  /**
   * runs the REPL.
   */
  public void run() throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String line;
    while ((line = in.readLine()) != null) {
      String[] input = RegexParser.parseCommand(line);
      if (input.length > 0) {
        String command = input[0];
        TriggerAction trigger =
            registered.stream()
                .filter(triggerAction -> command.contains(triggerAction.name()))
                .findAny()
                .orElse(null);
        if (trigger != null) {
          try {
            trigger.action(input);
          } catch (Exception e) {
            int errorStart = e.getMessage().indexOf("ERROR:");
            System.err.println(errorStart > -1
                ? e.getMessage().substring(errorStart)
                : "ERROR: Something went wrong with the command " + command);
          }
        } else {
          System.err.println("ERROR: Command not recognized");
        }
      } else {
        System.err.println("Enter a command: ");
        for (TriggerAction ta : registered) {
          System.err.println("- " + ta.name());
        }
      }
    }
  }


}
