package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class PuzzleRoomController {

  private static PuzzleRoomController instance;

  public static PuzzleRoomController getInstance() {
    return instance;
  }

  @FXML
  private ComboBox<String> inventoryChoiceBox;

  // AnimationTimer timer = new AnimationTimer() {

  // @Override
  // public void handle(long timestamp) {
  // updateInventory();
  // }
  // };

  public void initialize() {

    instance = this;

  }

  @FXML
  private void clickPuzzle(MouseEvent event) throws IOException {
    App.setRoot(AppUi.PUZZLE);
  }

  @FXML
  private void clickedDoor(MouseEvent event) throws IOException {

    if (GameState.puzzleRoomSolved)
      App.setRoot(AppUi.CORRIDOR);
  }

  @FXML
  void onReturnToCorridorClicked(ActionEvent event) {
    // return to corridor scene
    try {

      Button button = (Button) event.getSource();
      Scene sceneButtonIsIn = button.getScene();

      sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CORRIDOR));
      SceneManager.getUiRoot(AppUi.CORRIDOR).requestFocus();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
  }
}
