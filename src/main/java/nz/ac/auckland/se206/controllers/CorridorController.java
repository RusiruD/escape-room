package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Riddle;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.DungeonMaster;

public class CorridorController implements Controller {

  private static CorridorController instance;

  public static CorridorController getInstance() {
    return instance;
  }

  private BooleanProperty forwardPressed = new SimpleBooleanProperty();
  private BooleanProperty leftPressed = new SimpleBooleanProperty();
  private BooleanProperty backwardPressed = new SimpleBooleanProperty();
  private BooleanProperty rightPressed = new SimpleBooleanProperty();

  private BooleanBinding keyPressed = forwardPressed.or(leftPressed)
      .or(backwardPressed).or(rightPressed);

  private int movementSpeed = 2;

  @FXML
  private Rectangle player;
  @FXML
  private Rectangle treasureChest;
  @FXML
  private Rectangle door1;
  @FXML
  private Rectangle door2;
  @FXML
  private Rectangle door3;
  @FXML
  private Rectangle left;
  @FXML
  private Rectangle top;
  @FXML
  private Rectangle right;
  @FXML
  private Rectangle bottom;
  @FXML
  private ImageView sword;
  @FXML
  private Pane room;

  @FXML
  private Pane popUp;
  @FXML
  private Pane riddleDisplay;

  @FXML
  private Label lblTime;

  @FXML
  private ComboBox<String> inventoryChoiceBox;

  private Riddle riddle;
  private Boolean riddleCalled = false;

  private AnimationTimer playerTimer = new AnimationTimer() {

    @Override
    public void handle(long timestamp) {
      // updateInventory();
      if (forwardPressed.get()) {
        player.setY(player.getY() - movementSpeed);
      }

      if (leftPressed.get()) {
        player.setX(player.getX() - movementSpeed);
      }

      if (backwardPressed.get()) {
        player.setY(player.getY() + movementSpeed);
      }

      if (rightPressed.get()) {
        player.setX(player.getX() + movementSpeed);
      }
    }
  };

  private AnimationTimer collisionTimer = new AnimationTimer() {
    @Override
    public void handle(long timestamp) {
      checkCollision();
    }
  };

  public void initialize() {
    instance = this;
    keyPressed.addListener((observable, boolValue, randomVar) -> {
      if (!boolValue) {
        playerTimer.start();
        collisionTimer.start();
      } else {
        playerTimer.stop();
        collisionTimer.stop();
      }
    });

    DungeonMaster dungeonMaster = new DungeonMaster();
    Task<Void> task = new Task<Void>() {
      @Override
      public Void call() throws Exception {
        riddle = new Riddle(dungeonMaster);
        return null;
      }
    };
    Thread thread = new Thread(task);
    thread.start();
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

    // hit door3
    if (player.getBoundsInParent().intersects(door3.getBoundsInParent())) {
      try {
        stopMovement();
        App.setRoot(SceneManager.AppUi.UNTANGLE);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void stopMovement() {
    forwardPressed.set(false);
    leftPressed.set(false);
    backwardPressed.set(false);
    rightPressed.set(false);
  }

  @FXML
  public void onKeyPressed(KeyEvent event) {
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
    System.out.println("clicked");
    String selectedItem = inventoryChoiceBox.getSelectionModel().getSelectedItem();
    if (GameState.isLock2Unlocked == true && GameState.isLock1Unlocked == true) {
      sword.setVisible(true);
      sword.setDisable(false);
      sword.toFront();

    }
    if (selectedItem != null) {
      if (selectedItem.contains("key1")) {
        Inventory.removeFromInventory(selectedItem);

        GameState.isLock1Unlocked = true;
      } else if (selectedItem.contains("key2")) {
        Inventory.removeFromInventory(selectedItem);
        GameState.isLock2Unlocked = true;
      }
    }

  }

  @FXML
  public void onSwordClicked(MouseEvent event) {
    Inventory.addToInventory("sword");
    sword.setVisible(false);
    sword.setDisable(true);
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

  @FXML
  public void getRiddle() {
    if (!riddle.hasRiddle()) {
      return;
    }
    if (riddleCalled) {
      String riddleText = riddle.getRiddle();
      Pane riddlePane = riddle.riddlePane(riddleText);
      riddleDisplay.getChildren().add(riddlePane);
      riddlePane.getStyleClass().add("riddle");
      riddleDisplay.toFront();
    } else {
      DungeonMaster dungeonMaster = riddle.getDungeonMaster();
      Pane dialogue = dungeonMaster.getPopUp();
      popUp.getChildren().add(dialogue);
      dialogue.getStyleClass().add("popUp");
      Rectangle exitButton = (Rectangle) ((StackPane) dialogue.getChildren()
          .get(1)).getChildren().get(2);
      Text dialogueText = (Text) ((VBox) ((StackPane) dialogue.getChildren()
          .get(1)).getChildren().get(0)).getChildren().get(1);
      ImageView nextButton = (ImageView) ((StackPane) dialogue.getChildren()
          .get(1)).getChildren().get(1);
      exitButton.setOnMouseClicked(event1 -> {
        popUp.visibleProperty().set(false);
      });
      dialogueText.setOnMouseClicked(event1 -> {
        if (!dungeonMaster.isSpeaking()) {
          dungeonMaster.update();
        }
      });
      nextButton.setOnMouseClicked(event1 -> {
        if (!dungeonMaster.isSpeaking()) {
          dungeonMaster.update();
        }
      });
      riddleCalled = true;
    }
  }
}
