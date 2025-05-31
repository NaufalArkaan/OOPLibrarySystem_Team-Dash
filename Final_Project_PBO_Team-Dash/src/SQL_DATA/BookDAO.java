package SQL_DATA;

// File: src/Data/BookDAO.java (atau package yang sesuai)

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Data.Book;
import SQL_DATA.DatabaseUtil;

public class BookDAO {

    /**
     * Menambahkan buku baru ke database.
     * @param book Objek Book yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addBook(Book book) {
        // SQL: INSERT INTO books (code, title, author, category, quantity) VALUES (?, ?, ?, ?, ?)
        String sql = "INSERT INTO books (code, title, author, category, quantity) VALUES (?, ?, ?, ?, ?)";
        boolean added = false;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getCode());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getCategory());
            pstmt.setInt(5, book.getQuantity());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                added = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menambah buku: " + e.getMessage());
            // Jika error karena duplikasi kode (PK), tangani dengan lebih spesifik jika perlu
        }
        return added;
    }

    /**
     * Mengambil semua buku dari database.
     * @return ArrayList berisi objek Book.
     */
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        // SQL: SELECT * FROM books
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua buku: " + e.getMessage());
        }
        return books;
    }

    /**
     * Mencari buku berdasarkan kode (ISBN).
     * @param bookCode Kode buku.
     * @return Objek Book jika ditemukan, null jika tidak.
     */
    public Book findBookByCode(String bookCode) {
        // SQL: SELECT * FROM books WHERE code = ?
        String sql = "SELECT * FROM books WHERE code = ?";
        Book book = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book(
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("quantity")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari buku berdasarkan kode: " + e.getMessage());
        }
        return book;
    }

    /**
     * Mengupdate data buku yang sudah ada.
     * @param book Objek Book dengan data yang sudah diperbarui.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateBook(Book book) {
        // SQL: UPDATE books SET title = ?, author = ?, category = ?, quantity = ? WHERE code = ?
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, quantity = ? WHERE code = ?";
        boolean updated = false;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setInt(4, book.getQuantity());
            pstmt.setString(5, book.getCode());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                updated = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengupdate buku: " + e.getMessage());
        }
        return updated;
    }

    /**
     * Mengupdate kuantitas (stok) buku tertentu.
     * @param bookCode Kode buku.
     * @param newQuantity Kuantitas baru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateBookQuantity(String bookCode, int newQuantity) {
        String sql = "UPDATE books SET quantity = ? WHERE code = ?";
        boolean updated = false;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setString(2, bookCode);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                updated = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengupdate kuantitas buku: " + e.getMessage());
        }
        return updated;
    }


    /**
     * Menghapus buku dari database berdasarkan kodenya.
     * @param bookCode Kode buku yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deleteBook(String bookCode) {
        // SQL: DELETE FROM books WHERE code = ?
        // Perhatian: Jika ada foreign key constraint di tabel 'loans' yang merujuk ke 'books',
        // penghapusan mungkin gagal jika buku sedang dipinjam (tergantung setting ON DELETE).
        String sql = "DELETE FROM books WHERE code = ?";
        boolean deleted = false;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookCode);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                deleted = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghapus buku: " + e.getMessage());
            // Tangani error constraint jika perlu
        }
        return deleted;
    }
}
