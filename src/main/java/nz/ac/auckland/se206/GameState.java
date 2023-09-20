package nz.ac.auckland.se206;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

/** Represents the state of the game. */
public class GameState {

  public enum roomState {
    MARCELLIN,
    RUSIRU,
    ZACH,
    CHEST
  }

  public static roomState currentRoom = roomState.CHEST;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKey1Collected = false;

  public static boolean isKey2Collected = false;
  public static boolean isKey3Collected = false;
  public static boolean isLock1Unlocked = false;
  public static boolean isLock2Unlocked = false;
  public static boolean isLock3Unlocked = false;

  public static String difficultyLevel = "";
  public static String gameTime = "";

  public static String firstPotion = "";
  public static String secondPotion = "";

  public static Riddle riddle;

  public static int hintsLeft = 0;

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
