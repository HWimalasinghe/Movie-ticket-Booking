package com.moviebooking.onlinetickte.models;

import java.time.LocalDate;

public class Booking {
    private Long id;
    private String username;
    private Long movieId;
    private String movieTitle;
    private LocalDate bookingDate;
    private String time;
    private int numberOfSeats;
    private double totalPrice;
    private String status;

    // Default constructor
    public Booking() {
        this.status = "PENDING";
    }

    // Constructor that matches how it's being used in MovieController
    public Booking(String username, Long movieId, String movieTitle, 
                  LocalDate bookingDate, String time, 
                  int numberOfSeats, double totalPrice) {
        this.username = username;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.bookingDate = bookingDate;
        this.time = time;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = totalPrice;
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", movieId=" + movieId +
                ", movieTitle='" + movieTitle + '\'' +
                ", bookingDate=" + bookingDate +
                ", time='" + time + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                '}';
    }
}
