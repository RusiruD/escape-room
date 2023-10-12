package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import nz.ac.auckland.se206.Controller;

public class ObjectiveMarker {
  @FXML private static String objective = "placeholder";

  public static void setObjective(String string) {
    objective = string;
  }

  public static String getObjective() {
    return objective;
  }

  public static void update() {
    for (Controller controller : SceneManager.getControllers()) {
      if (controller.equals(WinLossController.getInstance())) {
        continue;
      }
      controller.updateObjective();
    }
  }
}
