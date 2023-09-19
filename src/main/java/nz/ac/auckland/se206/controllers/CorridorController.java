package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Controller;

public class CorridorController implements Controller {

  private static CorridorController instance;

  public static CorridorController getInstance() {
    return instance;
  }

  // Boolean properties to track key presses for movement
  private BooleanProperty forwardPressed = new SimpleBooleanProperty();
  private BooleanProperty leftPressed = new SimpleBooleanProperty();
  private BooleanProperty backwardPressed = new SimpleBooleanProperty();
  private BooleanProperty rightPressed = new SimpleBooleanProperty();

  // A binding to check if any movement key is pressed
  private BooleanBinding keyPressed = forwardPressed.or(leftPressed)
      .or(backwardPressed).or(rightPressed);

  private int movementSpeed = 2;

  // JavaFX UI elements

  @FXML
  private Polygon polygon;
  @FXML
  private Group group;
  @FXML
  private Rectangle player;
  @FXML
  private Rectangle treasureChest;
  @FXML
  private Rectangle door1;
  @FXML
  private Rectangle door2;
  @FXML
  private Rectangle border1;
  @FXML

  private Rectangle door3;
  @FXML
  private ImageView sword;
  @FXML
  private Pane room;
  @FXML
  private Label lblTime;
  @FXML
  private ComboBox<String> inventoryChoiceBox;

  // Animation timer for player movement
  private AnimationTimer playerTimer = new AnimationTimer() {
    @Override
    public void handle(long timestamp) {
      // Handle player movement
      if (forwardPressed.get()) {
        player.rotateProperty().set(0);
        if (playerStaysInRoom(polygon, player, "W")) {
          player.setY(player.getY() - movementSpeed);
        }
      }
      // Handle left movement
      if (leftPressed.get()) {
        player.rotateProperty().set(-90);
        if (playerStaysInRoom(polygon, player, "A")) {
          player.setX(player.getX() - movementSpeed);
        }
      }
      // Handle backward movement
      if (backwardPressed.get()) {
        player.rotateProperty().set(180);
        if (playerStaysInRoom(polygon, player, "S")) {
          player.setY(player.getY() + movementSpeed);
        }
      }
      // Handle right movement
      if (rightPressed.get()) {
        player.rotateProperty().set(90);
        if (playerStaysInRoom(polygon, player, "D")) {
          player.setX(player.getX() + movementSpeed);
        }
      }
    }
  };

  // Animation timer for collision detection
  private AnimationTimer collisionTimer = new AnimationTimer() {
    @Override
    public void handle(long timestamp) {
      // Check for collisions with doors and handle navigation
      checkCollision();
    }
  };

  @FXML
  public void onSwordClicked(MouseEvent event) {
    Inventory.addToInventory("sword");
    sword.setVisible(false);
    sword.setDisable(true);
  }

  public void initialize() {
    instance = this;
    Image image = new Image("/images/character.png");
    player.setFill(new ImagePattern(image));
    // Listener to start/stop timers based on key presses
    keyPressed.addListener((observable, boolValue, randomVar) -> {
      if (!boolValue) {
        // Start the player movement and collision detection timers
        playerTimer.start();
        collisionTimer.start();
      } else {
        // Stop the timers when no movement keys are pressed
        playerTimer.stop();
        collisionTimer.stop();
      }
    });
  }

  // Method to check if the player stays in the room while moving
  private boolean playerStaysInRoom(Polygon polygon, Rectangle player, String direction) {
    double bottomRightX = player.getX() + player.getWidth();
    double bottomRightY = player.getY() + player.getHeight();

    // Check if player stays in the room while moving in the specified direction
    if (direction.equals("W")) {
      return (polygon.contains(player.getX(), player.getY() - movementSpeed))
          && (polygon.contains(bottomRightX, bottomRightY - movementSpeed));
    } else if (direction.equals("A")) {
      return (polygon.contains(player.getX() - movementSpeed, player.getY()))
          && polygon.contains(bottomRightX - movementSpeed, bottomRightY);
    } else if (direction.equals("S")) {
      return (polygon.contains(player.getX(), player.getY() + movementSpeed))
          && polygon.contains(bottomRightX, bottomRightY + movementSpeed);
    } else if (direction.equals("D")) {
      return (polygon.contains(player.getX() + movementSpeed, player.getY()))
          && polygon.contains(bottomRightX + movementSpeed, bottomRightY);
    } else {
      return false;
    }
  }

  // Method to check collision with doors and handle navigation
  private void checkCollision() {
    // Check collision with door1 and navigate to a new room if needed
    if (player.intersects(door1.getBoundsInParent())) {
      try {
        stopMovement();
        App.setRoot(SceneManager.AppUi.PUZZLEROOM);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Check collision with door2 and navigate to a new room if needed
    if (player.getBoundsInParent().intersects(door2.getBoundsInParent())) {
      try {
        stopMovement();
        App.setRoot(SceneManager.AppUi.FIRST_ROOM);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Check collision with door3 and navigate to a new room if needed
    if (player.getBoundsInParent().intersects(door3.getBoundsInParent())) {
      try {
        stopMovement();
        App.setRoot(SceneManager.AppUi.UNTANGLE);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Method to stop player movement
  private void stopMovement() {
    forwardPressed.set(false);
    leftPressed.set(false);
    backwardPressed.set(false);
    rightPressed.set(false);
  }

  @FXML
  public void onKeyPressed(KeyEvent event) {
    // Handle key press events
    switch (event.getCode()) {
      case W:
        forwardPressed.set(true);
        break;
      case A:
        leftPressed.set(true);
        break;
      case S:
        backwardPressed.set(true);
        break;
      case D:
        rightPressed.set(true);
        break;
      default:
        break;
    }
  }

  @FXML
  public void onKeyReleased(KeyEvent event) {
    // Handle key release events
    switch (event.getCode()) {
      case W:
        forwardPressed.set(false);
        break;
      case A:
        leftPressed.set(false);
        break;
      case S:
        backwardPressed.set(false);
        break;
      case D:
        rightPressed.set(false);
        break;
      default:
        break;
    }
  }

  @FXML
  public void onTreasureChestClicked(MouseEvent event) {
    // Handle click on treasure chest
    System.out.println("clicked");
    String selectedItem = inventoryChoiceBox.getSelectionModel().getSelectedItem();
    if (GameState.isLock2Unlocked == true && GameState.isLock1Unlocked == true && GameState.isLock3Unlocked == true) {
      sword.setVisible(true);
      sword.setDisable(false);
    } else {
      if (selectedItem != null) {
        if (selectedItem.equals("1")) {
          GameState.isLock1Unlocked = true;
          Inventory.removeFromInventory("key1");
          inventoryChoiceBox.getSelectionModel().clearSelection();
        }
        if (selectedItem.equals("key2")) {
          GameState.isLock2Unlocked = true;
          Inventory.removeFromInventory("key2");
          inventoryChoiceBox.getSelectionModel().clearSelection();
        }
        if (selectedItem.equals("key3")) {
          GameState.isLock3Unlocked = true;
          Inventory.removeFromInventory("key3");
          inventoryChoiceBox.getSelectionModel().clearSelection();
        }
      }
    }
  }

  @FXML
  private void clickExit(MouseEvent event) {
    // Handle click on exit
    System.exit(0);
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
}