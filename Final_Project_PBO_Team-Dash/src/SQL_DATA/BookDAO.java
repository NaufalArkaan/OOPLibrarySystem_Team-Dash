package SQL_DATA;

import Data.Book;
import ExceptionHandle.BookNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookDAO {

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("code"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("category"),
                rs.getBytes("image"),
                rs.getString("status"),
                rs.getInt("quantity")
        );
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, image = ?, status = ?, quantity = ? WHERE code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setBytes(4, book.getImage());
            pstmt.setString(5, book.getStatus());
            pstmt.setInt(6, book.getQuantity());
            pstmt.setString(7, book.getCode());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat mengupdate buku: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gagal mengupdate buku di database.", e);
        }
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua buku: " + e.getMessage());
            throw new RuntimeException("Gagal mengambil daftar buku dari database.", e);
        }
        return books;
    }

    public byte[] getImageByBookCode(String bookCode) {
        String sql = "SELECT image FROM books WHERE code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("image");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil gambar untuk buku " + bookCode + ": " + e.getMessage());
            throw new RuntimeException("Gagal mengambil gambar buku dari database.", e);
        }
        return null;
    }

    public Book findBookByCode(String bookCode) throws BookNotFoundException {
        String sql = "SELECT * FROM books WHERE code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBook(rs);
            } else {
                throw new BookNotFoundException("Buku dengan kode " + bookCode + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari buku berdasarkan kode: " + e.getMessage());
            throw new RuntimeException("Terjadi kesalahan database saat mencari buku.", e);
        }
    }

    public boolean updateBookQuantity(String bookCode, int quantity) {
        String sql = "UPDATE books SET quantity = ? WHERE code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, bookCode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat update kuantitas buku: " + e.getMessage());
            throw new RuntimeException("Gagal mengupdate kuantitas buku di database.", e);
        }
    }

    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (code, title, author, category, image, status, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getCode());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getCategory());
            pstmt.setBytes(5, book.getImage());
            pstmt.setString(6, book.getStatus());
            pstmt.setInt(7, book.getQuantity());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menambah buku: " + e.getMessage());
            if (e.getErrorCode() == 1062) {
                throw new RuntimeException("Kode buku (ISBN) sudah ada.", e);
            }
            throw new RuntimeException("Gagal menambah buku baru ke database.", e);
        }
    }

    public boolean updateBookStatus(String bookCode, String newStatus) {
        String sql = "UPDATE books SET status = ? WHERE code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, bookCode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat update status buku: " + e.getMessage());
            throw new RuntimeException("Gagal mengupdate status buku di database.", e);
        }
    }

    public boolean deleteBook(String bookCode) {
        String sql = "DELETE FROM books WHERE code = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookCode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus buku: " + e.getMessage());
            throw new RuntimeException("Gagal menghapus buku dari database. Mungkin ada data terkait.", e);
        }
    }

    public int getTotalBookTitlesCount() {
        String sql = "SELECT COUNT(*) FROM books";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghitung total judul buku: " + e.getMessage());
            throw new RuntimeException("Gagal menghitung total judul buku dari database.", e);
        }
        return 0;
    }

    public int getTotalBookCopiesCount() {
        String sql = "SELECT SUM(quantity) FROM books";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghitung total eksemplar buku: " + e.getMessage());
            throw new RuntimeException("Gagal menghitung total eksemplar buku dari database.", e);
        }
        return 0;
    }
}