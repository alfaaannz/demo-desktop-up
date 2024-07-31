package org.example.demodesktop.repository;

import org.example.demodesktop.config.DatabaseConnection;
import org.example.demodesktop.model.Product;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductRepository {
    private static final Logger log = Logger.getLogger(ProductRepository.class.getName());

    public void save(Product product) {
        String sql = "INSERT INTO product (product_code, name, description, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getProductCode());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setBigDecimal(4, product.getPrice());
            statement.executeUpdate();

        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error saving product: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Product findByProductCode(String productCode) {
        String sql = "select product_code, name, price, description, created_at, updated_at from product where product_code = ?";
        Product product = null;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, productCode);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    product = mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding product by ID: {0}", e.getMessage());
        }
        return product;
    }

    public List<Product> findAll() {
        String sql = "select product_code, name, price, description, created_at, updated_at from product";
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getDbConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding all products: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return products;
    }

    public boolean update(Product product) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, updated_at = ? WHERE product_code = ?";
        boolean updated = false;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setTimestamp(4, Timestamp.from(Instant.now()));
            statement.setString(5, product.getProductCode());
            updated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating product: {0}", e.getMessage());
        }
        return updated;
    }

    public boolean delete(String productCode) {
        String sql = "DELETE FROM product WHERE product_code = ?";
        boolean deleted = false;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, productCode);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error deleting product: {0}", e.getMessage());
        }
        return deleted;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductCode(rs.getString("product_code"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
        if (updatedAtTimestamp != null) {
            product.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
        } else {
            product.setUpdatedAt(null);
        }
        return product;
    }
}