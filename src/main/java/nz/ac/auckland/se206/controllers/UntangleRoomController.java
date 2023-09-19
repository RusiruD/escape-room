package nz.ac.auckland.se206.controllers;

import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.GameState;

import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

/** Drag the anchors around to change a polygon's points. */
// see https://stackoverflow.com/questions/13056795/cubiccurve-javafx
// and https://stackoverflow.com/questions/15981274/javafx-modify-polygons
public class UntangleRoomController implements Controller {

  private static UntangleRoomController instance;

  public static UntangleRoomController getInstance() {
    return instance;
  }

  @FXML
  private Pane pane;
  @FXML
  private ImageView key3;
  @FXML
  private Label lblTime;
  @FXML
  private ComboBox<String> inventoryChoiceBox;

  private boolean isSolved = false;

  public void initialize() {
    instance = this;
    key3.setVisible(false);
    key3.mouseTransparentProperty().set(true);
    Polygon polygon = createStartingTriangle();

    Group root = new Group();
    root.getChildren().add(polygon);
    root.getChildren().addAll(createControlAnchorsFor(polygon.getPoints()));
    pane.getChildren().add(root);
  }

  // creates a triangle.
  private Polygon createStartingTriangle() {
    Polygon polygon = new Polygon();

    polygon.getPoints().setAll(
        450d, 80d,
        720d, 425d,
        195d, 160d,
        690d, 160d,
        225d, 410d);

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
      // Line line = shortenLine(x1, y1, x2, y2);

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
          System.out.println("Lines " + i + " and " + j + " intersect" +
              intersection.getBoundsInLocal().getWidth());
          return;
        }
      }
    }
    // for debug
    // Group liness = new Group();
    // liness.getChildren().addAll(lines);
    // pane.getChildren().add(liness);

    System.out.println(polygon);
    puzzleSolved();
  }

  private void puzzleSolved() {
    if (isSolved) {
      return;
    }
    isSolved = true;
    System.out.println("Puzzle solved");
    key3.setVisible(true);
    key3.mouseTransparentProperty().set(false);
  }

  private ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    for (int i = 0; i < points.size(); i += 2) {
      final int idx = i;

      DoubleProperty xval = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yval = new SimpleDoubleProperty(points.get(i + 1));

      xval.addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
          points.set(idx, (double) x);
        }
      });

      yval.addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
          points.set(idx + 1, (double) y);
        }
      });

      anchors.add(new Anchor(Color.rgb(230, 69, 83, 0.8), xval, yval));
    }

    return anchors;
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle {
    private final DoubleProperty x;
    private final DoubleProperty y;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      this.x = x;
      this.y = y;

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          isIntersecting((Polygon) getParent().getChildrenUnmodifiable().get(0));
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }
          double newY = mouseEvent.getY() + dragDelta.y;
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }
        }
      });
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta {
      private double x;
      private double y;
    }
  }

  @FXML
  private void onKey3Clicked(MouseEvent event) {
    Inventory.addToInventory("key3");
    key3.setVisible(false);
    key3.setDisable(true);
    GameState.isKey3Collected = true;
  }

  @FXML
  private void onReturnToCorridorClicked(ActionEvent event) {
    App.returnToCorridor();
  }

  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }
}