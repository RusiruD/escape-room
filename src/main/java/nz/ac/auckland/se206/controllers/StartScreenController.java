package nz.ac.auckland.se206.controllers;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerCounter;

public class StartScreenController {

    @FXML
    private ChoiceBox<String> timerChoice;
    @FXML
    private ChoiceBox<String> difficultyChoice;

    @FXML
    private void initialize() {
        timerChoice.getItems().add("2 Minutes");
        timerChoice.getItems().add("4 Minutes");
        timerChoice.getItems().add("6 Minutes");
        timerChoice.setValue("2 Minutes");
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
            TimerCounter time = new TimerCounter();

    // App.setRoot(SceneManager.AppUi.ROOM);
    
    if(s.equals("2 Minutes")){
        System.out.println("f");
    time.twoMinutes();
    }
    else if(s.equals("4 Minutes")){
        System.out.println("d");
        time.fourMinutes();
    }
    else{System.out.println("w");
time.sixMinutes();}
  }


}





