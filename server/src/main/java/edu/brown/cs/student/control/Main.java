package edu.brown.cs.student.control;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.brown.cs.student.maps.*;
import spark.Route;
import org.json.JSONObject;
import com.google.gson.Gson;

import edu.brown.cs.student.stars.*;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  //list of trigger actions
  private List<TriggerAction> actionlist;

  //Stars object used for passing star data into other classes
  private static Stars stars;

  //NaiveNeighbors object
  private static NaiveNeighbors nn;

  //NaiveRadius object
  private static NaiveRadius nr;

  //Neighbors Object
  private static Neighbors n;

  //Radius Object
  private static Radius r;

  //Map Object
  private static Maps map;

  //Nearest Object
  private static Nearest near;

  //Ways Object
  private static Ways w;

  //Route Object
  private static MapRoute route;

  //Strings for displaying results in gui
  private static String results1 = "";

  private static String results2 = "";

  private static String results3 = "";

  private static String results4 = "";

  //GSON instance for handling JSON requests
  private static final Gson GSON = new Gson();

  //parse function used to get args from input
  private static String[] parse(String s) {

    /*this regex piece of code was taken off of Stack Overflow:
    https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
    */
    List<String> list = new ArrayList();

    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(s);
    while (m.find()) {
      list.add(m.group(1));
    }

    String[] in = new String[list.size()];

    for (int i = 0; i < list.size(); i++) {
      in[i] = list.get(i);
    }

    return in;
  }


  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
            .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    //initializing objects
    actionlist = new ArrayList();
    stars = new Stars();
    nn = new NaiveNeighbors(stars);
    nr = new NaiveRadius(stars);
    n = new Neighbors(stars);
    r = new Radius(stars);

    //Maps
    map = new Maps();
    near = new Nearest(map);
    w = new Ways(map);
    route = new MapRoute(map, near);

    actionlist.add(stars);
    actionlist.add(nn);
    actionlist.add(nr);
    actionlist.add(n);
    actionlist.add(r);
    actionlist.add(new Mock());

    actionlist.add(map);
    actionlist.add(near);
    actionlist.add(w);
    actionlist.add(route);


    REPL repl = new REPL();

    // register triggeractions to repl
    for (TriggerAction ta: actionlist) {
      repl.registerAction(ta);
    }

    repl.run();

    try {
      if (map.getConnected()) {
        map.getParser().deleteUsers();
      }
    } catch (SQLException e) {
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
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

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/stars", new FrontHandler(), freeMarker);
    Spark.post("/naiveneighbors", new NNHandler(), freeMarker);
    Spark.post("/naiveradius", new NRHandler(), freeMarker);
    Spark.post("/neighbors", new NHandler(), freeMarker);
    Spark.post("/radius", new RHandler(), freeMarker);

    Spark.post("/map", new MapHandler());
    Spark.post("/route", new RouteHandler());
    Spark.post("/street", new StreetHandler());
    Spark.post("/ways", new WaysHandler());
    Spark.post("/nearest", new NearestHandler());
    Spark.post("/updates", new UpdatesHandler());
    Spark.post("/user", new UserHandler());

  }

  /**
   * Handle requests to the front page of our Stars website.
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Stars: Query the database", "results1", results1, "results2", results2, "results3", results3, "results4", results4);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handle NaiveNeighbors requests for querying the database.
   */
  private static class NNHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String textFromTextField = qm.value("text1");

      results1 = "";
      String[] arg = parse(textFromTextField);
      ArrayList<String> neighbors = new ArrayList();

      if (stars.getstars() == null) {
        results1 = "ERROR: Stars not loaded";
      } else {

        try {
          if (arg.length == 4) {
            neighbors = nn.naiveneighbors(Integer.parseInt(arg[0]), Double.parseDouble(arg[1]), Double.parseDouble(arg[2]), Double.parseDouble(arg[3]), false);
          } else if (arg.length == 2) {
            neighbors = nn.naiveneighbors(Integer.parseInt(arg[0]), arg[1]);
          } else {
            System.out.println("ERROR: Invalid number of arguments");
            results1 = "ERROR: Invalid number of arguments";
          }
        } catch (NumberFormatException e) {
          System.out.println("ERROR: Expected number argument(s) were non-numbers");
          results1 = "ERROR: Expected number argument(s) were non-numbers";
        }

        if (neighbors == null) {
          if (results1.equals("")) {
            results1 = "ERROR: Arguments were invalid";
          }
        } else {
          for (String s : neighbors) {
            results1 = results1 + s + "-";

            for (String[] star : stars.getstars()) {
              if (star[0].equals(s)) {
                results1 = results1 + star[1] + " ";
              }
            }
          }
        }
      }

      Map<String, String> vars = ImmutableMap.of("title", "someTitle", "results1", results1, "results2", results2, "results3", results3, "results4", results4);
      return new ModelAndView(vars, "query.ftl");
    }
  }

  /**
   * Handle NaiveRadius requests for querying the database.
   */
  private static class NRHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String textFromTextField = qm.value("text2");

      results2 = "";
      String[] arg = parse(textFromTextField);
      ArrayList<String> neighbors = new ArrayList();

      if (stars.getstars() == null) {
        results2 = "ERROR: Stars not loaded";
      } else {

        try {
          if (arg.length == 4) {
            neighbors = nr.naiveradius(Integer.parseInt(arg[0]), Double.parseDouble(arg[1]), Double.parseDouble(arg[2]), Double.parseDouble(arg[3]), false);
          } else if (arg.length == 2) {
            neighbors = nr.naiveradius(Integer.parseInt(arg[0]), arg[1]);
          } else {
            System.out.println("ERROR: Invalid number of arguments");
            results2 = "ERROR: Invalid number of arguments";
          }
        } catch (NumberFormatException e) {
          System.out.println("ERROR: Expected number argument(s) were non-numbers");
          results2 = "ERROR: Expected number argument(s) were non-numbers";
        }

        if (neighbors == null) {
          if (results2.equals("")) {
            results2 = "ERROR: Arguments were invalid";
          }
        } else {
          for (String s : neighbors) {
            results2 = results2 + s + "-";

            for (String[] star : stars.getstars()) {
              if (star[0].equals(s)) {
                results2 = results2 + star[1] + " ";
              }
            }
          }
        }
      }

      Map<String, String> vars = ImmutableMap.of("title", "someTitle", "results1", results1, "results2", results2, "results3", results3, "results4", results4);
      return new ModelAndView(vars, "query.ftl");
    }
  }

  /**
   * Handle Neighbors requests for querying the database.
   */
  private static class NHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String textFromTextField = qm.value("text3");

      results3 = "";
      String[] arg = parse(textFromTextField);
      ArrayList<String> neighbors = new ArrayList();

      if (stars.getstars() == null) {
        results3 = "ERROR: Stars not loaded";
      } else {

        try {
          if (arg.length == 4) {
            neighbors = n.neighbors(Integer.parseInt(arg[0]), new double[]{Double.parseDouble(arg[1]), Double.parseDouble(arg[2]), Double.parseDouble(arg[3])}, false);
          } else if (arg.length == 2) {
            neighbors = n.neighbors(Integer.parseInt(arg[0]), arg[1]);
          } else {
            System.out.println("ERROR: Invalid number of arguments");
            results3 = "ERROR: Invalid number of arguments";
          }
        } catch (NumberFormatException e) {
          System.out.println("ERROR: Expected number argument(s) were non-numbers");
          results3 = "ERROR: Expected number argument(s) were non-numbers";
        }

        if (neighbors == null) {
          if (results3.equals("")) {
            results3 = "ERROR: Arguments were invalid";
          }
        } else {
          for (String s : neighbors) {
            results3 = results3 + s + "-";

            for (String[] star : stars.getstars()) {
              if (star[0].equals(s)) {
                results3 = results3 + star[1] + " ";
              }
            }
          }
        }
      }

      Map<String, String> vars = ImmutableMap.of("title", "someTitle", "results1", results1, "results2", results2, "results3", results3, "results4", results4);
      return new ModelAndView(vars, "query.ftl");
    }
  }

  /**
   * Handle Radius requests for querying the database.
   */
  private static class RHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String textFromTextField = qm.value("text4");

      results4 = "";
      String[] arg = parse(textFromTextField);
      ArrayList<String> neighbors = new ArrayList();

      if (stars.getstars() == null) {
        results4 = "ERROR: Stars not loaded";
      } else {

        try {
          if (arg.length == 4) {
            neighbors = r.radius(Integer.parseInt(arg[0]), new double[]{Double.parseDouble(arg[1]), Double.parseDouble(arg[2]), Double.parseDouble(arg[3])}, false);
          } else if (arg.length == 2) {
            neighbors = r.radius(Integer.parseInt(arg[0]), arg[1]);
          } else {
            System.out.println("ERROR: Invalid number of arguments");
            results4 = "ERROR: Invalid number of arguments";
          }
        } catch (NumberFormatException e) {
          System.out.println("ERROR: Expected number argument(s) were non-numbers");
          results4 = "ERROR: Expected number argument(s) were non-numbers";
        }

        if (neighbors == null) {
          if (results4.equals("")) {
            results4 = "ERROR: Arguments were invalid";
          }
        } else {
          for (String s : neighbors) {
            results4 = results4 + s + "-";

            for (String[] star : stars.getstars()) {
              if (star[0].equals(s)) {
                results4 = results4 + star[1] + " ";
              }
            }
          }
        }
      }

      Map<String, String> vars = ImmutableMap.of("title", "someTitle", "results1", results1, "results2", results2, "results3", results3, "results4", results4);
      return new ModelAndView(vars, "query.ftl");
    }
  }

  /**
   * Handles the maps command.
   * Takes in coordinates for the boundary box for searching ways (top, left, bottom, right)
   * Loads the map with the given boundaries. Returns a confirmation message when loaded.
   */
  private static class MapHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());

      double sLat = data.getDouble("top");
      double sLon = data.getDouble("left");
      double dLat = data.getDouble("bottom");
      double dLon = data.getDouble("right");

      map.setBounds(sLat, sLon, dLat, dLon);
      map.maps("data/maps/maps.sqlite3");

      Map<String, Object> variables = ImmutableMap.of("map", "Map Loaded!");

      return GSON.toJson(variables);
    }
  }

  /**
   * Handles requests for routes. (copied over from the react lab)
   * Takes in the coordinates for the start point (srclat, srclong) and end point (destlat, destlong)
   * Returns an arraylist of nodes for the path from start to end (List<String[]>) where
   * the string array holds {lat,lon} for each node.
   */
  private static class RouteHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());

      double sLat = data.getDouble("srclat");
      double sLon = data.getDouble("srclong");
      double dLat = data.getDouble("destlat");
      double dLon = data.getDouble("destlong");

      //converts the doubles into string args
      String[] args = new String[]{"route", Double.toString(sLat), Double.toString(sLon), Double.toString(dLat), Double.toString(dLon)};

      Map<String, Object> variables;

      //check if the arguments were correct, if so, we get the route returned by MapRoute object
      if (!route.action(args)) {
        variables = ImmutableMap.of("route", "ERROR when finding route");
      } else {
        List<String[]> path = route.getRouteList();
        variables = ImmutableMap.of("route", path);
      }

      return GSON.toJson(variables);

    }
  }

  /**
   * Handles requests for routes by street intersections.
   * Takes in the street names for the intersections
   * Returns an arraylist of nodes for the path from start to end (List<String[]>) where
   * the string array holds {lat,lon} for each node.
   */
  private static class StreetHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());

      String s1 = data.getString("street1");
      String s2 = data.getString("street2");
      String s3 = data.getString("street3");
      String s4 = data.getString("street4");

      //converts the doubles into string args
      String[] args = new String[]{"route", s1, s2, s3, s4};

      Map<String, Object> variables;

      //check if the arguments were correct, if so, we get the route returned by MapRoute object
      if (!route.action(args)) {
        variables = ImmutableMap.of("street", "ERROR when finding route");
      } else {
        List<String[]> path = route.getRouteList();
        variables = ImmutableMap.of("street", path);
      }

      return GSON.toJson(variables);

    }
  }

  /**
   * Handles the ways command.
   * Takes in coordinates for the boundary box for searching ways (top, left, bottom, right)
   * returns an List<String[]> of ways (coordinates of start/end points), String[] = {startlat, startlon, endlat, endlon}
   */
  private static class WaysHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());

      double sLat = data.getDouble("top");
      double sLon = data.getDouble("left");
      double dLat = data.getDouble("bottom");
      double dLon = data.getDouble("right");

      //converts the doubles into string args
      String[] args = new String[]{"ways", Double.toString(sLat), Double.toString(sLon), Double.toString(dLat), Double.toString(dLon)};

      Map<String, Object> variables;

      //check if the arguments were correct, if so, we get the route returned by MapRoute object
      if (!w.action(args)) {
        variables = ImmutableMap.of("ways", "ERROR when finding ways");
      } else {

        List<String[]> paths = w.getWaysList();
        variables = ImmutableMap.of("ways", paths);
      }

      return GSON.toJson(variables);

    }
  }

  /**
   * Handles nearest requests. Takes in the coordinates of the point selected (srclat, srclong).
   * Returns the coordinates of the nearest node in a String[]
   */
  private static class NearestHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());

      double sLat = data.getDouble("srclat");
      double sLon = data.getDouble("srclong");

      //converts the doubles into string args
      String[] args = new String[]{"nearest", Double.toString(sLat), Double.toString(sLon)};

      Map<String, Object> variables;

      //check if the arguments were correct, if so, we get the route returned by MapRoute object
      if (!near.action(args)) {
        variables = ImmutableMap.of("nearest", "ERROR when finding nearest");
      } else {

        String[] point = near.getNearestPoint();
        variables = ImmutableMap.of("nearest", point);
      }

      return GSON.toJson(variables);

    }
  }

  /**
   * Handles checkin updates requests and feeds them to frontend.
   * Sends an arraylist of String[] which contain the update info.
   */
  private static class UpdatesHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      Map<Double, UserCheckin> updates = map.getCheckinThread().getLatestCheckins();

      List<String[]> l = new ArrayList();

      Iterator it = updates.entrySet().iterator();

      //adds the updates from the hashmap into the arraylist
      while (it.hasNext()) {
        Map.Entry pair = (Map.Entry) it.next();
        Double key = (Double) pair.getKey();
        UserCheckin user = updates.get(key);
        String[] s = new String[]{Double.toString(key), Integer.toString(user.getId()), user.getName(), Double.toString(user.getLat()), Double.toString(user.getLon())};
        l.add(s);
        it.remove();
      }

      Map<String, Object> variables = ImmutableMap.of("updates", l);

      return GSON.toJson(variables);

    }
  }

  /**
   * Handles user requests and feeds them to frontend. Takes in an user id in integer form
   * and returns the data of the user.
   */
  private static class UserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      int id = data.getInt("id");

      Map<String, Object> variables;

      try {
        List<List<String>> user = map.getParser().getUser(String.valueOf(id));
        variables = ImmutableMap.of("user", user);
      } catch (SQLException e) {
        variables = ImmutableMap.of("user", "Database not loaded!");
      }

      return GSON.toJson(variables);

    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
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
