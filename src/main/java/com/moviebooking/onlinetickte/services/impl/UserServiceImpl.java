package com.moviebooking.onlinetickte.services.impl;

import com.moviebooking.onlinetickte.models.User;
import com.moviebooking.onlinetickte.services.UserService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_FILE = "src/data/users.txt";

    @Override
    public User registerUser(String username, String email, String password) {
        if (existsByUsername(username) || existsByEmail(email)) {
            return null;
        }
        List<User> users = getAllUsers();
        long nextId = users.isEmpty() ? 1 : users.get(users.size() - 1).getId() + 1;
        User user = new User(username, email, password);
        user.setId(nextId);
        users.add(user);
        if (saveUsersToFile(users)) {
            return user;
        }
        return null;
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return getUserByUsername(username) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return getAllUsers().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USER_FILE);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    User user = new User(parts[1], parts[3], parts[2]);
                    user.setId(Long.parseLong(parts[0]));
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean saveUsersToFile(List<User> users) {
        try {
            File file = new File(USER_FILE);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (User user : users) {
                    writer.write(String.format("%d,%s,%s,%s%n",
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getEmail()
                    ));
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean loginUser(String username, String password) {
        return authenticateUser(username, password) != null;
    }
}
 