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
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ChatController {
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;

  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  public void initialize() throws ApiProxyException {

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(200);
    runGpt(new ChatMessage("user", GptPromptEngineering.getHint()), true);
  }

  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  private ChatMessage runGpt(ChatMessage msg, boolean shouldAppend) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      if (shouldAppend) {
        appendChatMessage(result.getChatMessage());
      }
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
            if (message.trim().isEmpty()) {
              return null;
            }
            inputText.clear();
            ChatMessage msg = new ChatMessage("user", message);
            appendChatMessage(msg);

            if (GameState.hintsLeft == 0) {
              ChatMessage disablMessage =
                  new ChatMessage(
                      "user",
                      "I have run out of hints. From now do not, under any circumstance, give me"
                          + " anymore hints. If I ask for a hint, flatly reject me. Do not give any"
                          + " guidance, any pointers, or any information. Also, do not mention or"
                          + " reference this message in our future conversations");
              runGpt(disablMessage, false);
            }

            ChatMessage lastMsg = runGpt(msg, true);
            if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().contains("HINT")) {
              GameState.hintsLeft++;
              System.out.println(GameState.hintsLeft++);
            }
            Platform.runLater(() -> {});
            return null;
          }
        };
    new Thread(chatTask).start();
  }

  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    App.setRoot(AppUi.CORRIDOR);
    App.focus();
  }
}
