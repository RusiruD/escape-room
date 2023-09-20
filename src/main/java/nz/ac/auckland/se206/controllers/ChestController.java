package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.util.TypeKey;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;

public class ChestController implements Controller {

  private static ChestController instance;

  public static ChestController getInstance() {
    return instance;
  }

  private HashMap<String, String> keyHoleMap = new HashMap<String, String>();
  private HashMap<String, String> correctKeyMap = new HashMap<String, String>();
  private HashMap<String, String> keyMap = new HashMap<String, String>();
  private List<String> keys = Arrays.asList("key1", "key2", "key3");

  private int correctKeys = 0;

  @FXML
  private ComboBox<String> inventoryChoiceBox;

  @FXML
  private Label lblTime;

  @FXML
  private Rectangle keyHole1;
  @FXML
  private Rectangle keyHole2;
  @FXML
  private Rectangle keyHole3;
  @FXML
  private Rectangle keyHole4;
  @FXML
  private Rectangle keyHole5;
  @FXML
  private Rectangle keyHole6;

  @FXML
  private Label lblKey1;
  @FXML
  private Label lblKey2;
  @FXML
  private Label lblKey3;
  @FXML
  private Label lblKey4;
  @FXML
  private Label lblKey5;
  @FXML
  private Label lblKey6;

  private final String DEFAULT_COLOUR = "#1e90ff";
  private final String CORRECT_COLOUR = "#a8e6cf";
  private final String INCORRECT_COLOUR = "#f38ba8";

  public void initialize() {
    instance = this;
    keyHoleMap = new HashMap<String, String>();
    // get random keys for key holes

    lblKey1.setText("");
    lblKey2.setText("");
    lblKey3.setText("");
    lblKey4.setText("");
    lblKey5.setText("");
    lblKey6.setText("");

    // shuffle keys
    ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
    for (int i = 1; i <= 6; i++) {
      randomNumbers.add(i);
    }

    Collections.shuffle(randomNumbers);
    for (int i = 0; i < 3; i++) {
      keyHoleMap.put("hole" + randomNumbers.get(i), keys.get(i));
      System.out.println("hole" + randomNumbers.get(i) + " " + keys.get(i));
    }

    for (int i = 0; i < 6; i++) {
      correctKeyMap.put("hole" + (i + 1), "empty");
    }
  }

  public void openChest(MouseEvent event) {
    System.out.println("open chest");
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

    System.out.println("update keys");
    for (int i = 0; i < 6; i++) {
      if (correctKeyMap.get("hole" + (i + 1)) == "true") {
        // set to green for correct
        // get key hole
        switch (i) {
          case 0:
            keyHole1.styleProperty().set("-fx-fill: " + CORRECT_COLOUR);
            keyHole1.mouseTransparentProperty().set(true);
          case 1:
            keyHole2.styleProperty().set("-fx-fill: " + CORRECT_COLOUR);
            keyHole2.mouseTransparentProperty().set(true);
          case 2:
            keyHole3.styleProperty().set("-fx-fill: " + CORRECT_COLOUR);
            keyHole3.mouseTransparentProperty().set(true);
          case 3:
            keyHole4.styleProperty().set("-fx-fill: " + CORRECT_COLOUR);
            keyHole4.mouseTransparentProperty().set(true);
          case 4:
            keyHole5.styleProperty().set("-fx-fill: " + CORRECT_COLOUR);
            keyHole5.mouseTransparentProperty().set(true);
          case 5:
            keyHole6.styleProperty().set("-fx-fill: " + CORRECT_COLOUR);
            keyHole6.mouseTransparentProperty().set(true);
        }
      } else if (correctKeyMap.get("hole" + (i + 1)) == "false") {
        // set to red for incorrect and let them try again
        System.out.println("hole " + (i + 1) + " is incorrect");
        switch (i) {
          case 0:
            keyHole1.styleProperty().set("-fx-fill: " + INCORRECT_COLOUR);
          case 1:
            keyHole2.styleProperty().set("-fx-fill: " + INCORRECT_COLOUR);
          case 2:
            keyHole3.styleProperty().set("-fx-fill: " + INCORRECT_COLOUR);
          case 3:
            keyHole4.styleProperty().set("-fx-fill: " + INCORRECT_COLOUR);
          case 4:
            keyHole5.styleProperty().set("-fx-fill: " + INCORRECT_COLOUR);
          case 5:
            keyHole6.styleProperty().set("-fx-fill: " + INCORRECT_COLOUR);
        }
      } else if (correctKeyMap.get("hole" + (i + 1)) == "empty") {
        // set back to default
        switch (i) {
          case 0:
            keyHole1.styleProperty().set("-fx-fill: " + DEFAULT_COLOUR);
          case 1:
            keyHole2.styleProperty().set("-fx-fill: " + DEFAULT_COLOUR);
          case 2:
            keyHole3.styleProperty().set("-fx-fill: " + DEFAULT_COLOUR);
          case 3:
            keyHole4.styleProperty().set("-fx-fill: " + DEFAULT_COLOUR);
          case 4:
            keyHole5.styleProperty().set("-fx-fill: " + DEFAULT_COLOUR);
          case 5:
            keyHole6.styleProperty().set("-fx-fill: " + DEFAULT_COLOUR);
        }
      }
    }
  }

