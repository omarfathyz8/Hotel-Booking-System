/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.interfaces;

import java.util.List;

/**
 *
 * @author Mohamed Montasser
 */
public interface Filterable<T> {
    List<T> filter(List<T> items, String filterType, Object filterValue);
}