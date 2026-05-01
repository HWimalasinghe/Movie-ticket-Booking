package com.moviebooking.onlinetickte.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.moviebooking.onlinetickte.services.MovieService;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "index";
    }
} 