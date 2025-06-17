package controller.admin;

import Action.Transaction;
import Data.Loan;
import SQL_DATA.LoanDAO;
import SQL_DATA.TransactionDAO;
import SQL_DATA.UserDAO;
import User.Member;
import controller.model.ReturnRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFineController {

    @FXML private TextField nameField;
    @FXML private TextField studentIdField;
    @FXML private ComboBox<String> actionTypeComboBox;
    @FXML private TextField fineAmountField;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    @FXML private Label infoLabel;

    private ReturnRecord currentRecord;
    private final LoanDAO loanDAO = new LoanDAO();
    private final UserDAO userDAO = new UserDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private Loan currentLoanFromDB;

    @FXML
    public void initialize() {
        actionTypeComboBox.getItems().addAll("Add/Update Fine", "Pay Fine");
        actionTypeComboBox.setValue("Add/Update Fine");

        submitButton.setOnAction(e -> handleSubmit());
        cancelButton.setOnAction(e -> closeWindow());
    }

    public void setLoanData(ReturnRecord record) {
        this.currentRecord = record;

        this.currentLoanFromDB = loanDAO.getAllActiveLoansWithDetails().stream()
                .filter(loan -> loan.getLoanId() == record.getLoanId())
                .findFirst()
                .orElse(null);

        if (this.currentLoanFromDB != null) {
            nameField.setText(record.getMemberName());
            studentIdField.setText(record.getStudentId());
            fineAmountField.setText(String.format("%.0f", this.currentLoanFromDB.getFine()));
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Data pinjaman tidak dapat ditemukan di database.");
            closeWindow();
        }
    }

    @FXML
    private void handleSubmit() {
        String selectedAction = actionTypeComboBox.getValue();
        if (selectedAction == null) {
            showAlert(Alert.AlertType.ERROR, "Action Required", "Please select an action (Add/Pay Fine).");
            return;
        }

        if ("Add/Update Fine".equals(selectedAction)) {
            handleSaveFine();
        } else if ("Pay Fine".equals(selectedAction)) {
            handlePayFine();
        }
    }

    private void handleSaveFine() {
        try {
            double fineAmount = Double.parseDouble(fineAmountField.getText());
            if (fineAmount < 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Fine amount cannot be negative.");
                return;
            }

            boolean success = loanDAO.updateLoanFine(currentRecord.getLoanId(), fineAmount);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Fine has been updated successfully.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update fine in the database.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for the fine amount.");
        }
    }

    private void handlePayFine() {
        if (this.currentLoanFromDB == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak ada data pinjaman yang valid untuk diproses.");
            return;
        }

        try {
            double paymentAmount = Double.parseDouble(fineAmountField.getText());
            if (paymentAmount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Jumlah pembayaran harus lebih dari nol.");
                return;
            }

            Member member = userDAO.findMemberById(currentRecord.getUserId());
            if (member == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Data member tidak ditemukan.");
                return;
            }

            double fineBeforePayment = this.currentLoanFromDB.getFine();

            if (paymentAmount > fineBeforePayment) {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Jumlah bayar melebihi total denda. Pembayaran akan disesuaikan dengan total denda.");
                paymentAmount = fineBeforePayment;
            }

            double fineAfterPayment = fineBeforePayment - paymentAmount;
            if (fineAfterPayment < 0) fineAfterPayment = 0;

            Transaction transaction = new Transaction(member, paymentAmount, fineBeforePayment, fineAfterPayment);
            boolean transactionSuccess = transactionDAO.addTransaction(transaction);

            if (transactionSuccess) {
                loanDAO.updateLoanFine(currentRecord.getLoanId(), fineAfterPayment);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pembayaran sebesar Rp " + String.format("%,.0f", paymentAmount) + " telah dicatat.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menyimpan catatan transaksi.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Harap masukkan angka yang valid untuk jumlah pembayaran.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
