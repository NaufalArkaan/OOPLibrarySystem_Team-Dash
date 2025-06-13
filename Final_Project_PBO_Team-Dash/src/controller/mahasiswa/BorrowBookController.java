package controller.mahasiswa;

import controller.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class BorrowBookController {

    @FXML private TextField nameField;
    @FXML private TextField studentIdField;
    @FXML private TextField classField;
    @FXML private TextField bookTitleField;
    @FXML private TextField isbnField;

    @FXML
    public void initialize() {
        // Ambil buku yang dipilih dari SharedBookData
        Book selectedBook = SharedBookData.getSelectedBook();
        if (selectedBook != null) {
            bookTitleField.setText(selectedBook.getTitle());
            isbnField.setText(selectedBook.getIsbn());

            // Buat title & isbn readonly supaya tidak bisa diubah
            bookTitleField.setEditable(false);
            isbnField.setEditable(false);
        }
    }

    @FXML
    private void handleSubmit() {
        String name = nameField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String className = classField.getText().trim();
        String bookTitle = bookTitleField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (name.isEmpty() || studentId.isEmpty() || className.isEmpty() || bookTitle.isEmpty() || isbn.isEmpty()) {
            showCustomAlert("Please fill in all fields!");
            return;
        }

        // Validasi ketersediaan buku
        if (!SharedBookData.isBookAvailable(bookTitle)) {
            showCustomAlert("The book is currently not available for borrowing.");
            return;
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusDays(7);
        int nextNo = SharedReturnData.getReturnRecords().size() + 1;

        ReturnRecord record = new ReturnRecord(
                nextNo,
                studentId,
                name,
                bookTitle,
                borrowDate,
                returnDate,
                "-",
                "Belum Kembali",
                nextNo
        );

        SharedReturnData.addReturnRecord(record);
        SharedHistoryData.addHistoryRecord(new HistoryRecord(name, className, bookTitle, isbn));
        SharedBookData.markAsBorrowed(bookTitle);

        showCustomAlert("Book borrowing submitted successfully!");
        clearForm();
        SharedBookData.setSelectedBook(null); // Reset data buku terpilih
    }

    @FXML
    private void handleCancel() {
        clearForm();
        SharedBookData.setSelectedBook(null); // Optional: reset juga saat cancel
    }

    private void clearForm() {
        nameField.clear();
        studentIdField.clear();
        classField.clear();
        bookTitleField.clear(); // Kosongkan judul buku
        isbnField.clear();      // Kosongkan ISBN
    }

    private void showCustomAlert(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mahasiswa/CustomAlertBorrow.fxml"));
            Parent root = loader.load();

            CustomAlertController controller = loader.getController();
            controller.setMessage(message);

            Stage alertStage = new Stage();
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.setTitle("Message");
            alertStage.setScene(new Scene(root));
            alertStage.setResizable(false);
            alertStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
