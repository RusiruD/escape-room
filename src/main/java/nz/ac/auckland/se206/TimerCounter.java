package nz.ac.auckland.se206;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import nz.ac.auckland.se206.controllers.RoomController;

public class TimerCounter {

  private int timeCounter1;
  private int timeCounter2;
  private int timeCounter3;
  private RoomController roomController;

  public TimerCounter(RoomController roomController) {
    this.roomController = roomController;
    timeCounter1 = 120;
    timeCounter2 = 240;
    timeCounter3 = 360;
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
                      roomController.updateTimerLabel(string);

                      /*App.getChatController().updateTime(string);
                      App.getKeypadController().updateTime(string);
                      App.getHelpController().updateTime(string);
                      App.getDrawerController().updateTime(string);*/

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
                      roomController.updateTimerLabel(string);
                      // Updating the timer counter across the multiple screens
                      /*App.getRoomController().updateTime(string);
                      App.getChatController().updateTime(string);
                      App.getKeypadController().updateTime(string);
                      App.getHelpController().updateTime(string);
                      App.getDrawerController().updateTime(string);*/

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
                      roomController.updateTimerLabel(string);
                      // Updating the timer counter across the multiple screens
                      /*App.getRoomController().updateTime(string);
                      App.getChatController().updateTime(string);
                      App.getKeypadController().updateTime(string);
                      App.getHelpController().updateTime(string);
                      App.getDrawerController().updateTime(string);*/

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
    // Dialogue.showDialog("Game Over", "You've lost! :(", "You ran out of time to solve the
    // riddle!");
    System.exit(0);
  }
}
