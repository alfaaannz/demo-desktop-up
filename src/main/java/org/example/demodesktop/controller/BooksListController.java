package org.example.demodesktop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.example.demodesktop.model.Books;
import org.example.demodesktop.repository.BooksRepository;
import org.example.demodesktop.view.BooksDetailPage;
import org.example.demodesktop.view.CreateBooksPage;
import org.example.demodesktop.view.LoginPage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import static org.example.demodesktop.utils.UIUtils.showAlert;

public class BooksListController {

    private static final Logger log = Logger.getLogger(BooksListController.class.getName());
    private final BooksRepository booksRepository = new BooksRepository();

    @FXML
    private TableView<Books> booksTableView;
    @FXML
    private TableColumn<Books, String> titleColumn;
    @FXML
    private TableColumn<Books, String> authorColumn;
    @FXML
    private TableColumn<Books, String> publisherColumn;
    @FXML
    private TableColumn<Books, String> isbnColumn;
    @FXML
    private TableColumn<Books, LocalDateTime> createdAtColumn;
    @FXML
    private TableColumn<Books, LocalDateTime> updatedAtColumn;
    @FXML
    private Button createBooksButton;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() throws IOException {
        booksTableView.setItems(getBooks());

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));


        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        booksTableView.setRowFactory( tv -> {
            TableRow<Books> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY ) {
                    Books clickedBooks = row.getItem();
                    log.info("Books clicked: " + clickedBooks);
                    showBooksDetailPage((Stage) booksTableView.getScene().getWindow(), clickedBooks);
                }
            });
            return row;
        });
    }

    @FXML
    private void handleCreateBooks() throws IOException {
        new CreateBooksPage().start((Stage) createBooksButton.getScene().getWindow());
    }

    @FXML
    private void handleLogout() throws IOException {
        log.info("Logout button clicked");
        showAlert(Alert.AlertType.CONFIRMATION, "Logout successfully", "Thank you for using our app");
        log.info("Alert displayed");
        new LoginPage().start((Stage) logoutButton.getScene().getWindow());
        log.info("Navigated to login page");
    }


    private ObservableList<Books> getBooks() {
        List<Books> books = booksRepository.findAll();
        return FXCollections.observableList(books);
    }

    private void showBooksDetailPage(Stage primaryStage, Books books) {
        BooksDetailPage detailPage = new BooksDetailPage(books);
        try {
            detailPage.start(primaryStage);
        } catch (Exception e) {
            log.warning("Error while showing books detail page");
        }
    }

}