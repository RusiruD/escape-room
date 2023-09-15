package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class PuzzleRoomController {

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
}
