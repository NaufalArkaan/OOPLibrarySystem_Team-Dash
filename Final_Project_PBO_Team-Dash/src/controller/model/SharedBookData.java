package controller.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Kelas utilitas untuk menyimpan dan mengelola data buku secara global.
 */
public final class SharedBookData {

    // List buku yang dapat di-observe untuk perubahan data
    private static final ObservableList<Book> books = FXCollections.observableArrayList();

    static {
        // Data dummy awal
        books.add(new Book(1, "Otodidak Desain dan Pemrograman Website", "Jubilee Enterprise", "Fiction", "0000", "Borrowed"));
        books.add(new Book(2, "World History Sejarah Dunia Lengkap", "Hutton Webster PHD", "Fiction", "0001", "Available"));
        books.add(new Book(3, "Pulang", "Tere Liye", "Non-Fiction", "0002", "Borrowed"));
    }

    private SharedBookData() {
        // Private constructor supaya kelas tidak bisa diinstansiasi
    }

    public static ObservableList<Book> getBooks() {
        return books;
    }

    public static int getTotalBooks() {
        return books.size();
    }

    public static int getBorrowedBooksCount() {
        return (int) books.stream()
                .filter(book -> "Borrowed".equalsIgnoreCase(book.getStatus()))
                .count();
    }
}
