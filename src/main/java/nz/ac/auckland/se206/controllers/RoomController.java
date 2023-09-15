package nz.ac.auckland.se206.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
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
  void enlarge(ImageView image) {
    image.setScaleX(1.5);
    image.setScaleY(1.5);
  }

  @FXML
  void shrink(ImageView image) {
    image.setScaleX(1.0);
    image.setScaleY(1.0);
  }

  @FXML
  void addToInventory(ImageView image) {
    image.setVisible(false);
    image.setDisable(true);
    inventoryChoiceBox.getItems().add(image.getId());
  }

  @FXML
  void hideParchment() {
    parchment1duplicate.setVisible(false);
    parchment2duplicate.setVisible(false);
    parchment3duplicate.setVisible(false);
    parchment4duplicate.setVisible(false);
  }

  @FXML
  void HideRiddle() {
    chatTextArea.setVisible(false);
    chatTextArea.setDisable(true);
    btnHideRiddle.setDisable(true);
    btnHideRiddle.setVisible(false);
  }

  @FXML
  void showRiddle() {
    riddle.setVisible(true);
    riddle.setDisable(false);
  }

  @FXML
  void showRiddleWithoutButton() {
    riddle.setVisible(true);
    riddle.setDisable(false);
  }

  @FXML
  void allowImageToBeDragged(ImageView image) {
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
  void onParchment1MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    enlarge(parchment1);
  }

  @FXML
  void onParchment2MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    enlarge(parchment2);
  }

  @FXML
  void onParchment3MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    enlarge(parchment3);
  }

  @FXML
  void onRiddleMouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    enlarge(riddle);
  }

  @FXML
  void onRiddleMouseExited(MouseEvent event) {
    shrink(riddle);
  }

  @FXML
  void onParchment4MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    enlarge(parchment4);
  }

  @FXML
  void onParchment1MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    shrink(parchment1);
  }

  @FXML
  void onParchment2MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    shrink(parchment2);
  }

  @FXML
  void onParchment3MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    shrink(parchment3);
  }

  @FXML
  void onParchment4MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    shrink(parchment4);
  }

  @FXML
  void onParchment1Clicked(MouseEvent event) {
    // Add parchment1 to the ComboBox
    addToInventory(parchment1);
  }

  @FXML
  void onParchment2Clicked(MouseEvent event) {
    // Add parchment2 to the ComboBox
    addToInventory(parchment2);
  }

  @FXML
  void onParchment3Clicked(MouseEvent event) {
    // Add parchment3 to the ComboBox
    addToInventory(parchment3);
  }

  @FXML
  void onParchment4Clicked(MouseEvent event) {
    // Add parchment4 to the ComboBox
    addToInventory(parchment4);
  }

  @FXML
  void onRiddleClicked(MouseEvent event) {

    chatTextArea.setVisible(true);
    chatTextArea.setDisable(false);
    addToInventory(riddle);
    btnHideRiddle.setDisable(false);
    btnHideRiddle.setVisible(true);
  }

  @FXML
  void onReturnToCorridorClicked(ActionEvent event) {
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

  int parchmentPieces = 0;

  @FXML
  void onTableClicked(MouseEvent event) {

    // Check if a riddle is selected in the combo box
    String selectedItem = inventoryChoiceBox.getSelectionModel().getSelectedItem();

    if (selectedItem != null && selectedItem.contains("riddle")) {
      inventoryChoiceBox.getItems().remove(selectedItem);

      showRiddleWithoutButton();
      return;
    }
    // if a parchment piece is selected it is made visible in the scene
    // and the parchment piece is removed from the combo box
    // if already three pieces are visible the riddle is shown instead
    if (selectedItem != null && selectedItem.contains("parchment")) {
      inventoryChoiceBox.getItems().remove(selectedItem);
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

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title      the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message    the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
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

    chatCompletionRequest = new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
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