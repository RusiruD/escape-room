package nz.ac.auckland.se206.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class RoomController {
  private static RoomController instance;

  public static RoomController getInstance() {
    return instance;
  }

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
  private ImageView riddle;
  @FXML
  private ImageView boulder;
  private double xOffset = 0;
  private double yOffset = 0;

  private int parchmentPieces = 0;

  @FXML
  private ImageView parchment4;
  @FXML
  private ImageView parchment1duplicate;

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

  @FXML
  private void enlarge(ImageView image) {
    image.setScaleX(1.5);
    image.setScaleY(1.5);
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
  private void onReturnToCorridorClicked(ActionEvent event) {
    // return to corridor scene
    try {

      Button button = (Button) event.getSource();
      Scene sceneButtonIsIn = button.getScene();

      sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CORRIDOR));
      SceneManager.getUiRoot(AppUi.CORRIDOR).requestFocus();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
  public void initialize() throws ApiProxyException {
    instance = this;

    // style the chat text area and hide button
    chatTextArea
        .getStylesheets()
        .add(getClass().getResource("/css/roomStylesheet.css").toExternalForm());
    chatTextArea.getStyleClass().add("text-area .content");
    btnHideRiddle.getStyleClass().add("custom-button");
    // Bind the rotation of the image to the slider value
    imgArt
        .rotateProperty()
        .bind(
            Bindings.createDoubleBinding(
                () -> 360 * (slider.getValue() / 100.0), slider.valueProperty()));

    chatCompletionRequest = new ChatCompletionRequest().setN(1).setTemperature(0.2)
        .setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("rock")));
    // Allow the boulder to be dragged and dropped
    allowImageToBeDragged(boulder);
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      appendChatMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }
}