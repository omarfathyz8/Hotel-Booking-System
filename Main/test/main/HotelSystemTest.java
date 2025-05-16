package main;

import main.exception.InvalidBookingException;
import main.models.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class HotelSystemTest {

    private HotelSystem hotelSystem;

    @Before
    public void setUp() {
        hotelSystem = new HotelSystem();
    }

    @Test
    public void testLogin() {
        assertTrue(hotelSystem.login("admin@hotel.com", "admin123"));
        assertFalse(hotelSystem.login("wrong@hotel.com", "admin123"));
    }

    @Test
    public void testRegisterCustomer() {
        hotelSystem.registerCustomer("John Doe", "john@example.com", "password123");
        assertTrue(hotelSystem.login("john@example.com", "password123"));
    }

    @Test
    public void testGetAvailableRooms() {
        List<Room> availableRooms = hotelSystem.getAvailableRooms();
        assertNotNull(availableRooms);
        assertFalse(availableRooms.isEmpty());
        for (Room room : availableRooms) {
            assertTrue(room.isAvailable());
        }
    }

    @Test
    public void testGetSortedAvailableRooms() {
        List<Room> ascending = hotelSystem.getSortedAvailableRooms(true);
        List<Room> descending = hotelSystem.getSortedAvailableRooms(false);

        assertFalse(ascending.isEmpty());
        assertFalse(descending.isEmpty());

        assertTrue(ascending.get(0).getPricePerNight() <= ascending.get(ascending.size() - 1).getPricePerNight());
        assertTrue(descending.get(0).getPricePerNight() >= descending.get(descending.size() - 1).getPricePerNight());
    }

    @Test
    public void testFilterByType() {
        List<Room> rooms = hotelSystem.getAvailableRooms();
        List<Room> filtered = hotelSystem.filter(rooms, "type", "Single");

        assertNotNull(filtered);
        for (Room room : filtered) {
            assertEquals("Single", room.getType());
        }
    }

    @Test
    public void testFilterByPriceRange() {
        List<Room> rooms = hotelSystem.getAvailableRooms();
        List<Room> filtered = hotelSystem.filter(rooms, "price range", new double[]{100, 150});

        assertNotNull(filtered);
        for (Room room : filtered) {
            double price = room.getPricePerNight();
            assertTrue(price >= 100 && price <= 150);
        }
    }

    @Test
    public void testAddRoom() throws InvalidBookingException {
        Room newRoom = new SingleRoom(999, 200);
        hotelSystem.addRoom(newRoom);

        List<Room> rooms = hotelSystem.getAvailableRooms();
        boolean found = false;
        for (Room r : rooms) {
            if (r.getRoomNumber() == 999) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test(expected = InvalidBookingException.class)
    public void testAddRoomDuplicateNumberThrowsException() throws InvalidBookingException {
        Room duplicateRoom = new SingleRoom(101, 300); // Room 101 already exists
        hotelSystem.addRoom(duplicateRoom);
    }

    @Test
    public void testGetCurrentUser() {
        assertNull(hotelSystem.getCurrentUser());
        hotelSystem.login("admin@hotel.com", "admin123");
        assertNotNull(hotelSystem.getCurrentUser());
        assertEquals("admin@hotel.com", hotelSystem.getCurrentUser().getEmail());
    }

    @Test
    public void testGetOffers() {
        List<Offer> offers = hotelSystem.getOffers();
        assertNotNull(offers);
        assertTrue(offers.size() >= 2); // SUMMER10, WINTER20
    }
}
