package nz.ac.auckland.se206;

import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

public class CustomNotifications {
  public static void generateNotification(String title, String text) {
    Notifications.create()
        .title(title)
        .position(Pos.BOTTOM_RIGHT)
        .owner(null)
        .text(text)
        .threshold(1, Notifications.create().title("Collapsed Notification"))
        .showWarning();
  }
}
