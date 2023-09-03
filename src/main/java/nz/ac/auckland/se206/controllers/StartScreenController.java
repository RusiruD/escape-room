package nz.ac.auckland.se206.controllers;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class StartScreenController {

    @FXML
    private ChoiceBox<String> timerChoice;
    @FXML
    private ChoiceBox<String> difficultyChoice;

    @FXML
    private void initialize() {
        timerChoice.getItems().add("2 minutes");
        timerChoice.getItems().add("4 minutes");
        timerChoice.getItems().add("6 minutes");
        timerChoice.setValue("2 minutes");
        difficultyChoice.getItems().add("Easy");
        difficultyChoice.getItems().add("Medium");
        difficultyChoice.getItems().add("Hard");
        difficultyChoice.setValue("Easy");

        // You can perform additional initialization here if needed.
    }
    @FXML
  private void onStartGame(ActionEvent event) {
    String s=timerChoice.getValue();
    String x=difficultyChoice.getValue();
    System.out.println(s);
    System.out.println(x);
  }


}





