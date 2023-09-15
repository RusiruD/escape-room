package nz.ac.auckland.se206.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class Inventory {
    @FXML
    private static ObservableList<String> inventory = FXCollections.observableArrayList();
    private static PuzzleRoomController puzzleRoomController;
    private static RoomController roomController;
    private static CorridorController corridorController;

    public static void addToInventory(String string) {
        inventory.add(string);
        update();
    }

    public static ObservableList<String> getInventory() {
        return inventory;
    }

    public static void removeFromInventory(String string) {
        inventory.remove(string);
        update();
    }

    public static void setPuzzleRoomController(PuzzleRoomController controller) {
        puzzleRoomController = controller;
    }

    public static void setRoomController(RoomController controller) {
        roomController = controller;
    }

    public static void setCorridorController(CorridorController controller) {
        corridorController = controller;
    }

    private static void update() {
        puzzleRoomController.updateInventory();
        roomController.updateInventory();
        corridorController.updateInventory();
    }
}
