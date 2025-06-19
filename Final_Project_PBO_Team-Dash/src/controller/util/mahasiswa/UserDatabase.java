//package controller.util.mahasiswa;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class UserDatabase {
//
//    private static Map<String, String> users = new HashMap<>();
//
//    static {
//        users.put("user", "123");
//    }
//
//    public static boolean isValidUser(String username, String password) {
//        if (username == null || password == null) return false;
//        String storedPassword = users.get(username);
//        return storedPassword != null && storedPassword.equals(password);
//    }
//
//    public static boolean userExists(String username) {
//        return username != null && users.containsKey(username);
//    }
//
//    public static void addUser(String username, String password) {
//        if (username != null && password != null) {
//            users.put(username, password);
//        }
//    }
//
//    public static void removeUser(String username) {
//        if (username != null) {
//            users.remove(username);
//        }
//    }
//}
