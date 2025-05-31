// File: src/SQL_DATA/TransactionDAO.java (atau package yang sesuai)
package SQL_DATA; // Ganti dengan package Anda jika berbeda

import Action.Transaction;
import SQL_DATA.DatabaseUtil; // Pastikan package DatabaseUtil benar
import User.Member;       // Pastikan package Member benar

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionDAO {

    /**
     * Menambahkan transaksi pembayaran denda baru ke database.
     * Menggunakan konstruktor pertama dari Transaction.java (yang men-generate ID dan Waktu).
     * @param transaction Objek Transaction yang akan disimpan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_id, user_id, transaction_date_time, amount_paid, fine_before_payment, fine_after_payment) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        boolean added = false;

        if (transaction.getMember() == null) {
            System.err.println("Error: Objek member dalam transaksi tidak boleh null saat menambah transaksi.");
            return false;
        }
        if (transaction.getMember().getUserId() == 0 && transaction.getMember().getUsername() == null) {
            System.err.println("Error: Member dalam transaksi tidak memiliki ID atau username yang valid.");
            return false;
        }


        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transaction.getTransactionId()); // ID dari objek Transaction
            pstmt.setInt(2, transaction.getMember().getUserId()); // Asumsi Member punya getUserId()
            pstmt.setTimestamp(3, Timestamp.valueOf(transaction.getTransactionDateTime())); // Waktu dari objek Transaction
            pstmt.setDouble(4, transaction.getAmountPaid());
            pstmt.setDouble(5, transaction.getFineBeforePayment());
            pstmt.setDouble(6, transaction.getFineAfterPayment());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                added = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menambah transaksi ke DB: " + e.getMessage());
            // e.printStackTrace(); // Untuk debugging
        }
        return added;
    }

    /**
     * Mengambil semua riwayat transaksi dari database.
     * Menggunakan konstruktor kedua dari Transaction.java (yang menerima semua parameter).
     * @return ArrayList berisi objek Transaction.
     */
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.transaction_id, t.user_id, t.transaction_date_time, " +
                "t.amount_paid, t.fine_before_payment, t.fine_after_payment, " +
                "u.user_id AS member_db_id, u.username AS member_username, u.password AS member_password_hash, " + // Ambil password juga jika Member membutuhkannya di konstruktor
                "u.full_name AS member_name, u.id_member AS member_student_id, " +
                "u.major AS member_major, u.email AS member_email, u.role AS member_role " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.user_id " + // Pastikan nama tabel pengguna adalah 'users'
                "ORDER BY t.transaction_date_time DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Membuat objek Member berdasarkan data dari join dengan tabel users
                Member member = new Member(
                        rs.getInt("member_db_id"), // Menggunakan user_id dari tabel users
                        rs.getString("member_username"),
                        rs.getString("member_password_hash"), // Diperlukan jika konstruktor Member butuh password
                        rs.getString("member_name"),
                        rs.getString("member_email"),
                        rs.getString("member_major"),
                        rs.getString("member_student_id")
                );

                // Menggunakan konstruktor baru dari Transaction yang menerima semua field
                Transaction transactionFromDb = new Transaction(
                        rs.getString("transaction_id"),
                        member,
                        rs.getTimestamp("transaction_date_time").toLocalDateTime(),
                        rs.getDouble("amount_paid"),
                        rs.getDouble("fine_before_payment"),
                        rs.getDouble("fine_after_payment")
                );
                transactions.add(transactionFromDb);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua transaksi dari DB: " + e.getMessage());
            // e.printStackTrace(); // Untuk debugging
        }
        return transactions;
    }
}