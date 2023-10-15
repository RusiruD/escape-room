package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** Utility class for managing music playback in the game. */
public class Music {

  private static MediaPlayer mediaPlayer;

  /** Starts playing the background music. */
  public static void begin() {
    // Starts playing the background music.

    try {
      Media sound =
          new Media(App.class.getResource("/sounds/" + "music1" + ".mp3").toURI().toString());
      mediaPlayer = new MediaPlayer(sound);
      mediaPlayer.play();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public static void pause() {
    mediaPlayer.pause();
  }

  public static void unpause() {
    mediaPlayer.play();
  }

  public static void stop() {
    mediaPlayer.stop();
  }
}
