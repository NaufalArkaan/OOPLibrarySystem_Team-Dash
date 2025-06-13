package controller.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Kelas utilitas untuk menyimpan dan mengelola data buku secara global.
 */
public final class SharedBookData {

    // Daftar buku yang dapat di-observe untuk perubahan data
    private static final ObservableList<Book> books = FXCollections.observableArrayList();

    // Field untuk menyimpan buku yang dipilih saat klik "Pinjam"
    private static Book selectedBook = null;

    static {
        // Data dummy awal (dengan imagePath, opsional)
        books.add(new Book(1, "Otodidak Desain dan Pemrograman Website", "Jubilee Enterprise", "Fiction", "0000", "Borrowed", "buku.png"));
        books.add(new Book(2, "World History Sejarah Dunia Lengkap", "Hutton Webster PHD", "Fiction", "0001", "Available", "buku1.png"));
        books.add(new Book(3, "Pulang", "Tere Liye", "Non-Fiction", "0002", "Available", "buku2.png"));
        books.add(new Book(4, "Hukum Pidana", "Dr.H. Ishaq, S.H., M.Hum.", "Fiction", "0003", "Available", "buku3.png"));
    }

    private SharedBookData() {
        // Mencegah instansiasi
    }

    // Getter dan Setter untuk selectedBook
    public static void setSelectedBook(Book book) {
        selectedBook = book;
    }

    public static Book getSelectedBook() {
        return selectedBook;
    }

    // Mendapatkan semua buku
    public static ObservableList<Book> getBooks() {
        return books;
    }

    // Menambahkan buku baru
    public static void addBook(Book book) {
        books.add(book);
    }

    // Menghapus buku berdasarkan ID
    public static void removeBookById(int id) {
        books.removeIf(book -> book.getNo() == id);
    }

    // Mengupdate status buku berdasarkan ID
    public static void updateBookStatus(int id, String newStatus) {
        for (Book book : books) {
            if (book.getNo() == id) {
                book.setStatus(newStatus);
                break;
            }
        }
    }

    // Mengupdate seluruh data buku berdasarkan ID
    public static void updateBookData(int id, Book newBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getNo() == id) {
                books.set(i, newBook);
                break;
            }
        }
    }

    // Mendapatkan jumlah total buku
    public static int getTotalBooks() {
        return books.size();
    }

    // Mendapatkan jumlah buku yang dipinjam
    public static int getBorrowedBooksCount() {
        return (int) books.stream()
                .filter(book -> "Borrowed".equalsIgnoreCase(book.getStatus()))
                .count();
    }

    // Mendapatkan jumlah buku yang tersedia
    public static int getAvailableBooksCount() {
        return (int) books.stream()
                .filter(book -> "Available".equalsIgnoreCase(book.getStatus()))
                .count();
    }

    // Mencari buku berdasarkan judul
    public static ObservableList<Book> searchBooksByTitle(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return books.filtered(book -> book.getTitle().toLowerCase().contains(lowerKeyword));
    }

    // Cek apakah buku tersedia berdasarkan judul
    public static boolean isBookAvailable(String title) {
        return books.stream()
                .anyMatch(book -> book.getTitle().equalsIgnoreCase(title)
                        && book.getStatus().equalsIgnoreCase("Available"));
    }

    // Tandai buku sebagai "Borrowed"
    public static void markAsBorrowed(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)
                    && book.getStatus().equalsIgnoreCase("Available")) {
                book.setStatus("Borrowed");
                break;
            }
        }
    }

    // Tandai buku sebagai "Available" saat dikembalikan
    public static void markAsReturned(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)
                    && book.getStatus().equalsIgnoreCase("Borrowed")) {
                book.setStatus("Available");
                break;
            }
        }
    }

    // Dapatkan buku berdasarkan ID
    public static Book getBookById(int id) {
        for (Book book : books) {
            if (book.getNo() == id) {
                return book;
            }
        }
        return null;
    }
}
