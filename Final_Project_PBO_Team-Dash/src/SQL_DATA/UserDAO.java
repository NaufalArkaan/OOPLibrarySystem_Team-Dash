package SQL_DATA;

import User.Admin;
import User.Member;
import User.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDAO {

    /**
     * Mencari pengguna berdasarkan username dan password untuk proses login.
     * Mengembalikan objek Admin atau Member tergantung pada 'role' di database.
     */
    public User findUserByCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        User user = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                String role = rs.getString("role");
                String name = rs.getString("username"); // Asumsi nama diambil dari username
                String email = rs.getString("email");
                String major = rs.getString("major");
                String studentId = rs.getString("id_member");

                if ("Admin".equalsIgnoreCase(role)) {
                    // Konstruktor Admin tidak butuh major dan studentId
                    user = new Admin(userId, dbUsername, dbPassword, name, email);
                } else if ("Member".equalsIgnoreCase(role)) {
                    user = new Member(userId, dbUsername, dbPassword, name, email, major, studentId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari user berdasarkan kredensial: " + e.getMessage());
        }
        return user;
    }

    /**
     * Mengambil semua pengguna dengan role 'Member' dari database.
     */
    public ArrayList<Member> getAllMembers() {
        ArrayList<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'Member'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("username"), // Nama diambil dari kolom username
                        rs.getString("email"),
                        rs.getString("major"),
                        rs.getString("id_member")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua member: " + e.getMessage());
        }
        return members;
    }

    /**
     * METODE BARU: Mencari satu member spesifik berdasarkan user_id-nya.
     * Digunakan oleh ReturnBooksController untuk mendapatkan detail peminjam.
     */
    public Member findMemberById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND role = 'Member'";
        Member member = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new Member(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("username"), // Asumsi nama diambil dari username
                        rs.getString("email"),
                        rs.getString("major"),
                        rs.getString("id_member")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari member by ID: " + e.getMessage());
        }
        return member;
    }
}