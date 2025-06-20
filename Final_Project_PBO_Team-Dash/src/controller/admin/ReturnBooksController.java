package controller.admin;

import Data.Book;
import Data.Loan;
import SQL_DATA.BookDAO;
import SQL_DATA.LoanDAO;
import SQL_DATA.TransactionDAO;
import SQL_DATA.UserDAO;
import User.Member;
import Action.Transaction;
import model.ReturnRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class ReturnBooksController {

    @FXML private TableView<ReturnRecord> tableView;
    @FXML private TableColumn<ReturnRecord, Integer> colNo;
    @FXML private TableColumn<ReturnRecord, String> colStudentId;
    @FXML private TableColumn<ReturnRecord, String> colMemberName;
    @FXML private TableColumn<ReturnRecord, String> colBookTitle;
    @FXML private TableColumn<ReturnRecord, String> colBorrowDate;
    @FXML private TableColumn<ReturnRecord, String> colReturnDate;
    @FXML private TableColumn<ReturnRecord, String> colFine;
    @FXML private TableColumn<ReturnRecord, Void> colAction;
    @FXML private TextField searchField;

    private final LoanDAO loanDAO = new LoanDAO();
    private final UserDAO userDAO = new UserDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    private final ObservableList<ReturnRecord> returnList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initTableColumns();
        addReturnButtonToTable();
        refreshTableFromDatabase();
        setupSearchFilter();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initTableColumns() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDateFormatted"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDateFormatted"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));

        String centerStyle = "-fx-alignment: CENTER;";
        colNo.setStyle(centerStyle);
        colStudentId.setStyle(centerStyle);
        colMemberName.setStyle(centerStyle);
        colBookTitle.setStyle(centerStyle);
        colBorrowDate.setStyle(centerStyle);
        colReturnDate.setStyle(centerStyle);
        colFine.setStyle(centerStyle);
    }

    private void addReturnButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button returnBtn = new Button("📄 Return");
            private final Button fineBtn = new Button("💲 Fine");
            private final HBox actionButtons = new HBox(10, returnBtn, fineBtn);

            {
                actionButtons.setAlignment(Pos.CENTER);
                returnBtn.setStyle("-fx-background-color: #C3F5D5; -fx-text-fill: black; -fx-background-radius: 8;");
                fineBtn.setStyle("-fx-background-color: #FFDAB9; -fx-text-fill: black; -fx-background-radius: 8;");

                returnBtn.setOnAction(event -> {
                    ReturnRecord record = getTableView().getItems().get(getIndex());
                    if (record == null) return;
                    Loan loanToReturn = findLoanInDatabase(record.getLoanId());
                    if (loanToReturn == null) {
                        showErrorAlert("Error", "Data peminjaman tidak ditemukan.");
                        refreshTableFromDatabase();
                        return;
                    }
                    processReturn(loanToReturn);
                });

                fineBtn.setOnAction(event -> {
                    ReturnRecord record = getTableView().getItems().get(getIndex());
                    if (record == null) return;
                    openFineWindow(record);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    ReturnRecord record = getTableRow().getItem();
                    String fineText = record.getFine();
                    double fineValue = 0;
                    try {
                        fineValue = Double.parseDouble(fineText.replace(",", "").replaceAll("[^\\d.]", ""));
                    } catch (NumberFormatException e) {
                        System.err.println("Gagal parse nilai denda dari string: " + fineText + ". Error: " + e.getMessage());
                    }

                    returnBtn.setDisable(fineValue > 0);

                    setGraphic(actionButtons);
                }
            }
        });
    }

    private void openFineWindow(ReturnRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/AddFine.fxml"));
            Parent page = loader.load();

            AddFineController controller = loader.getController();
            controller.setLoanData(record);

            Stage fineStage = new Stage();
            fineStage.setTitle("Manage Fine");
            fineStage.initModality(Modality.APPLICATION_MODAL);
            fineStage.setScene(new Scene(page));
            fineStage.showAndWait();

            refreshTableFromDatabase();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Could not open the fine management window: " + e.getMessage());
        }
    }

    private Loan findLoanInDatabase(int loanId) {
        try {
            ArrayList<Loan> allLoans = loanDAO.getAllActiveLoansWithDetails();
            for(Loan loan : allLoans) {
                if(loan.getLoanId() == loanId) {
                    return loan;
                }
            }
        }
        catch (RuntimeException e) {
            showErrorAlert("Error Database", "Gagal mencari data peminjaman di database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void processReturn(Loan loan) {
        try {
            boolean success = loanDAO.returnLoan(loan.getLoanId());
            if(success) {
                Book returnedBook = bookDAO.findBookByCode(loan.getBook().getCode());
                if (returnedBook != null) {
                    int newStock = returnedBook.getQuantity() + loan.getQuantity();
                    bookDAO.updateBookQuantity(returnedBook.getCode(), newStock);
                    if (newStock > 0) {
                        bookDAO.updateBookStatus(returnedBook.getCode(), "Available");
                    } else {
                        bookDAO.updateBookStatus(returnedBook.getCode(), "Borrowed");
                    }
                } else {
                    showErrorAlert("Peringatan", "Buku yang dikembalikan tidak ditemukan di master data buku.");
                }
                showInfoAlert("Pengembalian Sukses", "Buku \"" + loan.getBook().getTitle() + "\" telah berhasil dikembalikan.");
                refreshTableFromDatabase();
            } else {
                showErrorAlert("Gagal", "Gagal memproses pengembalian di database.");
            }
        } catch (RuntimeException e) {
            showErrorAlert("Error Database", "Terjadi kesalahan saat memproses pengembalian: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorAlert("Error", "Terjadi kesalahan tak terduga saat memperbarui stok buku: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshTableFromDatabase() {
        returnList.clear();
        try {
            ArrayList<Loan> allActiveLoans = loanDAO.getAllActiveLoansWithDetails();
            int counter = 1;

            for (Loan loan : allActiveLoans) {
                Member member = userDAO.findMemberById(loan.getUserId());
                if (member != null) {
                    double currentFine = loan.getFine() > 0 ? loan.getFine() : loan.calculateFine();

                    ReturnRecord record = new ReturnRecord(
                            counter++,
                            member.getStudentId(),
                            member.getName(),
                            loan.getBook().getTitle(),
                            loan.getBorrowDate(),
                            loan.getDueDate(),
                            "Rp " + String.format("%,.0f", currentFine),
                            "Belum Kembali",
                            loan.getQuantity()
                    );
                    record.setUserId(member.getUserId());
                    record.setBookCode(loan.getBook().getCode());
                    record.setLoanId(loan.getLoanId());
                    returnList.add(record);
                } else {
                    System.err.println("Peringatan: Member dengan ID " + loan.getUserId() + " tidak ditemukan untuk peminjaman " + loan.getLoanId());
                }
            }
            tableView.setItems(returnList);
        } catch (RuntimeException e) {
            showErrorAlert("Error Database", "Gagal memuat data pengembalian dari database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        FilteredList<ReturnRecord> filteredData = new FilteredList<>(returnList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (record.getStudentId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (record.getMemberName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (record.getBookTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<ReturnRecord> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }


    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}