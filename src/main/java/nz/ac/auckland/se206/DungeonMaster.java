package nz.ac.auckland.se206;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class DungeonMaster {
  private Pane popUp;

  public Pane getPopUp() {
    popUp = new HBox();

    // DUNGEON MASTER IMAGE
    StackPane dungeonMasterStack = new StackPane();
    Rectangle dungeonMasterRectangle = new Rectangle();
    dungeonMasterRectangle.setWidth(60);
    dungeonMasterRectangle.setHeight(60);
    dungeonMasterRectangle.setStyle("-fx-fill: #000000");
    ImageView dungeonMasterImage = new ImageView("images/player.png");
    dungeonMasterStack.getChildren().addAll(dungeonMasterRectangle, dungeonMasterImage);

    // DIALOG BOX
    VBox dialogueBox = new VBox();

    // NAME
    Label name = new Label("Dungeon Master");

    // DIALOG
    Text dialogue = new Text(
        "Hello, I am the Dungeon Master. I will be your guide on this adventure. Lorem Ipsum is simply dummy text");
    dialogueBox.getChildren().addAll(name);
    dialogueBox.getChildren().addAll(dialogue);
    dialogue.setWrappingWidth(430);

    // DIALOG NEXT BUTTON
    ImageView nextButton = new ImageView("images/down.png");
    nextButton.setFitHeight(20);
    nextButton.setFitWidth(20);
    nextButton.setOnMouseClicked(e -> {
      // String nextText = getNextText();

    });
    nextButton.setOnMouseEntered(e -> {

    });

    TranslateTransition translateTransition = new TranslateTransition();
    translateTransition.setDuration(Duration.millis(500));
    translateTransition.setNode(nextButton);
    translateTransition.setToY(5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();

    StackPane dialogueContainer = new StackPane();
    dialogueContainer.getChildren().addAll(dialogueBox, nextButton);
    nextButton.setTranslateY(25);
    nextButton.setTranslateX(220);

    // ADDING TO POP UP
    popUp.getChildren().addAll(dungeonMasterStack);
    popUp.getChildren().addAll(dialogueContainer);
    return popUp;
  }
}
