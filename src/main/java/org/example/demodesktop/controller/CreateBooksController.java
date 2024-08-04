package org.example.demodesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demodesktop.model.Books;
import org.example.demodesktop.repository.BooksRepository;
import org.example.demodesktop.view.BooksListPage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.logging.Logger;

import static org.example.demodesktop.utils.UIUtils.showAlert;

public class CreateBooksController {

    private static final Logger log = Logger.getLogger(CreateBooksController.class.getName());
    private static final BooksRepository BOOKS_REPOSITORY = new BooksRepository();

    @FXML
    private TextField booksTitle;
    @FXML
    private TextField booksAuthor;
    @FXML
    private TextField booksPublisher;
    @FXML
    private TextField booksYear;
    @FXML
    private TextField booksIsbn;
    @FXML
    private TextField booksPages;
    @FXML
    private TextField booksGenre;
    @FXML
    private Button createBooksButton;

    @FXML
    public void initialize() {
        booksIsbn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                booksIsbn.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    @FXML
    private void handleCreateBooks() {
        try {
            String booksTitleValue = booksTitle.getText();
            String booksAuthorValue = booksAuthor.getText();
            String booksPublisherValue = booksPublisher.getText();
            String booksIsbnValue = booksIsbn.getText();
            String booksYearValue = booksYear.getText();
            String booksPagesValue = booksPages.getText();
            String booksGenreValue = booksGenre.getText();

            if (booksTitleValue.isEmpty() || booksAuthorValue.isEmpty() || booksPublisherValue.isEmpty() || booksIsbnValue.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Bad Request", "Please enter all the details");
                return;
            }

            if (BOOKS_REPOSITORY.findByIsbn(booksIsbnValue) != null) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Request", "ISBN already exists");
                return;
            }

            Books books = new Books();
            books.setTitle(booksTitleValue);
            books.setAuthor(booksAuthorValue);
            books.setPublisher(booksPublisherValue);
            books.setYear(Integer.parseInt(booksYearValue));
            books.setIsbn(booksIsbnValue);
            books.setPages(Integer.parseInt(booksPagesValue));
            books.setGenre(booksGenreValue);
            books.setCreatedAt(Timestamp.from(Instant.now()).toLocalDateTime());

            log.info("Books created: " + books);

            BOOKS_REPOSITORY.save(books);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Successfully added books");
            back((Stage) createBooksButton.getScene().getWindow());
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Server Error", "Please contact the administrator");
            log.warning(exception.getMessage());
        }
    }


    @FXML
    private void handleBack() {
        Stage primaryStage = (Stage) createBooksButton.getScene().getWindow();
        back(primaryStage);
    }

    public void back(Stage stage) {
        BooksListPage booksListPage = new BooksListPage();
        try {
            booksListPage.start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}