package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameState.ROOM_STATE;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ChatController {

  private static ChatController instance;

  public static ChatController getInstance() {
    return instance;
  }

  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;

  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  public void initialize() throws ApiProxyException {
    instance = this;
  }

  public void intialiseHints() throws ApiProxyException {

    Task<Void> chatTask =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            chatCompletionRequest =
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.2)
                    .setTopP(0.5)
                    .setMaxTokens(500);
            runGpt(new ChatMessage("user", GptPromptEngineering.getHint()));

            Platform.runLater(() -> {});
            return null;
          }
        };
    new Thread(chatTask).start();
  }

  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());

      appendChatMessage(result.getChatMessage());

      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {

    Task<Void> chatTask =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            String message = inputText.getText();
            String extra = "";
            if (GameState.currentRoom == ROOM_STATE.CHEST) {
              extra = " (chestRoom)";
            } else if (GameState.currentRoom == ROOM_STATE.ZACH) {
              extra = " (zachRoom)";
            } else if (GameState.currentRoom == ROOM_STATE.RUSIRU) {
              extra = " (rusiruRoom)";
            } else {
              extra = " (marcellinRoom)";
            }
            message = message.concat(extra);
            if (message.trim().isEmpty()) {
              return null;
            }
            inputText.clear();
            ChatMessage actualMessage = new ChatMessage("user", message);
            ChatMessage fakeMessage = new ChatMessage("user", message.replace(extra, ""));
            appendChatMessage(fakeMessage);
            runGpt(actualMessage);
            Platform.runLater(() -> {});
            return null;
          }
        };
    new Thread(chatTask).start();
  }

  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    App.setRoot(App.oldScene);
  }
}
