package org.example.demodesktop.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demodesktop.App;
import org.example.demodesktop.controller.ProductDetailController;
import org.example.demodesktop.model.Product;

import java.io.IOException;

import static org.example.demodesktop.utils.UIUtils.show;

public class ProductDetailPage {
    private final Product product;

    public ProductDetailPage(Product product) {
        this.product = product;
    }

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("product-detail-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        ProductDetailController productDetailController = fxmlLoader.getController();
        productDetailController.initData(product);

        show(primaryStage, scene);
    }
}