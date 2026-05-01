package com.moviebooking.onlinetickte.services.impl;

import com.moviebooking.onlinetickte.models.Booking;
import com.moviebooking.onlinetickte.services.BookingService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final String BOOKING_FILE = "src/data/bookings.txt";
    private static final int MAX_SEATS_PER_SHOW = 50;

    @Override
    public Booking createBooking(Booking booking) {
        if (!isSeatsAvailable(booking.getMovieId(), 
                            booking.getBookingDate(), 
                            booking.getTime(), 
                            booking.getNumberOfSeats())) {
            throw new RuntimeException("Not enough seats available");
        }

        List<Booking> bookings = getAllBookings();
        long nextId = bookings.isEmpty() ? 1 : bookings.get(bookings.size() - 1).getId() + 1;
        
        booking.setId(nextId);
        booking.setStatus("PENDING");
        bookings.add(booking);

        if (saveAllBookingsToFile(bookings)) {
            return booking;
        } else {
            throw new RuntimeException("Failed to save booking to file");
        }
    }

    @Override
    public boolean saveBooking(Booking booking) {
        List<Booking> bookings = getAllBookings();
        if (booking.getId() == null) {
            long nextId = bookings.isEmpty() ? 1 : bookings.get(bookings.size() - 1).getId() + 1;
            booking.setId(nextId);
            bookings.add(booking);
        } else {
            for (int i = 0; i < bookings.size(); i++) {
                if (bookings.get(i).getId().equals(booking.getId())) {
                    bookings.set(i, booking);
                    break;
                }
            }
        }
        return saveAllBookingsToFile(bookings);
    }

    @Override
    public List<Booking> getUserBookings(String username) {
        return getAllBookings().stream()
                .filter(b -> b.getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    @Override
    public Booking getBookingById(Long id) {
        return getAllBookings().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking != null) {
            booking.setStatus("CANCELLED");
            return saveBooking(booking);
        }
        return false;
    }

    @Override
    public boolean isSeatsAvailable(Long movieId, LocalDate date, String showTime, int requestedSeats) {
        int remainingSeats = getRemainingSeats(movieId, date, showTime);
        return remainingSeats >= requestedSeats;
    }

    @Override
    public int getRemainingSeats(Long movieId, LocalDate date, String showTime) {
        List<Booking> existingBookings = getAllBookings().stream()
            .filter(b -> b.getMovieId().equals(movieId) && 
                        b.getBookingDate().equals(date) && 
                        b.getTime().equals(showTime) && 
                        !"CANCELLED".equals(b.getStatus()))
            .collect(Collectors.toList());

        int bookedSeats = existingBookings.stream()
                .mapToInt(Booking::getNumberOfSeats)
                .sum();

        return MAX_SEATS_PER_SHOW - bookedSeats;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKING_FILE);
        if (!file.exists()) return bookings;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    Booking booking = new Booking();
                    booking.setId(Long.parseLong(parts[0]));
                    booking.setUsername(parts[1]);
                    booking.setMovieId(Long.parseLong(parts[2]));
                    booking.setMovieTitle(parts[3]);
                    booking.setBookingDate(LocalDate.parse(parts[4]));
                    booking.setTime(parts[5]);
                    booking.setNumberOfSeats(Integer.parseInt(parts[6]));
                    booking.setTotalPrice(Double.parseDouble(parts[7]));
                    booking.setStatus(parts[8]);
                    bookings.add(booking);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    private boolean saveAllBookingsToFile(List<Booking> bookings) {
        try {
            File file = new File(BOOKING_FILE);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Booking b : bookings) {
                    writer.write(String.format("%d,%s,%d,%s,%s,%s,%d,%.2f,%s%n",
                        b.getId(),
                        b.getUsername(),
                        b.getMovieId(),
                        b.getMovieTitle(),
                        b.getBookingDate().toString(),
                        b.getTime(),
                        b.getNumberOfSeats(),
                        b.getTotalPrice(),
                        b.getStatus()
                    ));
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
 