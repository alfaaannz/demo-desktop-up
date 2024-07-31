package org.example.demodesktop.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.example.demodesktop.model.Product;
import org.example.demodesktop.repository.ProductRepository;
import org.example.demodesktop.view.ProductDetailPage;
import org.example.demodesktop.view.ProductListPage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

public class ProductDetailController {

    private static final Logger log = Logger.getLogger(ProductDetailController.class.getSimpleName());
    private Product product;
    private final ProductRepository productRepository = new ProductRepository();

    @FXML
    private Label productCodeLabel;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label createdAtLabel;
    @FXML
    private Label updatedAtLabel;
    @FXML
    private Button editProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private Button backButton;

    public void initData(Product product) {
        this.product = product;
        productCodeLabel.setText(productCodeLabel.getText() + " " + product.getProductCode());
        productNameLabel.setText(productNameLabel.getText() + " " + product.getName());
        priceLabel.setText(priceLabel.getText() + " " + product.getPrice());
        descriptionLabel.setText(descriptionLabel.getText() + " " + product.getDescription());
        createdAtLabel.setText(createdAtLabel.getText() + " " + product.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        updatedAtLabel.setText(updatedAtLabel.getText() + " " + (product.getUpdatedAt() != null ? product.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "N/A"));
    }

    @FXML
    private void handleBack() throws IOException {
        new ProductListPage().start((Stage) backButton.getScene().getWindow());
    }

    @FXML
    private void handleDelete() throws IOException {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this product?");
        alert.setContentText("Product: " + product.getName());

        // Show the alert and wait for a response
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user confirmed the deletion
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productRepository.delete(product.getProductCode());
            log.info("Product deleted: " + product);
            showListPage((Stage) deleteProductButton.getScene().getWindow()); // Go back to the product list page
        } else {
            log.info("Product deletion canceled: " + product);
        }
    }

    @FXML
    private void handleEdit() {
        // init dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // init grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // fill the content with product information
        TextField nameField = new TextField(product.getName());
        TextField descriptionField = new TextField(product.getDescription());
        TextField priceField = new TextField(product.getPrice().toString());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(nameField.getText(), descriptionField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(nameDescriptionPair -> {
            product.setName(nameDescriptionPair.getKey());
            product.setDescription(nameDescriptionPair.getValue());
            product.setPrice(BigDecimal.valueOf(Double.parseDouble(priceField.getText())));
            product.setUpdatedAt(LocalDateTime.now());
            // update product to table
            productRepository.update(product);
            log.info("Product updated: " + product);
            showProductDetailPage((Stage) editProductButton.getScene().getWindow(), product);
        });
    }

    private void showProductDetailPage(Stage primaryStage, Product product) {
        ProductDetailPage detailPage = new ProductDetailPage(product);
        try {
            detailPage.start(primaryStage);
        } catch (Exception e) {
            log.warning("Error while showing product detail page");
        }
    }

    private void showListPage(Stage primary) throws IOException {
        new ProductListPage().start(primary);
    }
}