  // Method to update inventory in the UI
  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
  }

  @FXML
  public void updateTimerLabel(String time) {
    // Update the timer label in the UI
    lblTime.setText(time);
  }

  @FXML
  public void clickKeyHole1(MouseEvent event) {
    // check if correct key
    Rectangle keyHole = (Rectangle) event.getSource();
    clickKeyHoleHelper(1, keyHole);
  }

  @FXML
  public void clickKeyHole2(MouseEvent event) {
    // check if correct key
    Rectangle keyHole = (Rectangle) event.getSource();
    clickKeyHoleHelper(2, keyHole);
  }

  @FXML
  public void clickKeyHole3(MouseEvent event) {
    // check if correct key
    Rectangle keyHole = (Rectangle) event.getSource();
    clickKeyHoleHelper(3, keyHole);
  }

  @FXML
  public void clickKeyHole4(MouseEvent event) {
    // check if correct key
    Rectangle keyHole = (Rectangle) event.getSource();
    clickKeyHoleHelper(4, keyHole);
  }

  @FXML
  public void clickKeyHole5(MouseEvent event) {
    // check if correct key
    Rectangle keyHole = (Rectangle) event.getSource();
    clickKeyHoleHelper(5, keyHole);
  }

  @FXML
  public void clickKeyHole6(MouseEvent event) {
    // check if correct key
    Rectangle keyHole = (Rectangle) event.getSource();
    clickKeyHoleHelper(6, keyHole);
  }

  @FXML
  private void onReturnToCorridorClicked(ActionEvent event) {
    App.returnToCorridor();
  }

  private void clickKeyHoleHelper(int num, Rectangle keyHole) {
    // check if correct key
    // if the key is already correct then do nothing
    // check if inserting a key
    System.out.println("click key hole " + num);
    if (correctKeyMap.get("hole" + num) == "empty") {
      // in the case that the key hole is empty when clicked
      if (!keys.contains(inventoryChoiceBox.getValue())) {
        return;
      }

      // set to orange for inserting and add to key map
      keyHole.styleProperty().set("-fx-fill: #fab387");
      keyMap.put("hole" + num, inventoryChoiceBox.getValue());
      setLabelKeyHole(num, inventoryChoiceBox.getValue());

      // check if correct key
      if (keyHoleMap.get("hole" + num) == inventoryChoiceBox.getValue()) {
        System.out.println("correct key");
        correctKeys++;
        correctKeyMap.put("hole" + num, "true");
        System.out.println("removed key " + inventoryChoiceBox.getValue() + " from inventory");

      } else {
        System.out.println("incorrect key");
        correctKeyMap.put("hole" + num, "false");
      }
      inventoryChoiceBox.getItems().remove(inventoryChoiceBox.getValue());
      // sets to yellow for filled
    } else {
      // if its filled then set back to default (get back key) on click
      keyHole.styleProperty().set("-fx-fill: #1e90ff");
      // puts key states back to normal
      correctKeyMap.put("hole" + num, "empty");
      setLabelKeyHole(num, "");
      System.out.println("got back key " + keyHoleMap.get("hole" + num));
      inventoryChoiceBox.getItems().add(keyMap.get("hole" + num));
    }
  }

  private void setLabelKeyHole(int num, String key) {
    System.out.println("set label key hole " + num + " to " + key);
    if (key != "") {
      switch (num) {
        case 1:
          lblKey1.setText(key + " inserted");
          break;
        case 2:
          lblKey2.setText(key + " inserted");
          break;
        case 3:
          lblKey3.setText(key + " inserted");
          break;
        case 4:
          lblKey4.setText(key + " inserted");
          break;
        case 5:
          lblKey5.setText(key + " inserted");
          break;
        case 6:
          lblKey6.setText(key + " inserted");
          break;
      }
    } else {
      switch (num) {
        case 1:
          lblKey1.setText("");
          break;
        case 2:
          lblKey2.setText("");
          break;
        case 3:
          lblKey3.setText("");
          break;
        case 4:
          lblKey4.setText("");
          break;
        case 5:
          lblKey5.setText("");
          break;
        case 6:
          lblKey6.setText("");
          break;
      }
    }
  }
}
