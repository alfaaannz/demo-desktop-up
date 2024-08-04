package org.example.demodesktop.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.example.demodesktop.model.Books;
import org.example.demodesktop.repository.BooksRepository;
import org.example.demodesktop.view.BooksDetailPage;
import org.example.demodesktop.view.BooksListPage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

public class BooksDetailController {

    private static final Logger log = Logger.getLogger(BooksDetailController.class.getSimpleName());
    private Books books;
    private final BooksRepository booksRepository = new BooksRepository();

    @FXML
    private Label booksTitle;
    @FXML
    private Label booksAuthor;
    @FXML
    private Label booksPublisher;
    @FXML
    private Label booksYear;
    @FXML
    private Label booksIsbn;
    @FXML
    private Label booksPages;
    @FXML
    private Label booksGenre;
    @FXML
    private Label createdAtLabel;
    @FXML
    private Label updatedAtLabel;
    @FXML
    private Button editBooksButton;
    @FXML
    private Button deleteBooksButton;
    @FXML
    private Button backButton;

    public void initData(Books books) {
        this.books = books;
        System.out.println("Title: " + books.getTitle());
        System.out.println("Author: " + books.getAuthor());
        System.out.println("Publisher: " + books.getPublisher());
        System.out.println("Year: " + books.getYear());
        System.out.println("Isbn: " + books.getIsbn());
        System.out.println("Pages: " + books.getPages());
        System.out.println("Genre: " + books.getGenre());
        System.out.println("Created At: " + books.getCreatedAt());
        System.out.println("Updated At: " + books.getUpdatedAt());

        booksTitle.setText("Title: " + books.getTitle());
        booksAuthor.setText("Author name: " + books.getAuthor());
        booksPublisher.setText("Publisher: " + books.getPublisher());
        booksYear.setText("Year: " + books.getYear());
        booksIsbn.setText("Isbn: " + books.getIsbn());
        booksPages.setText("Pages: " + books.getPages());
        booksGenre.setText("Genre: " + books.getGenre());
        createdAtLabel.setText("Created at: " + books.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        updatedAtLabel.setText("Updated at: " + (books.getUpdatedAt() != null ? books.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "N/A"));
    }


    @FXML
    private void handleBack() throws IOException {
        new BooksListPage().start((Stage) backButton.getScene().getWindow());
    }

    @FXML
    private void handleDelete() throws IOException {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this books?");
        alert.setContentText("Books: " + books.getTitle());

        // Show the alert and wait for a response
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user confirmed the deletion
        if (result.isPresent() && result.get() == ButtonType.OK) {
            booksRepository.delete(books.getTitle());
            log.info("Books deleted: " + books);
            showListPage((Stage) deleteBooksButton.getScene().getWindow()); // Go back to the product list page
        } else {
            log.info("Books deletion canceled: " + books);
        }
    }

    @FXML
    private void handleEdit() {
        // init dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Books");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // init grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // fill the content with product information
        TextField titleField = new TextField(books.getTitle());
        TextField authorField = new TextField(books.getAuthor());
        TextField publisherField = new TextField(books.getPublisher());
        TextField yearField = new TextField(String.valueOf(books.getYear()));
        TextField isbnField = new TextField(books.getIsbn());
        TextField pagesField = new TextField(String.valueOf(books.getPages()));
        TextField genreField = new TextField(books.getGenre());

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Publisher:"), 0, 3);
        grid.add(publisherField, 1, 3);
        grid.add(new Label("Year:"), 0, 4);
        grid.add(yearField, 1, 4);
        grid.add(new Label("Isbn:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Pages:"), 0, 5);
        grid.add(pagesField, 1, 5);
        grid.add(new Label("Genre:"), 0, 6);
        grid.add(genreField, 1, 6);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(titleField.getText(), authorField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(titleAuthorPair -> {
            books.setTitle(titleAuthorPair.getKey());
            books.setAuthor(titleAuthorPair.getValue());
            books.setIsbn(String.valueOf(Double.parseDouble(isbnField.getText())));
            books.setCreatedAt(LocalDateTime.now());
            books.setUpdatedAt(LocalDateTime.now());
            // update product to table
            booksRepository.update(books);
            log.info("Books updated: " + books);
            showBooksDetailPage((Stage) editBooksButton.getScene().getWindow(), books);
        });
    }

    private void showBooksDetailPage(Stage primaryStage, Books books) {
        BooksDetailPage detailPage = new BooksDetailPage(books);
        try {
            detailPage.start(primaryStage);
        } catch (Exception e) {
            log.warning("Error while showing books detail page");
        }
    }

    private void showListPage(Stage primary) throws IOException {
        new BooksListPage().start(primary);
    }
}