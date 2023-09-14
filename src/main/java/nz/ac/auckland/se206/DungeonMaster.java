package nz.ac.auckland.se206;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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

public class DungeonMaster {
  private Pane popUp;

  private boolean taskDone = false;

  private String[] messages;
  private int messageIndex = 0;

  private ChatCompletionRequest chatCompletionRequest;

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
    Text dialogue = new Text(messages[messageIndex]);
    messageIndex++;
    dialogueBox.getChildren().addAll(name);
    dialogueBox.getChildren().addAll(dialogue);
    dialogue.setWrappingWidth(430);

    // DIALOG NEXT BUTTON
    ImageView nextButton = new ImageView("images/down.png");
    nextButton.setFitHeight(20);
    nextButton.setFitWidth(20);

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

  public Pane getText() {
    System.out.println("getting text");
    chatCompletionRequest = new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(50);
    chatCompletionRequest.addMessage(new ChatMessage("user", "Can you give me some random chat messages"));
    Task<Void> gptTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        try {
          ChatCompletionResult chatCompetionResult = chatCompletionRequest.execute();
          Choice result = chatCompetionResult.getChoices().iterator().next();
          chatCompletionRequest.addMessage(result.getChatMessage());
          appendChatMessage(result.getChatMessage());
          return null;
        } catch (ApiProxyException e) {
          e.printStackTrace();
          return null;
        }
      }
    };

    gptTask.setOnSucceeded(e -> {
      System.out.println("gpt task succeeded");
      taskDone = true;
    });
    gptTask.setOnFailed(e -> {
      System.out.println("gpt task failed");
    });
    gptTask.setOnCancelled(e -> {
      System.out.println("gpt task cancelled");
    });

    System.out.print(messages);
    Thread thread = new Thread(gptTask);
    thread.setDaemon(true);
    thread.start();

    ExecutorService executor = Executors.newSingleThreadExecutor();
    CountDownLatch latch = new CountDownLatch(1);
    // create executor service to wait until task is done
    executor.submit(() -> {
      try {
        while (!taskDone) {
          System.out.println("waiting");
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

    return getPopUp();
  }

  public void appendChatMessage(ChatMessage msg) {
    messages = msg.getContent().split("\n");
    System.out.println("test");
  }

  public Pane update() {
    System.out.println("update");
    nextMessage();
    return popUp;
  }

  public void nextMessage() {
    System.out.println("mss " + messages.length + " " + messageIndex);
    if (messageIndex < messages.length) {
      System.out.println("next message: " + messages[messageIndex]);
      messageIndex++;
    } else {
      popUp.setOnMouseClicked(e -> {
        popUp.visibleProperty().set(false);
      });
    }
  }
}
