package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Controller;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ChatController implements Controller {

  private static ChatController instance;

  public static ChatController getInstance() {

    return instance;
  }

  @FXML private Label hintField;
  @FXML private Label lblTime;
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private AnchorPane chatPane;
  @FXML private TextArea lastHintTextArea;
  private boolean isThinking = false;
  private boolean isShowingLastHint = true;

  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  public void initialize() throws ApiProxyException {
    hintField.setVisible(false);
    instance = this;
  }

  @FXML
  public double getChatWidth() {
    return chatPane.getPrefWidth();
  }

  @FXML
  public double getChatHeight() {
    return chatPane.getPrefHeight();
  }

  /**
   * Initialize hints for the chat interface. This method sets up the initial chat request and
   * configuration for requesting hints from GPT-3. It runs GPT-3 in the background to prepare for
   * hint requests.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public void initialiseAfterStart() throws ApiProxyException {

    if (GameState.currentDifficulty == GameState.Difficulty.MEDIUM) {
      hintField.setVisible(true);
    }

    // Create a CompletableFuture for the background task

    CompletableFuture.runAsync(
        () -> {
          sendButton.setVisible(false);
          isThinking = true;
          // Configure the chat completion request
          chatCompletionRequest =
              new ChatCompletionRequest()
                  .setN(1)
                  .setTemperature(0.7)
                  .setTopP(0.8)
                  .setMaxTokens(100);

          // Run GPT-3 with an initial hint request
          runGpt(new ChatMessage("user", GptPromptEngineering.getHint()));
          isThinking = false;
          // Ensure that the UI updates on the JavaFX application thread
          Platform.runLater(
              () -> {
                sendButton.setVisible(true);
              });
        });
  }

  /**
   * Append a chat message to the chat interface.
   *
   * @param msg The chat message to append
   */
  private void appendChatMessage(ChatMessage msg, String role) {
    String message = role + ": " + msg.getContent() + "\n\n";
    chatTextArea.appendText(message);
    lastHintTextArea.appendText(message);
  }

  /**
   * Run GPT-3 with a chat message and update the chat interface with the response.
   *
   * @param msg The chat message to send to GPT-3
   * @return The response chat message from GPT-3
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) {
    chatCompletionRequest.addMessage(msg);
    try {
      // Execute the chat completion request with GPT-3
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());

      // Append the GPT-3 response to the chat interface
      appendChatMessage(result.getChatMessage(), "Dungeon Master");

      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Handle the action of sending a message in the chat interface. This method processes user input,
   * appends it to the chat, and sends it to GPT-3. It also adds context information based on the
   * current room.
   *
   * @param event The action event triggered by sending a message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    lastHintTextArea.clear();

    CompletableFuture.runAsync(
        () -> {
          sendButton.setVisible(false);
          if (isThinking) {
            return;
          }
          isThinking = true;
          String message = inputText.getText();

          // If the message is empty, return early
          if (message.trim().isEmpty()) {
            return;
          }

          // Clear the input field and create actual and fake chat messages
          inputText.clear();
          String hint = "";
          if (GameState.currentRoom == GameState.State.CHEST) {
            if (GameState.hasKeyOne && GameState.hasKeyTwo && GameState.hasKeyThree) {
              hint = "\"The riddle gives the information of the keys unlock the chest\"";
            } else {
              hint = "\"Search the dungeon for three keys\"";
            }
          } else if (GameState.currentRoom == GameState.State.MARCELLIN) {
            hint = "\"Move the points of the shape such that no lines between points overlap.\"";
          } else if (GameState.currentRoom == GameState.State.ZACH) {
            hint = "\"Investigate the door to find a sliding puzzle\"";
          } else if (GameState.currentRoom == GameState.State.RUSIRU) {
            if (GameState.noPapers == true) {
              hint = "\"Pick up all the papers.\"";
            } else if (GameState.noCombination == true && GameState.noPapers == false) {
              hint = "\"Put all the papers on the table and read it.\"";
            } else if (GameState.noPotionBoulder == true && GameState.noCombination == false) {
              hint = "\"Brew a potion of strength to move the big rock.\"";
            }
          }

          String contextMsg;
          if (GameState.currentDifficulty == GameState.Difficulty.HARD) {
            contextMsg = message;
          } else if (GameState.currentDifficulty == GameState.Difficulty.EASY) {
            contextMsg = GptPromptEngineering.hintPrompt(message, hint);
          } else {
            if (GameState.hintsGiven < 5) {
              contextMsg = GptPromptEngineering.hintPrompt(message, hint);
            } else {
              contextMsg = GptPromptEngineering.noHintPrompt(message);
            }
          }

          ChatMessage actualMessage = new ChatMessage("user", contextMsg);

          // Append the fake message to the chat interface
          appendChatMessage(new ChatMessage("user", message), "Player");

          if (runGpt(actualMessage).getContent().toLowerCase().contains("hint")) {
            GameState.hintsGiven++;
            System.out.println("HINT DETECTED!");
          }

          // Ensure that the UI updates on the JavaFX application thread
          Platform.runLater(
              () -> {
                int hintsLeft = 5 - GameState.hintsGiven;
                if (hintsLeft < 0) {
                  hintsLeft = 0;
                }
                hintField.setText(hintsLeft + " Hints(s) Remaining");
                sendButton.setVisible(true);
              });
          isThinking = false;
        });
  }

  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    if (App.oldScene.equals(AppUi.CORRIDOR)) {
      App.returnToCorridor();

    } else if (App.oldScene.equals(AppUi.FIRST_ROOM)) {
      App.goToDoor1();
    } else if (App.oldScene.equals(AppUi.PUZZLEROOM)) {
      App.goToDoor3();
    } else if (App.oldScene.equals(AppUi.UNTANGLE)) {
      App.goToDoor2();
    } else if (App.oldScene.equals(AppUi.CHEST)) {
      App.goToChest();
    } else if (App.oldScene.equals(AppUi.PUZZLE)) {
      App.goToPuzzle();
    }
  }

  @FXML
  private void onKeyPressed(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode() == KeyCode.ENTER) {
      onSendMessage(null);
    }
  }

  @FXML
  public void updateTimerLabel(String time) {
    lblTime.setText(time);
  }

  @FXML
  public void updateInventory() {
    // does nothing
  }

  @FXML
  public void onSwitch(ActionEvent event) {
    isShowingLastHint = !isShowingLastHint;
    lastHintTextArea.setVisible(isShowingLastHint);
  }
}
