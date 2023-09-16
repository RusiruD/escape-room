package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class PuzzleController implements Controller {

  private String[][] tiles;
  private String[][] solution;
  @FXML
  private ImageView one;
  @FXML
  private ImageView two;
  @FXML
  private ImageView three;
  @FXML
  private ImageView four;
  @FXML
  private ImageView five;
  @FXML
  private ImageView six;
  @FXML
  private ImageView zero;
  @FXML
  private ImageView eight;
  @FXML
  private ImageView nine;

  private boolean hasSelection = false;
  private ImageView firstSelection;
  private ImageView secondSelection;

  @FXML
  private Label lblTime;

  private static PuzzleController instance;

  public static PuzzleController getInstance() {
    return instance;
  }

  public void initialize() {
    instance = this;
    tiles = new String[][] { { "one", "two", "three" }, { "four", "five", "six" },
        { "zero", "eight", "nine" } };
    solution = new String[][] { { "one", "two", "three" }, { "four", "five", "six" },
        { "nine", "eight", "zero" } };
  }

  @FXML
  private void clickedBack(MouseEvent event) throws IOException {
    App.setRoot(AppUi.PUZZLEROOM);
  }

  private void clicked(ImageView object) {

    if (!hasSelection && !object.equals(zero)) {
      hasSelection = true;
      firstSelection = object;
      firstSelection.setBlendMode(BlendMode.RED);
    } else if (hasSelection) {
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
    int[] apos = findPos(a.getId());
    int[] bpos = findPos(b.getId());

    if (!a.equals(zero) && !b.equals(zero)) {
      return;
    }

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
    checkSolution();
  }

  private int[] findPos(String s) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tiles[i][j].equals(s)) {
          return new int[] { i, j };
        }
      }
    }
    return null;
  }

  private void checkSolution() {
    int counter = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tiles[i][j].equals(solution[i][j])) {
          counter++;
        }
      }
    }
    if (counter == 9) {
      GameState.puzzleRoomSolved = true;
    }
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }

  public void updateInventory() {
  }

}
