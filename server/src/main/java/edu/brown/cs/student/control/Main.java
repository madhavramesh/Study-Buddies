package edu.brown.cs.student.control;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.student.DataStructures.Pair;
import edu.brown.cs.student.groups.ClassInfo;
import edu.brown.cs.student.groups.DBCode;
import edu.brown.cs.student.groups.NewGroupsDatabase;
import edu.brown.cs.student.groups.PersonInfo;
import edu.brown.cs.student.groups.PersonPreferences;
import edu.brown.cs.student.heuristic.HeuristicUtils;
import edu.brown.cs.student.groups.testcommands.CreateClassCommand;
import edu.brown.cs.student.groups.testcommands.GetAllClassesCommand;
import edu.brown.cs.student.groups.testcommands.GetClassesWithOwnerIdCommand;
import edu.brown.cs.student.groups.testcommands.GetEnrollmentsCommand;
import edu.brown.cs.student.groups.testcommands.GetPersonInfoCommand;
import edu.brown.cs.student.groups.testcommands.GetPersonsInClassCommand;
import edu.brown.cs.student.groups.testcommands.JoinClassCommand;
import edu.brown.cs.student.groups.testcommands.RegisterUserCommand;
import edu.brown.cs.student.groups.testcommands.ValidateUserCommand;
import edu.brown.cs.student.input.ReCAPTCHAVerification;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {

  private static final String RECAPTCHA_SECRET_KEY = "6LfWUKQaAAAAALzRNvkz7PQtNeVml5WIprs-BzlA";

  // list of trigger actions for testing
  private final List<TriggerAction> actionList = List.of(
      new RegisterUserCommand(),
      new ValidateUserCommand(),
      new GetAllClassesCommand(),
      new GetClassesWithOwnerIdCommand(),
      new GetEnrollmentsCommand(),
      new CreateClassCommand(),
      new JoinClassCommand(),
      new GetPersonInfoCommand(),
      new GetPersonsInClassCommand()
  );
  // GSON instance for handling JSON requests
  private static final Gson GSON = new Gson();

  private static final String PATH_TO_DB = "data/groups_db.sqlite3";

  private static NewGroupsDatabase GROUPS_DATABASE;

  private static HeuristicUtils heuristic;

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

  private Main(String[] args) {
  }

  private void run() throws SQLException, ClassNotFoundException, IOException {
    runSparkServer();

    GROUPS_DATABASE = new NewGroupsDatabase(PATH_TO_DB);
    heuristic = new HeuristicUtils();
    REPL repl = new REPL();

    // register trigger actions to repl
    for (TriggerAction ta : actionList) {
      repl.registerAction(ta);
    }

    repl.run();
  }

  private void runSparkServer() {
    Spark.port(4567);
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
    // account setup
    Spark.post("/register_account", new RegisterUserHandler());
    Spark.post("/validate_account", new LoginUserHandler());
    Spark.post("/delete_account", new DeleteUserHandler());
    // join/create/get all class info handlers
    Spark.get("/get_all_classes", new GetAllClasses());
    Spark.get("/get_classes_with/:owner_id", new GetClassesWithOwnerId());
    Spark.get("/get_class_with/:class_id", new GetClassWithClassId());
    Spark.get("/get_enrollments/:id", new GetEnrollments());
    Spark.post("/create_class", new CreateClass());
    Spark.post("/join_class", new JoinClass());
    Spark.post("/leave_class", new LeaveClass());
    Spark.post("/delete_class", new DeleteClass());
    // person info
    Spark.get("/person_info/:id", new GetPersonInfo());
    // specific class handlers
    Spark.get("/get_persons_in/:class_id", new GetPersonsInClass());
    Spark.get("/get_person_pref_in/:class_id/:id", new GetPersonPrefInClass());
    Spark.get("/get_person_prefs_in/:class_id", new GetPersonsPrefsInClass());
    Spark.post("/set_preferences", new SetPreferences());
    Spark.get("/form_groups/:class_id/:group_size", new FormGroups());
  }

  /**
   * Process user registration requests. JSON objects must be in the form:
   * {
   * firstname: ...,
   * lastname: ...,
   * email: ...,
   * password: ...,
   * password2: ...,
   * token: ...,
   * }.
   * The returned JSON object will have the form:
   * {
   * status: [status of registration command],
   * first_name: [message if first name validation failed],
   * last_name: [message if last name validation failed],
   * email: [message if email validation failed],
   * password: [message if password is invalid],
   * password2: [message if confirm password is invalid]
   * }
   */
  private static class RegisterUserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      Map<String, String> messages = new HashMap<>();
      // if first name is empty, set the error message
      String firstName = data.getString("firstname");
      messages.put("first_name", firstName.isEmpty() ? "Enter a first name!" : "");
      // if last name is empty, set the error message
      String lastName = data.getString("lastname");
      messages.put("last_name", lastName.isEmpty() ? "Enter a last name!" : "");
      // if email is empty, set the error message
      String email = data.getString("email");
      messages.put("email", email.isEmpty() ? "Enter an email!" : "");
      // if password is <6 characters, set the error message
      String password = data.getString("password");
      String password2 = data.getString("password2");
      messages
          .put("password", password.length() < 6 ? "Password must be 6 or more characters!" : "");
      messages.put("password2", password2.isEmpty() ? "Confirm your password!" : "");
      // if passwords don't match, set error message; otherwise, register the user
      DBCode code = password.equals(password2)
          ? GROUPS_DATABASE.registerUser(firstName, lastName, email, password)
          : DBCode.PASSWORD_MISMATCH;
      // if error code is related to password, means password mismatch, so set that message
      if (code.getCode() == 3) {
        messages.put("password", code.getMessage());
        messages.put("password2", code.getMessage());
        // otherwise, an error occurred with the email
      } else if (code.getCode() == 2) {
        messages.put("email", code.getMessage());
      }
      String reCAPTCHAToken = data.getString("token");
      messages
          .put("token", !ReCAPTCHAVerification.isCaptchaValid(RECAPTCHA_SECRET_KEY, reCAPTCHAToken)
              ? "Confirm that you're not a robot!" : "");

      Map<String, Object> variables = Map.of(
          "status", code.getCode(),
          "first_name", messages.get("first_name"),
          "last_name", messages.get("last_name"),
          "email", messages.get("email"),
          "password", messages.get("password"),
          "password2", messages.get("password2"),
          "token", messages.get("token")
      );
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
      boolean success = code.getCode() == 0;
      Pair<DBCode, PersonInfo> personInfo = success
        ?  GROUPS_DATABASE.getPersonInfo(result.getFirst()) : null;
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage(),
          "id", success ? result.getFirst() : -1,
          "first_name", success ? personInfo.getSecond().getFirstName() : "",
          "last_name", success ? personInfo.getSecond().getLastName() : ""
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Deletes a user from the database.JSON objects must be in the form:
   * {
   *   id: ...,
   *   password: ...,
   * }.
   * The returned JSON object will have the form:
   * {
   *   status: [status of delete request],
   *   message: [message explaining delete status],
   * }
   */
  private static class DeleteUserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      int id = Integer.parseInt(data.getString("id"));
      String password = data.getString("password");
      DBCode code = GROUPS_DATABASE.deleteUser(id, password);
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
   * Retrieves the class with specified ID. The returned JSON object will have the form:
   * {
   * class: the ClassInfo
   * }
   */
  private static class GetClassWithClassId implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      ClassInfo c =
          GROUPS_DATABASE.getClassByClassId(Integer.parseInt(request.params(":class_id")));
      Map<String, Object> variables =
          Map.of("class_name", c.getClassName(), "class_number", c.getClassNumber(),
              "class_description", c.getClassDescription(), "class_term", c.getClassTerm(),
              "class_code", c.getClassCode(), "owner_id", c.getOwnerId());
      return GSON.toJson(variables);
    }
  }

  /**
   * Forms groups.
   * {
   * class: the ClassInfo
   * }
   */
  private static class FormGroups implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      List<List<Pair<Integer, PersonInfo>>> theGroups =
          heuristic.getGroups(
              Integer.parseInt(request.params(":class_id")),
              Integer.parseInt(request.params(":group_size")));
      Map<String, Object> variables = ImmutableMap.of("class", theGroups);
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
   * Processes class creation requests. JSON objects must have the form:
   * {
   * id: ...,
   * class_name: ...,
   * class_number: ...,
   * class_description: ...,
   * class_term: ...,
   * }
   * The returned JSON object will have the form:
   * {
   * status: [status of class creation request],
   * message: [message explaining class creation status],
   * class_id: [id of class created],
   * class_code: [code of class created]
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
      int ownerId = data.getInt("id");
      Pair<Pair<Integer, String>, DBCode> result =
          GROUPS_DATABASE.createClass(className, classNumber, classDescription, classTerm, ownerId);
      DBCode code = result.getSecond();
      boolean status = code.getCode() == 0;
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage(),
          "class_id", status ? result.getFirst().getFirst() : "",
          "class_code", status ? result.getFirst().getSecond() : ""
      );
      return GSON.toJson(variables);
    }
  }

  /**
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
      int id = Integer.parseInt(data.getString("id"));
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
   * Attempts to leave a class. JSON objects must be in the form:
   * {
   *   id: ...,
   *   class_id: ...,
   * }.
   * The returned JSON object will have the form:
   * {
   *   status: [status explaining leave class request],
   *   message: [message explaining leave class status]
   * }
   */
  private static class LeaveClass implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      int id = Integer.parseInt(data.getString("id"));
      int classId = data.getInt("class_id");
      DBCode code = GROUPS_DATABASE.leaveClass(id, classId);
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage()
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Attempts to delete a class. JSON objects must be in the form:
   * {
   *   id: ...,
   *   class_id: ...,
   * }.
   * The returned JSON object will have the form:
   * {
   *   status: [status explaining delete class request],
   *   message: [message explaining delete class status]
   * }
   */
  private static class DeleteClass implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      int id = Integer.parseInt(data.getString("id"));
      int classId = data.getInt("class_id");
      DBCode code = GROUPS_DATABASE.deleteClass(id, classId);
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage()
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Gets the person's info. The returned JSON object will have the form:
   * {
   * status: [the status of the person fetching operation],
   * message: [message explaining person fetching status],
   * id: person's id,
   * first_name: person's first name,
   * last_name: person's last name,
   * email: person's email,
   * enrollments: person's enrollments
   * }
   * The last 3 will be the empty string if the query fails.
   */
  private static class GetPersonInfo implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      int id = Integer.valueOf(request.params(":id"));
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
          "email", success ? personInfo.getEmail() : "",
          "enrollments", success ? GROUPS_DATABASE.getEnrollments(id) : ""
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Gets the person info of all enrollees in the class. The returned JSON object will have the
   * form:
   * {
   * persons: [a list of PersonInfo objects]
   * }
   */
  private static class GetPersonsInClass implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      int classId = Integer.parseInt(request.params(":class_id"));
      List<PersonInfo> personInfos = GROUPS_DATABASE.getPersonsInClass(classId);
      Map<String, Object> variables = ImmutableMap.of(
          "persons", personInfos
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Gets the person's preferences in a class. The returned JSON object will have the form:
   * {
   * status: [the status of the preferences fetching operation],
   * message: [message explaining preferences fetching status],
   * preferences: [person's preferences if successful, or empty string]
   * }
   */
  private static class GetPersonPrefInClass implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      int classId = Integer.parseInt(request.params(":class_id"));
      int personId = Integer.parseInt(request.params(":id"));
      Pair<DBCode, PersonPreferences> result = GROUPS_DATABASE.getPersonPrefInClass(personId,
          classId);
      DBCode code = result.getFirst();
      boolean success = code.getCode() == 0;
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage(),
          "preferences", success ? result.getSecond() : ""
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Gets all persons in a specified class. The returned JSON object will have the form:
   * {
   * persons: {list of persons in a class},
   * }
   */
  private static class GetPersonsPrefsInClass implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      int classId = Integer.parseInt(request.params(":class_id"));
      List<PersonPreferences> persons = GROUPS_DATABASE.getPersonsPrefsInClass(classId);
      System.out.println(persons);
      Map<String, Object> variables = ImmutableMap.of(
          "persons", persons
      );
      return GSON.toJson(variables);
    }
  }

  /**
   * Sets preferences given information from the frontend. JSON objects must have the form:
   * {
   * person_id: ...,
   * class_id: ...,
   * dorm: ...,
   * person_preferences: ...,
   * time_preferences: ...,
   * }
   * The returned JSON object will have the form:
   * {
   * status: [status of setting preferences operation]
   * message: [message explaining status],
   * }
   */
  private static class SetPreferences implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      int personId = data.getInt("person_id");
      int classId = data.getInt("class_id");
      String dorm = data.getString("dorm");
      String personPreferences = data.getString("person_preferences");
      String timePreferences = data.getString("time_preferences");
      DBCode code =
          GROUPS_DATABASE
              .setPreferences(personId, classId, dorm, personPreferences, timePreferences);
      Map<String, Object> variables = ImmutableMap.of(
          "status", code.getCode(),
          "message", code.getMessage()
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
