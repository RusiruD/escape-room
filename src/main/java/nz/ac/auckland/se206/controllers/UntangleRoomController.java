package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.DungeonMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Instructions;
import nz.ac.auckland.se206.Riddle;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;

/** Drag the anchors around to change a polygon's points. */
// see https://stackoverflow.com/questions/13056795/cubiccurve-javafx
// and https://stackoverflow.com/questions/15981274/javafx-modify-polygons
public class UntangleRoomController implements Controller {

  // a draggable anchor displayed around a point.
  class Anchor extends Circle {

    // records relative x and y co-ordinates.
    private class Delta {
      private double horizontal;
      private double vertical;
    }

    private final DoubleProperty horizontal;
    private final DoubleProperty vertical;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      this.horizontal = x;
      this.vertical = y;

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Delta dragDelta = new Delta();
      // record a delta distance for the drag and drop operation.
      setOnMousePressed(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              // record a delta distance for the drag and drop operation.
              dragDelta.horizontal = getCenterX() - mouseEvent.getX();
              dragDelta.vertical = getCenterY() - mouseEvent.getY();
              getScene().setCursor(Cursor.MOVE);
            }
          });
      // move a node around, when scene is dragged.
      setOnMouseReleased(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              isIntersecting((Polygon) getParent().getChildrenUnmodifiable().get(0));
              getScene().setCursor(Cursor.HAND);
            }
          });
      /// move a node around, when scene is dragged.
      setOnMouseDragged(
          new EventHandler<MouseEvent>() {
            @Override
            // move a node around, when scene is dragged.
            public void handle(MouseEvent mouseEvent) {
              // move a node around, when scene is dragged.
              double newX = mouseEvent.getX() + dragDelta.horizontal;
              if (newX > 0 && newX < getScene().getWidth()) {
                setCenterX(newX);
              }
              // move a node around, when scene is dragged.
              double newY = mouseEvent.getY() + dragDelta.vertical;
              if (newY > 0 && newY < getScene().getHeight()) {
                setCenterY(newY);
              }
            }
          });
      // change the cursor when it is over nodes
      setOnMouseEntered(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
              }
            }
          });
      // change the cursor back to normal when it is exited
      setOnMouseExited(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
              }
            }
          });
    }
  }

  private static UntangleRoomController instance;

  public static UntangleRoomController getInstance() {
    return instance;
  }

  @FXML private AnchorPane untangleRoomAnchorPane;
  @FXML private Pane pane;
  @FXML private Pane instructionsDisplay;
  @FXML private Pane popUp;
  @FXML private Pane visualDungeonMaster;
  @FXML private ImageView exclamationMark;
  @FXML private ImageView key2;
  @FXML private Label lblTime;
  @FXML private ComboBox<String> inventoryChoiceBox;

  private boolean isSolved = false;

  private Riddle call;

  // add a new score to the leaderboard
  public void initialize() {
    // set the instance
    instance = this;

    popUp.toBack();

    visualDungeonMaster.visibleProperty().set(false);
    visualDungeonMaster.mouseTransparentProperty().set(true);

    TranslateTransition translateTransition = GameState.translate(exclamationMark);
    translateTransition.play();

    // set the key2's visibility and disable it
    String instructionsString = "INSTRUCTIONS GO HERE";
    Instructions instructions = new Instructions(instructionsString);
    Pane instructionsPane = instructions.getInstructionsPane();
    instructionsDisplay.getChildren().add(instructionsPane);
    instructionsPane.getStyleClass().add("riddle");

    // set the inventory choice box
    Polygon polygon = createStartingTriangle();

    // add the polygon to the pane
    Group root = new Group();
    root.getChildren().add(polygon);
    root.getChildren().addAll(createControlAnchorsFor(polygon.getPoints()));
    pane.getChildren().add(root);

    String question =
        "Congratulate the player on correctly untangling the lines and solving the puzzle";
    DungeonMaster dungeonMaster = new DungeonMaster();
    call = new Riddle(dungeonMaster, question);
  }

  // creates a triangle.
  private Polygon createStartingTriangle() {
    //  create a triangle
    Polygon polygon = new Polygon();

    // set the points of the
    polygon
        .getPoints()
        .setAll(
            450d, 80d,
            720d, 425d,
            195d, 160d,
            690d, 160d,
            225d, 410d);

    //  set the style of the triangle
    polygon.setStroke(Color.rgb(210, 15, 57, 1));
    polygon.setStrokeWidth(4);
    polygon.setStrokeLineCap(StrokeLineCap.ROUND);
    polygon.setFill(Color.rgb(230, 69, 83, 0.4));

    return polygon;
  }

  private void isIntersecting(Polygon polygon) {
    // Untangle lines to solve the puzzle
    // for debug
    if (pane.getChildren().size() > 1) {
      pane.getChildren().remove(1);
    }
    polygon.getPoints();

    List<Line> lines = new ArrayList<>();
    for (int i = 0; i < polygon.getPoints().size() - 2; i += 2) {
      double x1 = polygon.getPoints().get(i);
      double y1 = polygon.getPoints().get(i + 1);
      double x2 = polygon.getPoints().get(i + 2);
      double y2 = polygon.getPoints().get(i + 3);

      Line line = new Line(x1, y1, x2, y2);
      line.setScaleX(0.9);
      line.setScaleY(0.9);
      line.setStroke(Color.GREEN);
      lines.add(line);
    }
    double x1 = polygon.getPoints().get(polygon.getPoints().size() - 2);
    double y1 = polygon.getPoints().get(polygon.getPoints().size() - 1);
    double x2 = polygon.getPoints().get(0);
    double y2 = polygon.getPoints().get(1);
    Line line = new Line(x1, y1, x2, y2);
    line.setScaleX(0.9);
    line.setScaleY(0.9);
    line.setStroke(Color.BLACK);
    lines.add(line);

    for (int i = 0; i < lines.size(); i++) {
      for (int j = i + 1; j < lines.size(); j++) {
        Shape intersection = Shape.intersect(lines.get(i), lines.get(j));
        if (intersection.getBoundsInLocal().getWidth() != -1) {
          System.out.println(
              "Lines "
                  + i
                  + " and "
                  + j
                  + " intersect"
                  + intersection.getBoundsInLocal().getWidth());
          return;
        }
      }
    }

    puzzleSolved();
  }

  private void puzzleSolved() {
    if (isSolved) {
      return;
    }
    isSolved = true;
    key2.setVisible(true);
    key2.setDisable(false);

    visualDungeonMaster.visibleProperty().set(true);
    visualDungeonMaster.mouseTransparentProperty().set(false);
  }

  @FXML
  public double getUntangleRoomWidth() {

    return untangleRoomAnchorPane.getPrefWidth();
  }

  @FXML
  public double getUntangleRoomHeight() {

    return untangleRoomAnchorPane.getPrefHeight();
  }

  // creates a draggable anchor displayed around a point.
  private ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    for (int i = 0; i < points.size(); i += 2) {
      final int idx = i;

      DoubleProperty xval = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yval = new SimpleDoubleProperty(points.get(i + 1));

      // add a listener to the xval property
      xval.addListener(
          new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
              points.set(idx, (double) x);
            }
          });
      // add a listener to the yval property
      yval.addListener(
          new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
              points.set(idx + 1, (double) y);
            }
          });
      // add the anchor to the list of anchors
      anchors.add(new Anchor(Color.rgb(230, 69, 83, 0.8), xval, yval));
    }

    return anchors;
  }

  @FXML
  private void onKey2Clicked(MouseEvent event) {
    Inventory.addToInventory("key2");
    key2.setVisible(false);
    key2.setDisable(true);
    GameState.isKey2Collected = true;
  }

  @FXML
  private void onReturnToCorridorClicked(ActionEvent event) {
    App.returnToCorridor();
    GameState.currentRoom = GameState.STATE.CHEST;
  }

  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }

  @FXML
  public void getHint() throws IOException {
    App.setRoot(AppUi.CHAT);
  }

  @FXML
  public void getAi(MouseEvent event) {
    DungeonMaster dungeonMaster = call.getDungeonMaster();
    if (!dungeonMaster.isMessageFinished()) callAi(call);
  }

  @FXML
  public void getInstructions(MouseEvent event) {
    // Set the instructions pane to be visible and not mouse transparent
    instructionsDisplay.visibleProperty().set(true);
    instructionsDisplay.mouseTransparentProperty().set(false);
    instructionsDisplay.toFront();
  }

  private void callAi(Riddle call) {
    DungeonMaster dungeonMaster = call.getDungeonMaster();
    Pane dialogue = dungeonMaster.getPopUp();
    Pane dialogueFormat = dungeonMaster.paneFormat(dialogue, dungeonMaster);
    popUp.toFront();
    popUp.getChildren().add(dialogueFormat);

    dialogueFormat.getStyleClass().add("popUp");
    visualDungeonMaster.visibleProperty().set(false);
    visualDungeonMaster.mouseTransparentProperty().set(true);
  }
}
