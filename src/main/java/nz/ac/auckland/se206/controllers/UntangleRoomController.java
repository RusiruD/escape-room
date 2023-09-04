package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/** Drag the anchors around to change a polygon's points. */
public class UntangleRoomController {
  
  @FXML StackPane pane;

  public void initialize() {
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
        200d, 90d,
        110d, 110d,
        250d, 130d,
        130d, 190d,
        220d, 250d
    );

    polygon.setStroke(Color.GOLD);
    polygon.setStrokeWidth(4);
    polygon.setStrokeLineCap(StrokeLineCap.ROUND);
    polygon.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.6));

    return polygon;
  }

  private void isIntersecting(Polygon polygon) {
    //TODO: shorten lines so the points don't overlap and cause false intersections
    //for debug
    if (pane.getChildren().size() > 1) {
      pane.getChildren().remove(1);
    }
    polygon.getPoints();

    List<Line> lines = new ArrayList<>(); 
    for (int i = 0; i < polygon.getPoints().size() - 2; i += 2) {
      double x1 = polygon.getPoints().get(i) ;
      double y1 = polygon.getPoints().get(i + 1);
      double x2 = polygon.getPoints().get(i + 2);
      double y2 = polygon.getPoints().get(i + 3);
      //Line line = shortenLine(x1, y1, x2, y2);
      
      Line line = new Line(x1, y1, x2, y2);
      line.setScaleX(0.9);
      line.setScaleY(0.9);
      line.setStroke (Color.GREEN);
      lines.add(line);
    }
    double x1 = polygon.getPoints().get(polygon.getPoints().size() - 2);
    double y1 = polygon.getPoints().get(polygon.getPoints().size() - 1);
    double x2 = polygon.getPoints().get(0);
    double y2 = polygon.getPoints().get(1);
    Line line = new  Line(x1, y1, x2, y2);
    line.setScaleX(0.9);
    line.setScaleY(0.9);
    line.setStroke(Color.BLACK);
    lines.add(line);

    for (int i = 0; i < lines.size(); i++) {
      for (int j = i + 1; j < lines.size(); j++) {
        Shape intersection = Shape.intersect(lines.get(i), lines.get(j));
        if (intersection.getBoundsInLocal().getWidth() != -1) {
          System.out.println("Lines " + i + " and " + j + " intersect" + intersection.getBoundsInLocal().getWidth());
          //return;
        }
      }
    }
    //for debug
    Group liness = new Group();
    liness.getChildren().addAll(lines);
    pane.getChildren().add(liness);
    
    System.out.println(polygon);
    System.out.println();
    puzzleSolved();
  }

  private void puzzleSolved() {
    System.out.println("Puzzle solved");
  }

  private ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    for (int i = 0; i < points.size(); i+=2) {
      final int idx = i;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
          points.set(idx, (double) x);
        }
      });

      yProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
          points.set(idx + 1, (double) y);
        }
      });

      anchors.add(new Anchor(Color.GOLD, xProperty, yProperty));
    }

    return anchors;
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle {
    private final DoubleProperty x, y;

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
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          isIntersecting((Polygon)getParent().getChildrenUnmodifiable().get(0));
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
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
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
  }
}