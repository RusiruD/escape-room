package nz.ac.auckland.se206;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DraggableImageView {

  private double xOffset = 0;
  private double yOffset = 0;

  public void makeDraggable(ImageView imageView) {
    // Mouse Pressed Event
    imageView.setOnMousePressed(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            // Record the initial mouse cursor position
            xOffset = event.getSceneX() - imageView.getLayoutX();
            yOffset = event.getSceneY() - imageView.getLayoutY();
          }
        });

    // Mouse Dragged Event
    imageView.setOnMouseDragged(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            // Calculate new position based on mouse cursor position
            double newX = event.getSceneX() - xOffset;
            double newY = event.getSceneY() - yOffset;

            // Set the new position for the image
            imageView.setLayoutX(newX);
            imageView.setLayoutY(newY);
          }
        });

    // Mouse Released Event (optional)
    imageView.setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            // Perform any actions you want when the mouse is released
          }
        });
  }
}
