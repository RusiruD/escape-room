package nz.ac.auckland.se206;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The Riddle class represents a riddle in the game. It provides functionality to create a graphical
 * user interface for displaying riddles to the player.
 */
public class Riddle {
  /**
   * Creates a Pane containing the riddle text, a hint button, and a close button.
   *
   * @param riddleText The text of the riddle to be displayed.
   * @return A Pane containing the riddle, hint button, and close button.
   */
  public static Pane riddlePane(String riddleText) {
    // create a pane to hold the riddle
    Pane riddlePane = new Pane();

    StackPane stackPane = new StackPane();

    VBox riddleBox = new VBox();

    Label title = new Label("Riddle");

    TextArea riddle = new TextArea(riddleText);
    riddle.setWrapText(true);
    riddle.setEditable(false);
    riddle.setPrefWidth(350);
    riddle.setPrefHeight(300);

    riddleBox.getChildren().addAll(title, riddle);

    ImageView closeButton = new ImageView("images/close.png");
    closeButton.setFitHeight(20);
    closeButton.setFitWidth(20);
    closeButton.setOnMouseClicked(
        event -> {
          riddlePane.getParent().visibleProperty().set(false);
          riddlePane.getParent().mouseTransparentProperty().set(true);
          riddlePane.getParent().toBack();
        });

    stackPane.getChildren().addAll(riddleBox, closeButton);
    StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);

    riddlePane.getChildren().add(stackPane);
    return riddlePane;
  }
}
