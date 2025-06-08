package controller.util.mahasiswa;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {

    // username -> password (hanya user biasa)
    private static Map<String, String> users = new HashMap<>();

    static {
        // Contoh data user awal (user biasa)
        users.put("user", "123");
    }

    // Validasi login user biasa
    public static boolean isValidUser(String username, String password) {
        if (username == null || password == null) return false;
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    // Cek apakah user ada
    public static boolean userExists(String username) {
        return username != null && users.containsKey(username);
    }

    // Tambah user baru
    public static void addUser(String username, String password) {
        if (username != null && password != null) {
            users.put(username, password);
        }
    }

    // Hapus user
    public static void removeUser(String username) {
        if (username != null) {
            users.remove(username);
        }
    }
}
