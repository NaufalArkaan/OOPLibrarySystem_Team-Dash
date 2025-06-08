package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import controller.model.SharedBookData;

public class DashboardController {

    @FXML
    private Text totalBooksText;

    @FXML
    private Text borrowedBooksText;

    @FXML
    public void initialize() {
        // Ambil data dari SharedBookData dan tampilkan
        int totalBooks = SharedBookData.getTotalBooks();
        int borrowedBooks = SharedBookData.getBorrowedBooksCount();
        updateDashboard(totalBooks, borrowedBooks);
    }

    // Digunakan saat ingin update manual dari luar
    public void updateDashboard(int totalBooks, int borrowedBooks) {
        totalBooksText.setText("📚 Total Books: " + totalBooks);
        borrowedBooksText.setText("📚 Borrowed: " + borrowedBooks);
    }
}
