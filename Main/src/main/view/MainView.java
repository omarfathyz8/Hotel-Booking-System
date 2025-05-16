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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import main.Main;
import main.HotelSystem;
import main.exception.InvalidBookingException;
import main.models.Room;
import main.models.Offer;
import main.models.Customer;
import main.models.Booking;

public class MainView {

    private final Main mainApp;
    private final HotelSystem hotelSystem;
    private final BorderPane view;
    private final TableView<Room> roomsTable;
    private final TableColumn<Room, Integer> roomNumberColumn;
    private final TableColumn<Room, String> roomTypeColumn;
    private final TableColumn<Room, Double> roomPriceColumn;
    private final ComboBox<String> sortComboBox;
    private final ComboBox<String> filterTypeComboBox;
    private final ComboBox<String> filterValueComboBox;
    private final Slider minPriceSlider;
    private final Slider maxPriceSlider;
    private final Label minPriceLabel;
    private final Label maxPriceLabel;
    private final DatePicker checkInDatePicker;
    private final DatePicker checkOutDatePicker;
    private final TextField offerCodeField;
    private final Label totalPriceLabel;
    private final Label bookingMessageLabel;

    public MainView(Main mainApp, HotelSystem hotelSystem) {
        this.mainApp = mainApp;
        this.hotelSystem = hotelSystem;

        // Initialize all UI components first
        this.view = new BorderPane();
        this.roomsTable = new TableView<>();
        this.roomNumberColumn = new TableColumn<>("Room Number");
        this.roomTypeColumn = new TableColumn<>("Type");
        this.roomPriceColumn = new TableColumn<>("Price/Night");
        this.sortComboBox = new ComboBox<>();
        this.filterTypeComboBox = new ComboBox<>();
        this.filterValueComboBox = new ComboBox<>();
        this.minPriceSlider = new Slider();
        this.maxPriceSlider = new Slider();
        this.minPriceLabel = new Label();
        this.maxPriceLabel = new Label();
        this.checkInDatePicker = new DatePicker();
        this.checkOutDatePicker = new DatePicker();
        this.offerCodeField = new TextField();
        this.totalPriceLabel = new Label();
        this.bookingMessageLabel = new Label();

        initializeUI();
    }

    private void initializeUI() {
        // Create UI components
        Label titleLabel = new Label("Hotel Booking System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> mainApp.showLoginView());

        // In initializeUI():
        Button applyFiltersButton = new Button("Apply Filters");
        applyFiltersButton.setOnAction(e -> {
            updateRoomsTable();
            bookingMessageLabel.setText("Filters applied");
        });

        // Configure table columns
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
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
        centerColumnText(roomNumberColumn);
        centerColumnText(roomTypeColumn);
        centerColumnText(roomPriceColumn);

        roomsTable.getColumns().addAll(roomNumberColumn, roomTypeColumn, roomPriceColumn);

        // Initialize combo boxes
        sortComboBox.setItems(FXCollections.observableArrayList(
                "Price (Low to High)", "Price (High to Low)"
        ));
        sortComboBox.getSelectionModel().selectFirst();

        filterTypeComboBox.setItems(FXCollections.observableArrayList(
                "All", "Type", "Price Range"
        ));
        filterTypeComboBox.setOnAction(e -> {
            String selected = filterTypeComboBox.getValue();
            if ("Type".equalsIgnoreCase(selected)) {
                filterValueComboBox.setDisable(false);
                filterValueComboBox.setItems(FXCollections.observableArrayList("Single", "Double", "Suite"));
                filterValueComboBox.getSelectionModel().selectFirst();
            } else {
                filterValueComboBox.setDisable(true);
                filterValueComboBox.getItems().clear();
            }
        });

        filterTypeComboBox.getSelectionModel().selectFirst();

        filterValueComboBox.setDisable(true);

        // Initialize price sliders
        List<Room> rooms = hotelSystem.getAvailableRooms();
        double minPrice = rooms.stream().mapToDouble(Room::getPricePerNight).min().orElse(0);
        double maxPrice = rooms.stream().mapToDouble(Room::getPricePerNight).max().orElse(500);

        minPriceSlider.setMin(minPrice);
        minPriceSlider.setMax(maxPrice);
        minPriceSlider.setValue(minPrice);

        maxPriceSlider.setMin(minPrice);
        maxPriceSlider.setMax(maxPrice);
        maxPriceSlider.setValue(maxPrice);

        minPriceLabel.setText(String.format("$%.2f", minPrice));
        maxPriceLabel.setText(String.format("$%.2f", maxPrice));

        // Initialize date pickers
        checkInDatePicker.setValue(LocalDate.now());
        checkOutDatePicker.setValue(LocalDate.now().plusDays(1));

        // Initialize other controls
        offerCodeField.setPromptText("Offer Code");
        totalPriceLabel.setStyle("-fx-font-weight: bold;");
        bookingMessageLabel.setStyle("-fx-text-fill: red;");

        // Layout
        HBox topBox = new HBox(10, titleLabel, logoutButton);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.getChildren().addAll(
                new Label("Sort by:"), sortComboBox,
                new Label("Filter by:"), filterTypeComboBox, filterValueComboBox,
                applyFiltersButton
        );

        HBox priceRangeBox = new HBox(10);
        priceRangeBox.setAlignment(Pos.CENTER_LEFT);
        priceRangeBox.getChildren().addAll(
                new Label("Price Range:"), minPriceLabel,
                minPriceSlider, maxPriceSlider, maxPriceLabel
        );
        priceRangeBox.setVisible(false);

        HBox bookingBox = new HBox(10);
        bookingBox.setAlignment(Pos.CENTER_LEFT);
        Button calculateButton = new Button("Calculate Price");
        calculateButton.setOnAction(e -> handleCalculatePrice());

        bookingBox.getChildren().addAll(
                new Label("Check-in:"), checkInDatePicker,
                new Label("Check-out:"), checkOutDatePicker,
                new Label("Offer Code:"), offerCodeField,
                calculateButton,
                totalPriceLabel
        );

        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));
        Button bookRoomButton = new Button("Book Selected Room");
        bookRoomButton.setOnAction(e -> handleBookRoom());

