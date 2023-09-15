package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

public class PuzzleController {

  private String[][] tiles;
  private String[][] solution;
  @FXML
  private ImageView one, two, three, four, five, six, zero, eight, nine;
  private boolean hasSelection = false;
  private ImageView firstSelection;
  private ImageView secondSelection;

  public void initialize() {
    tiles = new String[][] { { "one", "two", "three" }, { "four", "five", "six" }, { "zero", "eight", "nine" } };
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
    int[] aPos = findPos(a.getId());
    int[] bPos = findPos(b.getId());

    if (!a.equals(zero) && !b.equals(zero))
      return;

    if (Math.abs(aPos[0] - bPos[0]) == 1 ^ Math.abs(aPos[1] - bPos[1]) == 1) {
      tiles[aPos[0]][aPos[1]] = b.getId();
      tiles[bPos[0]][bPos[1]] = a.getId();
      double aX = a.getLayoutX();
      double aY = a.getLayoutY();
      a.setLayoutX(b.getLayoutX());
      a.setLayoutY(b.getLayoutY());
      b.setLayoutX(aX);
      b.setLayoutY(aY);
    }
    checkSolution();
  }

  private int[] findPos(String s) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tiles[i][j].equals(s))
          return new int[] { i, j };
      }
    }
    return null;
  }

  private void checkSolution() {
    int counter = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tiles[i][j].equals(solution[i][j]))
          counter++;
      }
    }
    if (counter == 9) {
      GameState.puzzleRoomSolved = true;
    }
  }
}
