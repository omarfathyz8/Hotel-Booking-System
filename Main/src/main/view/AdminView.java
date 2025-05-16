/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.view;

/**
 *
 * @author Mohamed Montasser
 */
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.models.Room;
import main.models.SingleRoom;
import main.models.DoubleRoom;
import main.models.Suite;
import main.models.Offer;
import main.exception.InvalidBookingException;
import main.Main;

public class AdminView {

    private final Main mainApp;
    private final BorderPane view;
    private final TableView<Room> roomsTable;
    private final TableView<Offer> offersTable;
    private final TextField newRoomNumberField;
    private final ComboBox<String> newRoomTypeComboBox;
    private final TextField newRoomPriceField;
    private final TextField newOfferCodeField;
    private final TextField newOfferDiscountField;

    public AdminView(Main mainApp) {
        this.mainApp = mainApp;

        // Create UI components
        Label titleLabel = new Label("Hotel Booking System - Admin");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> mainApp.showLoginView());

        // Rooms tab
        roomsTable = new TableView<>();
        TableColumn<Room, Integer> roomNumberColumn = new TableColumn<>("Room Number");
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> roomTypeColumn = new TableColumn<>("Type");
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Room, Double> roomPriceColumn = new TableColumn<>("Price/Night");
        roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        roomPriceColumn.setCellFactory(tc -> new TableCell<Room, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        roomsTable.getColumns().addAll(roomNumberColumn, roomTypeColumn, roomPriceColumn);

        newRoomNumberField = new TextField();
        newRoomNumberField.setPromptText("Number");
        newRoomNumberField.setPrefWidth(60);

        newRoomTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Single", "Double", "Suite"
        ));
        newRoomTypeComboBox.getSelectionModel().selectFirst();

        newRoomPriceField = new TextField();
        newRoomPriceField.setPromptText("Price");
        newRoomPriceField.setPrefWidth(60);

        Button addRoomButton = new Button("Add Room");
        addRoomButton.setOnAction(e -> handleAddRoom());

        HBox addRoomBox = new HBox(10);
        addRoomBox.setAlignment(Pos.CENTER_LEFT);
        addRoomBox.getChildren().addAll(
                new Label("Add New Room:"),
                new Label("Number:"), newRoomNumberField,
                new Label("Type:"), newRoomTypeComboBox,
                new Label("Price:"), newRoomPriceField,
                addRoomButton
        );

        VBox roomsTab = new VBox(10);
        roomsTab.setPadding(new Insets(10));
        roomsTab.getChildren().addAll(roomsTable, addRoomBox);

        // Offers tab
        offersTable = new TableView<>();
        TableColumn<Offer, String> offerCodeColumn = new TableColumn<>("Code");
        offerCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Offer, Double> offerDiscountColumn = new TableColumn<>("Discount");
        offerDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
        offerDiscountColumn.setCellFactory(tc -> new TableCell<Offer, Double>() {
            @Override
            protected void updateItem(Double discount, boolean empty) {
                super.updateItem(discount, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", discount));
                }
            }
        });

        offersTable.getColumns().addAll(offerCodeColumn, offerDiscountColumn);

        newOfferCodeField = new TextField();
        newOfferCodeField.setPromptText("Code");

        newOfferDiscountField = new TextField();
        newOfferDiscountField.setPromptText("Discount %");
        newOfferDiscountField.setPrefWidth(60);

        Button addOfferButton = new Button("Add Offer");
        addOfferButton.setOnAction(e -> handleAddOffer());

        HBox addOfferBox = new HBox(10);
        addOfferBox.setAlignment(Pos.CENTER_LEFT);
        addOfferBox.getChildren().addAll(
                new Label("Add New Offer:"),
                new Label("Code:"), newOfferCodeField,
                new Label("Discount (%):"), newOfferDiscountField,
                addOfferButton
        );

        VBox offersTab = new VBox(10);
        offersTab.setPadding(new Insets(10));
        offersTab.getChildren().addAll(offersTable, addOfferBox);

        // Tab pane
        TabPane tabPane = new TabPane();
        Tab roomsTabPane = new Tab("Rooms", roomsTab);
        Tab offersTabPane = new Tab("Offers", offersTab);
        tabPane.getTabs().addAll(roomsTabPane, offersTabPane);

        // Layout
        HBox topBox = new HBox(10, titleLabel, logoutButton);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));

        view = new BorderPane();
        view.setTop(topBox);
        view.setCenter(tabPane);

        // Initialize data
        updateRoomsTable();
        updateOffersTable();
    }

    private void updateRoomsTable() {
        roomsTable.getItems().setAll(mainApp.getHotelSystem().getAvailableRooms());
    }

    private void updateOffersTable() {
        offersTable.getItems().setAll(mainApp.getHotelSystem().getOffers());
    }

    private void handleAddRoom() {
        try {
            int roomNumber = Integer.parseInt(newRoomNumberField.getText());
            String roomType = newRoomTypeComboBox.getSelectionModel().getSelectedItem();
            double price = Double.parseDouble(newRoomPriceField.getText());

            Room newRoom;
            switch (roomType) {
                case "Single":
                    newRoom = new SingleRoom(roomNumber, price);
                    break;
                case "Double":
                    newRoom = new DoubleRoom(roomNumber, price);
                    break;
                case "Suite":
                    newRoom = new Suite(roomNumber, price);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid room type");
            }

            try {
                mainApp.getHotelSystem().addRoom(newRoom);
                updateRoomsTable();
                newRoomNumberField.clear();
                newRoomPriceField.clear();
            } catch (InvalidBookingException e) {
                showAlert("Duplicate Room", e.getMessage());
            }

            // Clear fields
            newRoomNumberField.clear();
            newRoomPriceField.clear();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for room number and price");
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void handleAddOffer() {
        try {
            String code = newOfferCodeField.getText();
            double discount = Double.parseDouble(newOfferDiscountField.getText());

            if (code.isEmpty()) {
                throw new IllegalArgumentException("Offer code cannot be empty");
            }

            if (discount <= 0 || discount >= 100) {
                throw new IllegalArgumentException("Discount must be between 0 and 100");
            }

            mainApp.getHotelSystem().addOffer(new Offer(code, discount));
            updateOffersTable();

            // Clear fields
            newOfferCodeField.clear();
            newOfferDiscountField.clear();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for discount");
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getView() {
        return view;
    }
}
