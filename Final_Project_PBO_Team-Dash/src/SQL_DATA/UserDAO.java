// File: src/Data/UserDAO.java (atau package yang sesuai, misal Dao)
package SQL_DATA; // Atau package Dao

import User.Admin;
import User.Member;
import User.User; // Mungkin tidak selalu dibutuhkan jika kita selalu mengembalikan Admin/Member
import SQL_DATA.DatabaseUtil; // Asumsi Anda memiliki kelas ini

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    /**
     * Mencari pengguna berdasarkan username dan password.
     * Metode ini bisa mengembalikan Admin atau Member.
     * @param username Username pengguna.
     * @param password Password pengguna (sebaiknya password yang sudah di-hash di aplikasi dan dicocokkan dengan hash di DB).
     * @return Objek User (Admin atau Member) jika ditemukan dan kredensial valid, null jika tidak.
     */
    public User findUserByCredentials(String username, String password) {
        // SQL: SELECT * FROM users WHERE username = ? AND password = ?
        // (CATATAN: Menyimpan dan membandingkan password plain text tidak aman. Gunakan hashing!)
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?"; // 'users' adalah nama tabel pengguna Anda
        User user = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Untuk perbandingan hash, logikanya akan berbeda

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password"); // Seharusnya hash password
                String role = rs.getString("role");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String major = rs.getString("major");
                String studentId = rs.getString("id_member");

                if ("Admin".equalsIgnoreCase(role)) {
                    user = new Admin(userId, dbUsername, dbPassword, fullName, email);
                } else if ("Member".equalsIgnoreCase(role)) {
                    user = new Member(userId, dbUsername, dbPassword, fullName, email, major, studentId);
                    // Anda mungkin perlu mengambil data fee dan borrowedLoans untuk member di sini
                    // atau dalam metode terpisah setelah login berhasil.
                    // ((Member) user).setFee(loadMemberFee(userId, conn));
                    // ((Member) user).setBorrowedLoans(new LoanDAO().getLoansByUserId(userId, conn));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari user berdasarkan kredensial: " + e.getMessage());
            // e.printStackTrace(); // Untuk debugging
        }
        return user;
    }

    /**
     * Mendaftarkan pengguna baru (Member) ke database.
     * @param member Objek Member yang akan didaftarkan.
     * @return true jika registrasi berhasil, false jika gagal (misalnya username sudah ada).
     */
    public boolean registerMember(Member member) {
        // SQL: INSERT INTO users (full_name, id_member, major, password, email, username, role)
        // VALUES (?, ?, ?, ?, ?, ?, 'Member')
        String sqlCheckUser = "SELECT user_id FROM users WHERE username = ?";
        String sqlInsert = "INSERT INTO users (full_name, id_member, major, password, email, username, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean registered = false;

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Cek apakah username sudah ada
            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheckUser)) {
                pstmtCheck.setString(1, member.getUsername());
                ResultSet rsCheck = pstmtCheck.executeQuery();
                if (rsCheck.next()) {
                    System.err.println("Username '" + member.getUsername() + "' sudah digunakan.");
                    return false; // Username sudah ada
                }
            }

            // Jika username belum ada, lakukan insert
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                pstmtInsert.setString(1, member.getName());
                pstmtInsert.setString(2, member.getStudentId());
                pstmtInsert.setString(3, member.getMajor());
                pstmtInsert.setString(4, member.getPassword()); // Sebaiknya hash password
                pstmtInsert.setString(5, member.getEmail());
                pstmtInsert.setString(6, member.getUsername());
                pstmtInsert.setString(7, "Member"); // Role di-set sebagai Member

                int affectedRows = pstmtInsert.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            member.setUserId(generatedKeys.getInt(1)); // Set ID yang di-generate ke objek member
                        }
                    }
                    registered = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat registrasi member: " + e.getMessage());
            // e.printStackTrace();
        }
        return registered;
    }

    /**
     * Mengambil semua member dari database.
     * @return ArrayList berisi objek Member.
     */
    public java.util.ArrayList<Member> getAllMembers() {
        java.util.ArrayList<Member> members = new java.util.ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'Member'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"), // Sebaiknya tidak diambil atau digunakan untuk tampilan
                        rs.getString("full_name"),
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
     * Mengupdate total denda seorang member di database.
     * Anda mungkin perlu kolom 'fee' di tabel 'users' atau menghitungnya dari tabel 'loans'.
     * Contoh ini mengasumsikan ada kolom 'fee'.
     * @param userId ID member.
     * @param newFee Jumlah denda baru.
     * @return true jika update berhasil, false jika gagal.
     */
    public boolean updateMemberFee(int userId, double newFee) {
        // SQL: UPDATE users SET fee = ? WHERE user_id = ? AND role = 'Member'
        // (Ini memerlukan kolom 'fee' di tabel 'users')
        // Jika 'fee' tidak ada di tabel 'users', logika ini perlu diubah.
        // Mungkin denda dihitung on-the-fly atau saat pengembalian buku.
        // Untuk contoh, kita asumsikan ada kolom 'fee'
        String sql = "UPDATE users SET fee = ? WHERE user_id = ? AND role = 'Member'"; // Contoh, sesuaikan jika tidak ada kolom fee
        boolean updated = false;

        // Cek apakah tabel users memiliki kolom 'fee'. Jika tidak, metode ini perlu penyesuaian.
        // Misalnya, jika fee disimpan di Member object saja dan direfleksikan dari perhitungan Loan.
        // Untuk saat ini, kita asumsikan ada.

        /*
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newFee);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                updated = true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat update denda member: " + e.getMessage());
        }
        */
        System.out.println("DEBUG: UserDAO.updateMemberFee dipanggil untuk userId " + userId + " dengan fee " + newFee + ". Implementasi DB pending.");
        // Jika tidak ada kolom 'fee' di DB, Anda tidak perlu metode ini,
        // karena 'fee' di objek Member akan dikelola di memori dan dihitung dari Loan.
        // Pembayaran denda akan dicatat di tabel 'transactions'.
        return true; // Sementara
    }


    // Anda bisa menambahkan metode lain seperti:
    // - findUserById(int userId)
    // - updateUser(User user)
    // - deleteUser(int userId)
}