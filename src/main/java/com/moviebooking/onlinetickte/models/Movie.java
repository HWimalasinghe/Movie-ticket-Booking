package com.moviebooking.onlinetickte.models;

public class Movie {
    private Long id;
    private String title;
    private String genre;
    private int duration;
    private String imageUrl;
    private double price;
    private String description;
    private boolean isActive;

    public Movie() {}

    // Constructor that matches how it's being called in MovieService
    public Movie(Long id, String title, String genre, int duration, String imageUrl, double price) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isActive = true;
    }

    // Add another constructor with description
    public Movie(String title, String genre, int duration, String imageUrl, double price) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isActive = true;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // Additional Methods (If Needed)

    // Method to display movie information (for movie details page)
    public String getMovieDetails() {
        return "Title: " + title + "\n" +
                "Genre: " + genre + "\n" +
                "Duration: " + duration + " minutes\n" +
                "Price: $" + price;
    }

    // Optionally, you can have a method to generate a brief description of the movie.
    public String getShortDescription() {
        return title + " (" + genre + ") - " + duration + " minutes.";
    }
}
