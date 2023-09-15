package nz.ac.auckland.se206.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class Inventory {
    @FXML
    private static ObservableList<String> inventory = FXCollections.observableArrayList();
    private static PuzzleRoomController PuzzleRoomController;
    private static RoomController RoomController;
    private static CorridorController CorridorController;

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
        PuzzleRoomController = controller;
    }

    public static void setRoomController(RoomController controller) {
        RoomController = controller;
    }

    public static void setCorridorController(CorridorController controller) {
        CorridorController = controller;
    }

    private static void update() {
        PuzzleRoomController.updateInventory();
        RoomController.updateInventory();
        CorridorController.updateInventory();
    }
}
