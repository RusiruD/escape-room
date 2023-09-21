package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.CustomNotifications;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Instructions;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class PuzzleController implements Controller {

  private static PuzzleController instance;

  public static PuzzleController getInstance() {
    return instance;
  }

  private String[][] tiles;
  private String[][] solution;
  @FXML private ImageView one;
  @FXML private ImageView two;
  @FXML private ImageView three;
  @FXML private ImageView four;
  @FXML private ImageView five;
  @FXML private ImageView six;
  @FXML private ImageView zero;
  @FXML private ImageView eight;
  @FXML private ImageView nine;

  private boolean hasSelection = false;
  private ImageView firstSelection;
  private ImageView secondSelection;

  @FXML private Label lblTime;

  @FXML private Pane instructionsDisplay;

  public void initialize() {
    // set the instance
    instance = this;

    String instructionsString = "INSTRUCTIONS GO HERE";
    Instructions instructions = new Instructions(instructionsString);
    Pane instructionsPane = instructions.getInstructionsPane();
    instructionsDisplay.getChildren().add(instructionsPane);
    instructionsPane.getStyleClass().add("riddle");
    // set the tiles and solution
    tiles =
        new String[][] {
          {"one", "two", "three"}, {"four", "five", "six"}, {"zero", "eight", "nine"}
        };
    solution =
        new String[][] {
          {"one", "two", "zero"}, {"four", "six", "three"}, {"eight", "five", "nine"}
        };
  }

  @FXML
  private void clickedBack(MouseEvent event) throws IOException {
    App.setRoot(AppUi.PUZZLEROOM);
  }

  private void clicked(ImageView object) {
    // if there is no selection, select the object
    if (!hasSelection && !object.equals(zero)) {
      // set the selection
      hasSelection = true;
      firstSelection = object;
      firstSelection.setBlendMode(BlendMode.RED);
      // if there is a selection, swap the tiles
    } else if (hasSelection) {
      // set the selection
      hasSelection = false;
      secondSelection = object;
      swapTiles(firstSelection, secondSelection);
      firstSelection.setBlendMode(BlendMode.SRC_OVER);
    }
  }

  @FXML
  private void clickedTile(MouseEvent event) throws IOException {
    clicked((ImageView) event.getSource());
  }

  private void swapTiles(ImageView a, ImageView b) {
    // find the positions of the tiles
    int[] apos = findPos(a.getId());
    int[] bpos = findPos(b.getId());

    // if one of the tiles is the zero tile, then the other tile must be adjacent to it
    if (!a.equals(zero) && !b.equals(zero)) {
      return;
    }

    // if the tiles are adjacent, swap them
    if (Math.abs(apos[0] - bpos[0]) == 1 ^ Math.abs(apos[1] - bpos[1]) == 1) {
      tiles[apos[0]][apos[1]] = b.getId();
      tiles[bpos[0]][bpos[1]] = a.getId();
      double ax = a.getLayoutX();
      double ay = a.getLayoutY();
      a.setLayoutX(b.getLayoutX());
      a.setLayoutY(b.getLayoutY());
      b.setLayoutX(ax);
      b.setLayoutY(ay);
    }
    // check if the puzzle is solved
    checkSolution();
  }

  private int[] findPos(String s) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tiles[i][j].equals(s)) {
          return new int[] {i, j};
        }
      }
    }
    return null;
  }

  // check if the puzzle is solved
  private void checkSolution() {
    // count the number of tiles in the correct position
    int counter = 0;
    // check if the tiles are in the correct position
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tiles[i][j].equals(solution[i][j])) {
          counter++;
        }
      }
    }
    // if all the tiles are in the correct position, the puzzle is solved
    if (counter == 9) {
      CustomNotifications.generateNotification(
          "Something Happens!", "You hear something fall to the ground...");
      GameState.setPuzzleRoomSolved(true);
    }
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }

  public void updateInventory() {}

  @FXML
  public void getHint() throws IOException {
    App.setRoot(AppUi.CHAT);
  }

  @FXML
  public void getInstructions(MouseEvent event) {
    // Set the instructions pane to be visible and not mouse transparent
    instructionsDisplay.visibleProperty().set(true);
    instructionsDisplay.mouseTransparentProperty().set(false);
    instructionsDisplay.toFront();
  }
}
