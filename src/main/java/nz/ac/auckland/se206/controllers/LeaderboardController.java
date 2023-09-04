package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.ScoreEntry;

public class LeaderboardController {
  
  @FXML VBox leaderboard;

  private ArrayList<ScoreEntry> scores = new ArrayList<ScoreEntry>();

  public void initialize() {
    scores.add(new ScoreEntry("John Doe1", 0, 0));
    scores.add(new ScoreEntry("John Doe2", 0, 10));
    scores.add(new ScoreEntry("John Doe3", 0, 20));
    scores.add(new ScoreEntry("John Doe4", 0, 30));
    scores.add(new ScoreEntry("John Doe5", 0, 40));
    scores.add(new ScoreEntry("John Doe6", 0, 50));
    scores.add(new ScoreEntry("John Doe7", 0, 60));
    scores.add(new ScoreEntry("John Doe8", 0, 70));
    scores.add(new ScoreEntry("John Doe9", 0, 80));
    scores.add(new ScoreEntry("John Doe10", 0, 90));
    sortScores();
  }

  public void addTime(int time, int position, boolean isFinal) {
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

    Label nameLabel = new Label("John Doe");
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
        addTime(score, i, false);
      } else {
        addTime(-1, i, false);
      }
    }
    int time = temp.getTime();
    addTime(time, scores.indexOf(temp), true);
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
