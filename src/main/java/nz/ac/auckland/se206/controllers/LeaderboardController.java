package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.ScoreEntry;

public class LeaderboardController {
  
  @FXML VBox leaderboard;
  @FXML StackPane graph;
  @FXML Label curretProfile;
  @FXML Label label1;
  @FXML Label label2;
  @FXML Label label3;

  private ArrayList<ScoreEntry> scores = new ArrayList<ScoreEntry>();

  private int SCALE_FACTOR = 200;

  public void initialize() {
    scores.add(new ScoreEntry("John Doe1", 0, 0, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe2", 0, 10, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe3", 0, 20, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe4", 0, 30, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe5", 0, 40, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe6", 0, 50, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe7", 0, 60, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe8", 0, 70, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe9", 0, 80, new double[] {0, 0, 0, 0, 0, 0}));
    scores.add(new ScoreEntry("John Doe10", 0, 90, new double[] {0.6, 0.5, 0.4, 0.2, 0.4, 0.3}));
    sortScores();
    createGraph();
  }

  public void addTime(String name, int time, int position, boolean isFinal) {
    HBox entry = new HBox();
    entry.setPrefHeight(60);

    HBox firstHalf = new HBox();
    firstHalf.setPrefWidth(250);
    firstHalf.setAlignment(Pos.CENTER_LEFT);

    HBox secondHalf = new HBox();
    secondHalf.setPrefWidth(250);
    secondHalf.setAlignment(Pos.CENTER_RIGHT);

    String hexcode = getColour(position);

    entry.setStyle("-fx-background-color: " + hexcode + "; -fx-padding: 15;");
    if (isFinal) {
      entry.setStyle("-fx-background-color: linear-gradient(to top, #3a404d, #181c26); -fx-padding: 15;");
    }
    entry.setAlignment(Pos.CENTER);

    StackPane pos = new StackPane();
    Circle circle = new Circle(15);
    circle.setFill(Color.WHITE);
    Label posLabel = new Label(Integer.toString(position + 1));
    posLabel.setStyle("-fx-text-fill: " + hexcode + "; -fx-font-size: 20; -fx-font-weight: bold;");

    pos.getChildren().addAll(circle, posLabel);
    pos.setPadding(new Insets(0, 20, 0, 0));

    firstHalf.getChildren().add(pos);

    if (time == -1) {
      firstHalf.getChildren().add(new Label("No time set"));
      leaderboard.getChildren().add(entry);
      return;
    }

    Label nameLabel = new Label(name);
    nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-padding: 0 0 0 20;");
    firstHalf.getChildren().add(nameLabel);

    // Label movementLabel = new Label();
    // movementLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-padding: 0 0 0 20;");
    // switch (scores.get(position).getMovement()) {
    //   case UP:
    //     movementLabel.setText("↑" + posDiff);
    //     break;
    //   case DOWN:
    //     movementLabel.setText("↓" + posDiff);
    //     break;
    //   case NONE:
    //     movementLabel.setText("");
    //     break;
    // }
    // secondHalf.getChildren().add(movementLabel);

    Label timeLabel = new Label("Time: " + time);
    timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-padding: 0 0 0 20;");
    secondHalf.getChildren().add(timeLabel);

    entry.getChildren().addAll(firstHalf, secondHalf);
    leaderboard.getChildren().add(entry);
  }

  private void sortScores() {
    ScoreEntry temp = scores.get(scores.size() - 1);
    scores.sort(Comparator.comparing(ScoreEntry::getTime));
    for (int i = 0; i < 10; i++) {
      if (i < scores.size()) {
        int score = scores.get(i).getTime();
        //int posDiff = 0;
        // if (scores.get(i).getLeaderboardPos() == -1) {
        //   posDiff = calcPosChange(i);
        // }
        scores.get(i).setLeaderboardPos(i);
        String name = scores.get(i).getName();
        addTime(name, score, i, false);
      } else {
        addTime(null, -1, i, false);
      }
    }
    int time = temp.getTime();
    String name = temp.getName();
    addTime(name, time, scores.indexOf(temp), true);
    setGraph(temp);
  }

