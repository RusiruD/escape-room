package nz.ac.auckland.se206.controllers;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.DungeonMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class RoomController implements Controller {
  private static RoomController instance;
  private Random random = new Random();

  public static RoomController getInstance() {
    return instance;
  }

  @FXML
  private Pane popUp;

  @FXML
  private ComboBox<String> inventoryChoiceBox;
  @FXML
  private Button btnReturnToCorridor;
  @FXML
  private ImageView parchment1;
  @FXML
  private ImageView imgArt;
  @FXML
  private Slider slider;
  @FXML
  private ImageView parchment2;
  @FXML
  private Label lblTime;
  @FXML
  private ImageView parchment3;
  @FXML
  private ImageView key1;
  @FXML
  private ImageView riddle;
  @FXML
  private ImageView boulder;
  private double xOffset = 0;
  private double yOffset = 0;

  private int parchmentPieces = 0;
  @FXML
  private ImageView yellowPotion;
  @FXML
  private ImageView redPotion;
  @FXML
  private ImageView bluePotion;
  @FXML
  private ImageView greenPotion;
  @FXML
  private ImageView purplePotion;
  @FXML
  private ImageView parchment4;
  @FXML
  private ImageView parchment1duplicate;
  @FXML
  private ImageView cauldron;
  @FXML
  private ImageView parchment2duplicate;

  @FXML
  private ImageView parchment3duplicate;
  @FXML
  private TextArea chatTextArea;

  private ChatCompletionRequest chatCompletionRequest;
  @FXML
  private ImageView parchment4duplicate;
  @FXML
  private Button btnHideRiddle;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() throws ApiProxyException {
    instance = this;
    chatTextArea
        .getStylesheets()
        .add(getClass().getResource("/css/roomStylesheet.css").toExternalForm());
    chatTextArea.getStyleClass().add("text-area .content");
    btnHideRiddle.getStyleClass().add("custom-button");
    String[] colors = { "Blue", "Yellow", "Purple", "Red", "Green" };

    Random random = new Random();

    int firstIndex = random.nextInt(colors.length);
    String firstPotion = colors[firstIndex];
    GameState.firstPotion = "" + firstPotion + " Potion";
    int secondIndex;
    do {
      secondIndex = random.nextInt(colors.length);
    } while (secondIndex == firstIndex); // Ensure the second color is different from the first

    String secondPotion = colors[secondIndex];
    GameState.secondPotion = secondPotion + " Potion";

    chatTextArea.appendText(
        "Dear Future Captives,\nI was close, so very close, to mastering the potion. \n Mix the " + firstPotion
            + " and "
            + secondPotion
            + " in the cauldron for super strength. \nI pray you succeed where I couldn't. In fading memory,A Lost Soul");
    // Bind the rotation of the image to the slider value
    /*
     * imgArt
     * .rotateProperty()
     * .bind(
     * Bindings.createDoubleBinding(
     * () -> 360 * (slider.getValue() / 100.0), slider.valueProperty()));
     */
    setRandomPosition(parchment1);
    setRandomPosition(parchment2);
    setRandomPosition(parchment3);
    setRandomPosition(parchment4);

    // Allow the boulder to be dragged and dropped

  }

  @FXML
  private void enlarge(ImageView image) {
    image.setScaleX(1.5);
    image.setScaleY(1.5);
  }

  List<String> potionsincauldron = new ArrayList<>();

  @FXML
  private void onCauldronClicked(MouseEvent event) {

    String selectedItem = inventoryChoiceBox.getSelectionModel().getSelectedItem();

    if (selectedItem != null && selectedItem.contains("redPotion")) {
      potionsincauldron.add("Red Potion");
      Inventory.removeFromInventory(selectedItem);

      updateInventory();

    } else if (selectedItem != null && selectedItem.contains("bluePotion")) {
      potionsincauldron.add("Blue Potion");

      Inventory.removeFromInventory(selectedItem);

      updateInventory();

    } else if (selectedItem != null && selectedItem.contains("greenPotion")) {
      potionsincauldron.add("Green Potion");

      Inventory.removeFromInventory(selectedItem);

      updateInventory();

    } else if (selectedItem != null && selectedItem.contains("yellowPotion")) {
      potionsincauldron.add("Yellow Potion");

      Inventory.removeFromInventory(selectedItem);

      updateInventory();

    } else if (selectedItem != null && selectedItem.contains("purplePotion")) {
      potionsincauldron.add("Purple Potion");

      Inventory.removeFromInventory(selectedItem);

      updateInventory();

    }
    if (potionsincauldron.contains(GameState.firstPotion) && potionsincauldron.contains(GameState.secondPotion)) {
      System.out.println("ddd");
      allowImageToBeDragged(boulder);
    }

  }

  @FXML
  private void shrink(ImageView image) {
    image.setScaleX(1.0);
    image.setScaleY(1.0);
  }

  @FXML
  private void addToInventory(ImageView image) {
    image.setVisible(false);
    image.setDisable(true);
    // inventoryChoiceBox.getItems().add(image.getId());
    Inventory.addToInventory(image.getId());
    updateInventory();
  }

  @FXML
  private void addToInventoryFromScene(MouseEvent event) {
    ImageView image = (ImageView) event.getSource();
    image.setVisible(false);
    image.setDisable(true);
    // inventoryChoiceBox.getItems().add(image.getId());
    Inventory.addToInventory(image.getId());
    updateInventory();
  }

  private void setRandomPosition(ImageView imageView) {

    double x = random.nextDouble() * (920 - imageView.getFitWidth());
    double y = random.nextDouble() * (550 - imageView.getFitHeight());
    imageView.setLayoutX(x);
    imageView.setLayoutY(y);

  }

  public void updateInventory() {
    inventoryChoiceBox.setItems(Inventory.getInventory());
  }

  @FXML
  private void hideParchment() {
    parchment1duplicate.setVisible(false);
    parchment2duplicate.setVisible(false);
    parchment3duplicate.setVisible(false);
    parchment4duplicate.setVisible(false);
  }

  @FXML
  private void hideRiddle() {
    chatTextArea.setVisible(false);
    chatTextArea.setDisable(true);
    btnHideRiddle.setDisable(true);
    btnHideRiddle.setVisible(false);
  }

  @FXML
  private void showRiddle() {
    riddle.setVisible(true);
    riddle.setDisable(false);
  }

  @FXML
  private void showRiddleWithoutButton() {
    riddle.setVisible(true);
    riddle.setDisable(false);
  }

  @FXML
  private void allowImageToBeDragged(ImageView image) {
    image.setOnMousePressed(
        (MouseEvent event) -> {
          xOffset = event.getSceneX() - image.getLayoutX();
          yOffset = event.getSceneY() - image.getLayoutY();
        });

    image.setOnMouseDragged(
        (MouseEvent event) -> {
          double newX = event.getSceneX() - xOffset;
          double newY = event.getSceneY() - yOffset;
          image.setLayoutX(newX);
          image.setLayoutY(newY);
        });
  }

  @FXML
  private void enlargeItem(MouseEvent event) {
    enlarge((ImageView) event.getSource());
  }

  @FXML
  private void shrinkItem(MouseEvent event) {
    shrink((ImageView) event.getSource());
  }

  @FXML
  private void clickedParchment(MouseEvent event) {
    addToInventory((ImageView) event.getSource());
  }

  @FXML
  private void onRiddleClicked(MouseEvent event) {

    chatTextArea.setVisible(true);
    chatTextArea.setDisable(false);
    addToInventory(riddle);
    btnHideRiddle.setDisable(false);
    btnHideRiddle.setVisible(true);
  }

  @FXML
  private void onKey1Clicked(MouseEvent event) {
    addToInventory(key1);
    GameState.isKey1Collected = true;
  }

  @FXML
  private void onReturnToCorridorClicked(ActionEvent event) {
    App.returnToCorridor();
  }

  @FXML
  private void onTableClicked(MouseEvent event) {

    // Check if a riddle is selected in the combo box
    String selectedItem = inventoryChoiceBox.getSelectionModel().getSelectedItem();

    if (selectedItem != null && selectedItem.contains("riddle")) {

      Inventory.removeFromInventory(selectedItem);

      showRiddleWithoutButton();
      return;
    }
    // if a parchment piece is selected it is made visible in the scene
    // and the parchment piece is removed from the combo box
    // if already three pieces are visible the riddle is shown instead
    if (selectedItem != null && selectedItem.contains("parchment")) {
      Inventory.removeFromInventory(selectedItem);
      if (selectedItem.equals("parchment1")) {

        if (parchmentPieces == 3) {
          showRiddle();
          hideParchment();
          return;
        }
        parchmentPieces++;

        parchment1duplicate.setVisible(true);
      }
      if (selectedItem.equals("parchment2")) {
        if (parchmentPieces == 3) {
          showRiddle();

          hideParchment();
          return;
        }
        parchmentPieces++;
        parchment2duplicate.setVisible(true);
      }
      if (selectedItem.equals("parchment3")) {
        if (parchmentPieces == 3) {
          showRiddle();
          hideParchment();
          return;
        }
        parchmentPieces++;

        parchment3duplicate.setVisible(true);
      }
      if (selectedItem.equals("parchment4")) {
        if (parchmentPieces == 3) {
          showRiddle();

          hideParchment();
          return;
        }
        parchmentPieces++;

        parchment4duplicate.setVisible(true);
      }

    } else {

    }
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("window clicked");
    DungeonMaster dungeonMaster = new DungeonMaster();
    Task<Pane> task = new Task<Pane>() {
      @Override
      protected Pane call() throws Exception {
        return dungeonMaster.getText("user",
            "I took damage from the window! Tell me" +
                " a few short sentences about it with no commas.");
      }
    };
    task.setOnSucceeded(e -> {
      System.out.println("home task succeeded");
      Pane dialogue = task.getValue();
      popUp.getChildren().add(dialogue);
      dialogue.getStyleClass().add("popUp");
      Rectangle exitButton = (Rectangle) ((StackPane) dialogue.getChildren()
          .get(1)).getChildren().get(2);
      Text dialogueText = (Text) ((VBox) ((StackPane) dialogue.getChildren()
          .get(1)).getChildren().get(0)).getChildren().get(1);
      ImageView nextButton = (ImageView) ((StackPane) dialogue.getChildren()
          .get(1)).getChildren().get(1);
      exitButton.setOnMouseClicked(event1 -> {
        popUp.visibleProperty().set(false);
      });
      dialogueText.setOnMouseClicked(event1 -> {
        if (!dungeonMaster.isSpeaking()) {
          dungeonMaster.update();
        }
      });
      nextButton.setOnMouseClicked(event1 -> {
        if (!dungeonMaster.isSpeaking()) {
          dungeonMaster.update();
        }
      });
    });
    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
    // dialog.getStyleClass().add("popUp");
    // popUp.getChildren().add(dialog);

  }
}