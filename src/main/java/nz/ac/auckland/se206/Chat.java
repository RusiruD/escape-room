package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class Chat {
  private static Chat instance;

  public static Chat getInstance() {
    return instance;
  }

  @FXML private TextArea chatTextArea;

  private ChatCompletionRequest chatCompletionRequest;
  private List<TextArea> variousChatScreens;

  public Chat() {
    instance = this;
    chatTextArea = new TextArea();
    variousChatScreens = new ArrayList<>();
  }

  public void addChat(TextArea chat) {
    variousChatScreens.add(chat);
  }

  public void initialiseAfterStart() throws ApiProxyException {

    if (GameState.currentDifficulty == GameState.Difficulty.MEDIUM) {}

    // Create a CompletableFuture for the background task

    CompletableFuture.runAsync(
        () -> {
          // Configure the chat completion request
          chatCompletionRequest =
              new ChatCompletionRequest()
                  .setN(1)
                  .setTemperature(0.7)
                  .setTopP(0.8)
                  .setMaxTokens(100);

          // Run GPT-3 with an initial hint request
          runGpt(new ChatMessage("user", GptPromptEngineering.getHint()));
        });
  }

  private void appendChatMessage(ChatMessage msg, String role) {

    String message = role + ": " + msg.getContent() + "\n\n";

    chatTextArea.appendText(message);
    updateChats();
  }

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

  public void onSendMessage(String inputText, TextArea actualText)
      throws ApiProxyException, IOException {

    String message = inputText;

    // If the message is empty, return early
    if (message.trim().isEmpty()) {
      return;
    }

    // Append the fake message to the chat interface
    appendChatMessage(new ChatMessage("user", message), "Player");

    CompletableFuture.runAsync(
        () -> {

          // Clear the input field and create actual and fake chat messages
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

          if (runGpt(actualMessage).getContent().toLowerCase().substring(0, 4).equals("hint")) {
            GameState.hintsGiven++;
            System.out.println("HINT DETECTED!");
          }

          // Ensure that the UI updates on the JavaFX application thread
          Platform.runLater(() -> {});
        });
  }

  private void updateChats() {
    for (TextArea textArea : variousChatScreens) {
      textArea.setText(chatTextArea.getText());
    }
  }
}
