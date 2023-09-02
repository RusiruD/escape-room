package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;

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
  private Rectangle collider;

  @FXML
  private Pane room;

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

  public void initialize() {
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

  private void checkCollision(Rectangle player, Rectangle colliderObject) {
    if (player.getBoundsInParent().intersects(colliderObject.getBoundsInParent())) {
      try {
        stopMovement();
        App.setRoot(SceneManager.AppUi.CHAT);
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
    System.out.println("key " + event.getCode() + " pressed");
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
    }
  }

}
