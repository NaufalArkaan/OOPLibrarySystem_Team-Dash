package controller.admin;

import Action.Transaction;
import Data.Loan;
import SQL_DATA.LoanDAO;
import SQL_DATA.TransactionDAO;
import SQL_DATA.UserDAO;
import User.Member;
import model.ReturnRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.beans.property.SimpleStringProperty;


import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ReportsAndChartsController {

    private enum ReportType {
        LOANS, TRANSACTIONS
    }

    @FXML private Label titleLabel;
    @FXML private TableView tableView;
    @FXML private Button btnPrev;
    @FXML private Button btnNext;

    private final LoanDAO loanDAO = new LoanDAO();
    private final UserDAO userDAO = new UserDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    private final DateTimeFormatter loanDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter transactionDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private ReportType currentReport = ReportType.LOANS;

    @FXML
    public void initialize() {
        setupAndLoadLoanReport();
        btnPrev.setDisable(true);
    }

    @FXML
    private void handlePrev() {
        if (currentReport == ReportType.TRANSACTIONS) {
            setupAndLoadLoanReport();
            currentReport = ReportType.LOANS;
            btnPrev.setDisable(true);
            btnNext.setDisable(false);
            titleLabel.setText("Laporan Peminjaman Buku");
        }
    }

    @FXML
    private void handleNext() {
        if (currentReport == ReportType.LOANS) {
            setupAndLoadTransactionReport();
            currentReport = ReportType.TRANSACTIONS;
            btnPrev.setDisable(false);
            btnNext.setDisable(true);
            titleLabel.setText("Laporan Transaksi Pembayaran Denda");
        }
    }

    private void setupAndLoadLoanReport() {
        tableView.getColumns().clear();

        TableColumn<ReturnRecord, Integer> colNo = new TableColumn<>("No");
        TableColumn<ReturnRecord, String> colStudentId = new TableColumn<>("Student ID");
        TableColumn<ReturnRecord, String> colMemberName = new TableColumn<>("Member Name");
        TableColumn<ReturnRecord, String> colBorrowDate = new TableColumn<>("Borrow Date");
        TableColumn<ReturnRecord, String> colReturnDate = new TableColumn<>("Return Date");
        TableColumn<ReturnRecord, Integer> colTotalBorrowed = new TableColumn<>("Total Borrowed");
        TableColumn<ReturnRecord, String> colFine = new TableColumn<>("Fine");
        TableColumn<ReturnRecord, String> colStatus = new TableColumn<>("Status");

        tableView.getColumns().addAll(colNo, colStudentId, colMemberName, colBorrowDate, colReturnDate, colTotalBorrowed, colFine, colStatus);

        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colTotalBorrowed.setCellValueFactory(new PropertyValueFactory<>("totalBorrowed"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colBorrowDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorrowDate().format(loanDateFormatter)));
        colReturnDate.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getActualReturnDate() != null) ? cellData.getValue().getActualReturnDate().format(loanDateFormatter) : cellData.getValue().getReturnDate().format(loanDateFormatter)));

        colStatus.setCellFactory(column -> createLoanStatusCell());

        loadLoanReportData();
    }

    private void loadLoanReportData() {
        ObservableList<ReturnRecord> reportList = FXCollections.observableArrayList();
        ArrayList<Loan> allLoans = loanDAO.getAllLoansWithDetails();
        int counter = 1;

        for (Loan loan : allLoans) {
            Member member = userDAO.findMemberById(loan.getUserId());
            if (member != null) {
                ReturnRecord record = new ReturnRecord(
                        counter++, member.getStudentId(), member.getName(), loan.getBook().getTitle(),
                        loan.getBorrowDate(), loan.getDueDate(), "Rp " + String.format("%,.0f", loan.getFine()),
                        loan.getStatus(), loan.getQuantity()
                );
                record.setActualReturnDate(loan.getActualReturnDate());
                reportList.add(record);
            }
        }
        tableView.setItems(reportList);
    }

    private void setupAndLoadTransactionReport() {
        tableView.getColumns().clear();

        TableColumn<Transaction, Integer> colNo = new TableColumn<>("Nomor");
        TableColumn<Transaction, String> colName = new TableColumn<>("Nama");
        TableColumn<Transaction, String> colStudentId = new TableColumn<>("Student ID");
        TableColumn<Transaction, String> colFine = new TableColumn<>("Jumlah Bayar");
        TableColumn<Transaction, String> colDate = new TableColumn<>("Tanggal Transaksi");
        TableColumn<Transaction, String> colStatus = new TableColumn<>("Status Denda");

        tableView.getColumns().addAll(colNo, colName, colStudentId, colFine, colDate, colStatus);

        // --- PERBAIKAN UTAMA DI SINI ---
        // Menerapkan perataan tengah untuk semua kolom
        setTransactionColumnAlignment(colNo, Pos.CENTER);
        colNo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                    setAlignment(Pos.CENTER);
                }
            }
        });

        setTransactionColumnAlignment(colName, Pos.CENTER);
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().getName()));

        setTransactionColumnAlignment(colStudentId, Pos.CENTER);
        colStudentId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().getStudentId()));

        setTransactionColumnAlignment(colFine, Pos.CENTER);
        colFine.setCellValueFactory(cellData -> new SimpleStringProperty("Rp " + String.format("%,.0f", cellData.getValue().getAmountPaid())));

        setTransactionColumnAlignment(colDate, Pos.CENTER);
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransactionDateTime().format(transactionDateFormatter)));

        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getFineAfterPayment() == 0 ? "Lunas" : "Belum Lunas"
        ));
        colStatus.setCellFactory(column -> createTransactionStatusCell());

        loadTransactionReportData();
    }

    private void loadTransactionReportData() {
        ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
        transactionList.setAll(transactionDAO.getAllTransactions());
        tableView.setItems(transactionList);
    }

    private TableCell<ReturnRecord, String> createLoanStatusCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setStyle("");
                } else {
                    setText(item);
                    if ("Returned".equalsIgnoreCase(item)) {
                        setTextFill(Color.GREEN);
                    } else {
                        setTextFill(Color.ORANGERED);
                    }
                    setStyle("-fx-font-weight: bold;");
                    setAlignment(Pos.CENTER);
                }
            }
        };
    }

    private TableCell<Transaction, String> createTransactionStatusCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setStyle("");
                } else {
                    setText(item);
                    if ("Lunas".equalsIgnoreCase(item)) {
                        setTextFill(Color.GREEN);
                    } else {
                        setTextFill(Color.ORANGERED);
                    }
                    setStyle("-fx-font-weight: bold;");
                    setAlignment(Pos.CENTER);
                }
            }
        };
    }

    private <T> void setTransactionColumnAlignment(TableColumn<Transaction, T> column, Pos alignment) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setAlignment(alignment);
                }
            }
        });
    }
}