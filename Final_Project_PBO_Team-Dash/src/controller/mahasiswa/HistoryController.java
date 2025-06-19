package controller.mahasiswa;

import Data.Loan;
import SQL_DATA.LoanDAO;
import User.Member;
import controller.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import ExceptionHandle.NoDataFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HistoryController {

    @FXML private TableView<Loan> historyTable;
    @FXML private TableColumn<Loan, String> noCol;
    @FXML private TableColumn<Loan, String> titleCol;
    @FXML private TableColumn<Loan, String> isbnCol;
    @FXML private TableColumn<Loan, String> borrowDateCol;
    @FXML private TableColumn<Loan, String> dueDateCol;
    @FXML private TableColumn<Loan, Void> actionsCol;

    private LoanDAO loanDAO = new LoanDAO();
    private ObservableList<Loan> loanHistoryList = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @FXML
    public void initialize() {
        setupTableColumns();
        loadLoanHistory();
    }

    private void setupTableColumns() {
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        noCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(loanHistoryList.indexOf(cellData.getValue()) + 1))
        );
        noCol.setStyle("-fx-alignment: CENTER;");

        titleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getTitle())
        );

        isbnCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getCode())
        );
        isbnCol.setStyle("-fx-alignment: CENTER;");

        borrowDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBorrowDate().format(formatter))
        );
        borrowDateCol.setStyle("-fx-alignment: CENTER;");

        dueDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDueDate().format(formatter))
        );
        dueDateCol.setStyle("-fx-alignment: CENTER;");

        if (actionsCol != null) {
            actionsCol.setVisible(false);
        }
    }

    private void loadLoanHistory() {
        User.User currentUser = LoginController.loggedInUser;
        if (currentUser instanceof Member) {
            Member currentMember = (Member) currentUser;
            loanHistoryList.clear();

            try {

                ArrayList<Loan> activeLoans = loanDAO.getActiveLoansByUserId(currentMember.getUserId());
                loanHistoryList.addAll(activeLoans);
            } catch (NoDataFoundException e) {
                System.out.println("Info: " + e.getMessage());
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat pinjaman aktif: " + e.getMessage());
                e.printStackTrace();
            }

            try {
                ArrayList<Loan> returnedLoans = loanDAO.getReturnedLoansByUserId(currentMember.getUserId());
                loanHistoryList.addAll(returnedLoans);
            } catch (NoDataFoundException e) {
                System.out.println("Info: " + e.getMessage());
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal memuat pinjaman yang dikembalikan: " + e.getMessage());
                e.printStackTrace();
            }

            if (loanHistoryList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Riwayat Peminjaman", "Anda belum memiliki riwayat peminjaman buku.");
            }

            historyTable.setItems(loanHistoryList);

        } else {
            historyTable.getItems().clear();
            showAlert(Alert.AlertType.WARNING, "Akses Ditolak", "Silakan login sebagai member untuk melihat riwayat peminjaman.");
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