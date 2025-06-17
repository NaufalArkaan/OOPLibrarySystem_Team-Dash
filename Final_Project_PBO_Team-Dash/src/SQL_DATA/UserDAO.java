package SQL_DATA;

import User.Admin;
import User.Member;
import User.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {


    public boolean registerMember(Member member) {

        String sql = "INSERT INTO users (username, password, role, email, major, id_member) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getUsername());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, "Member");
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getMajor());
            pstmt.setString(6, member.getStudentId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("Registrasi gagal: Username '" + member.getUsername() + "' atau ID Member sudah ada.");
            } else {
                System.err.println("Error saat mendaftarkan member baru: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }


    public User findUserByCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
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
                String email = rs.getString("email");
                String major = rs.getString("major");
                String studentId = rs.getString("id_member");


                String name = dbUsername;

                if ("Admin".equalsIgnoreCase(role)) {
                    return new Admin(userId, dbUsername, dbPassword, name, email);
                } else if ("Member".equalsIgnoreCase(role)) {
                    return new Member(userId, dbUsername, dbPassword, name, email, major, studentId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari user berdasarkan kredensial: " + e.getMessage());
        }
        return null;
    }


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
                        rs.getString("username"),
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


    public Member findMemberById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND role = 'Member'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Member(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("major"),
                        rs.getString("id_member")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari member by ID: " + e.getMessage());
        }
        return null;
    }
}