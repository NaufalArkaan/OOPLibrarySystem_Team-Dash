package controller.mahasiswa;

import Data.Loan; // <-- GANTI import ke Data.Loan
import SQL_DATA.LoanDAO; // <-- IMPORT BARU
import User.Member;
import controller.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import java.time.format.DateTimeFormatter;

public class HistoryController {

    // Kolom disesuaikan untuk menampilkan data dari objek Loan
    @FXML private TableView<Loan> historyTable;
    @FXML private TableColumn<Loan, String> noCol;
    @FXML private TableColumn<Loan, String> titleCol;
    @FXML private TableColumn<Loan, String> isbnCol;
    @FXML private TableColumn<Loan, String> borrowDateCol;
    @FXML private TableColumn<Loan, String> dueDateCol;
    @FXML private TableColumn<Loan, Void> actionsCol; // Kolom aksi tidak lagi diperlukan

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

        // Kolom Nomor
        noCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(loanHistoryList.indexOf(cellData.getValue()) + 1))
        );
        noCol.setStyle("-fx-alignment: CENTER;");

        // Kolom Judul Buku
        titleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getTitle())
        );

        // Kolom ISBN
        isbnCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getCode())
        );
        isbnCol.setStyle("-fx-alignment: CENTER;");

        // Kolom Tanggal Pinjam
        borrowDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBorrowDate().format(formatter))
        );
        borrowDateCol.setStyle("-fx-alignment: CENTER;");

        // Kolom Tanggal Jatuh Tempo
        dueDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDueDate().format(formatter))
        );
        dueDateCol.setStyle("-fx-alignment: CENTER;");

        // Sembunyikan kolom aksi karena tidak lagi digunakan
        if (actionsCol != null) {
            actionsCol.setVisible(false);
        }
    }

    private void loadLoanHistory() {
        User.User currentUser = LoginController.loggedInUser;
        if (currentUser instanceof Member) {
            Member currentMember = (Member) currentUser;
            // Ambil riwayat pinjaman dari database melalui LoanDAO
            loanHistoryList.setAll(loanDAO.getActiveLoansByUserId(currentMember.getUserId()));
            historyTable.setItems(loanHistoryList);
        } else {
            // Kosongkan tabel jika tidak ada user yang login atau bukan member
            historyTable.getItems().clear();
        }
    }
}