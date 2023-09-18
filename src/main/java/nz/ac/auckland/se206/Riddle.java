package nz.ac.auckland.se206;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Riddle {
  private String question;
  private String riddle;
  private boolean hasRiddle = false;
  private DungeonMaster dungeonMaster;

  public Riddle(DungeonMaster dungeonMaster) {
    this.question = "Write me two lines of code that will print out the numbers 1 to 10";
    this.dungeonMaster = dungeonMaster;
    // get the riddle from the dungeon master
    riddle = dungeonMaster.getRiddle(question);
    hasRiddle = true;
  }

  public Pane riddlePane(String riddleText) {
    // create a pane to hold the riddle
    Pane riddlePane = new Pane();

    StackPane stackPane = new StackPane();

    VBox riddleBox = new VBox();

    Label title = new Label("Riddle");

    Label riddle = new Label(riddleText);

    riddleBox.getChildren().addAll(title, riddle);

    // add hint button add close button
    ImageView hintButton = new ImageView("images/question.png");
    hintButton.setFitHeight(20);
    hintButton.setFitWidth(20);
    hintButton.setOnMouseClicked(event -> {
    });

    ImageView closeButton = new ImageView("images/close.png");
    closeButton.setFitHeight(20);
    closeButton.setFitWidth(20);
    closeButton.setOnMouseClicked(event -> {
      riddlePane.getParent().visibleProperty().set(false);
      riddlePane.getParent().mouseTransparentProperty().set(true);
      riddlePane.getParent().toBack();
    });

    stackPane.getChildren().addAll(riddleBox, hintButton, closeButton);
    StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
    StackPane.setAlignment(hintButton, Pos.BOTTOM_RIGHT);

    riddlePane.getChildren().add(stackPane);
    return riddlePane;
  }

  public DungeonMaster getDungeonMaster() {
    return dungeonMaster;
  }

  public boolean hasRiddle() {
    return hasRiddle;
  }

  public String getRiddle() {
    return riddle;
  }
}
