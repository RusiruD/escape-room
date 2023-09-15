package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.TimerCounter;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class StartScreenController {

  @FXML
  private ChoiceBox<String> timerChoice;
  @FXML
  private ChoiceBox<String> difficultyChoice;

  @FXML
  private void initialize() {

    // Add items to the choice box
    timerChoice.getItems().add("2 Minutes");
    timerChoice.getItems().add("4 Minutes");
    timerChoice.getItems().add("6 Minutes");
    timerChoice.setValue("2 Minutes");
    difficultyChoice.getItems().add("Easy");
    difficultyChoice.getItems().add("Medium");
    difficultyChoice.getItems().add("Hard");
    difficultyChoice.setValue("Easy");

    // You can perform additional initialization here if needed.
  }

  @FXML
  private void onStartGame(ActionEvent event) {

    // Get the chosen values from the choice box
    String chosenTimeLimit = timerChoice.getValue();
    String chosenDifficulty = difficultyChoice.getValue();
    GameState.gameTime = chosenTimeLimit;
    GameState.difficultyLevel = chosenDifficulty;

    // Create a new timer object
    TimerCounter time = new TimerCounter(RoomController.getInstance());
    Inventory.setPuzzleRoomController(PuzzleRoomController.getInstance());
    Inventory.setRoomController(RoomController.getInstance());
    Inventory.setCorridorController(CorridorController.getInstance());
    // App.setRoot(SceneManager.AppUi.ROOM);

    if (chosenTimeLimit.equals("2 Minutes")) {
      time.twoMinutes();
    } else if (chosenTimeLimit.equals("4 Minutes")) {
      time.fourMinutes();
    } else {
      time.sixMinutes();
    }
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    timerChoice.getStyleClass().add("choice-box");
    difficultyChoice.getStyleClass().add("choice-box");
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CORRIDOR));
    SceneManager.getUiRoot(AppUi.CORRIDOR).requestFocus();

  }
}
