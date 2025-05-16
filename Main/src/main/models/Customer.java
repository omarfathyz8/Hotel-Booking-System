/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mohamed Montasser
 */
public class Customer extends User {

    private List<Booking> bookings;

    public Customer(String name, String email, String password) {
        super(name, email, password);
        this.bookings = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public double getTotalBookingCost() {
        return bookings.stream().mapToDouble(Booking::getTotalPrice).sum();
    }

}
