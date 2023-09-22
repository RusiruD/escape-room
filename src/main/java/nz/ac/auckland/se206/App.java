package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.GameState.State;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.ChestController;
import nz.ac.auckland.se206.controllers.CorridorController;
import nz.ac.auckland.se206.controllers.PuzzleController;
import nz.ac.auckland.se206.controllers.PuzzleRoomController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SceneManager;
import nz.ac.auckland.se206.controllers.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.StartScreenController;
import nz.ac.auckland.se206.controllers.UntangleRoomController;
import nz.ac.auckland.se206.controllers.WinLossController;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  public static SceneManager.AppUi oldScene = null;
  public static SceneManager.AppUi newScene = AppUi.CORRIDOR;

  private static Scene scene;

  private static Parent root;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(SceneManager.AppUi appUi) throws IOException {

    oldScene = newScene;
    newScene = appUi;

    scene.setRoot(SceneManager.getUiRoot(appUi));

    root.requestFocus();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {

    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {

    SceneManager.addUi(AppUi.UNTANGLE, loadFxml("untangleRoom"));
    SceneManager.addUi(AppUi.WINLOSS, loadFxml("winloss"));
    SceneManager.addUi(AppUi.LEADERBOARD, loadFxml("leaderboard"));
    SceneManager.addUi(AppUi.FIRST_ROOM, loadFxml("room"));
    SceneManager.addUi(AppUi.CORRIDOR, loadFxml("corridor"));
    SceneManager.addUi(AppUi.START, loadFxml("startScreen"));
    SceneManager.addUi(AppUi.PUZZLE, loadFxml("puzzle"));
    SceneManager.addUi(AppUi.PUZZLEROOM, loadFxml("puzzleroom"));
    SceneManager.addUi(AppUi.CHEST, loadFxml("chest"));
    SceneManager.addUi(AppUi.CHAT, loadFxml("chat"));

    SceneManager.addController(PuzzleRoomController.getInstance());
    SceneManager.addController(WinLossController.getInstance());
    SceneManager.addController(RoomController.getInstance());
    SceneManager.addController(CorridorController.getInstance());
    SceneManager.addController(PuzzleController.getInstance());
    SceneManager.addController(UntangleRoomController.getInstance());
    SceneManager.addController(ChestController.getInstance());

    root = SceneManager.getUiRoot(AppUi.START);
    scene = new Scene(root, 780.0, 780.0);
    stage.setScene(scene);
    stage.show();
    focus();
  }

  public static void focus() {
    root.requestFocus();
  }

  public static void resetToDefault() {
    // Reset the game state
    GameState.isGameWon = false;
    GameState.isKey1Collected = false;
    GameState.isKey2Collected = false;
    GameState.isKey3Collected = false;
    GameState.isChestOpened = false;
    GameState.isRiddleResolved = false;
    // Reset the puzzle room state
    GameState.puzzleRoomSolved.set(false);
    GameState.firstPotion = "";
    GameState.secondPotion = "";
    GameState.riddle = null;
    GameState.currentRoom = State.CHEST;
  }

  /**
   * Return to the corridor and navigate to the corridor view. Adjust the stage size to fit the
   * corridor dimensions.
   */
  public static void setStageToSize(double width, double height) {
    Stage primaryStage = (Stage) scene.getWindow();
    primaryStage.setWidth(width + 15);
    primaryStage.setHeight(height + 38);
  }

  
  public static void goToChest(){
    try{
      App.setRoot(AppUi.CHEST);
      ChestController chestController = ChestController.getInstance();
      
      setStageToSize(chestController.getChestWidth(), chestController.getChestHeight());}
      catch (IOException e) {
        e.printStackTrace();
      }
    
    }
    public static void goToPuzzle(){
      try{
        App.setRoot(AppUi.PUZZLE);
        PuzzleController puzzleController = PuzzleController.getInstance();
        
       setStageToSize(puzzleController.getPuzzleWidth(),puzzleController.getPuzzleHeight());}
        catch (IOException e) {
          e.printStackTrace();
      }
    }
    

    
  
  public static void goToChat(){
    try{
      App.setRoot(AppUi.CHAT);
     ChatController chatController = ChatController.getInstance();
    setStageToSize(chatController.getChatWidth(), chatController.getChatHeight());}
      catch (IOException e) {
        e.printStackTrace();
      }
  }
  public static void returnToCorridor() {
    try {
      // Set the root view to the corridor
      App.setRoot(AppUi.CORRIDOR);
CorridorController corridorController = CorridorController.getInstance();
      // Get the corridor controller instance
   setStageToSize(corridorController.getCorridorWidth(), corridorController.getCorridorHeight());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void makeSwordAndShieldAppear() {
    CorridorController corridorController = CorridorController.getInstance();
    corridorController.onTreasureChestUnlocked();
  }

  /**
   * Navigate to Door 1 and enter the first room. Adjust the stage size to fit the first room
   * dimensions.
   */
  public static void resetPlayerImage() {
    CorridorController corridorController = CorridorController.getInstance();
    corridorController.resetPlayerImage();
  }

  public static void goToDoor1() {

    try {
      // Set the root view to the first room
      App.setRoot(AppUi.FIRST_ROOM);

      // Get the first room controller instance
      RoomController roomController = RoomController.getInstance();

      // Get the dimensions of the first room
     setStageToSize(roomController.getRoomWidth(), roomController.getRoomHeight());

    } catch (IOException e) {
      e.printStackTrace();
    }

    // Focus on the first room
    focus();
  }

  /**
   * Navigate to Door 2 and enter the Untangle room. Adjust the stage size to fit the Untangle room
   * dimensions.
   */
  public static void goToDoor2() {

    try {
      // Set the root view to the Untangle room
      App.setRoot(AppUi.UNTANGLE);

      // Get the Untangle room controller instance
      UntangleRoomController untangleroomController = UntangleRoomController.getInstance();

      // Get the dimensions of the Untangle room
     setStageToSize(untangleroomController.getUntangleRoomWidth(), untangleroomController.getUntangleRoomHeight());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Navigate to Door 3 and enter the Puzzle room. Adjust the stage size to fit the Puzzle room
   * dimensions.
   */
  public static void goToDoor3() {

    try {
      // Set the root view to the Puzzle room
      App.setRoot(AppUi.PUZZLEROOM);

      // Get the Puzzle room controller instance
      PuzzleRoomController puzzleroomController = PuzzleRoomController.getInstance();

      // Get the dimensions of the Puzzle room
      setStageToSize(puzzleroomController.getPuzzleRoomWidth(), puzzleroomController.getPuzzleRoomHeight());

    } catch (IOException e) {

      e.printStackTrace();
    }

    // Focus on the new room
    focus();
  }

  /**
   * Switches the application UI to the Win/Loss screen. Adjusts the primary stage size to fit the
   * Win/Loss screen content.
   */
  public static void goToWinLoss() {
    try {
      // Set the root of the application to the Win/Loss screen.
      App.setRoot(AppUi.WINLOSS);

      // Get the instance of the WinLossController.
      WinLossController winLossController = WinLossController.getInstance();

      // Get the dimensions of the Win/Loss screen content.
     setStageToSize(winLossController.getWinLossWidth(), winLossController.getWinLossHeight());

    } catch (IOException e) {
      // Handle any IOException that might occur during the switch.
      e.printStackTrace();
    }

    // Set the focus to an unspecified method.
    focus();
  }

  /**
   * Switches the application UI to the Start Screen. Adjusts the primary stage size to fit the
   * Start Screen content.
   */
  public static void goToStartScreen() {
    try {
      // Set the root of the application to the Start Screen.
      App.setRoot(AppUi.START);

      // Get the instance of the StartScreenController.
      StartScreenController startScreenController = StartScreenController.getInstance();

      // Get the dimensions of the Start Screen content.
      setStageToSize(startScreenController.getStartScreenWidth(), startScreenController.getStartScreenHeight());

    } catch (IOException e) {
      // Handle any IOException that might occur during the switch.
      e.printStackTrace();
    }

    // Set the focus to an unspecified method.
    focus();
  }
}
