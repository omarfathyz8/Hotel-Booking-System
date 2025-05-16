/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import main.exception.InvalidBookingException;

/**
 *
 * @author Mohamed Montasser
 */
public class Booking {

    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private Offer appliedOffer;

    public Booking(Customer customer, Room room, LocalDate checkInDate,
            LocalDate checkOutDate, Offer offer) throws InvalidBookingException {
        if (!room.isAvailable()) {
            throw new InvalidBookingException("Room is not available");
        }
        if (checkInDate.isAfter(checkOutDate)) {
            throw new InvalidBookingException("Check-in date must be before check-out date");
        }
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Cannot check in to past dates");
        }
        if (!checkInDate.isBefore(checkOutDate)) {
            throw new InvalidBookingException("Check-in and check-out must be on different days");
        }

        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.appliedOffer = offer;

        calculateTotalPrice();
        room.setAvailable(false);
        customer.addBooking(this);
    }

    private void calculateTotalPrice() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double price = nights * room.getPricePerNight();

        if (appliedOffer != null) {
            price *= (1 - appliedOffer.getDiscountPercentage() / 100);
        }

        this.totalPrice = price;
    }

    // Getters
    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Offer getAppliedOffer() {
        return appliedOffer;
    }
}
