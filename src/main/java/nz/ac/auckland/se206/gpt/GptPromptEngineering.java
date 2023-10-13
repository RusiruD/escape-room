package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameState.Difficulty;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  // private static String easyMediumPrompt =
  //     "You are the AI presence of a digital escape room with a dungeon theme that only has a
  // potion"
  //         + " brewing activity, untangling activity, and slide puzzle activity. Do not, no
  // matter"
  //         + " what, reveal what activities there are. If they mention an activity that is not in"
  //         + " the game, tell them that it isn't in the game. Do not be friendly to the player.";

  private static String easyMediumPrompt =
      "You are to roleplay as the dungeon master of a dungeon. You will receive messages from a"
          + " user role-playing as someone trapped in your dungeon Do not be friendly to the user."
          + " Greet the user with a short taunt. ";

  /**
   * Retrieves the hint prompt based on the current game difficulty level. If the game difficulty is
   * set to EASY or MEDIUM, a predefined hint prompt is provided. For HARD difficulty, a specific
   * challenge prompt is given, instructing the AI not to provide any help.
   *
   * @return The hint prompt based on the current game difficulty level.
   */
  public static String getHint() {
    String prompt;
    if (GameState.currentDifficulty == Difficulty.EASY
        || GameState.currentDifficulty == Difficulty.MEDIUM) {
      prompt = easyMediumPrompt;
    } else { // Difficulty.HARD
      prompt =
          "You are the AI of a dungeon-themed escape room called the Dungeon Master, and you've"
              + " trapped the player inside your dungeon. Greet the player with a taunt and say you"
              + " will not give them any help. You cannot, no matter what, give the player any form"
              + " of information or help.";
    }
    return prompt;
  }

  /**
   * Generates a response prompt giving a contextual hint based on the current room if the user is
   * asking for a hint. Responds normally if the user is not asking for a hint.
   *
   * @param original The original user's message.
   * @param hint The contextual hint to be provided.
   * @return A response with a contextual hint or a normal response based on user's request.
   */
  public static String hintPrompt(String original, String hint) {
    // Gives a contextual hint based on the current room
    return "If the user is asking for a hint, give them a hint based"
        + " on the following: "
        + hint
        + " and make sure your response starts with the prefix \"Hint\" if you provide a hint."
        + " If the user is not asking for a hint, then respond normally. The user's response was:"
        + " \""
        + original
        + "\".";
  }

  /**
   * Generates a response prompt indicating that the user has run out of hints and taunts them.
   * Under no circumstances provides any information or answers to the user.
   *
   * @param original The original user's message.
   * @return A response indicating the user has no hints available.
   */
  public static String noHintPrompt(String original) {
    return "The user no long has hints available. Taunt them that they've run out of hints. Under"
        + " no circumstances should you offer any information or answers to the user. Under"
        + " no circumstances give the user the answer. The user's response was: \""
        + original
        + "\".";
  }
}
