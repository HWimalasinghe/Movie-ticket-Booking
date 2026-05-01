package com.moviebooking.onlinetickte.services;

import com.moviebooking.onlinetickte.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public interface UserService {
    /**
     * Register a new user
     * @param username the username
     * @param email the email
     * @param password the password
     * @return the registered user
     */
    User registerUser(String username, String email, String password);

    /**
     * Authenticate a user
     * @param username the username
     * @param password the password
     * @return the authenticated user or null if authentication fails
     */
    User authenticateUser(String username, String password);

    /**
     * Get user by username
     * @param username the username to search for
     * @return the user if found, null otherwise
     */
    User getUserByUsername(String username);

    /**
     * Check if username exists
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Get all users from the system
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Save users to file
     * @param users list of users to save
     * @return true if successful, false otherwise
     */
    boolean saveUsersToFile(List<User> users);

    /**
     * Login user with username and password
     * @param username the username
     * @param password the password
     * @return true if login successful, false otherwise
     */
    boolean loginUser(String username, String password);
}
