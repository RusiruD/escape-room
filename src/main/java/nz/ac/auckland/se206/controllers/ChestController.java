package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class ChestController {

  private static ChestController instance;

  public static ChestController getInstance() {
    return instance;
  }

  private HashMap<String, String> keyHoleMap = new HashMap<String, String>();
  private HashMap<String, String> correctKeyMap = new HashMap<String, String>();

  private int correctKeys = 0;

  @FXML
  private ComboBox<String> inventoryChoiceBox;

  public void initialize() {
    instance = this;
    keyHoleMap = new HashMap<String, String>();
    // get random keys for key holes
    String[] keys = { "key1", "key2", "key3" };

    // shuffle keys
    ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
    for (int i = 1; i <= 6; i++) {
      randomNumbers.add(i);
    }

    Collections.shuffle(randomNumbers);
    for (int i = 0; i < 3; i++) {
      keyHoleMap.put("hole" + randomNumbers.get(i), keys[i]);
      System.out.println("hole" + randomNumbers.get(i) + " " + keys[i]);
    }

    for (int i = 0; i < 6; i++) {
      correctKeyMap.put("hole" + (i + 1), "empty");
    }
  }

  public void openChest(MouseEvent event) {
    // check if correct combination
    if (correctKeys == 3) {
      // open chest
      System.out.println("chest opened");
    } else {
      // change inserted keys red or green depending if they are correct
      updateKeys();
    }
  }

  private void updateKeys() {
    // check if correct key
    for (int i = 0; i < 6; i++) {
      if (correctKeyMap.get("hole" + (i + 1)) == "true") {
        // set to green for correct
        Rectangle keyHole = (Rectangle) inventoryChoiceBox.getParent().lookup("#hole" + (i + 1));
        keyHole.styleProperty().set("-fx-fill: #a8e6cf");
      } else if (correctKeyMap.get("hole" + (i + 1)) == "false") {
        // set to red for incorrect and let them try again
        Rectangle keyHole = (Rectangle) inventoryChoiceBox.getParent().lookup("#hole" + (i + 1));
        keyHole.styleProperty().set("-fx-fill: #fab387");
        keyHole.mouseTransparentProperty().set(false);
      } else {
        // set back to default
        Rectangle keyHole = (Rectangle) inventoryChoiceBox.getParent().lookup("#hole" + (i + 1));
        keyHole.styleProperty().set("-fx-fill: #1e90ff");
      }
    }
  }

  @FXML
  public void clickKeyHole1(MouseEvent event) {
    // check if correct key
    clickKeyHoleHelper(1);
  }

  @FXML
  public void clickKeyHole2(MouseEvent event) {
    // check if correct key
    clickKeyHoleHelper(2);
  }

  @FXML
  public void clickKeyHole3(MouseEvent event) {
    // check if correct key
    clickKeyHoleHelper(3);
  }

  @FXML
  public void clickKeyHole4(MouseEvent event) {
    // check if correct key
    clickKeyHoleHelper(4);
  }

  @FXML
  public void clickKeyHole5(MouseEvent event) {
    // check if correct key
    clickKeyHoleHelper(5);
  }

  @FXML
  public void clickKeyHole6(MouseEvent event) {
    // check if correct key
    clickKeyHoleHelper(6);
  }

  private void clickKeyHoleHelper(int num) {
    // check if correct key
    if (correctKeyMap.get("hole" + num) == "empty") {

      if (keyHoleMap.get("hole" + num) == inventoryChoiceBox.getValue()) {

        correctKeys++;
        correctKeyMap.put("hole" + num, "true");

        inventoryChoiceBox.getItems().remove(inventoryChoiceBox.getValue());

      } else {
        correctKeyMap.put("hole" + num, "false");
      }
      // sets to yellow for filled
      Rectangle keyHole = (Rectangle) inventoryChoiceBox.getParent().lookup("#hole" + num);
      keyHole.styleProperty().set("-fx-fill: #fab387");
      keyHole.mouseTransparentProperty().set(true);
    } else if (correctKeyMap.get("hole" + num) == "false") {
      // if its false then set back to default (get back key) on click
      Rectangle keyHole = (Rectangle) inventoryChoiceBox.getParent().lookup("#hole" + num);
      keyHole.styleProperty().set("-fx-fill: #1e90ff");
      keyHole.mouseTransparentProperty().set(true);

      inventoryChoiceBox.getItems().add(keyHoleMap.get("hole" + num));
    }
  }
}
