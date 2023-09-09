package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;;

public class PuzzleController {

    @FXML
    public void clickedBack(MouseEvent event) throws IOException {
        App.setRoot(AppUi.PUZZLEROOM);
    }
}
