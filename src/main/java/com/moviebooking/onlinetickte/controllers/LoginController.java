package com.moviebooking.onlinetickte.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/users/login2")
    public String loginPage(@RequestParam(value = "redirectToMovie", required = false) boolean redirectToMovie) {
        // Add a message or flag to notify user they need to login to book a movie
        if (redirectToMovie) {
            // Display an alert/message on login page indicating the need to log in
            return "login"; // The login page view name
        }
        return "login"; // Just the normal login page if no redirection is needed
    }
}
