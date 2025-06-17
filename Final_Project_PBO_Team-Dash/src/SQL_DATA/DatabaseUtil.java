package SQL_DATA;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
    public static Connection getConnection() {
        String url = "jdbc:mysql://gondola.proxy.rlwy.net:43115/railway";
        String username = "root";
        String password = "WxwIBAASCcMswrEYozLWJIahjSfoAJUS";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("Gagal koneksi ke database: " + e.getMessage(), e);
        }
    }
}
