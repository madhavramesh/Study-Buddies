package edu.brown.cs.student.control;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.student.groups.RegisterUser;
import edu.brown.cs.student.groups.NewGroupsDatabase;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONObject;
import spark.*;

/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {

  private static final int DEFAULT_PORT = 4567;
  // list of trigger actions
  private final List<TriggerAction> actionList = List.of(new RegisterUser());
  // GSON instance for handling JSON requests
  private static final Gson GSON = new Gson();

  private static final String PATH_TO_DB = "data/groups_db.sqlite3";
  private static NewGroupsDatabase GROUPS_DATABASE;

  public static NewGroupsDatabase getGroupsDatabase() {
    return GROUPS_DATABASE;
  }

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    new Main(args).run();
  }

  private final String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() throws SQLException, ClassNotFoundException {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    GROUPS_DATABASE = new NewGroupsDatabase(PATH_TO_DB);
    REPL repl = new REPL();

    // register trigger actions to repl
    for (TriggerAction ta : actionList) {
      repl.registerAction(ta);
    }

    repl.run();
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    Spark.exception(Exception.class, new ExceptionPrinter());

    // Setup Spark Routes
    Spark.post("/new-account", new RegisterUserHandler());
//    Spark.post("/validate-account", new LoginUserHandler());

  }

  /**
   * Process user registration requests. JSON objects must be in the form:
   * {
   *   firstname: ...,
   *   lastname: ...,
   *   email: ...,
   *   password: ...,
   * }
   */
  private static class RegisterUserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String firstName = data.getString("firstname");
      String lastName = data.getString("lastname");
      String email = data.getString("email");
      String password = data.getString("password");
      boolean status = GROUPS_DATABASE.registerUser(firstName, lastName, email, password);
      Map<String, Object> variables = ImmutableMap.of(
          "status", status,
          "message", status ? "User successfully registered!" : "Email already taken!");
      return GSON.toJson(variables);
    }
  }

//  /**
//   * Process user login requests. JSON objects must be in the form:
//   * {
//   *   email: ...,
//   *   password: ...,
//   * }
//   */
//  private static class LoginUserHandler implements Route {
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//      JSONObject data = new JSONObject(request.body());
//      String email = data.getString("email");
//      String rawPassword = data.getString("password");
//      String passToken = rawPassword; // TODO: implement hashing & salting
//      int[] results = GROUPS_DATABASE.validateUser(email, passToken);
//      boolean status = results[0] == 1;
//      Person person = null;
//      if (status) {
//        person = GROUPS_DATABASE.getPerson(results[1]);
//      }
//      Map<String, Object> variables = ImmutableMap.of(
//          "status", status,
//          "message", status ? "User successfully logged in!" : "Incorrect credentials!",
//          "firstname", status ? person.getFirstName() : "",
//          "lastname", status ? person.getLastName() : ""
//          // classes
//      );
//      return GSON.toJson(variables);
//    }
//  }
  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
