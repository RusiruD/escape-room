package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {

  private static MediaPlayer mediaPlayer;

  public static void begin() {
    try {
      Media sound =
          new Media(App.class.getResource("/sounds/" + "music1" + ".mp3").toURI().toString());
      mediaPlayer = new MediaPlayer(sound);
      mediaPlayer.play();
    } catch (Exception e) {

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
