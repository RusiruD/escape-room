package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Chat.AppUi;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.CustomNotifications;
import nz.ac.auckland.se206.DungeonMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Instructions;
import nz.ac.auckland.se206.Riddle;
import nz.ac.auckland.se206.TimerCounter;
import nz.ac.auckland.se206.Utility;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class ChestController implements Controller {

  private static ChestController instance;

  public static ChestController getInstance() {

    return instance;
  }

  private HashMap<String, String> keyHoleMap = new HashMap<String, String>();
  private HashMap<String, String> correctKeyMap = new HashMap<String, String>();
  private HashMap<String, String> keyMap = new HashMap<String, String>();
  private List<String> keys = Arrays.asList("key1", "key2", "key3");

  @FXML private Button riddleButton;

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

  @FXML private ImageView exclamationMark;
  @FXML private ImageView soundToggle;

  @FXML private Pane chestPane;
  @FXML private Pane popUp;
  @FXML private Pane riddleDisplay;
  @FXML private Pane visualDungeonMaster;

  @FXML private Pane instructionsDisplay;

  private final String defaultColour = "#1e90ff";
  private final String correctColour = "#a8e6cf";
  private final String incorrectColour = "#f38ba8";

  private String riddleQuestion;
  private String callQuestion;

  private DungeonMaster callDungeonMaster;
  private DungeonMaster riddleDungeonMaster;
  private Boolean riddleCalled = false;
  private Boolean key1Correct = false;
  private Boolean key2Correct = false;
  private Boolean key3Correct = false;

  @FXML private TextArea textArea;
  @FXML private TextField inputText;
  @FXML private Button showButton;
  @FXML private Button closeButton;
  @FXML private Button sendButton;
  @FXML private ImageView chatBackground;
  @FXML private Button switchButton;
  @FXML private Label hintField;
  private HintNode hintNode;
  private AppUi appUi;

  public void initialize() {
    TimerCounter.addTimerLabel(lblTime);

    // Initialize the instance field with the current instance of the class
    instance = this;

    callDungeonMaster = new DungeonMaster();
    riddleDungeonMaster = new DungeonMaster();

    visualDungeonMaster.visibleProperty().set(false);
    visualDungeonMaster.mouseTransparentProperty().set(true);

    TranslateTransition translateTransition = GameState.translate(exclamationMark);
    translateTransition.play();

    String instructionsString =
        "THE CHEST IS LOCKED!. \n \n"
            + "You need to find the correct keys to unlock the chest. \n \n"
            + "The keys are hidden in the dungeon. \n \n"
            + "You need to find the keys and insert them into the correct key holes. \n \n"
            + "The correct keys will turn green. \n \n"
            + "The incorrect keys will turn red. \n \n"
            + "The empty key holes will are blue. \n \n"
            + "Once you have inserted the correct keys, the chest will open. \n \n"
            + "Good luck!";
    Instructions instructions = new Instructions(instructionsString);
    Pane instructionsPane = instructions.getInstructionsPane();
    instructionsDisplay.getChildren().add(instructionsPane);
    instructionsPane.getStyleClass().add("riddle");

    instructionsDisplay.visibleProperty().set(false);
    instructionsDisplay.mouseTransparentProperty().set(true);

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
    riddleQuestion =
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
            + " long. Do not go over 100 words.";
    System.out.println(riddleQuestion);

    callQuestion =
        "Congratulate the player on solving the riddle and unlocking the chest with the solution"
            + " that key1 goest into keyhole "
            + solutions[0]
            + ", key2 goes into keyhole "
            + solutions[1]
            + ", and key3 goes into keyhole "
            + solutions[2]
            + " Tell the player that the sword and shield have fallen out to the corridor. Tell the"
            + " player that they can now return to the corridor and fight you. Be antagonistic and"
            + " confident that you will win. Keep this message short";
  }

  public void openChest(MouseEvent event) {
    System.out.println("open chest");
    // check if correct combination
    updateKeys();
    System.out.println("key1Correct " + key1Correct);
    System.out.println("key2Correct " + key2Correct);
    System.out.println("key3Correct " + key3Correct);
    if (key1Correct && key2Correct && key3Correct) {
      GameState.isChestOpened = true;
      // disable riddle button when finished
      riddleButton.visibleProperty().set(false);
      riddleButton.mouseTransparentProperty().set(true);
      App.makeSwordAndShieldAppear();
      // open chest
      System.out.println("chest opened");
      visualDungeonMaster.visibleProperty().set(true);
      visualDungeonMaster.mouseTransparentProperty().set(false);
      CustomNotifications.generateNotification(
          "Chest Opened!", "You hear the clanging of metal on the floor of the corridor...");
    }
  }

  public double getChestHeight() {
    return chestPane.getPrefHeight();
  }

  public double getChestWidth() {
    return chestPane.getPrefWidth();
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
  public void getInstructions(MouseEvent event) {
    // Set the instructions pane to be visible and not mouse transparent
    instructionsDisplay.visibleProperty().set(true);
    instructionsDisplay.mouseTransparentProperty().set(false);
    instructionsDisplay.toFront();
  }

  @FXML
  public void getAi(MouseEvent event) {
    callAi();
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
  public void getRiddle() {
    System.out.println("get riddle");
    if (riddleCalled) {
      System.out.println("riddle pane called");
      // gets the riddle pane if already asked dungeon master for riddle
      String riddleText = riddleDungeonMaster.getRiddle();
      Pane riddlePane = Riddle.riddlePane(riddleText);
      riddleDisplay.getChildren().add(riddlePane);
      riddlePane.getStyleClass().add("riddle");
      riddleDisplay.toFront();
      riddleDisplay.visibleProperty().set(true);
      riddleDisplay.mouseTransparentProperty().set(false);

    } else {
      // gets the dungeon master to speak the riddle dialogue
      popUp.visibleProperty().set(false);
      riddleDungeonMaster.createPopUp(popUp);
      riddleDungeonMaster.getText("user", riddleQuestion);
      // set style class
      popUp.getStyleClass().add("popUp");
      popUp.visibleProperty().set(true);
      popUp.mouseTransparentProperty().set(false);
      popUp.toFront();
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
        // sets the key states
        if (inventoryChoiceBox.getValue().equals("key1")) {
          key1Correct = true;
          System.out.println("key1 correct");
        } else if (inventoryChoiceBox.getValue().equals("key2")) {
          key2Correct = true;
          System.out.println("key2 correct");
        } else if (inventoryChoiceBox.getValue().equals("key3")) {
          key3Correct = true;
          System.out.println("key3 correct");
        }
        correctKeyMap.put("hole" + num, "true");
        System.out.println("removed key " + inventoryChoiceBox.getValue() + " from inventory");

      } else {
        System.out.println("incorrect key");
        correctKeyMap.put("hole" + num, "false");
      }
      inventoryChoiceBox.getItems().remove(inventoryChoiceBox.getValue());
      inventoryChoiceBox.setStyle(" -fx-effect: dropshadow(gaussian, #ff00ff, 10, 0.5, 0, 0);");

      // Create a Timeline to revert the shadow back to its original state after 2 seconds
      Duration duration = Duration.seconds(0.5);
      javafx.animation.Timeline timeline =
          new javafx.animation.Timeline(
              new javafx.animation.KeyFrame(
                  duration,
                  event -> {
                    // Revert the CSS style to remove the shadow (or set it to the original style)
                    inventoryChoiceBox.setStyle("");
                  }));
      timeline.play();
      // sets to yellow for filled
    } else {
      // if its filled then set back to default (get back key) on click
      keyHole.styleProperty().set("-fx-fill: #1e90ff");
      // resets the key states
      if (keyMap.get("hole" + num).equals("key1")) {
        key1Correct = false;
        System.out.println("key1 incorrect");
      } else if (keyMap.get("hole" + num).equals("key2")) {
        key2Correct = false;
        System.out.println("key2 incorrect");
      } else if (keyMap.get("hole" + num).equals("key3")) {
        key3Correct = false;
        System.out.println("key3 incorrect");
      }
      // puts key states back to normal
      correctKeyMap.put("hole" + num, "empty");
      setLabelKeyHole(num, "");
      System.out.println("got back key " + keyHoleMap.get("hole" + num));
      inventoryChoiceBox.getItems().add(keyMap.get("hole" + num));
      inventoryChoiceBox.setStyle(" -fx-effect: dropshadow(gaussian, #ff00ff, 10, 0.5, 0, 0);");

      // Create a Timeline to revert the shadow back to its original state after 2 seconds
      Duration duration = Duration.seconds(0.5);
      javafx.animation.Timeline timeline =
          new javafx.animation.Timeline(
              new javafx.animation.KeyFrame(
                  duration,
                  event -> {
                    // Revert the CSS style to remove the shadow (or set it to the original style)
                    inventoryChoiceBox.setStyle("");
                  }));
      timeline.play();
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

  // Call the AI to give a hint
  private void callAi() {
    // Get the dungeon master and the pop up pane
    popUp.visibleProperty().set(false);
    callDungeonMaster.createPopUp(popUp);
    callDungeonMaster.getText("user", callQuestion);
    // Set style class
    popUp.getStyleClass().add("popUp");
    popUp.visibleProperty().set(true);
    popUp.mouseTransparentProperty().set(false);
    popUp.toFront();

    visualDungeonMaster.visibleProperty().set(false);
    visualDungeonMaster.mouseTransparentProperty().set(true);
  }

  @FXML
  private void clickExit(MouseEvent event) {
    // Handle click on exit
    Utility.exitGame();
  }

  @FXML
  private void mute() {
    // Handle click on mute
    GameState.mute();
  }

  @FXML
  public void updateMute() {
    if (!GameState.isMuted) {
      soundToggle.setImage(new ImageView("images/sound/audioOn.png").getImage());
      return;
    }
    soundToggle.setImage(new ImageView("images/sound/audioOff.png").getImage());
  }

  @FXML
  private void onKeyboardInput(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode() == KeyCode.ENTER) {
      onClickSend(null);
    }
  }

  @FXML
  private void onCreate(ActionEvent event) {
    GameState.chat.massEnable(appUi);
  }

  private void processChatRequest() {
    try {
      GameState.chat.onSendMessage(inputText.getText(), appUi);
    } catch (Exception e) {
      e.printStackTrace();
    }
    inputText.clear();
  }

  @FXML
  private void onClickSend(ActionEvent event) {
    processChatRequest();
  }

  public void initialiseStart() {
    // Set the initial UI state to 'CHEST'.
    appUi = AppUi.CHEST;

    // Create a new HintNode instance with various UI elements.
    hintNode =
        new HintNode(
            textArea,
            inputText,
            showButton,
            closeButton,
            sendButton,
            chatBackground,
            switchButton,
            hintField);

    // Add the UI state and its corresponding HintNode to the chat interface.
    GameState.chat.addToMap(appUi, hintNode);

    // Close the chat interface to reset its state.
    onHandleChat(null);

    // Add the text area for displaying chat messages to the chat interface.
    GameState.chat.addChat(textArea);
  }

  @FXML
  private void onHandleChat(ActionEvent event) {
    GameState.chat.massDisable(appUi);
  }

  @FXML
  private void onSwitchView(ActionEvent event) {
    GameState.chat.lastHintToggle();
  }
}
