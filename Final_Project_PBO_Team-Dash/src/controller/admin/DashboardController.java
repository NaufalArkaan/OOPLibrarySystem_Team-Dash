package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import SQL_DATA.BookDAO;
import SQL_DATA.LoanDAO;

public class DashboardController {

    @FXML
    private Text totalBooksText;

    @FXML
    private Text borrowedBooksText;

    private final BookDAO bookDAO = new BookDAO();
    private final LoanDAO loanDAO = new LoanDAO();

    @FXML
    public void initialize() {

        int totalCopies = bookDAO.getTotalBookCopiesCount();
        int totalTitles = bookDAO.getTotalBookTitlesCount();
        int borrowedCount = loanDAO.getTotalActiveLoans();

        updateDashboard(totalCopies, totalTitles, borrowedCount);
    }

    public void updateDashboard(int totalCopies, int totalTitles, int borrowedCount) {
        totalBooksText.setText("📚 Total Buku: " + totalCopies + " (" + totalTitles + " Judul)");
        borrowedBooksText.setText("📤 Buku Dipinjam: " + borrowedCount);
    }
}