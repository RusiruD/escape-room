package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;;

public class PuzzleController {

    private int[][] tiles;
    private int[][] solution;
    @FXML
    private ImageView one, two, three, four, five, six, zero, eight, nine;
    private boolean hasSelection = false;
    private ImageView firstSelection;
    private ImageView secondSelection;

    public void initialize() {
        tiles = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 0, 8, 9 } };
        solution = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 9, 8, 0 } };
    }

    @FXML
    private void clickedBack(MouseEvent event) throws IOException {
        App.setRoot(AppUi.PUZZLEROOM);
    }

    private void clicked(ImageView object) {
        if (!hasSelection) {
            hasSelection = true;
            firstSelection = object;
        } else {
            hasSelection = false;
            secondSelection = object;
            swapTiles(firstSelection, secondSelection);
        }
    }

    @FXML
    private void clickedTile(MouseEvent event) throws IOException {
        clicked(((ImageView) event.getSource()));
    }

    private void swapTiles(ImageView a, ImageView b) {
        double aX = a.getLayoutX();
        double aY = a.getLayoutY();
        a.setLayoutX(b.getLayoutX());
        a.setLayoutY(b.getLayoutY());
        b.setLayoutX(aX);
        b.setLayoutY(aY);
    }

}
