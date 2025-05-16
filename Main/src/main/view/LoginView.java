/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.view;

/**
 *
 * @author Mohamed Montasser
 */
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import main.models.User;

public class LoginView {
    private final Main mainApp;
    private final VBox view;
    private final TextField emailField;
    private final PasswordField passwordField;
    private final Label messageLabel;

    public LoginView(Main mainApp) {
        this.mainApp = mainApp;
        
        // Create UI components
        Label titleLabel = new Label("Hotel Booking System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        emailField = new TextField();
        emailField.setPromptText("Email");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());
        
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegister());
        
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        
        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, registerButton);
        
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(20));
        view.getChildren().addAll(
            titleLabel, 
            loginLabel, 
            grid, 
            buttonBox, 
            messageLabel
        );
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password");
            return;
        }
        
        if (mainApp.getHotelSystem().login(email, password)) {
            User user = mainApp.getHotelSystem().getCurrentUser();
            if (user.getRole().equals("Admin")) {
                mainApp.showAdminView();
            } else {
                mainApp.showMainView();
            }
        } else {
            messageLabel.setText("Invalid email or password");
        }
    }

    private void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password");
            return;
        }
        
        mainApp.getHotelSystem().registerCustomer("New User", email, password);
        messageLabel.setText("Registration successful! Please login.");
    }

    public VBox getView() {
        return view;
    }
}