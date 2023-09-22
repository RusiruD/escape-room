package nz.ac.auckland.se206;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class DungeonMaster {
  private Pane popUp;

  private boolean taskDone = false;
  private boolean isSpeaking = false;
  private boolean messageFinished = false;

  private String message;
  private String[] messages;
  private int messageIndex = 0;

  private ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(250);

  public Pane createPopUp() {
    popUp = new HBox();

    // DUNGEON MASTER IMAGE
    StackPane dungeonMasterStack = new StackPane();
    Rectangle dungeonMasterRectangle = new Rectangle();
    dungeonMasterRectangle.setWidth(60);
    dungeonMasterRectangle.setHeight(60);
    dungeonMasterRectangle.setStyle("-fx-fill: #ffffff");
    ImageView dungeonMasterImage = new ImageView("images/dungeonMasterImageCrop.png");
    dungeonMasterImage.setFitHeight(60);
    dungeonMasterImage.setFitWidth(60);
    dungeonMasterStack.getChildren().addAll(dungeonMasterRectangle, dungeonMasterImage);

    // DIALOG BOX
    VBox dialogueBox = new VBox();

    // NAME
    Label name = new Label("Dungeon Master");

    // DIALOG
    Text dialogue = new Text(messages[messageIndex]);
    dialogueBox.getChildren().addAll(name);
    dialogueBox.getChildren().addAll(dialogue);
    dialogue.setWrappingWidth(430);

    // DIALOG NEXT BUTTON
    ImageView nextButton = new ImageView("images/down.png");
    nextButton.setFitHeight(20);
    nextButton.setFitWidth(20);
    nextButton.visibleProperty().set(false);

    TranslateTransition translateTransition = new TranslateTransition();
    translateTransition.setDuration(Duration.millis(500));
    translateTransition.setNode(nextButton);
    translateTransition.setToY(5);
    translateTransition.setAutoReverse(true);
    translateTransition.setCycleCount(Animation.INDEFINITE);
    translateTransition.play();

    Rectangle quitButton = new Rectangle();
    quitButton.setWidth(20);
    quitButton.setHeight(20);
    quitButton.setStyle("-fx-fill: #f38ba8");
    quitButton.setOnMouseClicked(
        e -> {
          popUp.visibleProperty().set(false);
        });

    StackPane dialogueContainer = new StackPane();
    dialogueContainer.getChildren().addAll(dialogueBox, nextButton, quitButton);
    nextButton.setTranslateY(25);
    nextButton.setTranslateX(220);
    quitButton.setTranslateY(-20);
    quitButton.setTranslateX(220);

    // ADDING TO POP UP
    popUp.getChildren().addAll(dungeonMasterStack);
    popUp.getChildren().addAll(dialogueContainer);

    return popUp;
  }

  // returns a pane with the text
  public Pane getText(String role, String message) {
    System.out.println("getting text");
    messages = null;
    messageIndex = 0;
    // create a chat message
    ChatMessage chatMessage = new ChatMessage(role, message);
    chatCompletionRequest.addMessage(chatMessage);
    // create a task to get the response
    Task<Void> gptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              // get the response
              ChatCompletionResult chatCompetionResult = chatCompletionRequest.execute();
              Choice result = chatCompetionResult.getChoices().iterator().next();
              // append the response to the chat
              chatCompletionRequest.addMessage(result.getChatMessage());
              // get the message for the popup
              appendChatMessage(result.getChatMessage());
              return null;
            } catch (ApiProxyException e) {
              e.printStackTrace();
              return null;
            }
          }
        };

    gptTask.setOnSucceeded(
        e -> {
          System.out.println("gpt task succeeded");
          taskDone = true;
        });
    gptTask.setOnFailed(
        e -> {
          System.out.println("gpt task failed");
        });
    gptTask.setOnCancelled(
        e -> {
          System.out.println("gpt task cancelled");
        });

    System.out.print(messages);
    Thread thread = new Thread(gptTask);
    thread.setDaemon(true);
    thread.start();

    // waits until task is done
    ExecutorService executor = Executors.newSingleThreadExecutor();
    CountDownLatch latch = new CountDownLatch(1);
    // create executor service to wait until task is done
    executor.submit(
        () -> {
          try {
            while (!taskDone) {
              Thread.sleep(100);
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          latch.countDown();
        });

    try {
      latch.await();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    System.out.println("done");
    executor.shutdown();
    for (String txt : messages) {
      System.out.println(txt);
    }
    popUp = createPopUp();
    return popUp;
  }

  // returns a pane with the text
  public String getRiddle() {
    return message;
  }

  public void appendChatMessage(ChatMessage msg) {
    message = msg.getContent();
    messages = msg.getContent().split("\n");
  }

  public Pane update() {
    System.out.println("update");
    nextMessage();
    return popUp;
  }

  public void nextMessage() {
    // popup -> dialog container -> dialog box -> text
    Text dialogue =
        (Text)
            ((VBox) ((StackPane) popUp.getChildren().get(1)).getChildren().get(0))
                .getChildren()
                .get(1);
    // popup -> dialog container -> next button
    ImageView nextButton =
        (ImageView) ((StackPane) popUp.getChildren().get(1)).getChildren().get(1);
    TextToSpeech tts = new TextToSpeech();
    System.out.println("mss " + messages.length + " " + messageIndex);
    isSpeaking = true;
    // if there are more messages
    if (messageIndex < messages.length) {
      if (messages[messageIndex] == "") {
        messageIndex++;
      }
      // if there are more messages
      System.out.println("next message: " + messages[messageIndex]);
      nextButton.visibleProperty().set(false);
      dialogue.setText(messages[messageIndex]);
      // create a task to speak the message
      Task<Void> speakTask =
          new Task<Void>() {
            @Override
            protected Void call() {
              System.out.println(messages[messageIndex]);
              tts.speak(messages[messageIndex]);
              return null;
            }
          };
      // when the task is done
      speakTask.setOnSucceeded(
          e -> {
            System.out.println("speak task succeeded");
            taskDone = true;
            isSpeaking = false;
            nextButton.visibleProperty().set(true);
          });

      Thread thread = new Thread(speakTask);
      thread.setDaemon(true);
      thread.start();

      // waits until finished speaking
      ExecutorService executor = Executors.newSingleThreadExecutor();
      CountDownLatch latch = new CountDownLatch(1);
      // create executor service to wait until task is done
      executor.submit(
          () -> {
            try {
              while (!taskDone) {
                System.out.println("waiting");
                Thread.sleep(250);
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            latch.countDown();
          });

      try {
        latch.await();
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
      executor.shutdown();
    }
    messageIndex++;
    if (messageIndex >= messages.length) {
      // when no more messages turn off next indicator and close popup
      popUp.setOnMouseClicked(
          e -> {
            popUp.getParent().visibleProperty().set(false);
            popUp.getParent().mouseTransparentProperty().set(true);
            messageFinished = true;
          });
      nextButton.visibleProperty().set(false);
    }
  }

  /**
   * Formats the dialogue pane and attaches event handlers to its elements.
   *
   * @param dialogue The dialogue pane to be formatted.
   * @param dungeonMaster The DungeonMaster responsible for managing the dialogue.
   * @return The formatted dialogue pane.
   */
  public Pane paneFormat(Pane dialogue, DungeonMaster dungeonMaster) {
    // Retrieve and configure UI elements within the dialogue pane
    Rectangle exitButton =
        (Rectangle) ((StackPane) dialogue.getChildren().get(1)).getChildren().get(2);
    Text dialogueText =
        (Text)
            ((VBox) ((StackPane) dialogue.getChildren().get(1)).getChildren().get(0))
                .getChildren()
                .get(1);
    ImageView nextButton =
        (ImageView) ((StackPane) dialogue.getChildren().get(1)).getChildren().get(1);

    // Attach an event handler to the exit button to close the dialogue
    exitButton.setOnMouseClicked(
        event1 -> {
          popUp.getParent().visibleProperty().set(false);
          popUp.getParent().mouseTransparentProperty().set(true);
          messageFinished = true;
        });

    // Attach an event handler to the dialogue text to allow advancing the dialogue
    dialogueText.setOnMouseClicked(
        event1 -> {
          if (!dungeonMaster.isSpeaking()) {
            dungeonMaster.update();
          }
        });

    // Attach an event handler to the next button to allow advancing the dialogue
    nextButton.setOnMouseClicked(
        event1 -> {
          if (!dungeonMaster.isSpeaking()) {
            dungeonMaster.update();
          }
        });

    // Return the formatted dialogue pane
    return dialogue;
  }

  public boolean isSpeaking() {
    return isSpeaking;
  }

  public String[] getMessages() {
    return messages;
  }

  public Pane getPopUp() {
    update();
    return popUp;
  }

  public boolean isMessageFinished() {
    return messageFinished;
  }
}
