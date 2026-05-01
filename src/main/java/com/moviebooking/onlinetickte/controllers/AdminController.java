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
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private BookingService bookingService;

    private boolean isAdmin(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return "admin".equals(username);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin-dashboard";
    }

    @PostMapping("/movies/add")
    public String addMovie(@RequestParam String title,
                           @RequestParam String genre,
                           @RequestParam int duration,
                           @RequestParam double price,
                           @RequestParam("imageFile") org.springframework.web.multipart.MultipartFile imageFile,
                           HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }

        try {
            String imageUrl = "/images/placeholder.jpg";
            if (!imageFile.isEmpty()) {
                String originalFilename = imageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String fileName = System.currentTimeMillis() + extension;
                // Get the absolute path to the static images directory
                String uploadDir = new java.io.File("src/main/resources/static/images").getAbsolutePath();
                java.io.File file = new java.io.File(uploadDir + java.io.File.separator + fileName);
                
                // Ensure directory exists
                file.getParentFile().mkdirs();
                
                // Save the file
                imageFile.transferTo(file);
                imageUrl = "/images/" + fileName;
                
                // Also copy to target directory so it's immediately available without restart
                String targetDir = new java.io.File("target/classes/static/images").getAbsolutePath();
                java.io.File targetFile = new java.io.File(targetDir + java.io.File.separator + fileName);
                if (new java.io.File(targetDir).exists()) {
                    org.springframework.util.FileCopyUtils.copy(file, targetFile);
                }
            }

            Movie movie = new Movie(title, genre, duration, imageUrl, price);
            movieService.addMovie(movie);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/movies/delete/{title}")
    public String deleteMovie(@PathVariable String title, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        movieService.removeMovie(title);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/movies/update")
    public String updateMovie(@RequestParam String originalTitle, @ModelAttribute Movie movie, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        movieService.updateMovie(originalTitle, movie);
        return "redirect:/admin/dashboard";
    }
}
