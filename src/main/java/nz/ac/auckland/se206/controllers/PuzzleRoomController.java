package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.beans.binding.BooleanExpression;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class PuzzleRoomController implements Controller {

  private static PuzzleRoomController instance;

  public static PuzzleRoomController getInstance() {
    return instance;
  }

  @FXML private Pane puzzleRoomPane;
  @FXML private Label lblTime;
  @FXML private ImageView key3;

  @FXML private ComboBox<String> inventoryChoiceBox;

  public void initialize() {
    key3.visibleProperty().bind(GameState.puzzleRoomSolved);
    key3.disableProperty().bind(((BooleanExpression) GameState.getPuzzleRoomSolved()).not());

    instance = this;
  }

  @FXML
  private void clickPuzzle(MouseEvent event) throws IOException {
    App.setRoot(AppUi.PUZZLE);
  }

  @FXML
  private void onKey3Clicked(MouseEvent event) {
    // change the key3's visibility and disable it
    key3.visibleProperty().unbind();
    key3.disableProperty().unbind();
    Inventory.addToInventory("key3");
    key3.setVisible(false);
    key3.setDisable(true);
    // update the game state
    GameState.isKey3Collected = true;
  }

  @FXML
  private void onReturnToCorridorClicked(ActionEvent event) {
    App.returnToCorridor();
    GameState.currentRoom = GameState.STATE.CHEST;
  }

  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
  }

  @FXML
  public double getPuzzleRoomWidth() {

    return puzzleRoomPane.getPrefWidth();
  }

  @FXML
  public double getPuzzleRoomHeight() {

    return puzzleRoomPane.getPrefHeight();
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }

  @FXML
  public void getHint() throws IOException {
    App.setRoot(AppUi.CHAT);
  }
}
