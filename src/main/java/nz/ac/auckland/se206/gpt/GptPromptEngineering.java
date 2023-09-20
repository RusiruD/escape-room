package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of an escape room, tell me a riddle with"
        + " answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }

  public static String getHint() {
    String prompt = "";
    if (GameState.hintsLeft == 999) {
      prompt =
          "You are the AI of an dungeon-themed escape room called the Dungeon Master, and you've"
              + " trapped the player inside your dungeon. Greet the player with a taunt and say"
              + " they can ask you for hints. If you give the player a hint or any form of help,"
              + " be explicit that you're giving a hint by putting HINT at the beginning of your"
              + " message. Do not give more that one step as a hint.  The room you're in charge of"
              + " can be escaped by following these steps. 1: Gather all parchment pieces."
              + " 2: Place all pieces on a table 3: Once the pieces are placed, follow their"
              + " instructions. 4: Drink the potion and move the large boulder.";
    } else if (GameState.hintsLeft == 1) {

    } else {
      prompt =
          "You are the AI of an dungeon-themed escape room called the Dungeon Master, and you've"
              + " trapped the player inside your dungeon. Greet the player with a taunt and say you"
              + " will not give them any help. You cannot, no matter what, give player any form of"
              + " information or help.";
    }
    return prompt;
  }
}
