package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.scene.shape.Rectangle;

import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.DungeonMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Instructions;
import nz.ac.auckland.se206.Riddle;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

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

  @FXML private ComboBox<String> inventoryChoiceBox;

  @FXML private Label lblTime;

  @FXML private Rectangle keyHole1;
  @FXML private Rectangle keyHole2;
  @FXML private Rectangle keyHole3;
  @FXML private Rectangle keyHole4;
  @FXML private Rectangle keyHole5;
  @FXML private Rectangle keyHole6;

  @FXML private Label lblKey1;
  @FXML private Label lblKey2;
  @FXML private Label lblKey3;
  @FXML private Label lblKey4;
  @FXML private Label lblKey5;
  @FXML private Label lblKey6;

  @FXML private Pane chest;
  @FXML private Pane popUp;
  @FXML private Pane riddleDisplay;

  @FXML private Pane instructionsDisplay;

  private final String defaultColour = "#1e90ff";
  private final String correctColour = "#a8e6cf";
  private final String incorrectColour = "#f38ba8";

  private Riddle riddle;
  private Boolean riddleCalled = false;

  public void initialize() {

    // Initialize the instance field with the current instance of the class
    instance = this;

    String instructionsString = "INSTRUCTIONS GO HERE";
    Instructions instructions = new Instructions(instructionsString);
    Pane instructionsPane = instructions.getInstructionsPane();
    instructionsDisplay.getChildren().add(instructionsPane);
    instructionsPane.getStyleClass().add("riddle");

    // Set text for key labels
    lblKey1.setText("");
    lblKey2.setText("");
    lblKey3.setText("");
    lblKey4.setText("");
    lblKey5.setText("");
    lblKey6.setText("");

    // Create an ArrayList to store random numbers and a map to represent key holes
    // Initialize the key hole map with empty slots
    ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
    for (int i = 1; i <= 6; i++) {
      randomNumbers.add(i);
      keyHoleMap.put("hole" + i, "empty");
    }

    // Create an array to store solutions and shuffle the random numbers
    int[] solutions = new int[3];
    Collections.shuffle(randomNumbers);

    // Assign keys to shuffled key holes and print the assignments
    for (int i = 0; i < 3; i++) {
      keyHoleMap.put("hole" + randomNumbers.get(i), keys.get(i));
      System.out.println("hole" + randomNumbers.get(i) + " " + keys.get(i));
      solutions[i] = randomNumbers.get(i);
    }

    // Initialize the correct key map with empty slots
    for (int i = 0; i < 6; i++) {
      correctKeyMap.put("hole" + (i + 1), "empty");
    }

    // Create a riddle question
    String question =
        "You are the dungeon master of an escape room. Tell me a riddle where the first solution is"
            + " "
            + solutions[0]
            + ", the second solution is "
            + solutions[1]
            + ", and the third solution is "
            + solutions[2]
            + ". Hide the answers within the riddle but do not use the numbers within the riddle"
            + " instead use synonyms. Do not, under no circumstance, give the user the answer to"
            + " the riddles. After every sentence do a line break. Make the riddle a few sentences"
            + " long. Do not go over 150 words.";
    System.out.println(question);

    // Create a DungeonMaster and initiate a task to generate a riddle
    DungeonMaster dungeonMaster = new DungeonMaster();
    riddle = new Riddle(dungeonMaster, question);
  }

  public void openChest(MouseEvent event) {
    System.out.println("open chest");
    // check if correct combination
    updateKeys();
    if (correctKeys == 3) {
      GameState.isChestOpened = true;
      // open chest
      System.out.println("chest opened");
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
            keyHole1.styleProperty().set("-fx-fill: " + correctColour);
            keyHole1.mouseTransparentProperty().set(true);
            break;
          case 1:
            keyHole2.styleProperty().set("-fx-fill: " + correctColour);
            keyHole2.mouseTransparentProperty().set(true);
            break;
          case 2:
            keyHole3.styleProperty().set("-fx-fill: " + correctColour);
            keyHole3.mouseTransparentProperty().set(true);
            break;
          case 3:
            keyHole4.styleProperty().set("-fx-fill: " + correctColour);
            keyHole4.mouseTransparentProperty().set(true);
            break;
          case 4:
            keyHole5.styleProperty().set("-fx-fill: " + correctColour);
            keyHole5.mouseTransparentProperty().set(true);
            break;
          case 5:
            keyHole6.styleProperty().set("-fx-fill: " + correctColour);
            keyHole6.mouseTransparentProperty().set(true);
            break;
        }
      } else if (correctKeyMap.get("hole" + (i + 1)) == "false") {
        // set to red for incorrect and let them try again
        System.out.println("hole " + (i + 1) + " is incorrect");
        switch (i) {
          case 0:
            keyHole1.styleProperty().set("-fx-fill: " + incorrectColour);
            break;
          case 1:
            keyHole2.styleProperty().set("-fx-fill: " + incorrectColour);
            break;
          case 2:
            keyHole3.styleProperty().set("-fx-fill: " + incorrectColour);
            break;
          case 3:
            keyHole4.styleProperty().set("-fx-fill: " + incorrectColour);
            break;
          case 4:
            keyHole5.styleProperty().set("-fx-fill: " + incorrectColour);
            break;
          case 5:
            keyHole6.styleProperty().set("-fx-fill: " + incorrectColour);
            break;
        }
      } else if (correctKeyMap.get("hole" + (i + 1)) == "empty") {
        // set back to default
        switch (i) {
          case 0:
            keyHole1.styleProperty().set("-fx-fill: " + defaultColour);
            break;
          case 1:
            keyHole2.styleProperty().set("-fx-fill: " + defaultColour);
            break;
          case 2:
            keyHole3.styleProperty().set("-fx-fill: " + defaultColour);
            break;
          case 3:
            keyHole4.styleProperty().set("-fx-fill: " + defaultColour);
            break;
          case 4:
            keyHole5.styleProperty().set("-fx-fill: " + defaultColour);
            break;
          case 5:
            keyHole6.styleProperty().set("-fx-fill: " + defaultColour);
            break;
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
  public void getInstructions(MouseEvent event) {
    // Set the instructions pane to be visible and not mouse transparent
    instructionsDisplay.visibleProperty().set(true);
    instructionsDisplay.mouseTransparentProperty().set(false);
    instructionsDisplay.toFront();
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

  @FXML
  public void clickButton(MouseEvent event) {
    DungeonMaster dungeonMaster = new DungeonMaster();
    String message = "print a lines of text";
    Riddle call = new Riddle(dungeonMaster, message);
    Button master = (Button) event.getSource();
    master.setOnMouseClicked(e -> {
      if (!dungeonMaster.isMessageFinished()) callAi(call);
      else { master.visibleProperty().set(false); }
    });
  }

  @FXML
  public void getRiddle() {
    DungeonMaster dungeonMaster = riddle.getDungeonMaster();
    System.out.println("get riddle");
    if (riddleCalled) {
      System.out.println("riddle pane called");
      // gets the riddle pane if already asked dungeon master for riddle
      String riddleText = riddle.getRiddle();

      Pane riddlePane = riddle.riddlePane(riddleText);
      riddleDisplay.getChildren().add(riddlePane);
      riddlePane.getStyleClass().add("riddle");
      riddleDisplay.toFront();
      riddleDisplay.visibleProperty().set(true);
      riddleDisplay.mouseTransparentProperty().set(false);

    } else {
      // gets the dungeon master to speak the riddle dialogue
      Pane dialogue = dungeonMaster.getPopUp();
      Pane dialogueFormat = dungeonMaster.paneFormat(dialogue, dungeonMaster);
      popUp.getChildren().add(dialogueFormat);

      dialogueFormat.getStyleClass().add("popUp");
      riddleCalled = true;
    }
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
      if (keyHoleMap.get("hole" + num).equals(inventoryChoiceBox.getValue())) {
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

  /**
   * Sets the label for a keyhole to display the inserted key's status.
   *
   * @param num The number of the keyhole.
   * @param key The key inserted into the keyhole.
   */
  private void setLabelKeyHole(int num, String key) {
    // Print a debug message indicating which keyhole is being set
    System.out.println("set label key hole " + num + " to " + key);

    // Check if a key is inserted
    if (!key.isEmpty()) {
      // Depending on the keyhole number, set the corresponding label to display the key inserted
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
      // If no key is inserted, clear the label for the corresponding keyhole
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

  @FXML
  public void getHint() throws IOException {
    App.setRoot(AppUi.CHAT);
  }

  private void callAi(Riddle call) {
    DungeonMaster dungeonMaster = call.getDungeonMaster();
    Pane dialogue = dungeonMaster.getPopUp();
    Pane dialogueFormat = dungeonMaster.paneFormat(dialogue, dungeonMaster);
    popUp.getChildren().add(dialogueFormat);

    dialogueFormat.getStyleClass().add("popUp");
  }
}
