package org.example.demodesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demodesktop.model.Product;
import org.example.demodesktop.repository.ProductRepository;
import org.example.demodesktop.view.ProductListPage;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.example.demodesktop.utils.UIUtils.showAlert;

public class CreateProductController {

    private static final Logger log = Logger.getLogger(CreateProductController.class.getName());
    private static final ProductRepository productRepository = new ProductRepository();

    @FXML
    private TextField productCode;
    @FXML
    private TextField productName;
    @FXML
    private TextField price;
    @FXML
    private TextField description;
    @FXML
    private Button createProductButton;

    @FXML
    public void initialize() {
        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                price.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void handleCreateProduct() {
        try {
            String productCodeValue = productCode.getText();
            String productNameValue = productName.getText();
            String priceValue = price.getText();
            String productDescValue = description.getText();

            if (productCodeValue.isEmpty() || productNameValue.isEmpty() || priceValue.isEmpty() || productDescValue.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Bad Request", "Please enter all the details");
                return;
            }

            if (productRepository.findByProductCode(productCodeValue) != null) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Request", "Product Code already exists");
                return;
            }

            Product product = new Product();
            product.setProductCode(productCodeValue);
            product.setName(productNameValue);
            product.setPrice(new BigDecimal(priceValue));
            product.setDescription(productDescValue);

            productRepository.save(product);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Successfully added product");
            back((Stage) createProductButton.getScene().getWindow());
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Server Error", "Please contact the administrator");
            log.warning(exception.getMessage());
        }
    }


    @FXML
    private void handleBack() {
        Stage primaryStage = (Stage) createProductButton.getScene().getWindow();
        back(primaryStage);
    }

    public void back(Stage stage) {
        ProductListPage productListPage = new ProductListPage();
        try {
            productListPage.start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}