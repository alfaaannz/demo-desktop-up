package org.example.demodesktop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.example.demodesktop.config.SessionManager;
import org.example.demodesktop.model.Product;
import org.example.demodesktop.repository.ProductRepository;
import org.example.demodesktop.view.CreateProductPage;
import org.example.demodesktop.view.LoginPage;
import org.example.demodesktop.view.ProductDetailPage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import static org.example.demodesktop.utils.UIUtils.showAlert;

public class ProductListController {

    private static final Logger log = Logger.getLogger(ProductListController.class.getName());
    private final ProductRepository productRepository = new ProductRepository();

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, String> productCodeColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;
    @FXML
    private TableColumn<Product, String> descriptionColumn;
    @FXML
    private TableColumn<Product, LocalDateTime> createdAtColumn;
    @FXML
    private TableColumn<Product, LocalDateTime> updatedAtColumn;
    @FXML
    private Button createProductButton;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() throws IOException {
        productTableView.setItems(getProducts());

        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        productTableView.setRowFactory( tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY ) {
                    Product clickedProduct = row.getItem();
                    log.info("Product clicked: " + clickedProduct);
                    showProductDetailPage((Stage) productTableView.getScene().getWindow(), clickedProduct);
                }
            });
            return row;
        });
    }

    @FXML
    private void handleCreateProduct() throws IOException {
        new CreateProductPage().start((Stage) createProductButton.getScene().getWindow());
    }

    @FXML
    private void handleLogout() throws IOException {
        log.info("Logout button clicked");
        showAlert(Alert.AlertType.CONFIRMATION, "Logout successfully", "Thank you for using our app");
        log.info("Alert displayed");
        new LoginPage().start((Stage) logoutButton.getScene().getWindow());
        log.info("Navigated to login page");
    }


    private ObservableList<Product> getProducts() {
        List<Product> products = productRepository.findAll();
        return FXCollections.observableList(products);
    }

    private void showProductDetailPage(Stage primaryStage, Product product) {
        ProductDetailPage detailPage = new ProductDetailPage(product);
        try {
            detailPage.start(primaryStage);
        } catch (Exception e) {
            log.warning("Error while showing product detail page");
        }
    }

}