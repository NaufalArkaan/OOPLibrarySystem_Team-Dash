// Contoh DatabaseUtil.java
package SQL_DATA; // Atau package yang sesuai

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/login_schema";
    private static final String USER = "root"; // Sesuai gambar image_ea8a7c.png
    private static final String PASSWORD = "informatika"; // Ganti dengan password root MySQL Anda

    public static Connection getConnection() throws SQLException {
        try {
            // Pastikan driver sudah ter-load jika menggunakan versi JDBC yang lebih lama
            // Class.forName("com.mysql.cj.jdbc.Driver"); // Untuk MySQL Connector/J 8+
        } catch (Exception e) {
            // Handle exception jika driver tidak ditemukan (jarang terjadi dengan JDBC 4.0+)
            // e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}