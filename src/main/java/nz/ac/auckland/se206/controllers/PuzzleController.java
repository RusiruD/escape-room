package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.CustomNotifications;
import nz.ac.auckland.se206.DungeonMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Instructions;
import nz.ac.auckland.se206.Utililty;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class PuzzleController implements Controller {

  private static PuzzleController instance;

  public static PuzzleController getInstance() {
    return instance;
  }

  private String[][] tiles;
  private String[][] solution;
  @FXML private Pane puzzlePane;
  @FXML private ImageView one;
  @FXML private ImageView two;
  @FXML private ImageView three;
  @FXML private ImageView four;
  @FXML private ImageView five;
  @FXML private ImageView six;
  @FXML private ImageView zero;
  @FXML private ImageView eight;
  @FXML private ImageView nine;

  @FXML private ImageView exclamationMark;
  @FXML private ImageView soundToggle;

  private boolean hasSelection = false;
  private ImageView firstSelection;
  private ImageView secondSelection;

  private DungeonMaster callDungeonMaster;
  private String callQuestion;

  @FXML private Label lblTime;

  @FXML private Pane instructionsDisplay;
  @FXML private Pane popUp;
  @FXML private Pane visualDungeonMaster;
  @FXML private ComboBox<String> inventoryChoiceBox;

  @FXML private TextArea textArea;
  @FXML private TextField inputText;
  @FXML private Button showButton;
  @FXML private Button closeButton;
  @FXML private Button sendButton;
  @FXML private ImageView chatBackground;
  @FXML private Button switchButton;
  @FXML private Label hintField;

  public void initialize() {
    // set the instance
    instance = this;

    popUp.toBack();

    visualDungeonMaster.visibleProperty().set(false);
    visualDungeonMaster.mouseTransparentProperty().set(true);

    TranslateTransition translateTransition = GameState.translate(exclamationMark);
    translateTransition.play();

    String instructionsString =
        "The tiles are in the wrong order. \n\n"
            + "Click on a tile to select it. Then click on the zero tile to swap the tiles. \n\n"
            + "The zero tile can only be swapped with the tiles directly above, below, to the left,"
            + " or to the right of it. \n\n"
            + "The puzzle is solved when the tiles are in the correct order. \n\n";
    Instructions instructions = new Instructions(instructionsString);
    Pane instructionsPane = instructions.getInstructionsPane();
    instructionsDisplay.getChildren().add(instructionsPane);
    instructionsPane.getStyleClass().add("riddle");

    instructionsDisplay.visibleProperty().set(false);
    instructionsDisplay.mouseTransparentProperty().set(true);
    // set the tiles and solution
    tiles =
        new String[][] {
          {"one", "two", "three"}, {"four", "five", "six"}, {"zero", "eight", "nine"}
        };
    solution =
        new String[][] {
          {"one", "two", "zero"}, {"four", "six", "three"}, {"eight", "five", "nine"}
        };

    callQuestion =
        "Congratulate the user on rearranging the tiles correctly and solving the puzzle.";

    callDungeonMaster = new DungeonMaster();
  }

  public double getPuzzleWidth() {
    return puzzlePane.getPrefWidth();
  }

  public double getPuzzleHeight() {
    return puzzlePane.getPrefHeight();
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
    if ((apos[0] == bpos[0] && Math.abs(apos[1] - bpos[1]) == 1)
        ^ (apos[1] == bpos[1] && Math.abs(apos[0] - bpos[0]) == 1)) {
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

      visualDungeonMaster.visibleProperty().set(true);
      visualDungeonMaster.mouseTransparentProperty().set(false);
    }
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
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
  public void getHint() throws IOException {
    App.goToChat();
  }

  @FXML
  public void getAi(MouseEvent event) {
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
  public void getInstructions(MouseEvent event) {
    // Set the instructions pane to be visible and not mouse transparent
    instructionsDisplay.visibleProperty().set(true);
    instructionsDisplay.mouseTransparentProperty().set(false);
    instructionsDisplay.toFront();
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

  private void handleTextInput() {
    try {
      GameState.chat.onSendMessage(
          inputText.getText(), textArea, sendButton, switchButton, hintField, closeButton);
    } catch (ApiProxyException | IOException e) {
      e.printStackTrace();
    }
    inputText.clear();
  }

  @FXML
  private void onSendMessage(ActionEvent event) {
    handleTextInput();
  }

  public void addChatToList() {
    GameState.chat.addChat(textArea);
  }

  public void initialiseAfterStart() {
    onCloseChat(null);
    addChatToList();
  }

  @FXML
  private void onSwitchChatView(ActionEvent event) {
    GameState.chat.lastHintToggle();
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
  private void onShowChat(ActionEvent event) {
    GameState.chat.massEnable(
        textArea,
        inputText,
        closeButton,
        showButton,
        chatBackground,
        sendButton,
        switchButton,
        hintField);
  }

  @FXML
  private void onCloseChat(ActionEvent event) {
    GameState.chat.massDisable(
        textArea,
        inputText,
        closeButton,
        showButton,
        chatBackground,
        sendButton,
        switchButton,
        hintField);
  }

  @FXML
  private void onKeyPressed(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode() == KeyCode.ENTER) {
      onSendMessage(null);
    }
  }
}
