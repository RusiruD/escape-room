package nz.ac.auckland.se206;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
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

    HBox instructionsBox = new HBox();
    VBox instructionsVBox = new VBox();

    Label title = new Label("Instructions");
    Label instructionsLabel = new Label(instructions);

    instructionsVBox.getChildren().addAll(title, instructionsLabel);

    StackPane stackPane = new StackPane();
    ImageView closeButton = new ImageView("images/close.png");
    closeButton.setFitHeight(20);
    closeButton.setFitWidth(20);

    ImageView hintButton = new ImageView("images/question.png");
    hintButton.setFitHeight(20);
    hintButton.setFitWidth(20);

    stackPane.getChildren().addAll(closeButton, hintButton);
    StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
    StackPane.setAlignment(hintButton, Pos.BOTTOM_RIGHT);

    instructionsBox.getChildren().addAll(instructionsVBox, stackPane);

    instructionsPane.getChildren().addAll(instructionsBox);
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
