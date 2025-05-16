/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.models;

/**
 *
 * @author Mohamed Montasser
 */
import main.models.Room;
import main.models.User;
import main.exception.InvalidBookingException;

public class Admin extends User {
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

}