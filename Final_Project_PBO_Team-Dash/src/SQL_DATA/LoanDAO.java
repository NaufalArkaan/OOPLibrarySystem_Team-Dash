package SQL_DATA;

import Data.Book;
import Data.Loan;
import User.Member;
import ExceptionHandle.NoDataFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class LoanDAO {

    public boolean addLoan(Loan loan, Member member) {
        String sql = "INSERT INTO loans (user_id, book_code, book_tittle, Class, name, borrow_date, due_date, fine, quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, member.getUserId());
            pstmt.setString(2, loan.getBook().getCode());
            pstmt.setString(3, loan.getBook().getTitle());
            pstmt.setString(4, member.getMajor());
            pstmt.setString(5, member.getName());
            pstmt.setDate(6, java.sql.Date.valueOf(loan.getBorrowDate()));
            pstmt.setDate(7, java.sql.Date.valueOf(loan.getDueDate()));
            pstmt.setDouble(8, 0.0);
            pstmt.setInt(9, loan.getQuantity());
            pstmt.setString(10, "Borrowed");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal menambah data peminjaman ke database.", e);
        }
    }

    public boolean returnLoan(int loanId) {
        String sql = "UPDATE loans SET status = 'Returned', actual_return_date = ? WHERE loan_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, loanId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat update status pengembalian: " + e.getMessage());
            throw new RuntimeException("Gagal mengupdate status pengembalian di database.", e);
        }
    }

    public boolean updateLoanFine(int loanId, double fine) {
        String sql = "UPDATE loans SET fine = ? WHERE loan_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, fine);
            pstmt.setInt(2, loanId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat update denda pinjaman: " + e.getMessage());
            throw new RuntimeException("Gagal mengupdate denda pinjaman di database.", e);
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Book book = new Book(
                rs.getString("book_code"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("category"),
                rs.getBytes("image"),
                rs.getString("book_status"),
                0
        );
        Loan loan = new Loan(book, rs.getDate("borrow_date").toLocalDate());
        loan.setDueDate(rs.getDate("due_date").toLocalDate());
        loan.setUserId(rs.getInt("user_id"));
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setFine(rs.getDouble("fine"));
        loan.setQuantity(rs.getInt("quantity"));
        loan.setStatus(rs.getString("status"));

        Date returnDate = rs.getDate("actual_return_date");
        if(returnDate != null){
            loan.setActualReturnDate(returnDate.toLocalDate());
        }

        return loan;
    }

    public ArrayList<Loan> getActiveLoansByUserId(int userId) throws NoDataFoundException {
        ArrayList<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title, b.author, b.category, b.image, b.status AS book_status " +
                "FROM loans l JOIN books b ON l.book_code = b.code WHERE l.user_id = ? AND l.status = 'Borrowed'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil peminjaman aktif by User ID: " + e.getMessage());
            throw new RuntimeException("Terjadi kesalahan database saat mengambil peminjaman aktif.", e);
        }
        if (loans.isEmpty()) {
            throw new NoDataFoundException("Tidak ada buku yang sedang dipinjam oleh user ini.");
        }
        return loans;
    }

    public ArrayList<Loan> getReturnedLoansByUserId(int userId) throws NoDataFoundException {
        ArrayList<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title, b.author, b.category, b.image, b.status AS book_status " +
                "FROM loans l JOIN books b ON l.book_code = b.code WHERE l.user_id = ? AND l.status = 'Returned'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil peminjaman yang sudah kembali: " + e.getMessage());
            throw new RuntimeException("Terjadi kesalahan database saat mengambil peminjaman yang sudah kembali.", e);
        }
        if (loans.isEmpty()) {
            throw new NoDataFoundException("Belum ada riwayat pengembalian buku untuk user ini.");
        }
        return loans;
    }

    /**
     * METODE PENTING: Mengambil semua pinjaman yang berstatus 'Borrowed'.
     * Pastikan metode ini ada.
     */
    public ArrayList<Loan> getAllActiveLoansWithDetails() {
        String sql = "SELECT l.*, b.title, b.author, b.category, b.image, b.status AS book_status " +
                "FROM loans l JOIN books b ON l.book_code = b.code WHERE l.status = 'Borrowed'";
        ArrayList<Loan> allLoans = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allLoans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua peminjaman aktif: " + e.getMessage());
            throw new RuntimeException("Terjadi kesalahan database saat mengambil semua peminjaman aktif.", e);
        }
        return allLoans;
    }

    public ArrayList<Loan> getAllLoansWithDetails() {
        String sql = "SELECT l.*, b.title, b.author, b.category, b.image, b.status AS book_status " +
                "FROM loans l JOIN books b ON l.book_code = b.code ORDER BY l.borrow_date DESC";
        ArrayList<Loan> allLoans = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allLoans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua peminjaman: " + e.getMessage());
            throw new RuntimeException("Terjadi kesalahan database saat mengambil semua peminjaman.", e);
        }
        return allLoans;
    }

    public int getTotalActiveLoans() {
        String sql = "SELECT COUNT(*) FROM loans WHERE status = 'Borrowed'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total peminjaman aktif: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gagal menghitung total peminjaman aktif dari database.", e);
        }
        return 0;
    }
}