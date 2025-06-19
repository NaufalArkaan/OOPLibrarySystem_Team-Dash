package controller.mahasiswa;

import Data.Loan;
import SQL_DATA.LoanDAO;
import User.Member;
import User.User;
import controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ExceptionHandle.NoDataFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DashboardMemberControll {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> borrowedBooksList;
    @FXML private ListView<String> returnBook;

    private Member currentMember;
    private final LoanDAO loanDAO = new LoanDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        User loggedInUser = LoginController.loggedInUser;
        if (loggedInUser instanceof Member) {
            this.currentMember = (Member) loggedInUser;
            tampilkanUsername();
            loadDashboardLists();
        }
    }

    private void tampilkanUsername() {
        if (currentMember != null) {
            welcomeLabel.setText("Selamat Datang, " + currentMember.getName());
        }
    }

    private void loadDashboardLists() {
        borrowedBooksList.getItems().clear();
        returnBook.getItems().clear();

        if (currentMember == null) return;


        try {
            ArrayList<Loan> activeLoans = loanDAO.getActiveLoansByUserId(currentMember.getUserId());
            for (Loan loan : activeLoans) {
                String entry = loan.getBook().getTitle() + " (x" + loan.getQuantity() + ") - Jatuh Tempo: " + loan.getDueDate().format(formatter);
                borrowedBooksList.getItems().add(entry);
            }
        } catch (NoDataFoundException e) {
            borrowedBooksList.getItems().add("Tidak ada buku yang sedang dipinjam.");
        } catch (RuntimeException e) {
            borrowedBooksList.getItems().add("Error memuat daftar buku pinjaman: " + e.getMessage());
            e.printStackTrace();
        }


        try {
            ArrayList<Loan> returnedLoans = loanDAO.getReturnedLoansByUserId(currentMember.getUserId());
            for (Loan loan : returnedLoans) {
                String returnDateStr = (loan.getActualReturnDate() != null) ? loan.getActualReturnDate().format(formatter) : "N/A";
                String entry = loan.getBook().getTitle() + " (x" + loan.getQuantity() + ") - Dikembalikan: " + returnDateStr;
                returnBook.getItems().add(entry);
            }
        } catch (NoDataFoundException e) {
            returnBook.getItems().add("Belum ada riwayat pengembalian buku.");
        } catch (RuntimeException e) {
            returnBook.getItems().add("Error memuat riwayat pengembalian buku: " + e.getMessage());
            e.printStackTrace();
        }
    }
}