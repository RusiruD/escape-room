package nz.ac.auckland.se206;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Instructions {
  private String instructions;
  private Pane instructionsPane;

  public Instructions(String instructions) {
    Pane instructionsPane = new Pane();

    StackPane stackPane = new StackPane();

    VBox instructionsBox = new VBox();

    Label title = new Label("Instructions");

    TextArea instructionsText = new TextArea(instructions);
    instructionsText.setWrapText(true);
    instructionsText.setEditable(false);
    instructionsText.setPrefWidth(350);
    instructionsText.setPrefHeight(300);

    instructionsBox.getChildren().addAll(title, instructionsText);

    ImageView closeButton = new ImageView("images/close.png");
    closeButton.setFitHeight(20);
    closeButton.setFitWidth(20);
    closeButton.setOnMouseClicked(
        event -> {
          instructionsPane.getParent().visibleProperty().set(false);
          instructionsPane.getParent().mouseTransparentProperty().set(true);
          instructionsPane.getParent().toBack();
        });

    stackPane.getChildren().addAll(instructionsBox, closeButton);
    StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);

    instructionsPane.getChildren().add(stackPane);

    this.instructionsPane = instructionsPane;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public String getInstructions() {
    return instructions;
  }

  public Pane getInstructionsPane() {
    return instructionsPane;
  }
}
