package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class RoomController {

  @FXML private ComboBox<String> inventoryChoiceBox;

  @FXML private ImageView parchment1;

  @FXML private ImageView parchment2;

  @FXML private ImageView parchment3;

  @FXML private ImageView riddle;

  @FXML private ImageView parchment4;
  @FXML private ImageView parchment1duplicate;

  @FXML private ImageView parchment2duplicate;

  @FXML private ImageView parchment3duplicate;
  @FXML private TextArea chatTextArea;

  private ChatCompletionRequest chatCompletionRequest;
  @FXML private ImageView parchment4duplicate;
  @FXML private Button btnHideRiddle;

  @FXML
  void HideRiddle(ActionEvent event) {
    chatTextArea.setVisible(false);
    chatTextArea.setDisable(true);
    btnHideRiddle.setDisable(true);
    btnHideRiddle.setVisible(false);
  }

  @FXML
  void onParchment1MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment1.setScaleX(1.5);
    parchment1.setScaleY(1.5);
  }

  @FXML
  void onParchment2MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment2.setScaleX(1.5);
    parchment2.setScaleY(1.5);
  }

  @FXML
  void onParchment3MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment3.setScaleX(1.5);
    parchment3.setScaleY(1.5);
  }

  @FXML
  void onRiddleMouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    riddle.setScaleX(1.5);
    riddle.setScaleY(1.5);
  }

  @FXML
  void onRiddleMouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    riddle.setScaleX(1.0);
    riddle.setScaleY(1.0);
  }

  @FXML
  void onParchment4MouseEntered(MouseEvent event) {
    // Increase the size of parchment1 by 150%
    parchment4.setScaleX(1.5);
    parchment4.setScaleY(1.5);
  }

  @FXML
  void onParchment1MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment1.setScaleX(1.0);
    parchment1.setScaleY(1.0);
  }

  @FXML
  void onParchment2MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment2.setScaleX(1.0);
    parchment2.setScaleY(1.0);
  }

  @FXML
  void onParchment3MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment3.setScaleX(1.0);
    parchment3.setScaleY(1.0);
  }

  @FXML
  void onParchment4MouseExited(MouseEvent event) {
    // Reset the size of parchment1 to 100%
    parchment4.setScaleX(1.0);
    parchment4.setScaleY(1.0);
  }

  @FXML
  void onParchment1Clicked(MouseEvent event) {
    // Add parchment1 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 1");
    // Make parchment1 non-visible and disabled
    parchment1.setVisible(false);
    parchment1.setDisable(true);
  }

  @FXML
  void onParchment1DuplicateClicked(MouseEvent event) {}

  @FXML
  void onParchment2DuplicateClicked(MouseEvent event) {}

  @FXML
  void onRiddleClicked(MouseEvent event) {
    riddle.setVisible(false);
    riddle.setDisable(true);
    chatTextArea.setVisible(true);
    chatTextArea.setDisable(false);
    inventoryChoiceBox.getItems().add("riddle");
    btnHideRiddle.setDisable(false);
    btnHideRiddle.setVisible(true);
  }

  @FXML
  void onParchment3DuplicateClicked(MouseEvent event) {}

  @FXML
  void onParchment4DuplicateClicked(MouseEvent event) {}

  @FXML
  void onParchment2Clicked(MouseEvent event) {
    // Add parchment2 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 2");
    // Make parchment2 non-visible and disabled
    parchment2.setVisible(false);
    parchment2.setDisable(true);
  }

  @FXML
  void onParchment3Clicked(MouseEvent event) {
    // Add parchment3 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 3");
    // Make parchment3 non-visible and disabled
    parchment3.setVisible(false);
    parchment3.setDisable(true);
  }

  @FXML
  void onParchment4Clicked(MouseEvent event) {
    // Add parchment4 to the ComboBox
    inventoryChoiceBox.getItems().add("parchment 4");
    // Make parchment4 non-visible and disabled
    parchment4.setVisible(false);
    parchment4.setDisable(true);
  }

  int x = 0;

  @FXML
  void onTableClicked(MouseEvent event) {
    // Check if a parchment is selected in the combo box
    String selectedParchment = inventoryChoiceBox.getSelectionModel().getSelectedItem();
    if (selectedParchment != null && selectedParchment.contains("riddle")) {
      inventoryChoiceBox.getItems().remove(selectedParchment);

      riddle.setVisible(true);
      riddle.setDisable(false);
      return;
    }

    if (selectedParchment != null && selectedParchment.contains("parchment")) {
      inventoryChoiceBox.getItems().remove(selectedParchment);
      if (selectedParchment.equals("parchment 1")) {
        if (x == 3) {

          parchment1duplicate.setVisible(false);
          parchment2duplicate.setVisible(false);
          parchment3duplicate.setVisible(false);
          parchment4duplicate.setVisible(false);
          return;
        }
        x++;
        System.out.println(x);
        parchment1duplicate.setVisible(true);
      }
      if (selectedParchment.equals("parchment 2")) {
        if (x == 3) {
          riddle.setVisible(true);
          riddle.setDisable(false);

          parchment1duplicate.setVisible(false);
          parchment2duplicate.setVisible(false);
          parchment3duplicate.setVisible(false);

          parchment4duplicate.setVisible(false);
          return;
        }
        x++;
        parchment2duplicate.setVisible(true);
      }
      if (selectedParchment.equals("parchment 3")) {
        if (x == 3) {
          riddle.setVisible(true);
          riddle.setDisable(false);

          parchment1duplicate.setVisible(false);
          parchment2duplicate.setVisible(false);
          parchment3duplicate.setVisible(false);
          parchment4duplicate.setVisible(false);
          return;
        }
        x++;
        System.out.println(x);
        parchment3duplicate.setVisible(true);
      }
      if (selectedParchment.equals("parchment 4")) {
        if (x == 3) {
          riddle.setVisible(true);
          riddle.setDisable(false);

          parchment1duplicate.setVisible(false);
          parchment3duplicate.setVisible(false);
          parchment4duplicate.setVisible(false);
          return;
        }
        x++;
        System.out.println(x);
        parchment4duplicate.setVisible(true);
      }

      System.out.println(selectedParchment);
    } else {
      System.out.println("nothing");
    }
  }

  // Add more methods for handling other parchments if needed

  // You can also add more event handlers for your specific requirements

  /** Initializes the room view, it is called when the room loads. */

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      showDialog("Info", "Riddle", "You need to resolve the riddle!");
      App.setRoot("chat");
      return;
    }

    if (!GameState.isKeyFound) {
      showDialog(
          "Info", "Find the key!", "You resolved the riddle, now you know where the key is.");
    } else {
      showDialog("Info", "You Won!", "Good Job!");
    }
  }

  /**
   * Handles the click event on the vase.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickVase(MouseEvent event) {
    System.out.println("vase clicked");
    if (GameState.isRiddleResolved && !GameState.isKeyFound) {
      showDialog("Info", "Key Found", "You found a key under the vase!");
      GameState.isKeyFound = true;
    }
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("window clicked");
  }

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("boxing bag")));
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
   * @throws ApiProxyException if there is an error communicating with the API proxy
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
