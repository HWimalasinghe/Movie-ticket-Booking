package com.moviebooking.onlinetickte.services;

import com.moviebooking.onlinetickte.models.Booking;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    /**
     * Save a new booking
     * @param booking the booking to save
     * @return true if successful, false otherwise
     */
    boolean saveBooking(Booking booking);

    /**
     * Get all bookings for a specific user
     * @param username the username to get bookings for
     * @return list of bookings for the user
     */
    List<Booking> getUserBookings(String username);

    /**
     * Get a specific booking by ID
     * @param id the booking ID
     * @return the booking if found, null otherwise
     */
    Booking getBookingById(Long id);

    /**
     * Cancel a booking
     * @param id the booking ID to cancel
     * @return true if successful, false otherwise
     */
    boolean cancelBooking(Long id);

    Booking createBooking(Booking booking);
    boolean isSeatsAvailable(Long movieId, LocalDate date, String showTime, int requestedSeats);
    int getRemainingSeats(Long movieId, LocalDate date, String showTime);
    List<Booking> getAllBookings();
}
