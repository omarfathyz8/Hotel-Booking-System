# 🏨 Hotel Booking System (JavaFX)

A hotel booking management system built using Java, JavaFX, and object-oriented design principles. The app supports Admin and Customer roles, booking logic, discounts, sorting, and filtering features.

---

## ✨ Features

### ✅ Core Functionality

- **Room Management**
  - Supports Single, Double, Suite rooms
  - Each room has a number, price per night, and availability
  - Admins can add new rooms with validation (no duplicates)

- **User Management**
  - Customers can register and log in
  - Admin login with a separate view and privileges

- **Booking System**
  - Customers can book rooms for a date range
  - Supports discount codes (offers)
  - Prevents:
    - Duplicate bookings
    - Check-in on past dates
    - Same-day check-in and check-out

- **Offers**
  - Admins can define offers like `SUMMER10` and `WINTER20`
  - Offers apply automatically during booking if valid

- **Filtering & Sorting**
  - Sort by price (low-to-high / high-to-low)
  - Filter by type or price range
  - Responsive GUI updates

- **GUI with JavaFX**
  - Responsive TableView
  - Separate Admin and Customer views
  - Center-aligned columns
  - Real-time price calculation and error handling

---

## 💡 OOP Concepts Used

- **Inheritance & Polymorphism**
  - Room subclasses: `SingleRoom`, `DoubleRoom`, `Suite`
  - User subclasses: `Admin`, `Customer`

- **Interfaces**
  - `Filterable<T>` for generic filtering

- **Custom Exceptions**
  - `InvalidBookingException` for invalid booking operations

---

## 🛠 Technologies

- Java 17+
- JavaFX
- MVC structure
- NetBeans or IntelliJ recommended

---

## 🧪 Unit Testing with JUnit 4

The project includes a complete suite of unit tests for `HotelSystem` functionality using **JUnit 4**.

### 🔍 Tested Scenarios

- ✅ Valid and invalid logins  
- ✅ Customer registration  
- ✅ Available room retrieval  
- ✅ Sorting rooms by price  
- ✅ Filtering by room type and price  
- ✅ Adding new rooms and avoiding duplicates  
- ✅ Offer listing  
- ✅ Current user tracking  

### 🔬 Sample Test Case

```java
@Test(expected = InvalidBookingException.class)
public void testAddRoomDuplicateNumberThrowsException() throws InvalidBookingException {
    Room duplicateRoom = new SingleRoom(101, 300); // Room 101 already exists
    hotelSystem.addRoom(duplicateRoom);
}
```

---

## 🧰 Tools Used

- **JUnit 4**
- Annotations:
  - `@Test`
  - `@Before`
- Assertions:
  - `assertTrue`
  - `assertEquals`
  - `assertNotNull`
  - `assertFalse`
  - `@Test(expected = ...)`

📁 **Test File Path:**  
`Main/test/main/HotelSystemTest.java`
