package nz.ac.auckland.se206.controllers;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;

public class StartScreenController {

    @FXML
    private ChoiceBox<String> timerChoice;

    @FXML
    private void initialize() {
        timerChoice.getItems().add("2 minutes");
        timerChoice.getItems().add("4 minutes");
        timerChoice.getItems().add("6 minutes");
        timerChoice.setValue("2 minutes");

        // You can perform additional initialization here if needed.
    }
    @FXML
  private void onStartGame(ActionEvent event) {
    String s=timerChoice.getValue();
    System.out.println(s);
  }


}





