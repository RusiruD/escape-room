package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class PuzzleRoomController implements Controller {

  private static PuzzleRoomController instance;

  public static PuzzleRoomController getInstance() {
    return instance;
  }

  @FXML
  private Label lblTime;
  @FXML
  private ImageView key2;

  @FXML
  private ComboBox<String> inventoryChoiceBox;

  public void initialize() {

    instance = this;

  }

  @FXML
  private void clickPuzzle(MouseEvent event) throws IOException {
    App.setRoot(AppUi.PUZZLE);
  }

  @FXML
  private void clickedDoor(MouseEvent event) throws IOException {
    if (GameState.puzzleRoomSolved) {
      App.setRoot(AppUi.CORRIDOR);
    }
  }

  @FXML
  private void onKey2Clicked(MouseEvent event) {
    Inventory.addToInventory("key2");
    key2.setVisible(false);
    key2.setDisable(true);
    GameState.isKey2Collected = true;
  }

  @FXML
  private void onReturnToCorridorClicked(ActionEvent event) {
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

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }
}
