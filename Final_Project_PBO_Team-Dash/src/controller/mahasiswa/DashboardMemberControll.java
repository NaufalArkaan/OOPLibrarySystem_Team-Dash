package controller.mahasiswa;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.Arrays;
import java.util.List;

public class DashboardMemberControll {
    @FXML
    private ListView<String> borrowedBooksList;

    @FXML
    private ListView<String> returnBook;

    @FXML
    public void initialize() {
        // Isi ListView dengan data awal
        List<String> borrowedBooks = Arrays.asList(
                "1. Laskar Pelangi - Andrea Hirata",
                "2. Bumi Manusia - Pramoedya Ananta Toer",
                "3. Filosofi Teras - Henry Manampiring"
        );
        borrowedBooksList.getItems().addAll(borrowedBooks);

        List<String> booksToReturn = Arrays.asList(
                "1. Atomic Habits - James Clear",
                "2. Rich Dad Poor Dad - Robert Kiyosaki"
        );
        returnBook.getItems().addAll(booksToReturn);
    }
}
