package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.beans.binding.BooleanExpression;
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
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Chat;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Instructions;
import nz.ac.auckland.se206.Utililty;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class PuzzleRoomController implements Controller {

  private static PuzzleRoomController instance;

  public static PuzzleRoomController getInstance() {
    return instance;
  }

  @FXML private Pane puzzleRoomPane;
  @FXML private Label lblTime;
  @FXML private ImageView key3;
  @FXML private ImageView soundToggle;

  @FXML private ComboBox<String> inventoryChoiceBox;
  @FXML private Pane instructionsDisplay;

  @FXML private TextArea textArea;
  @FXML private TextField inputText;
  @FXML private Button showButton;
  @FXML private Button closeButton;
  @FXML private Button sendButton;
  @FXML private ImageView chatBackground;
  @FXML private Button switchButton;
  @FXML private Label hintField;
  private HintNode hintNode;
  private Chat.AppUi appUi;

  public void initialize() {
    // Set the instance
    key3.visibleProperty().bind(GameState.puzzleRoomSolved);
    // Bind the key3's disable property to the puzzleRoomSolved property
    key3.disableProperty().bind(((BooleanExpression) GameState.getPuzzleRoomSolved()).not());
    // Bind the inventory choice box to the inventory
    instance = this;
    String instructionsString = "Click the center of the door to enter \n\n";
    // Add the instructions to the pane
    Instructions instructions = new Instructions(instructionsString);
    Pane instructionsPane = instructions.getInstructionsPane();
    instructionsDisplay.getChildren().add(instructionsPane);
    instructionsPane.getStyleClass().add("riddle");
    // Set the instructions pane to be invisible and mouse transparent
    instructionsDisplay.visibleProperty().set(false);
    instructionsDisplay.mouseTransparentProperty().set(true);
  }

  @FXML
  private void clickPuzzle(MouseEvent event) throws IOException {
    App.setRoot(AppUi.PUZZLE);
  }

  @FXML
  private void onKey3Clicked(MouseEvent event) {
    GameState.hasKeyThree = true;
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
    GameState.currentRoom = GameState.State.CHEST;
  }

  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
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

  @FXML
  public void getInstructions(MouseEvent event) {
    // Set the instructions pane to be visible and not mouse transparent
    instructionsDisplay.visibleProperty().set(true);
    instructionsDisplay.mouseTransparentProperty().set(false);
    instructionsDisplay.toFront();
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
  private void clickExit(MouseEvent event) {
    // Handle click on exit
    Utililty.exitGame();
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
  private void onKeyPressed(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode() == KeyCode.ENTER) {
      handleTextInput();
    }
  }

  private void handleTextInput() {
    try {
      GameState.chat.onSendMessage(inputText.getText(), appUi);
    } catch (Exception e) {
      e.printStackTrace();
    }
    inputText.clear();
  }

  @FXML
  private void onSendMessage(ActionEvent event) {
    handleTextInput();
  }

  @FXML
  private void onShowChat(ActionEvent event) {
    GameState.chat.massEnable(appUi);
  }

  @FXML
  private void onCloseChat(ActionEvent event) {
    GameState.chat.massDisable(appUi);
  }

  public void initialiseAfterStart() {
    // Set the current application UI to PUZZLEROOM
    appUi = Chat.AppUi.PUZZLEROOM;

    // Create a new HintNode with UI components: textArea, inputText, showButton,
    // closeButton, sendButton, chatBackground, switchButton, and hintField
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

    // Add the HintNode to the chat map in the GameState
    GameState.chat.addToMap(appUi, hintNode);

    // Close the chat interface (onCloseChat method is called with null parameter)
    onCloseChat(null);

    // Add the text area to the chat
    GameState.chat.addChat(textArea);
  }

  @FXML
  private void onSwitchChatView(ActionEvent event) {
    GameState.chat.lastHintToggle();
  }
}
