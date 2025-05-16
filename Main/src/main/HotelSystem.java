/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Mohamed Montasser
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.interfaces.Filterable;
import main.models.Room;
import main.models.User;
import main.models.Offer;
import main.models.SingleRoom;
import main.models.DoubleRoom;
import main.models.Suite;
import main.models.Admin;
import main.models.Customer;
import main.models.Booking;
import main.exception.InvalidBookingException;
import java.util.stream.Collectors;

public class HotelSystem implements Filterable<Room> {

    private List<User> users;
    private List<Room> rooms;
    private List<Offer> offers;
    private User currentUser;

    public HotelSystem() {
        this.users = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.offers = new ArrayList<>();
     
        //users.add(new Admin("Admin", "admin@hotel.com", "admin123", true));

        initializeData();
    }

    private void initializeData() {
        // Add some default rooms
        rooms.add(new SingleRoom(101, 100));
        rooms.add(new DoubleRoom(201, 150));
        rooms.add(new Suite(301, 250));
        rooms.add(new SingleRoom(102, 110));
        rooms.add(new DoubleRoom(202, 160));

        // Add default admin
        users.add(new Admin("admin", "admin@hotel.com", "admin123"));

        // Add some offers
        offers.add(new Offer("SUMMER10", 10));
        offers.add(new Offer("WINTER20", 20));
    }

    public boolean login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void registerCustomer(String name, String email, String password) {
        users.add(new Customer(name, email, password));
    }

    public List<Room> getAvailableRooms() {
        try {
            return rooms.stream()
                    .filter(Room::isAvailable)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getAvailableRooms: " + e.getMessage());
            return Collections.emptyList(); // Return empty list instead of null
        }
    }

    public List<Room> getSortedAvailableRooms(boolean ascending) {
        List<Room> availableRooms = getAvailableRooms();
        if (ascending) {
            Collections.sort(availableRooms);
        } else {
            Collections.sort(availableRooms, Collections.reverseOrder());
        }
        return availableRooms;
    }

    @Override
    public List<Room> filter(List<Room> items, String filterType, Object filterValue) {
        List<Room> filtered = new ArrayList<>();

        switch (filterType.toLowerCase()) {
            case "type":
                String type = (String) filterValue;
                filtered = items.stream()
                        .filter(r -> r.getType().equalsIgnoreCase(type))
                        .collect(Collectors.toList());
                break;

//            case "price":
//                double[] range = (double[]) filterValue;
//                filtered = items.stream()
//                        .filter(r -> r.getPricePerNight() >= range[0] && r.getPricePerNight() <= range[1])
//                        .collect(Collectors.toList());
//                break;

            case "price range":  // Make sure this matches exactly with ComboBox value
                double[] range = (double[]) filterValue;
                filtered = items.stream()
                        .filter(r -> r.getPricePerNight() >= range[0] && r.getPricePerNight() <= range[1])
                        .collect(Collectors.toList());
                break;
            default:
                filtered = items;
        }

        return filtered;
    }

    public Offer findOffer(String code) {
        return offers.stream()
                .filter(o -> o.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    public Booking createBooking(Customer customer, Room room, LocalDate checkIn,
            LocalDate checkOut, String offerCode) throws InvalidBookingException {
        Offer offer = offerCode != null ? findOffer(offerCode) : null;
        return new Booking(customer, room, checkIn, checkOut, offer);
    }

    public void addRoom(Room newRoom) throws InvalidBookingException {
    for (Room room : rooms) {
        if (room.getRoomNumber() == newRoom.getRoomNumber()) {
            throw new InvalidBookingException("Room with number " + newRoom.getRoomNumber() + " already exists.");
        }
    }
    rooms.add(newRoom);
}


    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    // Getters
    public User getCurrentUser() {
        return currentUser;
    }

    public List<Offer> getOffers() {
        return offers;
    }
}
