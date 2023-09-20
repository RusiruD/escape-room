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

  private static String chestRoom =
      "1: There's a chest in the room. 2: Upon investigating the chest, there will be a three verse"
          + " poem. The first verse subtly tells you where to put the first key, second verse"
          + " second key, and so on.";

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

  private static String initial =
      "You are the AI of an dungeon-themed escape room called the Dungeon Master, and you've"
          + " trapped the player inside your dungeon with four rooms named rusiruRoom,"
          + " marcellinRoom, zachRoom, and chestRoom. Never mention any of the room names under any"
          + " circumstances. ";

  private static String end =
      " To give you context, the player will include information in parenthesis. Since this is"
          + " role-play, do not mention that contextual information";

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
          initial
              + "Greet the player with a taunt and say they can ask you for hints. Do not give more"
              + " than one step or instruction as a hint."
              + end
              + hugePrompt;
    } else if (GameState.hintsLeft < 999 && GameState.hintsLeft != 0) { // MEDIUM
      prompt =
          initial
              + "Greet the player with a taunt and say"
              + " they can ask you for "
              + GameState.hintsLeft
              + " hints. You cannot give hints or information without asking. Do not give more than"
              + " one step or instruction as a hint. It is extremely important that once you've"
              + " given "
              + GameState.hintsLeft
              + " hints that you don't given any more, and if you're asked more for help, hints, or"
              + " information, reject it."
              + end
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
