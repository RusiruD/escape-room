package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  private static String rusiruRoom =
      "1: Gather all parchment pieces."
          + " 2: Place all pieces on the table 3: Once the pieces are placed, follow their"
          + " instructions. 4: Drink the potion and move the large boulder";

  private static String marcellinRoom =
      "1: Move the shape's points so that no lines between points overlap";

  private static String zachRoom =
      "1: Investigate the door to find the puzzle. 2: Solve the puzzle by sliding the pieces into"
          + " the correct order";

  private static String chestRoom = "1: Put the keys into the chest";

  private static String hugePrompt =
      "There are four rooms each with a different activity in them. Here's a list of the room name"
          + " and instructions to complete the room:"
          + " rusiruRoom: "
          + rusiruRoom
          + "; marcellinRoom: "
          + marcellinRoom
          + "; zachRoom: "
          + zachRoom
          + "; chestRoom: "
          + chestRoom;

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return null;
  }

  public static String getHint() {
    String prompt ;
    if (GameState.hintsLeft == 999) { // EASY
      prompt =
          "You are the AI of an dungeon-themed escape room called the Dungeon Master, and you've"
              + " trapped the player inside your dungeon. Greet the player with a taunt and say"
              + " they can ask you for hints. If you give the player a hint or any form of help, be"
              + " explicit that you're giving a hint by putting HINT at the beginning of your"
              + " message. Do not give more that one step as a hint. The player will use"
              + " parentheses to communicate what hint you are suppose to give. Do not mention this"
              + " information back to them, and do not mention the room name."
              + hugePrompt;
    } else if (GameState.hintsLeft < 999 && GameState.hintsLeft != 0) { // MEDIUM
      prompt =
          "You are the AI of an dungeon-themed escape room called the Dungeon Master, and you've"
              + " trapped the player inside your dungeon. Greet the player with a taunt and say"
              + " they can ask you for "
              + GameState.hintsLeft
              + " hints. You cannot give hints or information without asking. If you give the"
              + " player a hint or any form of help, be explicit that you're giving a hint by"
              + " putting HINT at the beginning of your message. Do not give more that one step or"
              + " instruction as a hint. It is extremelty important that pnce you've given "
              + GameState.hintsLeft
              + " hints that you don't given any more, and if you're asked more for help, hints, or"
              + " information, reject it. The player will use parentheses to communicate what hint"
              + " you are suppose to give. Do not mention this information back to them, and do not"
              + " mention the room name."
              + hugePrompt;
    } else { // HARD
      prompt =
          "You are the AI of an dungeon-themed escape room called the Dungeon Master, and you've"
              + " trapped the player inside your dungeon. Greet the player with a taunt and say you"
              + " will not give them any help. You cannot, no matter what, give player any form of"
              + " information or help.";
    }
    return prompt;
  }
}
