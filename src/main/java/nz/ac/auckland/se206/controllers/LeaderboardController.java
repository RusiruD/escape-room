package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.ScoreEntry;

public class LeaderboardController {

  @FXML private StackPane graph;
  @FXML private ScrollPane scrollPane;
  @FXML private Label curretProfile;
  @FXML private Label label1;
  @FXML private Label label2;
  @FXML private Label label3;
  @FXML private VBox leaderboard;
  @FXML private VBox leaderboardContainer;

  private ArrayList<ScoreEntry> scores = new ArrayList<ScoreEntry>();

  private int scaleFactor = 200;

  // add a new score to the leaderboard
  public void initialize() {
    // add some scores to the leaderboard
    scores.add(new ScoreEntry("John Doe1", 0, 0, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe2", 0, 10, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe3", 0, 20, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe4", 0, 30, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe5", 0, 40, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe6", 0, 50, new double[] {0, 0, 0, 0, 0, 0}));
    // sort the scores
    sortScores();
  }

  /**
   * Adds a time entry to the leaderboard with the specified name, time, position, and final status.
   *
   * @param name The name of the player.
   * @param time The time achieved by the player.
   * @param position The position of the player in the leaderboard.
   * @param isFinal Indicates whether the entry is for the final leaderboard.
   */
  public void addTime(String name, int time, int position, boolean isFinal) {
    // Create a horizontal box to represent the leaderboard entry
    HBox entry = new HBox();
    entry.setPrefHeight(60);

    // Create two horizontal boxes to divide the entry into two halves
    HBox firstHalf = new HBox();
    firstHalf.setPrefWidth(250);
    firstHalf.setAlignment(Pos.CENTER_LEFT);

    HBox secondHalf = new HBox();
    secondHalf.setPrefWidth(250);
    secondHalf.setAlignment(Pos.CENTER_RIGHT);

    // Determine the background color based on the position
    String hexcode = getColour(position);

    // Set the style for the entry, including background color and padding
    entry.setStyle("-fx-background-color: " + hexcode + "; -fx-padding: 15;");
    if (isFinal) {
      entry.setStyle(
          "-fx-background-color: linear-gradient(to top, #3a404d, #181c26); -fx-padding: 15;");
    }
    entry.setAlignment(Pos.CENTER);

    // Create a stack pane to display the position number as a circle with a label
    StackPane pos = new StackPane();
    Circle circle = new Circle(15);
    circle.setFill(Color.WHITE);
    Label posLabel = new Label(Integer.toString(position + 1));
    posLabel.setStyle("-fx-text-fill: " + hexcode + "; -fx-font-size: 20; -fx-font-weight: bold;");

    pos.getChildren().addAll(circle, posLabel);
    pos.setPadding(new Insets(0, 20, 0, 0));

    // Add the position display to the first half
    firstHalf.getChildren().add(pos);

    // Check if the time is -1 (indicating no time set)
    if (time == -1) {
      firstHalf.getChildren().add(new Label("No time set"));
      leaderboard.getChildren().add(entry);
      return;
    }

    // Create labels for the player's name and time
    Label nameLabel = new Label(name);
    nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-padding: 0 0 0 20;");
    firstHalf.getChildren().add(nameLabel);

    Label timeLabel = new Label("Time: " + time);
    timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-padding: 0 15 0 10;");
    secondHalf.getChildren().add(timeLabel);

    // Add the two halves to the entry
    entry.getChildren().addAll(firstHalf, secondHalf);

    // Depending on whether it's a final entry, add to the appropriate container
    if (isFinal) {
      leaderboardContainer.getChildren().add(entry);
    } else {
      leaderboard.getChildren().add(entry);
    }
  }

  /**
 * Sorts the scores list by time and updates the leaderboard UI.
 */
private void sortScores() {
  // Store the last (highest) score entry temporarily
  ScoreEntry temp = scores.get(scores.size() - 1);
  
  // Sort the scores list based on time using a comparator
  scores.sort(Comparator.comparing(ScoreEntry::getTime));
  
  // Iterate through the sorted scores list and update leaderboard positions
  for (int i = 0; i < scores.size(); i++) {
    if (i < scores.size()) {
      int score = scores.get(i).getTime();

      // Set the leaderboard position for each score entry
      scores.get(i).setLeaderboardPos(i);
      
      // Retrieve the name of the player
      String name = scores.get(i).getName();
      
      // Update the leaderboard UI with the player's name, score, and position
      addTime(name, score, i, false);
    } else {
      // Add a placeholder entry if there are no more scores
      addTime(null, -1, i, false);
    }
  }
  
  // Retrieve the time and name of the highest score entry
  int time = temp.getTime();
  String name = temp.getName();
  
  // Update the leaderboard UI with the highest score as the final entry
  addTime(name, time, scores.indexOf(temp), true);
  
  // Create a graph to visualize the highest score
  createGraph(temp);
}

  private void createGraph(ScoreEntry scoreEntry) {
    double[] max = {1, 1, 1, 1, 1, 1};
    double[] point8 = {0.8, 0.8, 0.8, 0.8, 0.8, 0.8};
    double[] point6 = {0.6, 0.6, 0.6, 0.6, 0.6, 0.6};
    double[] point4 = {0.4, 0.4, 0.4, 0.4, 0.4, 0.4};
    double[] point2 = {0.2, 0.2, 0.2, 0.2, 0.2, 0.2};
    Polygon graphMax = createGraphHelper(max);
    graphMax.setFill(Color.BEIGE.deriveColor(0, 1.2, 1, 0.6));

    Polygon graphPoint8 = createGraphHelper(point8);

    Polygon graphPoint6 = createGraphHelper(point6);

    Polygon graphPoint4 = createGraphHelper(point4);

    Polygon graphPoint2 = createGraphHelper(point2);

    Group lines = new Group();
    for (int i = 0; i < max.length; i++) {
      double angle = 2 * Math.PI * i / max.length;
      double radius = max[i] * scaleFactor;
      Point2D point = new Point2D(Math.cos(angle) * radius, Math.sin(angle) * radius);
      Line line = new Line(0, 0, point.getX(), point.getY());
      line.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
      lines.getChildren().add(line);
    }

    // to change - get array of string of labels then append to group
    String[] statStrings = {
      "lorem ipsum", "lorem ipsum", "lorem ipsum", "lorem ipsum", "lorem ipsum", "lorem ipsum"
    };
    Group labels = new Group();
    for (int i = 0; i < max.length; i++) {
      double angle = 2 * Math.PI * i / max.length;
      double radius = max[i] * scaleFactor + 55;
      Point2D point = new Point2D(Math.cos(angle) * radius, Math.sin(angle) * radius);
      Label label = new Label(statStrings[i]);
      label.setLayoutX(point.getX());
      label.setLayoutY(point.getY());
      label.setStyle("-fx-text-fill: black; -fx-font-size: 20;");
      labels.getChildren().add(label);
    }
    // 2
    graph.getChildren().add(lines);
    graph.getChildren().add(labels);
    // 5 -> 7 children total (0-6) datat would be 8th (7) child
    graph.getChildren().addAll(graphMax, graphPoint8, graphPoint6, graphPoint4, graphPoint2);
    
    setGraph(scoreEntry);
  }

  // create a graph from an array of doubles
  private Polygon createGraphHelper(double[] data) {
    // create a polygon
    Polygon polygon = new Polygon();
    // add points to the polygon
    for (int i = 0; i < data.length; i++) {
      // calculate the angle and radius of the point
      double angle = 2 * Math.PI * i / data.length;
      // scale the radius
      double radius = data[i] * scaleFactor;
      // calculate the point
      Point2D point = new Point2D(Math.cos(angle) * radius, Math.sin(angle) * radius);
      // add the point to the polygon
      polygon.getPoints().addAll(point.getX(), point.getY());
      // set the style of the polygon
      polygon.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
      // set the fill of the polygon
      polygon.setFill(Color.TRANSPARENT);
    }
    return polygon;
  }

  // set the graph to the most recent score
  private void setGraph(ScoreEntry scoreEntry) {
    // remove the old graph
    if (graph.getChildren().size() > 7) {
      
      graph.getChildren().remove(7);
    }
    // add the new graph
    double[] data = scoreEntry.getStatPoints();
    Polygon graphData = createGraphHelper(data);
    graphData.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.8));
    graphData.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 1));
    // add the new graph to the graph
    graph.getChildren().add(graphData);
  }

  // depending on integer input, return a colour for the leaderboard
  private String getColour(int i) {
    switch (i % 10) {
      case 0:
        // red
        return "#fa6855";

      case 1:
        // orange
        return "#ed5f52";

      case 2:
        // yellow
        return "#e0574f";

      case 3:
        // green
        return "#db544e";
      case 4:
        // blue
        return "#d7514d";

      case 5:
        // purple
        return "#d24e4c";

      case 6:
        // pink
        return "#cd4b4b";

      case 7:
        // brown
        return "#c74749";

      case 8:
        // grey
        return "#c24448";

      case 9:
        // black
        return "#b53f43";

      default:
        // white
        return "#ffffff";
    }
  }
}
