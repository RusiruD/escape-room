package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;

public class WinLossController {
  @FXML private Label gameOutcome;
  @FXML private Label gameOutcomeDescription;
  @FXML private Button exitGame;
  @FXML private Button replayGame;
  @FXML private Button newGame;

  public void initialize() {
    if (GameState.isGameWon) {
      gameOutcome.setText("You Win!");
      gameOutcomeDescription.setText(
          "You have successfully escaped the dungeon and slayed the dungeon master");
    } else {
      gameOutcome.setText("You Lose!");
      gameOutcomeDescription.setText(
          "You have failed to escape the dungeon and have succumb to the dungeon master");
    }
  }
}
