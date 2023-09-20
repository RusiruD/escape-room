package nz.ac.auckland.se206;

import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

public class CustomNotifications {
  // generate a notification
  public static void generateNotification(String title, String text) {
    // generate a notification
    Notifications.create()
        .title(title) // set the title
        .position(Pos.BOTTOM_RIGHT) // set the position
        .owner(null) // set the owner
        .text(text) // set the text
        .threshold(1, Notifications.create().title("Collapsed Notification")) // set the threshold
        .showWarning(); // show the notification
  }
}
