package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    ROOM,
    UNTANGLE
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi ui, Parent parent) {
    sceneMap.put(ui, parent);
  }

  public static Parent getUiRoot(AppUi ui) {
    return sceneMap.get(ui);
  }
}
