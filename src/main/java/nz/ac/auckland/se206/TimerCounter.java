package nz.ac.auckland.se206;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import nz.ac.auckland.se206.controllers.CorridorController;
import nz.ac.auckland.se206.controllers.PuzzleController;
import nz.ac.auckland.se206.controllers.PuzzleRoomController;
import nz.ac.auckland.se206.controllers.RoomController;

public class TimerCounter {

  private int timeCounter1;
  private int timeCounter2;
  private int timeCounter3;
  private RoomController roomController;
  private PuzzleController puzzleController;
  private CorridorController corridorController;
  private PuzzleRoomController puzzleRoomController;

  public TimerCounter() {
    timeCounter1 = 120;
    timeCounter2 = 240;
    timeCounter3 = 360;
  }

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

  public void twoMinutes() {
    new Timer()
        .schedule(
            new TimerTask() {

              @Override
              public void run() {
                timeCounter1--;

                // Formatting the seconds to be in a presentable/readable format
                int min = timeCounter1 / 60;
                int sec = timeCounter1 - min * 60;
                String string = min + ":" + String.format("%02d", sec);

                Platform.runLater(
                    () -> {

                      // Updating the timer counter across the multiple screens
                      updateTimers(string);

                      // Game over condition
                      if (timeCounter1 == 0) {

                        this.cancel();
                        gameOver();
                      }
                    });
              }
            },
            0,
            1000);
  }

  public void fourMinutes() {
    new Timer()
        .schedule(
            new TimerTask() {

              @Override
              public void run() {
                timeCounter2--;

                // Formatting the seconds to be in a presentable/readable format
                int min = timeCounter2 / 60;
                int sec = timeCounter2 - min * 60;
                String string = min + ":" + String.format("%02d", sec);

                Platform.runLater(
                    () -> {
                      updateTimers(string);
                      // Updating the timer counter across the multiple screens

                      // Game over condition
                      if (timeCounter2 == 0) {

                        this.cancel();
                        gameOver();
                      }
                    });
              }
            },
            0,
            1000);
  }

  public void sixMinutes() {
    new Timer()
        .schedule(
            new TimerTask() {

              @Override
              public void run() {
                timeCounter3--;

                // Formatting the seconds to be in a presentable/readable format
                int min = timeCounter3 / 60;
                int sec = timeCounter3 - min * 60;
                String string = min + ":" + String.format("%02d", sec);

                Platform.runLater(
                    () -> {
                      updateTimers(string);
                      // Updating the timer counter across the multiple screens

                      // Game over condition
                      if (timeCounter3 == 0) {

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
