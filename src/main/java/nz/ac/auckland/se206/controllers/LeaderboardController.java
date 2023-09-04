package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class LeaderboardController {
  
  @FXML VBox leaderboard;

  private ArrayList<Integer> scores = new ArrayList<Integer>();

  public void initialize() {
    scores.add(100);
    scores.add(200);
    scores.add(300);
    sortScores();
  }

  public void addTime(int time) {
    HBox entry = new HBox();
    entry.setAlignment(javafx.geometry.Pos.CENTER);
    entry.setStyle("-fx-background-color: blue; -fx-background-radius: 5; -fx-padding: 15;");
    entry.getChildren().add(new Label("Name"));
    entry.getChildren().add(new Label("Time: " + time));
    leaderboard.getChildren().add(entry);
  }

  private void sortScores() {
    scores.sort(Comparator.comparing(Integer::intValue));
    for (int i = 0; i < scores.size(); i++) {
      addTime(scores.get(i));
    }
  }
}
