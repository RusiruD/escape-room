package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** Utility class for managing music playback in the game. */
public class Music {

  private static MediaPlayer mediaPlayer;
  private static Media background = generateMedia("music1");
  private static Media win = generateMedia("Win");
  private static Media loss = generateMedia("Loss");

  public static void playBackgroundMusic() {
    playMusic(background);
  }

  public static void playWinMusic() {
    playMusic(win);
  }

  public static void playLossMusic() {
    playMusic(loss);
  }

  private static Media generateMedia(String name) {
    try {
      Media music = new Media(App.class.getResource("/sounds/" + name + ".mp3").toURI().toString());
      return music;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  /** Starts playing the music music. */
  private static void playMusic(Media music) {
    if (mediaPlayer != null) {
      stop();
    }
    mediaPlayer = new MediaPlayer(music);
    mediaPlayer.play();
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
