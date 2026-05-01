package com.moviebooking.onlinetickte.controllers;

import com.moviebooking.onlinetickte.models.Booking;
import com.moviebooking.onlinetickte.models.Movie;
import com.moviebooking.onlinetickte.services.BookingService;
import com.moviebooking.onlinetickte.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private BookingService bookingService;

    private static final List<String> SHOW_TIMES = Arrays.asList(
            "10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM", "10:00 PM"
    );

    @GetMapping("/{title}")
    public String movieDetails(@PathVariable String title, Model model, HttpSession session) {
        Movie movie = movieService.getMovieByTitle(title);
        if (movie == null) {
            return "redirect:/?error=Movie not found";
        }

        // Generate next 7 days
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            dates.add(today.plusDays(i));
        }

        model.addAttribute("movie", movie);
        model.addAttribute("dates", dates);
        model.addAttribute("showTimes", SHOW_TIMES);

        // Check if the user is logged in
        Boolean isLoggedIn = session.getAttribute("isLoggedIn") != null ?
                (Boolean) session.getAttribute("isLoggedIn") : false;
        String username = (String) session.getAttribute("username");

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("username", username);

        // If the user is logged in, show existing bookings
        if (username != null) {
            List<Booking> userBookings = bookingService.getUserBookings(username);
            model.addAttribute("userBookings", userBookings);
        }

        return "movie-details";
    }

    @PostMapping("/{title}/book")
    @ResponseBody
    public String bookMovie(@PathVariable String title,
                          @RequestParam String bookingDate,
                          @RequestParam String time,
                          @RequestParam int numberOfSeats,
                          HttpSession session) {
        try {
            // Check if user is logged in
            if (session.getAttribute("isLoggedIn") == null || 
                !(Boolean) session.getAttribute("isLoggedIn")) {
                return "redirect:/users/login";
            }

            String username = (String) session.getAttribute("username");
            Movie movie = movieService.getMovieByTitle(title);
            
            if (movie == null) {
                return "error: Movie not found";
            }

            // Calculate total price
            double totalPrice = numberOfSeats * movie.getPrice();

            // Create and save booking
            Booking booking = new Booking(
                username,
                movie.getId(),
                title,
                LocalDate.parse(bookingDate),
                time,
                numberOfSeats,
                totalPrice
            );

            if (bookingService.saveBooking(booking)) {
                return "success";
            } else {
                return "error";
            }
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
