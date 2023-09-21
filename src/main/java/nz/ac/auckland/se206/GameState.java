package nz.ac.auckland.se206;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

/** Represents the state of the game. */
public class GameState {

  public enum STATE {
    MARCELLIN,
    RUSIRU,
    ZACH,
    CHEST
  }

  public enum DIFFICULTY {
    EASY,
    MEDIUM,
    HARD,
  }

  public static DIFFICULTY currentDifficulty = DIFFICULTY.EASY;

  public static STATE currentRoom = STATE.CHEST;

  public static boolean aiCalled;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKey1Collected = false;

  public static boolean isKey2Collected = false;
  public static boolean isKey3Collected = false;
  public static boolean isChestOpened = false;

  public static String difficultyLevel = "";
  public static String gameTime = "";

  public static String firstPotion = "";
  public static String secondPotion = "";

  public static Riddle riddle;

  public static int hintsGiven = 0;

  public static boolean noPapers = true;
  public static boolean noCombination = true;
  public static boolean noPotionBoulder = true;

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
}
