package org.example.demodesktop.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demodesktop.App;

import java.io.IOException;

import static org.example.demodesktop.utils.UIUtils.show;

public class BooksListPage {
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("product-list-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        show(primaryStage, scene);
    }
}