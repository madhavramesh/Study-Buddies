package edu.brown.cs.student.control;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.student.DataStructures.Pair;
import edu.brown.cs.student.groups.ClassInfo;
import edu.brown.cs.student.groups.DBCode;
import edu.brown.cs.student.groups.NewGroupsDatabase;
import edu.brown.cs.student.groups.PersonInfo;
import edu.brown.cs.student.groups.testcommands.CreateClassCommand;
import edu.brown.cs.student.groups.testcommands.GetAllClassesCommand;
import edu.brown.cs.student.groups.testcommands.GetClassesWithOwnerIdCommand;
import edu.brown.cs.student.groups.testcommands.GetEnrollmentsCommand;
import edu.brown.cs.student.groups.testcommands.GetPersonInfoCommand;
import edu.brown.cs.student.groups.testcommands.JoinClassCommand;
import edu.brown.cs.student.groups.testcommands.RegisterUserCommand;
import edu.brown.cs.student.groups.testcommands.ValidateUserCommand;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {

  private static final int DEFAULT_PORT = 4567;
  // list of trigger actions
  private final List<TriggerAction> actionList = List.of(
      new RegisterUserCommand(),
      new ValidateUserCommand(),
      new GetAllClassesCommand(),
      new GetClassesWithOwnerIdCommand(),
      new GetEnrollmentsCommand(),
      new CreateClassCommand(),
      new JoinClassCommand(),
      new GetPersonInfoCommand()
  );
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
  public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
    new Main(args).run();
  }

  private final String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() throws SQLException, ClassNotFoundException, IOException {
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
    Spark.post("/register_account", new RegisterUserHandler());
    Spark.post("/validate_account", new LoginUserHandler());
    Spark.get("/get_all_classes", new GetAllClasses());
    Spark.get("/get_classes_with/:owner_id", new GetClassesWithOwnerId());
    Spark.get("/get_enrollments/:id", new GetEnrollments());
    Spark.post("/create_class", new CreateClass());
    Spark.post("/join_class", new JoinClass());
    Spark.get("/person_info/:id", new GetPersonInfo());
  }

  /**
   * Process user registration requests. JSON objects must be in the form:
   * {
   * firstname: ...,
   * lastname: ...,
   * email: ...,
   * password: ...,
   * password2: ...,
   * }.
   * The returned JSON object will have the form:
   * {
   * status: [status of registration command],
   * message: [message explaining registration status]
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
      String password2 = data.getString("password2");
      DBCode code = password.equals(password2)
          ? GROUPS_DATABASE.registerUser(firstName, lastName, email, password)
          : DBCode.INVALID_PASSWORD;
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage()
      );
      if (code.getCode() == 0) {
        response.redirect("/login");
      }
      return GSON.toJson(variables);
    }
  }

  /**
   * Process user login requests. JSON objects must be in the form:
   * {
   * email: ...,
   * password: ...,
   * }.
   * The returned JSON object will have the form:
   * {
   * status: [status of validation request],
   * message: [message explaining login status],
   * }
   */
  private static class LoginUserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String email = data.getString("email");
      String password = data.getString("password");
      Pair<Integer, DBCode> result = GROUPS_DATABASE.validateUser(email, password);
      DBCode code = result.getSecond();
      if (code.getCode() == 0) {
        int id = result.getFirst();
        request.session().attribute("user_id", id);
        response.redirect("/dashboard");
      }
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage()
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Retrieves classes info. The returned JSON object will have the form:
   * {
   * classes: [list of all classes]
   * }
   */
  private static class GetAllClasses implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      List<ClassInfo> classes = GROUPS_DATABASE.getAllClasses();
      Map<String, Object> variables = ImmutableMap.of("classes", classes);
      return GSON.toJson(variables);
    }
  }

  /**
   * Retrieves all classes owned by someone. The returned JSON object will have the form:
   * {
   * classes: [list of all classes owned by someone]
   * }
   */
  private static class GetClassesWithOwnerId implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      List<ClassInfo> classes =
          GROUPS_DATABASE.getClassesByOwnerId(Integer.parseInt(request.params(":owner_id")));
      Map<String, Object> variables = ImmutableMap.of("classes", classes);
      return GSON.toJson(variables);
    }
  }

  /**
   * Retrieves all enrollments of someone. The returned JSON object will have the form:
   * {
   * classes: [list of all classes owned by someone]
   * }
   */
  private static class GetEnrollments implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      List<ClassInfo> classes =
          GROUPS_DATABASE.getEnrollments(Integer.parseInt(request.params(":id")));
      Map<String, Object> variables = ImmutableMap.of("classes", classes);
      return GSON.toJson(variables);
    }
  }

  /**
   * //TODO: Redirect to newly created class page
   * Processes class creation requests. JSON objects must have the form:
   * {
   * class_name: ...,
   * class_number: ...,
   * class_description: ...,
   * class_term: ...,
   * class_code: ...,
   * owner_id: ...,
   * }
   * The returned JSON object will have the form:
   * {
   * status: [status of class creation request],
   * message: [message explaining class creation status],
   * }
   */
  private static class CreateClass implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String className = data.getString("class_name");
      String classNumber = data.getString("class_number");
      String classDescription = data.getString("class_description");
      String classTerm = data.getString("class_term");
      String classCode = data.getString("class_code");
      int ownerId = data.getInt("owner_id");
      DBCode code = GROUPS_DATABASE.createClass(className, classNumber, classDescription,
          classTerm, classCode, ownerId);
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage()
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * //TODO: Redirect to newly joined class page
   * Attempts to join a class. JSON objects must have the form:
   * {
   * id: ...,
   * class_id: ...,
   * class_code: ...,
   * }
   * The returned JSON object will have the form:
   * {
   * status: [status of class join request],
   * message: [message explaining class join status],
   * }
   */
  private static class JoinClass implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      int id = data.getInt("id");
      int classId = data.getInt("class_id");
      String classCode = data.getString("class_code");
      DBCode code = GROUPS_DATABASE.joinClass(id, classId, classCode);
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage()
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * // TODO: Integrate getting the enrollments
   * Gets the person's info. The returned JSON object will have the form:
   * {
   *   status: [the status of the person fetching operation],
   *   message: [message explaining person fetching status],
   *   id: person's id,
   *   first_name: person's first name,
   *   last_name: person's last name,
   *   email: person's email,
   * }
   * The last 3 will be the empty string if the query fails.
   */
  private static class GetPersonInfo implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      int id = Integer.parseInt(request.params(":id"));
      Pair<DBCode, PersonInfo> result = GROUPS_DATABASE.getPersonInfo(id);
      DBCode code = result.getFirst();
      PersonInfo personInfo = result.getSecond();
      boolean success = code.getCode() == 0;
      Map<String, Object> variables = Map.of(
          "status", code.getCode(),
          "message", code.getMessage(),
          "id", id,
          "first_name", success ? personInfo.getFirstName() : "",
          "last_name", success ? personInfo.getLastName() : "",
          "email", success ? personInfo.getEmail() : ""
      );
      return GSON.toJson(variables);
    }
  }

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
