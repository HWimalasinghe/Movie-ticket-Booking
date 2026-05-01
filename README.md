# 🎬 Online Movie Ticket Booking System

> **Academic Project** — 1st Year, 2nd Semester | Object-Oriented Programming (OOP) Module  
> Sri Lanka Institute of Information Technology (SLIIT)

---

## 📖 Project Overview

**OnlineTickte** is a full-stack web application for booking movie tickets online. Built using **Java** and **Spring Boot**, it demonstrates core Object-Oriented Programming principles including encapsulation, inheritance, abstraction, and polymorphism. Users can browse movies, register/login, book seats, and manage their bookings — while admins can manage the movie catalog and bookings.

---

## 🚀 Features

### 👤 User Features
- User registration and login (session-based authentication)
- Browse the list of available movies
- View detailed movie information (synopsis, show timings, etc.)
- Book tickets for a selected movie
- View and manage personal bookings

### 🔑 Admin Features
- Add, edit, and delete movies
- View all bookings across all users
- Manage the movie catalog

---

## 🛠️ Tech Stack

| Layer        | Technology                          |
|--------------|--------------------------------------|
| Backend      | Java 11, Spring Boot 2.7.0          |
| Web Layer    | Spring MVC, Thymeleaf (HTML Templates) |
| Data Storage | Flat File Storage (`.txt` files)    |
| Build Tool   | Apache Maven                        |
| Dev Tools    | Spring Boot DevTools (hot reload)   |

---

## 📁 Project Structure

```
OnlineTickte/
├── src/
│   ├── data/                        # Flat file data storage
│   │   ├── movies.txt               # Movie records
│   │   └── bookings.txt             # Booking records
│   └── main/
│       ├── java/com/moviebooking/onlinetickte/
│       │   ├── OnlineTickteApplication.java   # App entry point
│       │   ├── controllers/                   # MVC Controllers
│       │   │   ├── AdminController.java
│       │   │   ├── BookingController.java
│       │   │   ├── HomeController.java
│       │   │   ├── LoginController.java
│       │   │   ├── MovieController.java
│       │   │   └── UserController.java
│       │   ├── models/                        # Domain Models (OOP)
│       │   │   ├── User.java
│       │   │   ├── Movie.java
│       │   │   └── Booking.java
│       │   ├── repositories/                  # Data access layer
│       │   ├── services/                      # Business logic layer
│       │   └── utils/                         # Utility classes
│       └── resources/
│           └── templates/                     # Thymeleaf HTML templates
├── pom.xml                                    # Maven dependencies
└── README.md
```

---

## ⚙️ Getting Started

### Prerequisites

- **Java 11** or higher — [Download JDK](https://adoptium.net/)
- **Maven 3.6+** — [Download Maven](https://maven.apache.org/download.cgi)

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone https://github.com/<your-username>/OnlineTickte.git
   cd OnlineTickte
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Open in your browser:**
   ```
   http://localhost:8080
   ```

---

## 🎓 OOP Concepts Demonstrated

| Concept         | Where Applied                                                        |
|-----------------|----------------------------------------------------------------------|
| **Encapsulation** | `User`, `Movie`, `Booking` model classes with private fields + getters/setters |
| **Abstraction**   | Service layer separates business logic from controllers              |
| **Inheritance**   | Spring MVC controller hierarchy                                      |
| **Polymorphism**  | Overriding `toString()` in model classes                             |
| **Separation of Concerns** | MVC architecture (Model → Service → Controller → View)    |

---

## 📸 Screenshots

> *(Add screenshots of your application here)*

---

## 👨‍💻 Author

| Name                 | Student ID | Institute                    |
|----------------------|------------|------------------------------|
| Harindu WImalasinghe | IT24100600 | SLIIT — Faculty of Computing |

---

## 📄 License

This project is submitted as an academic assignment for the **OOP Module** at SLIIT.  
© 2024 — All rights reserved.
