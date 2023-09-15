package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class CorridorController {

  private BooleanProperty wPressed = new SimpleBooleanProperty();
  private BooleanProperty aPressed = new SimpleBooleanProperty();
  private BooleanProperty sPressed = new SimpleBooleanProperty();
  private BooleanProperty dPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

  private int movementSpeed = 2;

  @FXML
  private Rectangle player;
  @FXML
  private Rectangle door1;
  @FXML
  private Rectangle door2;
  @FXML
  private Rectangle left;
  @FXML
  private Rectangle top;
  @FXML
  private Rectangle right;
  @FXML
  private Rectangle bottom;

  @FXML
  private Pane room;

    @FXML
  private Label lblTime;

  @FXML
  private ComboBox<String> inventoryChoiceBox;

  private static CorridorController instance;

  public static CorridorController getInstance() {
    return instance;
  }

  AnimationTimer playerTimer = new AnimationTimer() {

    @Override
    public void handle(long timestamp) {
      // updateInventory();
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
      checkCollision();
    }
  };

  public void initialize() {
    instance = this;
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

  private void checkCollision() {

    // hit left wall
    if (player.getBoundsInParent().intersects(left.getBoundsInParent())) {
      stopMovement();
      player.setLayoutX(left.getLayoutX());
      player.setX(left.getX() + left.getWidth() + 1);
    }

    // hit right wall
    if (player.getBoundsInParent().intersects(right.getBoundsInParent())) {
      stopMovement();
      player.setLayoutX(right.getLayoutX());
      player.setX(right.getX() - player.getWidth() - 1);
    }

    // hit top wall
    if (player.getBoundsInParent().intersects(top.getBoundsInParent())) {
      stopMovement();
      player.setLayoutY(top.getLayoutY());
      player.setY(top.getY() + top.getHeight() + 1);
    }

    // hit bottom wall
    if (player.getBoundsInParent().intersects(bottom.getBoundsInParent())) {
      stopMovement();
      player.setLayoutY(bottom.getLayoutY());
      player.setY(bottom.getY() - player.getHeight() - 1);
    }

    // hit door1
    if (player.getBoundsInParent().intersects(door1.getBoundsInParent())) {
      try {
        stopMovement();
        App.setRoot(SceneManager.AppUi.PUZZLEROOM);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // hit door2

    if (player.getBoundsInParent().intersects(door2.getBoundsInParent())) {
      try {
        stopMovement();
        App.setRoot(SceneManager.AppUi.FIRST_ROOM);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void stopMovement() {
    wPressed.set(false);
    aPressed.set(false);
    sPressed.set(false);
    dPressed.set(false);
  }

  @FXML
  public void onKeyPressed(KeyEvent event) {
    switch (event.getCode()) {
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
      default:
        break;
    }
  }

  @FXML
  public void onKeyReleased(KeyEvent event) {
    switch (event.getCode()) {
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
      default:
        break;
    }
  }

  @FXML
  private void clickExit(MouseEvent event) {
    System.exit(0);
  }

  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }
}
