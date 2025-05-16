/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.models;

/**
 *
 * @author Mohamed Montasser
 */
public abstract class Room implements Comparable<Room> {
    private int roomNumber;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    // Getters and setters
    public int getRoomNumber() { return roomNumber; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public abstract String getType();

    @Override
    public int compareTo(Room other) {
        return Double.compare(this.pricePerNight, other.pricePerNight);
    }

    @Override
    public String toString() {
        return String.format("Room %d - %s - $%.2f per night - %s",
                roomNumber, getType(), pricePerNight, 
                isAvailable ? "Available" : "Booked");
    }
    public String getStatusText() {
    return isAvailable ? "Available" : "Booked";
}

}