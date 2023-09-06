package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class RoomController {

  @FXML private ComboBox<String> inventoryChoiceBox;

  @FXML private ImageView parchment1;

  @FXML private ImageView parchment2;

  @FXML private ImageView parchment3;

  @FXML private ImageView parchment4;

  @FXML
  void onParchment1MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment1.setScaleX(1.5);
    parchment1.setScaleY(1.5);
  }

  @FXML
  void onParchment2MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment2.setScaleX(1.5);
    parchment2.setScaleY(1.5);
  }

  @FXML
  void onParchment3MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment3.setScaleX(1.5);
    parchment3.setScaleY(1.5);
  }

  @FXML
  void onParchment4MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment4.setScaleX(1.5);
    parchment4.setScaleY(1.5);
  }

  @FXML
  void onParchment1MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment1.setScaleX(1.0);
    parchment1.setScaleY(1.0);
  }

  @FXML
  void onParchment2MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment2.setScaleX(1.0);
    parchment2.setScaleY(1.0);
  }

  @FXML
  void onParchment3MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment3.setScaleX(1.0);
    parchment3.setScaleY(1.0);
  }

  @FXML
  void onParchment4MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment4.setScaleX(1.0);
    parchment4.setScaleY(1.0);
  }

  @FXML
  void onParchment1Clicked(MouseEvent event) {
    // Add parchment1 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 1");
    // Make parchment1 non-visible and disabled
    parchment1.setVisible(false);
    parchment1.setDisable(true);
  }

  @FXML
  void onParchment2Clicked(MouseEvent event) {
    // Add parchment2 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 2");
    // Make parchment2 non-visible and disabled
    parchment2.setVisible(false);
    parchment2.setDisable(true);
  }

  @FXML
  void onParchment3Clicked(MouseEvent event) {
    // Add parchment3 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 3");
    // Make parchment3 non-visible and disabled
    parchment3.setVisible(false);
    parchment3.setDisable(true);
  }

  @FXML
  void onParchment4Clicked(MouseEvent event) {
    // Add parchment4 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 4");
    // Make parchment4 non-visible and disabled
    parchment4.setVisible(false);
    parchment4.setDisable(true);
  }

  // Add more methods for handling other parchments if needed

  // You can also add more event handlers for your specific requirements

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      showDialog("Info", "Riddle", "You need to resolve the riddle!");
      App.setRoot("chat");
      return;
    }

    if (!GameState.isKeyFound) {
      showDialog(
          "Info", "Find the key!", "You resolved the riddle, now you know where the key is.");
    } else {
      showDialog("Info", "You Won!", "Good Job!");
    }
  }

  /**
   * Handles the click event on the vase.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickVase(MouseEvent event) {
    System.out.println("vase clicked");
    if (GameState.isRiddleResolved && !GameState.isKeyFound) {
      showDialog("Info", "Key Found", "You found a key under the vase!");
      GameState.isKeyFound = true;
    }
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("window clicked");
  }
}