        centerBox.getChildren().addAll(
                filterBox, priceRangeBox, roomsTable, bookingBox,
                bookRoomButton,
                bookingMessageLabel
        );

        view.setTop(topBox);
        view.setCenter(centerBox);

        // Initialize data
        updateRoomsTable();
    }

    private void updateRoomsTable() {
        try {
            List<Room> rooms = hotelSystem.getAvailableRooms();

            // Apply sorting
            String sortOption = sortComboBox.getValue();
            if ("Price (Low to High)".equals(sortOption)) {
                rooms = hotelSystem.getSortedAvailableRooms(true);
            } else if ("Price (High to Low)".equals(sortOption)) {
                rooms = hotelSystem.getSortedAvailableRooms(false);
            }

            // Apply filtering
            String filterType = filterTypeComboBox.getValue();
            if (filterType != null && !"All".equals(filterType)) {
                Object filterValue = getFilterValue(filterType);
                if (filterValue != null) {
                    rooms = hotelSystem.filter(rooms, filterType, filterValue);
                }
            }

            roomsTable.getItems().setAll(rooms);
        } catch (Exception e) {
            bookingMessageLabel.setText("Error loading rooms: " + e.getMessage());
        }
    }

    private Object getFilterValue(String filterType) {
        if (filterType.equals("Type")) {
            return filterValueComboBox.getSelectionModel().getSelectedItem();
        } else if (filterType.equals("Price Range")) {
            return new double[]{
                minPriceSlider.getValue(),
                maxPriceSlider.getValue()
            };
        }
        return null;
    }

    private void handleCalculatePrice() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        LocalDate checkIn = checkInDatePicker.getValue();
        LocalDate checkOut = checkOutDatePicker.getValue();
        String offerCode = offerCodeField.getText();

        if (selectedRoom == null) {
            bookingMessageLabel.setText("Please select a room");
            return;
        }

        if (checkIn == null || checkOut == null) {
            bookingMessageLabel.setText("Please select check-in and check-out dates");
            return;
        }

        try {
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            double price = nights * selectedRoom.getPricePerNight();

            if (!offerCode.isEmpty()) {
                Offer offer = mainApp.getHotelSystem().findOffer(offerCode);
                if (offer != null) {
                    price *= (1 - offer.getDiscountPercentage() / 100);
                } else {
                    bookingMessageLabel.setText("Invalid offer code");
                    return;
                }
            }

            totalPriceLabel.setText(String.format("$%.2f", price));
            bookingMessageLabel.setText("");
        } catch (Exception e) {
            bookingMessageLabel.setText("Invalid date selection");
        }
    }

    private void handleBookRoom() {
        try {
            Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
            if (selectedRoom == null) {
                bookingMessageLabel.setText("Please select a room first");
                return;
            }

            LocalDate checkIn = checkInDatePicker.getValue();
            LocalDate checkOut = checkOutDatePicker.getValue();

            if (checkIn == null || checkOut == null) {
                bookingMessageLabel.setText("Please select both dates");
                return;
            }

            if (checkIn.isAfter(checkOut)) {
                bookingMessageLabel.setText("Check-in must be before check-out");
                return;
            }

            String offerCode = offerCodeField.getText().trim();
            Offer offer = offerCode.isEmpty() ? null : hotelSystem.findOffer(offerCode);

            Customer customer = (Customer) hotelSystem.getCurrentUser();
            Booking booking = hotelSystem.createBooking(customer, selectedRoom,
                    checkIn, checkOut, offerCode);

            bookingMessageLabel.setText("Booking successful! Total: $" + booking.getTotalPrice());
            updateRoomsTable(); // Refresh the room list
        } catch (InvalidBookingException e) {
            bookingMessageLabel.setText("Booking failed: " + e.getMessage());
        } catch (Exception e) {
            bookingMessageLabel.setText("System error: " + e.getMessage());
        }
    }

    public BorderPane getView() {
        return view;
    }

    private <T> void centerColumnText(TableColumn<Room, T> column) {
        column.setCellFactory(col -> new TableCell<Room, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
                setAlignment(Pos.CENTER);
                setStyle("-fx-alignment: CENTER;");
            }
        });
    }

}

