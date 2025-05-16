/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.models;

/**
 *
 * @author Mohamed Montasser
 */
public class Offer {
    private String code;
    private double discountPercentage;

    public Offer(String code, double discountPercentage) {
        this.code = code;
        this.discountPercentage = discountPercentage;
    }

    // Getters
    public String getCode() { return code; }
    public double getDiscountPercentage() { return discountPercentage; }
}