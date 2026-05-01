package com.moviebooking.onlinetickte.controllers;

import com.moviebooking.onlinetickte.models.User;
import com.moviebooking.onlinetickte.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final com.moviebooking.onlinetickte.services.BookingService bookingService;

    @Autowired
    public UserController(UserService userService, com.moviebooking.onlinetickte.services.BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, org.springframework.ui.Model model) {
        if (session.getAttribute("isLoggedIn") == null || !(Boolean) session.getAttribute("isLoggedIn")) {
            return "redirect:/users/login";
        }
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("bookings", bookingService.getUserBookings(username));
        return "profile";
    }

    // Display Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Process Login
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = userService.authenticateUser(username, password);

        if (user != null) {
            session.setAttribute("username", username);
            session.setAttribute("isLoggedIn", true);
            
            if ("admin".equals(username)) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/users/login";
        }
    }

    // Display Registration Page
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // Process Registration
    @PostMapping("/register")
    public String processRegistration(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/users/register";
        }

        if (userService.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/users/register";
        }

        if (userService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/users/register";
        }

        User user = userService.registerUser(username, email, password);

        if (user != null) {
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/users/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
            return "redirect:/users/register";
        }
    }

    // Logout User
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
