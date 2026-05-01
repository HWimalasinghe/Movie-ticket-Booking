package com.moviebooking.onlinetickte.services;

import com.moviebooking.onlinetickte.models.Movie;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final String MOVIE_FILE = "src/data/movies.txt";

    public MovieService() {
        // Ensure file exists or load initial data if empty
        if (getAllMovies().isEmpty()) {
            List<Movie> initialMovies = new ArrayList<>();
            initialMovies.add(new Movie(1L, "Inception", "Sci-Fi", 148, "https://example.com/inception.jpg", 12.99));
            initialMovies.add(new Movie(2L, "The Dark Knight", "Action", 152, "https://example.com/dark-knight.jpg", 11.99));
            initialMovies.add(new Movie(3L, "Interstellar", "Sci-Fi", 169, "https://example.com/interstellar.jpg", 13.99));
            initialMovies.add(new Movie(4L, "Pulp Fiction", "Crime", 154, "https://example.com/pulp-fiction.jpg", 10.99));
            initialMovies.add(new Movie(5L, "The Matrix", "Sci-Fi", 136, "https://example.com/matrix.jpg", 9.99));
            initialMovies.add(new Movie(6L, "Avengers: Endgame", "Action", 181, "https://example.com/endgame.jpg", 15.99));
            saveMoviesToFile(initialMovies);
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        File file = new File(MOVIE_FILE);
        if (!file.exists()) return movies;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                    if (parts.length >= 6) {
                        Movie movie = new Movie(
                            Long.parseLong(parts[0]),
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]),
                            parts[4],
                            Double.parseDouble(parts[5])
                        );
                        movies.add(movie);
                    }
                } catch (Exception e) {
                    System.err.println("Skipping corrupted movie line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public Movie getMovieByTitle(String title) {
        return getAllMovies().stream()
                .filter(movie -> movie.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public boolean addMovie(Movie movie) {
        List<Movie> movies = getAllMovies();
        if (movies.stream().anyMatch(m -> m.getTitle().equalsIgnoreCase(movie.getTitle()))) {
            return false;
        }
        long nextId = movies.isEmpty() ? 1 : movies.get(movies.size() - 1).getId() + 1;
        movie.setId(nextId);
        movies.add(movie);
        return saveMoviesToFile(movies);
    }

    public boolean removeMovie(String title) {
        List<Movie> movies = getAllMovies();
        boolean removed = movies.removeIf(m -> m.getTitle().equalsIgnoreCase(title));
        if (removed) {
            return saveMoviesToFile(movies);
        }
        return false;
    }

    public boolean updateMovie(String title, Movie updatedMovie) {
        List<Movie> movies = getAllMovies();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle().equalsIgnoreCase(title)) {
                updatedMovie.setId(movies.get(i).getId());
                movies.set(i, updatedMovie);
                return saveMoviesToFile(movies);
            }
        }
        return false;
    }

    public Movie getMovieById(Long id) {
        return getAllMovies().stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private boolean saveMoviesToFile(List<Movie> movies) {
        try {
            File file = new File(MOVIE_FILE);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Movie movie : movies) {
                    writer.write(String.format("%d,%s,%s,%d,%s,%.2f%n",
                        movie.getId(),
                        movie.getTitle(),
                        movie.getGenre(),
                        movie.getDuration(),
                        movie.getImageUrl(),
                        movie.getPrice()
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
