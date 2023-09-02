package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;

/** Controller class for the room view. */
public class RoomController {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

  private int movementSpeed = 2;

  @FXML
  private Rectangle player;
  @FXML
  private Rectangle collider;

  @FXML
  private Pane room;

  @FXML
  private Rectangle door;
  @FXML
  private Rectangle window;
  @FXML
  private Rectangle vase;

  AnimationTimer playerTimer = new AnimationTimer() {
    @Override
    public void handle(long timestamp) {
      if (wPressed.get()) {
        player.setY(player.getY() - movementSpeed);
      }

      if (aPressed.get()) {
        player.setX(player.getX() - movementSpeed);
      }

      if (sPressed.get()) {
        player.setY(player.getY() + movementSpeed);
      }

      if (dPressed.get()) {
        player.setX(player.getX() + movementSpeed);
      }
    }
  };

  AnimationTimer collisionTimer = new AnimationTimer() {
    @Override
    public void handle(long timestamp) {
      checkCollision(player, collider);
    }
  };

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    movementSetup();

    keyPressed.addListener((observable, aBoolean, t1) -> {
      if (!aBoolean) {
        playerTimer.start();
        collisionTimer.start();
      } else {
        playerTimer.stop();
        collisionTimer.stop();
      }
    });
  }

  public void movementSetup() {
    room.setOnKeyPressed(e -> {
      switch (e.getCode()) {
        case W:
          wPressed.set(true);
          break;
        case A:
          aPressed.set(true);
          break;
        case S:
          sPressed.set(true);
          break;
        case D:
          dPressed.set(true);
          break;
      }
    });

    room.setOnKeyReleased(e -> {
      switch (e.getCode()) {
        case W:
          wPressed.set(false);
          break;
        case A:
          aPressed.set(false);
          break;
        case S:
          sPressed.set(false);
          break;
        case D:
          dPressed.set(false);
          break;
      }
    });
  }

  private void checkCollision(Rectangle player, Rectangle colliderObject) {
    if (player.getBoundsInParent().intersects(colliderObject.getBoundsInParent())) {
      System.out.println("collisionROOM");
    }
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
   * @param title      the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message    the message content of the dialog box
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
      App.setRoot(SceneManager.AppUi.CHAT);
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
