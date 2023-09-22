package nz.ac.auckland.se206;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.WinLossController;

public class TimerCounter {

  public void timerStart(int time) {
    final int[] timeCounter = new int[1];
    System.out.println("timer started");
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
                        System.out.println("timer stopped");
                        gameOver();
                      }
                      if (GameState.isGameWon) {
                        System.out.println("timer stopped");
                        this.cancel();
                      }
                    });
              }
            },
            0,
            1000);
  }

  private void gameOver() {
    App.goToWinLoss();
    WinLossController.getInstance().checkGameStatus();
  }

  private void updateTimers(String string) {
    for (Controller controller : SceneManager.getControllers()) {
      if (controller.equals(WinLossController.getInstance())) {
        continue;
      }
      controller.updateTimerLabel(string);
    }
  }
}