  private void createGraph() {
    double[] max  = {1, 1, 1, 1, 1, 1};
    double[] point8 = {0.8, 0.8, 0.8, 0.8, 0.8, 0.8};
    double[] point6 = {0.6, 0.6, 0.6, 0.6, 0.6, 0.6};
    double[] point4 = {0.4, 0.4, 0.4, 0.4, 0.4, 0.4};
    double[] point2 = {0.2, 0.2, 0.2, 0.2, 0.2, 0.2};
    double[] data = scores.get(scores.size() - 1).getStatPoints();
    Polygon graphMax = createGraphHelper(max);
    graphMax.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
    graphMax.setFill(Color.BEIGE.deriveColor(0, 1.2, 1, 0.6));

    Polygon graphPoint8 = createGraphHelper(point8);
    graphPoint8.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
    graphPoint8.setFill(Color.TRANSPARENT);

    Polygon graphPoint6 = createGraphHelper(point6);
    graphPoint6.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
    graphPoint6.setFill(Color.TRANSPARENT);

    Polygon graphPoint4 = createGraphHelper(point4);
    graphPoint4.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
    graphPoint4.setFill(Color.TRANSPARENT);

    Polygon graphPoint2 = createGraphHelper(point2);
    graphPoint2.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
    graphPoint2.setFill(Color.TRANSPARENT);

    Group lines = new Group();
    for (int i = 0; i < max.length; i++) {
      double angle = 2 * Math.PI * i / max.length;
      double radius = max[i] * SCALE_FACTOR;
      Point2D point = new Point2D(Math.cos(angle) * radius, Math.sin(angle) * radius);
      Line line = new Line(0, 0, point.getX(), point.getY());
      line.setStroke(Color.BLACK.deriveColor(0, 1.2, 1, 0.6));
      lines.getChildren().add(line);
    }

    //to change - get array of string of labels then append to group
    String[] statStrings = {"lorem ipsum", "lorem ipsum", "lorem ipsum", "lorem ipsum", "lorem ipsum", "lorem ipsum"};
    Group labels = new Group();
    for (int i = 0; i < max.length; i++) {
      double angle = 2 * Math.PI * i / max.length;
      double radius = max[i] * SCALE_FACTOR + 55;
      Point2D point = new Point2D(Math.cos(angle) * radius, Math.sin(angle) * radius);
      Label label = new Label(statStrings[i]);
      label.setLayoutX(point.getX());
      label.setLayoutY(point.getY());
      label.setStyle("-fx-text-fill: black; -fx-font-size: 20;");
      labels.getChildren().add(label);
    }

    graph.getChildren().add(lines);
    graph.getChildren().add(labels);

    Polygon graphData = createGraphHelper(data);

    graphData.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.8));
    graphData.setStroke(Color.BLACK);
    System.out.println(graphData.getLayoutX() + " " + graphData.getLayoutY());
    graph.getChildren().addAll(graphMax, graphPoint8, graphPoint6, graphPoint4, graphPoint2, graphData);
  }

  private Polygon createGraphHelper(double[] data) {
    Polygon polygon = new Polygon();
    for (int i = 0; i < data.length; i++) {
      double angle = 2 * Math.PI * i / data.length;
      double radius = data[i] * SCALE_FACTOR;
      Point2D point = new Point2D(Math.cos(angle) * radius, Math.sin(angle) * radius);
      polygon.getPoints().addAll(point.getX(), point.getY());
    }
    return polygon;
  }

  private void setGraph(ScoreEntry temp) {
  }

  // private int calcPosChange(int pos) {
  //   int oldPos = scores.get(pos).getLeaderboardPos();
  //   if (oldPos == pos) {
  //     scores.get(pos).setMovement(ScoreEntry.Movement.NONE);
  //     return 0;
  //   } else { 
  //     int posDiff = oldPos - pos;
  //     if (posDiff < 0) {
  //       posDiff = posDiff * -1;
  //       scores.get(pos).setMovement(ScoreEntry.Movement.UP);
  //     } else {
  //       scores.get(pos).setMovement(ScoreEntry.Movement.DOWN);
  //     }
  //     return posDiff;
  //   }
  // }

  private String getColour(int i) {
    switch (i) {
      case 0:
        return "#fa6855";
      case 1:
        return "#ed5f52";
      case 2:
        return "#e0574f";
      case 3:
        return "#db544e";
      case 4:
        return "#d7514d";
      case 5:
        return "#d24e4c";
      case 6:
        return "#cd4b4b";
      case 7:
        return "#c74749";
      case 8:
        return "#c24448";
      case 9:
        return "#b53f43";
      default:
        return "#ffffff";
    }
  }
}
