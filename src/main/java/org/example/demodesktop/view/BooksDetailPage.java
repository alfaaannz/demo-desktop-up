package org.example.demodesktop.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demodesktop.App;
import org.example.demodesktop.controller.BooksDetailController;
import org.example.demodesktop.model.Books;

import java.io.IOException;

import static org.example.demodesktop.utils.UIUtils.show;

public class BooksDetailPage {
    private final Books books;

    public BooksDetailPage(Books books) {
        this.books = books;
    }

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("product-detail-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        BooksDetailController booksDetailController = fxmlLoader.getController();
        booksDetailController.initData(books);

        show(primaryStage, scene);
    }
}