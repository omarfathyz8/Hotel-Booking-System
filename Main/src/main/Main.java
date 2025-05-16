/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

/**
 *
 * @author Mohamed Montasser
 */
import main.view.AdminView;
import main.view.MainView;
import main.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private HotelSystem hotelSystem = new HotelSystem();
    private Stage primaryStage;  // This will hold our main stage

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  // Initialize the field
        showLoginView();  // Start with login view
    }

    public void showLoginView() {
        LoginView loginView = new LoginView(this);
        Scene scene = new Scene(loginView.getView(), 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hotel Booking System - Login");
        primaryStage.show();  // Make sure to show the stage
    }

   public void showMainView() {
    System.out.println("Showing main view for: " + 
        hotelSystem.getCurrentUser().getEmail()); // Debug
    
    MainView mainView = new MainView(this, hotelSystem);
    Scene scene = new Scene(mainView.getView(), 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Hotel Booking System");
    primaryStage.show();
}

    public void showAdminView() {
        AdminView adminView = new AdminView(this);
        Scene scene = new Scene(adminView.getView(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hotel Booking System - Admin");
        primaryStage.show();
    }

    public HotelSystem getHotelSystem() {
        return hotelSystem;
    }

    public static void main(String[] args) {
        launch(args);
    }
}