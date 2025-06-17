package controller.admin;

import Data.Loan;
import SQL_DATA.BookDAO;
import SQL_DATA.LoanDAO;
import SQL_DATA.UserDAO;
import User.Member;
import model.ReturnRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportsAndChartsController {

    @FXML private TableView<ReturnRecord> tableView;
    @FXML private TableColumn<ReturnRecord, Integer> colNo;
    @FXML private TableColumn<ReturnRecord, String> colStudentId;
    @FXML private TableColumn<ReturnRecord, String> colMemberName;
    @FXML private TableColumn<ReturnRecord, String> colBorrowDate;
    @FXML private TableColumn<ReturnRecord, String> colReturnDate;
    @FXML private TableColumn<ReturnRecord, Integer> colTotalBorrowed;
    @FXML private TableColumn<ReturnRecord, String> colFine;
    @FXML private TableColumn<ReturnRecord, String> colStatus;
    // Kolom Action sudah dihapus sesuai permintaan sebelumnya

    @FXML private TextField searchField;

    private final LoanDAO loanDAO = new LoanDAO();
    private final UserDAO userDAO = new UserDAO();
    private final BookDAO bookDAO = new BookDAO();

    private final ObservableList<ReturnRecord> reportList = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        setupTableColumns();
        loadReportsFromDatabase();
        setupSearchFilter();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupTableColumns() {
        // --- PERBAIKAN UTAMA: Mengatur perataan untuk semua kolom ---

        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        setColumnAlignment(colNo, Pos.CENTER);

        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        setColumnAlignment(colStudentId, Pos.CENTER);

        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        setColumnAlignment(colMemberName, Pos.CENTER); // Diubah menjadi tengah

        colTotalBorrowed.setCellValueFactory(new PropertyValueFactory<>("totalBorrowed"));
        setColumnAlignment(colTotalBorrowed, Pos.CENTER);

        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        setColumnAlignment(colFine, Pos.CENTER);

        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(column -> createStatusCell());

        colBorrowDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getBorrowDate().format(formatter)
        ));
        setColumnAlignment(colBorrowDate, Pos.CENTER);

        colReturnDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                (cellData.getValue().getActualReturnDate() != null) ?
                        cellData.getValue().getActualReturnDate().format(formatter) :
                        cellData.getValue().getReturnDate().format(formatter)
        ));
        setColumnAlignment(colReturnDate, Pos.CENTER);
    }

    /**
     * Metode helper baru untuk mengatur perataan kolom.
     */
    private <T> void setColumnAlignment(TableColumn<ReturnRecord, T> column, Pos alignment) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setAlignment(alignment);
                }
            }
        });
    }

    /**
     * Metode helper untuk kustomisasi sel Status.
     */
    private TableCell<ReturnRecord, String> createStatusCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Returned".equalsIgnoreCase(item)) {
                        setTextFill(Color.GREEN);
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setTextFill(Color.ORANGERED);
                        setStyle("-fx-font-weight: bold;");
                    }
                    setAlignment(Pos.CENTER);
                }
            }
        };
    }

    private void setupSearchFilter() {
        FilteredList<ReturnRecord> filteredData = new FilteredList<>(reportList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (record.getMemberName().toLowerCase().contains(lowerCaseFilter)) return true;
                if (record.getStudentId().toLowerCase().contains(lowerCaseFilter)) return true;
                if (record.getBookTitle().toLowerCase().contains(lowerCaseFilter)) return true;
                return false;
            });
        });
        SortedList<ReturnRecord> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void loadReportsFromDatabase() {
        reportList.clear();
        ArrayList<Loan> allLoans = loanDAO.getAllLoansWithDetails();
        int counter = 1;

        for (Loan loan : allLoans) {
            Member member = userDAO.findMemberById(loan.getUserId());
            if (member != null) {
                ReturnRecord record = new ReturnRecord(
                        counter++,
                        member.getStudentId(),
                        member.getName(),
                        loan.getBook().getTitle(),
                        loan.getBorrowDate(),
                        loan.getDueDate(),
                        "Rp " + String.format("%,.0f", loan.getFine()),
                        loan.getStatus(),
                        loan.getQuantity()
                );
                record.setUserId(member.getUserId());
                record.setBookCode(loan.getBook().getCode());
                record.setLoanId(loan.getLoanId());
                record.setActualReturnDate(loan.getActualReturnDate());
                reportList.add(record);
            }
        }
    }
}