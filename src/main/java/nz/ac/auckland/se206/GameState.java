package nz.ac.auckland.se206;

import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Represents the state of the game. */
public class GameState {

  /**
   * Enumeration representing different states or rooms in a game scenario. This enum helps
   * determine the current room the player is in. The possible states include MARCELLIN, RUSIRU,
   * ZACH, and CHEST. Players can use these states to track their location and progress within the
   * game.
   */
  public enum State {
    MARCELLIN,
    RUSIRU,
    ZACH,
    CHEST
  }

  /** Enum representing different difficulty levels for the game. */
  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD,
  }

  public enum TimeLimit {
    TWO_MINUTES,
    FOUR_MINUTES,
    SIX_MINUTES,
  }

  public static Difficulty currentDifficulty = Difficulty.EASY;

  public static TimeLimit currentTimeLimit = TimeLimit.TWO_MINUTES;

  public static State currentRoom = State.CHEST;

  public static boolean aiCalled;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKey1Collected = false;

  public static boolean isKey2Collected = false;
  public static boolean isKey3Collected = false;
  public static boolean isChestOpened = false;
  public static boolean isGameWon = false;
  public static boolean isMuted = false;
  public static String difficultyLevel = "";
  public static String gameTime = "";

  public static String firstPotion = "";
  public static String secondPotion = "";

  public static ArrayList<ScoreEntry> scores = new ArrayList<ScoreEntry>();

  public static Riddle riddle;
  public static TextToSpeech tts = new TextToSpeech();

  public static int hintsGiven = 0;
  public static int hintsUsed = 0;
  public static int totalTime = 0;
  public static int gamesWon = 0;  

  public static boolean hasKeyOne = false;
  public static boolean hasKeyTwo = false;
  public static boolean hasKeyThree = false;

  public static Chat chat = null;

  public static SimpleBooleanProperty puzzleRoomSolved = new SimpleBooleanProperty(false);

  public static ObservableBooleanValue getPuzzleRoomSolved() {
    return puzzleRoomSolved;
  }

  public static void setPuzzleRoomSolved(boolean value) {
    puzzleRoomSolved.set(value);
  }

  public static boolean isPuzzleRoomSolved() {
    return puzzleRoomSolved.get();
  }

  /**
   * Creates a TranslateTransition for the specified ImageView, making it bounce vertically.
   *
   * @param image The ImageView to apply the bouncing animation to.
   * @return The TranslateTransition with bouncing animation properties.
   */
  public static TranslateTransition translate(ImageView image) {
    // Default transition for bouncing
    TranslateTransition transition = new TranslateTransition();
    transition.setDuration(Duration.seconds(1));
    transition.setNode(image);
    transition.setFromY(0);
    transition.setToY(10);
    transition.setCycleCount(TranslateTransition.INDEFINITE);
    transition.setAutoReverse(true);

    return transition;
  }

  /**
   * Static method to reset all the game state variables. It sets various game state variables to
   * their initial values.
   */
  public static void reset() {
    // reset all the game state variables
    isRiddleResolved = false;
    isKey1Collected = false;
    isKey2Collected = false;
    isKey3Collected = false;
    isChestOpened = false;
    isGameWon = false;
    difficultyLevel = "";
    firstPotion = "";
    secondPotion = "";
    hintsGiven = 0;

    hasKeyOne = false;
    hasKeyTwo = false;
    hasKeyThree = false;
    puzzleRoomSolved.set(false);
  }

  /**
   * Static method to toggle the mute state of the game. If the game is currently muted, it will be
   * unmuted; otherwise, it will be muted. This method also updates the mute status for all
   * controllers in the scene.
   */
  public static void mute() {
    // mute the game
    if (isMuted) {
      System.out.println("unmute");
      isMuted = false;
    } else {
      System.out.println("mute");
      tts.cancel();
      isMuted = true;
    }
    for (Controller controller : SceneManager.getControllers()) {
      controller.updateMute();
    }
  }
}
