package SQL_DATA;

import Action.Transaction;
import User.Member;
import java.sql.*;
import java.util.ArrayList;

public class TransactionDAO {


    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (userid, transaction_date_time, amount_paid, fine_before_payment, fine_after_payment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transaction.getMember().getUserId());
            pstmt.setTimestamp(2, Timestamp.valueOf(transaction.getTransactionDateTime()));
            pstmt.setDouble(3, transaction.getAmountPaid());
            pstmt.setDouble(4, transaction.getFineBeforePayment());
            pstmt.setDouble(5, transaction.getFineAfterPayment());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saat menyimpan transaksi ke DB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.idtransactions AS transaction_id, t.userid, t.transaction_date_time, " +
                "t.amount_paid, t.fine_before_payment, t.fine_after_payment, " +
                "u.user_id AS member_db_id, u.username AS member_username, u.password AS member_password_hash, " +
                "u.username AS member_name, u.id_member AS member_student_id, " +
                "u.major AS member_major, u.email AS member_email, u.role AS member_role " +
                "FROM transactions t " +
                "JOIN users u ON t.userid = u.user_id " +
                "ORDER BY t.transaction_date_time DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("member_db_id"),
                        rs.getString("member_username"),
                        rs.getString("member_password_hash"),
                        rs.getString("member_name"),
                        rs.getString("member_email"),
                        rs.getString("member_major"),
                        rs.getString("member_student_id")
                );

                Transaction transactionFromDb = new Transaction(
                        rs.getString("transaction_id"), // Menggunakan alias 'transaction_id'
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
            e.printStackTrace();
        }
        return transactions;
    }
}