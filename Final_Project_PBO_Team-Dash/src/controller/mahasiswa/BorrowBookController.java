package controller.mahasiswa;

import Data.Book;
import Data.Loan;
import User.Member;
import SQL_DATA.LoanDAO; // Import LoanDAO
import SQL_DATA.BookDAO; // Import BookDAO
import controller.LoginController;
import model.SharedBookData;
import controller.util.mahasiswa.NavigationHelper;
import ExceptionHandle.BookNotFoundException;
import ExceptionHandle.BookOutOfStockException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;

public class BorrowBookController {

    @FXML private TextField nameField;
    @FXML private TextField studentIdField;
    @FXML private TextField bookTitleField;
    @FXML private TextField isbnField;
    @FXML private TextField classField;
    @FXML private TextField quantityField;
    @FXML private Button cancelButton;
    @FXML private Button submitButton;

    private final LoanDAO loanDAO = new LoanDAO();
    private final BookDAO bookDAO = new BookDAO(); // Inisialisasi BookDAO
    private Book selectedBook;

    @FXML
    public void initialize() {
        if(LoginController.loggedInUser instanceof Member) {
            Member member = (Member) LoginController.loggedInUser;
            nameField.setText(member.getName());
            studentIdField.setText(member.getStudentId());
            if (classField != null) classField.setText(member.getMajor());
        }

        this.selectedBook = SharedBookData.getSelectedBook();
        SharedBookData.setSelectedBook(null); // Bersihkan selectedBook dari SharedBookData

        if (selectedBook != null) {
            bookTitleField.setText(selectedBook.getTitle());
            isbnField.setText(selectedBook.getCode());
            bookTitleField.setEditable(false);
            isbnField.setEditable(false);

            if (selectedBook.getQuantity() <= 0) {
                quantityField.setText("0");
                quantityField.setDisable(true);
                submitButton.setDisable(true);
                showAlert(Alert.AlertType.WARNING, "Stok Habis", "Buku \"" + selectedBook.getTitle() + "\" sedang tidak tersedia (stok 0).");
            } else {
                quantityField.setText("1");
                quantityField.setDisable(false);
                submitButton.setDisable(false);
            }
        } else {
            bookTitleField.setText("");
            isbnField.setText("");
            bookTitleField.setEditable(true);
            isbnField.setEditable(true);
            quantityField.setText("1");
            quantityField.setDisable(false);
            submitButton.setDisable(false);
        }
    }

    @FXML
    protected void handleSubmit() {
        Member member = (Member) LoginController.loggedInUser;
        if (member == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Sesi pengguna tidak ditemukan. Silakan login kembali.");
            return;
        }

        if (this.selectedBook == null) {
            String manualIsbn = isbnField.getText().trim();
            if (manualIsbn.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Salah", "Kode ISBN harus diisi untuk meminjam buku secara manual.");
                return;
            }
            try {
                this.selectedBook = bookDAO.findBookByCode(manualIsbn);
            } catch (BookNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Buku Tidak Ditemukan", e.getMessage() + "\nHarap masukkan ISBN yang benar atau pilih dari katalog.");
                return;
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Error Database", "Terjadi kesalahan database saat mencari buku: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        if (this.selectedBook == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak ada buku yang valid untuk diproses.");
            return;
        }


        int requestedQuantity;
        try {
            requestedQuantity = Integer.parseInt(quantityField.getText());
            if (requestedQuantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input Salah", "Jumlah pinjam harus lebih dari 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Harap masukkan angka yang valid untuk jumlah pinjam.");
            return;
        }

        try {
            if (member.findLoanByBookCode(selectedBook.getCode()) != null) {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Anda sudah meminjam buku '" + selectedBook.getTitle() + "' dan belum mengembalikannya.");
                return;
            }

            if (requestedQuantity > selectedBook.getQuantity()) {
                throw new BookOutOfStockException("Stok buku tidak mencukupi. Stok tersedia: " + selectedBook.getQuantity());
            }

            Loan newLoan = new Loan(selectedBook, LocalDate.now(), requestedQuantity);
            newLoan.setUserId(member.getUserId());

            if (loanDAO.addLoan(newLoan, member)) {
                int newStock = selectedBook.getQuantity() - requestedQuantity;
                bookDAO.updateBookQuantity(selectedBook.getCode(), newStock);

                if (newStock == 0) {
                    bookDAO.updateBookStatus(selectedBook.getCode(), "Borrowed");
                } else if (selectedBook.getQuantity() > 0 && newStock > 0){
                    bookDAO.updateBookStatus(selectedBook.getCode(), "Available");
                }

                member.addLoan(newLoan);
                showCustomSuccessAlert("Buku berhasil dipinjam!");
                handleCancel();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan data peminjaman di database.");
            }
        } catch (BookOutOfStockException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Pinjam", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Terjadi kesalahan database: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan tak terduga: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showCustomSuccessAlert(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mahasiswa/CustomAlertBorrow.fxml"));
            Parent root = loader.load();

            CustomAlertController controller = loader.getController();
            controller.setMessage(message);

            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", message);
        }
    }

    @FXML
    protected void handleCancel() {
        try {
            AnchorPane mainContentPane = (AnchorPane) nameField.getScene().lookup("#contentPane");
            if (mainContentPane != null) {
                NavigationHelper.navigate(mainContentPane, "booksBtn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}