package controller.mahasiswa;

import Data.Book;
import Data.Loan;
import User.Member;
import SQL_DATA.LoanDAO;
import SQL_DATA.BookDAO;
import controller.LoginController;
import controller.model.SharedBookData;
import controller.util.mahasiswa.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
    private final BookDAO bookDAO = new BookDAO();
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
        if (selectedBook != null) {
            bookTitleField.setText(selectedBook.getTitle());
            isbnField.setText(selectedBook.getCode());
        }

        if (selectedBook != null && selectedBook.getQuantity() > 0) {
            quantityField.setText("1");
            submitButton.setDisable(false);
        } else {
            quantityField.setText("0");
            quantityField.setDisable(true);
            submitButton.setDisable(true);
        }
    }

    @FXML
    protected void handleSubmit() {
        if (selectedBook == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak ada buku yang dipilih.");
            return;
        }
        Member member = (Member) LoginController.loggedInUser;
        if (member == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Sesi pengguna tidak ditemukan. Silakan login kembali.");
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

        if (requestedQuantity > selectedBook.getQuantity()) {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Stok buku tidak mencukupi. Stok tersedia: " + selectedBook.getQuantity());
            return;
        }

        Loan newLoan = new Loan(selectedBook, LocalDate.now(), requestedQuantity);

        if (loanDAO.addLoan(newLoan, member)) {
            int newStock = selectedBook.getQuantity() - requestedQuantity;
            bookDAO.updateBookQuantity(selectedBook.getCode(), newStock);

            if (newStock == 0) {
                bookDAO.updateBookStatus(selectedBook.getCode(), "Borrowed");
            }

            showCustomSuccessAlert("Buku berhasil dipinjam!");

            handleCancel();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan data peminjaman.");
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