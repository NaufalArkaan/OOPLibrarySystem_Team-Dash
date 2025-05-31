// File: src/Data/LoanDAO.java (atau package yang sesuai, misal SQL_DATA)
package SQL_DATA; // Atau SQL_DATA jika Anda memindahkannya

import Data.Book;
import Data.Loan;
import SQL_DATA.DatabaseUtil; // Sesuaikan dengan lokasi DatabaseUtil Anda
// Jika Book dan Loan ada di package Data, import ini tidak wajib jika LoanDAO juga di Data
// import Data.Book;
// import Data.Loan;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Untuk getAllActiveLoans jika tidak pakai PreparedStatement
import java.time.LocalDate;
import java.util.ArrayList;

public class LoanDAO {

    /**
     * Menambahkan data peminjaman baru ke database.
     * @param loan Objek Loan yang akan ditambahkan.
     * @param userId ID pengguna (Member) yang meminjam.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addLoan(Loan loan, int userId) {
        String sql = "INSERT INTO loans (user_id, book_code, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        boolean added = false;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Minta generated keys untuk loan_id

            if (loan.getBook() == null) {
                System.err.println("Error: Objek buku dalam loan tidak boleh null saat menambah peminjaman.");
                return false;
            }

            pstmt.setInt(1, userId);
            pstmt.setString(2, loan.getBook().getCode());
            pstmt.setDate(3, java.sql.Date.valueOf(loan.getBorrowDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(loan.getDueDate()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Ambil loan_id yang di-generate jika perlu disimpan di objek Loan
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Jika Anda menambahkan field loanId ke kelas Loan:
                        // loan.setLoanId(generatedKeys.getInt(1));
                    }
                }
                added = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menambah data peminjaman: " + e.getMessage());
            e.printStackTrace(); // Untuk debugging
        }
        return added;
    }

    /**
     * Mengambil semua peminjaman aktif untuk seorang member berdasarkan userId.
     * @param userId ID member.
     * @return ArrayList berisi objek Loan.
     */
    public ArrayList<Loan> getActiveLoansByUserId(int userId) {
        ArrayList<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.book_code, l.borrow_date, l.due_date, " +
                "b.title AS book_title, b.author AS book_author, b.category AS book_category, b.quantity AS book_current_quantity " +
                "FROM loans l " +
                "JOIN books b ON l.book_code = b.code " +
                "WHERE l.user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("book_code"),
                        rs.getString("book_title"),
                        rs.getString("book_author"),
                        rs.getString("book_category"),
                        rs.getInt("book_current_quantity")
                );
                // Konstruktor Loan(Book book, LocalDate borrowDate)
                // akan otomatis menghitung dueDate.
                // Jika Anda ingin mengambil dueDate langsung dari DB (yang seharusnya konsisten):
                Loan loan = new Loan(book, rs.getDate("borrow_date").toLocalDate());
                loan.setDueDate(rs.getDate("due_date").toLocalDate()); // Override jika perlu

                // Jika Anda menambahkan field loanId ke kelas Loan dan ingin mengambilnya:
                // int loanId = rs.getInt("loan_id");
                // loan.setLoanId(loanId); // Perlu setter di kelas Loan

                loans.add(loan);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil peminjaman aktif member (ID: " + userId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return loans;
    }

    /**
     * Menghapus data peminjaman dari database (misalnya saat buku dikembalikan).
     * Menghapus berdasarkan user_id dan book_code.
     * Ini akan menghapus peminjaman paling awal jika ada beberapa buku yang sama dipinjam oleh user yang sama.
     * Untuk lebih spesifik, idealnya menggunakan loan_id.
     * @param userId ID pengguna.
     * @param bookCode Kode buku yang dikembalikan.
     * @return true jika berhasil menghapus setidaknya satu record, false jika gagal atau tidak ada yang dihapus.
     */
    public boolean removeLoan(int userId, String bookCode) {
        // Untuk menghapus peminjaman spesifik jika ada duplikasi, sebaiknya cari loan_id yang paling awal
        // atau gunakan kriteria lain. Jika hanya ada satu peminjaman aktif per buku per user, ini cukup.
        String sql = "DELETE FROM loans WHERE user_id = ? AND book_code = ? ORDER BY borrow_date ASC LIMIT 1";
        boolean removed = false;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, bookCode);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                removed = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghapus data peminjaman untuk user ID " + userId + " dan buku " + bookCode + ": " + e.getMessage());
            e.printStackTrace();
        }
        return removed;
    }

    /**
     * Menghapus data peminjaman dari database berdasarkan loan_id.
     * Ini cara yang lebih presisi untuk menghapus peminjaman tertentu.
     * @param loanId ID unik dari peminjaman.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean removeLoanById(int loanId) {
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        boolean removed = false;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loanId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                removed = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghapus data peminjaman dengan ID " + loanId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return removed;
    }


    /**
     * Mengambil semua peminjaman yang sedang aktif dari semua member.
     * Berguna untuk laporan admin.
     * @return ArrayList berisi objek Loan, masing-masing juga berisi detail Buku.
     * Anda mungkin juga perlu menyertakan detail Member jika diperlukan untuk tampilan.
     */
    public ArrayList<Loan> getAllActiveLoansWithDetails() {
        ArrayList<Loan> allLoans = new ArrayList<>();
        // Query ini mengambil detail pinjaman dan detail buku terkait.
        // Jika Anda perlu detail member juga, Anda perlu JOIN dengan tabel 'users'.
        String sql = "SELECT l.loan_id, l.user_id, l.book_code, l.borrow_date, l.due_date, " +
                "b.title AS book_title, b.author AS book_author, b.category AS book_category, b.quantity AS book_current_quantity " +
                // Tambahkan join dan kolom dari tabel users jika perlu detail member
                // ", u.full_name AS member_name, u.id_member AS member_student_id " +
                "FROM loans l " +
                "JOIN books b ON l.book_code = b.code ";
        // "JOIN users u ON l.user_id = u.user_id " + // Uncomment jika perlu info member
        // "ORDER BY l.due_date ASC"; // Urutkan sesuai kebutuhan

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement(); // Bisa juga PreparedStatement jika ada parameter
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("book_code"),
                        rs.getString("book_title"),
                        rs.getString("book_author"),
                        rs.getString("book_category"),
                        rs.getInt("book_current_quantity")
                );

                Loan loan = new Loan(book, rs.getDate("borrow_date").toLocalDate());
                loan.setDueDate(rs.getDate("due_date").toLocalDate());
                // int loanId = rs.getInt("loan_id");
                // loan.setLoanId(loanId); // Jika ada field dan setter loanId di Loan.java

                // Jika Anda join dengan tabel users untuk mendapatkan info Member:
                // Member member = new Member(rs.getInt("user_id"), ..., rs.getString("member_name"), ...);
                // Anda bisa membuat kelas wrapper baru yang menggabungkan Loan dan Member,
                // atau menambahkan field Member ke kelas Loan jika Loan selalu terkait Member.
                // Untuk saat ini, Loan hanya berisi informasi buku dan tanggal.

                allLoans.add(loan);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua peminjaman aktif: " + e.getMessage());
            e.printStackTrace();
        }
        return allLoans;
    }

    /**
     * Menghitung jumlah total peminjaman yang sedang aktif.
     * @return Jumlah peminjaman aktif.
     */
    public int getTotalActiveLoans() {
        String sql = "SELECT COUNT(*) AS total_active_loans FROM loans";
        int total = 0;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt("total_active_loans");
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total peminjaman aktif: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }
}