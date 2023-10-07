package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public interface Controller {
  public static Controller getInstance() {
    return null;
  }

  public void updateTimerLabel(String time);

  public void updateMute();

  public void updateInventory();

  @FXML
  private void showChat(ActionEvent event) {}

  @FXML
  private void closeChat(ActionEvent event) {}

  private void handleTextInput() {}

  @FXML
  private void onSendMessage(ActionEvent event) {
    handleTextInput();
  }

  public void addChatToList();

  public void initialiseAfterStart();

  @FXML
  public void switchChatView(ActionEvent event);

  @FXML
  private void onKeyPressed(KeyEvent event) {}
}
