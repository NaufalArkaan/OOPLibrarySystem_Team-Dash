package controller.admin;

import Action.Transaction;
import SQL_DATA.LoanDAO;
import SQL_DATA.TransactionDAO;
import SQL_DATA.UserDAO;
import User.Member;
import model.ReturnRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFineController {

    // Deklarasi disesuaikan dengan fx:id di FXML baru Anda
    @FXML private TextField nameField;
    @FXML private TextField studentIdField;
    @FXML private ComboBox<String> actionTypeComboBox;
    @FXML private TextField fineAmountField; // Mengganti nama dari amountField
    @FXML private Button submitButton; // Mengganti nama dari saveFineButton
    @FXML private Button cancelButton;
    @FXML private Label infoLabel; // Tetap dideklarasikan untuk keamanan

    private ReturnRecord currentRecord;
    private final LoanDAO loanDAO = new LoanDAO();
    private final UserDAO userDAO = new UserDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @FXML
    public void initialize() {
        // Mengisi pilihan pada ComboBox
        actionTypeComboBox.getItems().addAll("Add/Update Fine", "Pay Fine");
        actionTypeComboBox.setValue("Add/Update Fine"); // Nilai default

        // Menghubungkan tombol ke metode
        submitButton.setOnAction(e -> handleSubmit());
        cancelButton.setOnAction(e -> closeWindow());
    }

    public void setLoanData(ReturnRecord record) {
        this.currentRecord = record;

        nameField.setText(record.getMemberName());
        studentIdField.setText(record.getStudentId());

        String numericFine = record.getFine().replaceAll("[^\\d.]", "");
        fineAmountField.setText(numericFine);
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
        try {
            double paymentAmount = Double.parseDouble(fineAmountField.getText());
            if (paymentAmount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Payment amount must be greater than zero.");
                return;
            }

            Member member = userDAO.findMemberById(currentRecord.getUserId());
            if (member == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Member data not found.");
                return;
            }

            double fineBeforePayment = Double.parseDouble(currentRecord.getFine().replaceAll("[^\\d.]", ""));
            double fineAfterPayment = fineBeforePayment - paymentAmount;
            if (fineAfterPayment < 0) fineAfterPayment = 0;

            // 1. Buat dan simpan transaksi
            Transaction transaction = new Transaction(member, paymentAmount, fineBeforePayment, fineAfterPayment);
            transactionDAO.addTransaction(transaction);

            // 2. Update sisa denda di tabel loans
            loanDAO.updateLoanFine(currentRecord.getLoanId(), fineAfterPayment);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Payment of Rp " + paymentAmount + " has been recorded.");
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for the payment amount.");
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