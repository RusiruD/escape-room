package nz.ac.auckland.se206;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Represents the state of the game. */
public class GameState {

  public enum State {
    MARCELLIN,
    RUSIRU,
    ZACH,
    CHEST
  }

  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD,
  }

  public static Difficulty currentDifficulty = Difficulty.EASY;

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

  public static Riddle riddle;
  public static TextToSpeech tts = new TextToSpeech();

  public static int hintsGiven = 0;

  public static boolean noPapers = true;
  public static boolean noCombination = true;
  public static boolean noPotionBoulder = true;
  public static boolean hasKeyOne = false;
  public static boolean hasKeyTwo = false;
  public static boolean hasKeyThree = false;

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

  public static TranslateTransition translate(ImageView image) {
    // default transition for boucning
    TranslateTransition transition = new TranslateTransition();
    transition.setDuration(Duration.seconds(1));
    transition.setNode(image);
    transition.setFromY(0);
    transition.setToY(10);
    transition.setCycleCount(TranslateTransition.INDEFINITE);
    transition.setAutoReverse(true);

    return transition;
  }

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
    noPapers = true;
    noCombination = true;
    noPotionBoulder = true;
    hasKeyOne = false;
    hasKeyTwo = false;
    hasKeyThree = false;
    puzzleRoomSolved.set(false);
  }

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
