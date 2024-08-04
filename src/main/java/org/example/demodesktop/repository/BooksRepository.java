package org.example.demodesktop.repository;

import org.example.demodesktop.config.DatabaseConnection;
import org.example.demodesktop.model.Books;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BooksRepository {
    private static final Logger log = Logger.getLogger(BooksRepository.class.getName());

    // Menyimpan buku ke dalam database
    public void save(Books books) {
        String sql = "INSERT INTO books (title, author, publisher, year, isbn, pages, genre, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, books.getTitle());
            statement.setString(2, books.getAuthor());
            statement.setString(3, books.getPublisher());
            statement.setInt(4, books.getYear());
            statement.setString(5, books.getIsbn());
            statement.setInt(6, books.getPages());
            statement.setString(7, books.getGenre());
            statement.setTimestamp(8, Timestamp.from(Instant.now()));
            statement.setTimestamp(9, Timestamp.from(Instant.now()));
            statement.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error saving books: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Mencari buku berdasarkan ISBN
    public Books findByIsbn(String isbn) {
        String sql = "SELECT title, author, publisher, year, isbn, pages, genre, created_at, updated_at FROM books WHERE isbn = ?";
        Books books = null;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, isbn);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    books = mapResultSetToBooks(rs);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding books by ISBN: {0}", e.getMessage());
        }
        return books;
    }

    // Menampilkan semua buku
    public List<Books> findAll() {
        String sql = "SELECT title, author, publisher, year, isbn, pages, genre, created_at, updated_at FROM books";
        List<Books> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getDbConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBooks(rs));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding all books: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return books;
    }

    // Memperbarui buku berdasarkan ISBN
    public boolean update(Books books) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ?, isbn = ?, pages = ?, genre = ?, created_at = ?, updated_at = ? WHERE isbn = ?";
        boolean updated = false;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, books.getTitle());
            statement.setString(2, books.getAuthor());
            statement.setString(3, books.getPublisher());
            statement.setInt(4, books.getYear());
            statement.setString(5, books.getIsbn());
            statement.setInt(6, books.getPages());
            statement.setString(7, books.getGenre());
            statement.setTimestamp(8, Timestamp.from(Instant.now()));
            statement.setTimestamp(9, Timestamp.from(Instant.now()));
            statement.setString(10, books.getIsbn()); // Ini adalah parameter WHERE clause
            updated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating books: {0}", e.getMessage());
        }
        return updated;
    }

    // Menghapus buku berdasarkan title
    public boolean delete(String title) {
        String sql = "DELETE FROM books WHERE title = ?";
        boolean deleted = false;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, title);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error deleting books: {0}", e.getMessage());
        }
        return deleted;
    }

    // Memetakan ResultSet ke objek Books
    private Books mapResultSetToBooks(ResultSet rs) throws SQLException {
        Books books = new Books();
        books.setTitle(rs.getString("title"));
        books.setAuthor(rs.getString("author"));
        books.setPublisher(rs.getString("publisher"));
        books.setYear(rs.getInt("year"));
        books.setIsbn(rs.getString("isbn"));
        books.setPages(rs.getInt("pages"));
        books.setGenre(rs.getString("genre"));
        books.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
        if (updatedAtTimestamp != null) {
            books.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
        } else {
            books.setUpdatedAt(null);
        }
        return books;
    }
}
