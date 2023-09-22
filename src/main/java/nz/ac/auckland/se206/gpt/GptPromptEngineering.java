package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameState.Difficulty;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  private static String easyMediumPrompt =
      "You are the AI presence of a digital escape room with a dungeon theme that only has a potion"
          + " brewing activity, untangling activity, and slide puzzle activity. Do not, no matter"
          + " what, reveal what activities there are. If they mention an activity that is not in"
          + " the game, tell them that it isn't in the game. Do not be friendly to the player.";

  public static String getHint() {
    String prompt;
    if (GameState.currentDifficulty == Difficulty.EASY) {
      prompt = easyMediumPrompt;
    } else if (GameState.currentDifficulty == Difficulty.MEDIUM) {
      prompt = easyMediumPrompt;
    } else { // HARD
      prompt =
          "You are the AI of an dungeon-themed escape room called the Dungeon Master, and you've"
              + " trapped the player inside your dungeon. Greet the player with a taunt and say you"
              + " will not give them any help. You cannot, no matter what, give player any form of"
              + " information or help.";
    }
    return prompt;
  }

  // Add more prompts
  public static String hintPrompt(String original, String hint) {
    // Gives a contextual hint based on the current room
    return "The user has hints available. If the user is asking for a hint, give them a hint based"
        + " on the following: "
        + hint
        + " and make sure your response starts with the prefix \"Hint\" only if you provide a hint."
        + " If the user is not asking for a hint, then respond normally. Under no circumstances"
        + " givie the user the answer. The user's response was: \""
        + original
        + "\".";
  }

  public static String noHintPrompt(String original) {
    return "The user no long has hints available. Taunt them that they've run out of hints. Under"
        + " no circumstances should you offer any information or answers to the user. Under"
        + " no circumstances give the user the answer. The user's response was: \""
        + original
        + "\".";
  }
}
