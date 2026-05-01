package com.moviebooking.onlinetickte.controllers;

import com.moviebooking.onlinetickte.models.Booking;
import com.moviebooking.onlinetickte.models.Movie;
import com.moviebooking.onlinetickte.services.MovieService;
import com.moviebooking.onlinetickte.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private BookingService bookingService;

    private static final int MAX_SEATS = 8;
    private static final String[] SHOW_TIMES = {
        "9:30 - 11:30", "1:30 - 3:30", "4:30 - 6:30", "7:30 - 9:30"
    };

    @GetMapping("/{movieTitle}")
    public String showBookingPage(@PathVariable String movieTitle, 
                                Model model, 
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        if (session.getAttribute("isLoggedIn") == null || 
            !(Boolean) session.getAttribute("isLoggedIn")) {
            redirectAttributes.addFlashAttribute("error", "Please login to book tickets");
            return "redirect:/users/login";
        }

        Movie movie = movieService.getMovieByTitle(movieTitle);
        if (movie == null) {
            redirectAttributes.addFlashAttribute("error", "Movie not found");
            return "redirect:/";
        }

        if (!movie.isActive()) {
            redirectAttributes.addFlashAttribute("error", "This movie is not available for booking");
            return "redirect:/";
        }

        model.addAttribute("movie", movie);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("showTimes", SHOW_TIMES);
        model.addAttribute("maxSeats", MAX_SEATS);

        return "booking";
    }

    @PostMapping("/confirm")
    @ResponseBody
    public String confirmBooking(@RequestParam Long movieId,
                               @RequestParam String bookingDate,
                               @RequestParam String selectedTime,
                               @RequestParam int numberOfSeats,
                               HttpSession session) {
        try {
            // Validate user session
            if (session.getAttribute("isLoggedIn") == null || 
                !(Boolean) session.getAttribute("isLoggedIn")) {
                return "redirect:/users/login";
            }

            String username = (String) session.getAttribute("username");
            Movie movie = movieService.getMovieById(movieId);
            
            if (movie == null || !movie.isActive()) {
                return "error: Movie not available";
            }

            // Calculate total price
            double totalPrice = numberOfSeats * movie.getPrice();

            // Create booking
            Booking booking = new Booking(
                username,
                movieId,
                movie.getTitle(),
                LocalDate.parse(bookingDate),
                selectedTime,
                numberOfSeats,
                totalPrice
            );

            // Use createBooking instead of saveBooking
            Booking createdBooking = bookingService.createBooking(booking);
            if (createdBooking != null) {
                return "success";
            } else {
                return "error: Booking failed";
            }

        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    private boolean isValidShowTime(String showTime) {
        for (String time : SHOW_TIMES) {
            if (time.equals(showTime)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/check-availability")
    @ResponseBody
    public int checkAvailability(@RequestParam Long movieId,
                               @RequestParam String date,
                               @RequestParam String showTime) {
        try {
            LocalDate bookingDate = LocalDate.parse(date);
            return bookingService.getRemainingSeats(movieId, bookingDate, showTime);
        } catch (Exception e) {
            return 0;
        }
    }
}
