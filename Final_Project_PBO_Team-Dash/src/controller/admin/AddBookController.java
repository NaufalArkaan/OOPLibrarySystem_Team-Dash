package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import controller.model.Book;
import controller.model.SharedBookData;

import java.io.File;

public class AddBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private TextField isbnField;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button addImageButton;

    private File selectedImageFile;
    private Book selectedBookToEdit = null;

    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> saveOrUpdateBook());
        cancelButton.setOnAction(e -> clearForm());
        addImageButton.setOnAction(e -> chooseImage());
    }

    public void setBookToEdit(Book book) {
        if (book != null) {
            this.selectedBookToEdit = book;
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            categoryField.setText(book.getCategory());
            isbnField.setText(book.getIsbn());
            saveButton.setText("Update Book");
        }
    }

    private void saveOrUpdateBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String category = categoryField.getText();
        String isbn = isbnField.getText();

        if (title.isEmpty() || author.isEmpty() || category.isEmpty() || isbn.isEmpty()) {
            showAlert("Validation Failed", "All fields must be filled.");
            return;
        }

        if (selectedBookToEdit != null) {
            // Update mode: buat objek baru dengan data baru, tetap pakai no dan status lama
            Book updatedBook = new Book(
                    selectedBookToEdit.getNo(),
                    title,
                    author,
                    category,
                    isbn,
                    selectedBookToEdit.getStatus()
            );

            int index = SharedBookData.getBooks().indexOf(selectedBookToEdit);
            if (index >= 0) {
                SharedBookData.getBooks().set(index, updatedBook);
                showAlert("Success", "Book updated successfully.");
            } else {
                showAlert("Error", "Book to update not found.");
            }
        } else {
            // Add mode
            int no = SharedBookData.getBooks().size() + 1;
            Book newBook = new Book(no, title, author, category, isbn, "Tersedia");
            SharedBookData.getBooks().add(newBook);
            showAlert("Success", "Book added successfully.");
        }

        clearForm();
    }

    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Book Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Window window = addImageButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(window);

        if (selectedImageFile != null) {
            showAlert("Image Selected", "File: " + selectedImageFile.getName());
        }
    }

    private void clearForm() {
        titleField.clear();
        authorField.clear();
        categoryField.clear();
        isbnField.clear();
        selectedImageFile = null;
        selectedBookToEdit = null;
        saveButton.setText("Add Book");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
