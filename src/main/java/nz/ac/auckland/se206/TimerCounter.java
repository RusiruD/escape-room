package nz.ac.auckland.se206;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import nz.ac.auckland.se206.controllers.CorridorController;
import nz.ac.auckland.se206.controllers.PuzzleController;
import nz.ac.auckland.se206.controllers.PuzzleRoomController;
import nz.ac.auckland.se206.controllers.RoomController;

public class TimerCounter {

  private RoomController roomController;
  private PuzzleController puzzleController;
  private CorridorController corridorController;
  private PuzzleRoomController puzzleRoomController;

  public void setRoomController(RoomController controller) {
    roomController = controller;
  }

  public void setPuzzleController(PuzzleController controller) {
    puzzleController = controller;
  }

  public void setCorridorController(CorridorController controller) {
    corridorController = controller;
  }

  public void setPuzzleRoomController(PuzzleRoomController controller) {
    puzzleRoomController = controller;
  }

  public void timerStart(int time) {
    final int[] timeCounter = new int[1];
    timeCounter[0] = time;
    new Timer()
        .schedule(
            new TimerTask() {

              @Override
              public void run() {
                timeCounter[0]--;

                // Formatting the seconds to be in a presentable/readable format
                int min = timeCounter[0] / 60;
                int sec = timeCounter[0] - min * 60;
                String string = min + ":" + String.format("%02d", sec);

                Platform.runLater(
                    () -> {

                      // Updating the timer counter across the multiple screens
                      updateTimers(string);

                      // Game over condition
                      if (timeCounter[0] == 0) {
                        this.cancel();
                        gameOver();
                      }
                    });
              }
            },
            0,
            1000);
  }

  private void gameOver() {
    System.exit(0);
  }

  private void updateTimers(String string) {
    roomController.updateTimerLabel(string);
    puzzleController.updateTimerLabel(string);
    puzzleRoomController.updateTimerLabel(string);
    corridorController.updateTimerLabel(string);
  }
}
