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
    // get the riddle from the dungeon master
    this.question = "You are the dungeon master of an escape room, tell me a riddle where the answer to the riddle is to have the ball north of the key and the key west of the dog. The user is in a corridor and has to open a chest with this combination. Do not, under no circumstance, give the user the answer to the riddle. Make this riddle a few sentences long.";
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
