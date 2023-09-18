package nz.ac.auckland.se206;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

    VBox riddleBox = new VBox();
    Label title = new Label("Riddle");

    Label riddle = new Label(riddleText);

    riddleBox.getChildren().addAll(title, riddle);

    riddlePane.getChildren().add(riddleBox);
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
