package edu.brown.cs.student.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Read, Evaluate, Print, Loop: that is what happens when you instantiate
 * this object and call run(). That is the purpose of this class.
 */
public class REPL {
  private List<TriggerAction> registered = new ArrayList<>();

  /**
   * Class constructor.
   */
  public REPL() { }

  /**
   * Method that registers a TriggerAction.
   *
   * @param action A TriggerAction class that represents an action to be performed.
   * @return A boolean that confirms the action was recorded.
   */
  public boolean registerAction(TriggerAction action) {
    return registered.add(action);
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
  public void run() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      String s;
      while ((s = br.readLine()) != null) {
        boolean actionFound = false;
        String[] input = parseLine(s);
        String command = input[0];
        for (TriggerAction action : registered) {
          if (action.name().equals(command)) {
            action.action(input);
            actionFound = true;
          }
        }
        if (!actionFound && (input.length > 0)) {
          System.out.println("ERROR: Not a command");
        }
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }
}
