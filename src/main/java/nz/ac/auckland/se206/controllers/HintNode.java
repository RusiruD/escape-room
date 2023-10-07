package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class HintNode {
  @FXML private TextArea textArea;
  @FXML private TextField inputText;
  @FXML private Button showButton;
  @FXML private Button closeButton;
  @FXML private Button sendButton;
  @FXML private ImageView chatBackground;
  @FXML private Button switchButton;
  @FXML private Label hintField;

  private List<Node> nodeList;

  public HintNode(
      TextArea textArea,
      TextField inputText,
      Button showButton,
      Button closeButton,
      Button sendButton,
      ImageView chatBackground,
      Button switchButton,
      Label hintField) {
    this.textArea = textArea;
    this.inputText = inputText;
    this.showButton = showButton;
    this.sendButton = sendButton;
    this.closeButton = closeButton;
    this.chatBackground = chatBackground;
    this.switchButton = switchButton;
    this.hintField = hintField;

    nodeList = new ArrayList<>();
    nodeList.add(textArea);
    nodeList.add(inputText);
    nodeList.add(closeButton);
    nodeList.add(chatBackground);
    nodeList.add(sendButton);
    nodeList.add(switchButton);
  }

  public List<Node> getNodeList() {
    return nodeList;
  }

  public Button getShowButton() {
    return showButton;
  }

  public Label getHintField() {
    return hintField;
  }

  public Button getCloseButton() {
    return closeButton;
  }

  public Button getSendButton() {
    return sendButton;
  }

  public Button getSwiButton() {
    return switchButton;
  }
}
