//// File: src/User/Register.java
//package User;
//
//import SQL_DATA.UserDAO; // Import UserDAO
//import java.util.Scanner;
//
//public class Register {
//
//    /**
//     * Menangani proses registrasi pengguna baru (Member).
//     * Metode ini akan meminta detail identitas pengguna, melakukan validasi,
//     * dan jika berhasil, membuat objek Member baru lalu menambahkannya ke database melalui UserDAO.
//     * @param scanner Objek Scanner untuk input.
//     * @param userDAO Objek UserDAO untuk interaksi database.
//     */
//    public static void performRegistration(Scanner scanner, UserDAO userDAO) { // Terima UserDAO
//        System.out.println("\n=== MENU REGISTRASI PENGGUNA BARU ===");
//
//        System.out.print("Masukkan username: ");
//        String newUsername = scanner.nextLine();
//
//        // Cek duplikasi username melalui UserDAO (UserDAO.findUserByUsername() bisa dimodifikasi)
//        // atau UserDAO.registerMember() sudah menghandle ini.
//        // Jika UserDAO.registerMember() mengembalikan false karena username sudah ada, itu sudah cukup.
//
//        System.out.print("Masukkan password: ");
//        String newPassword = scanner.nextLine(); // INGAT: Implementasi hashing password!
//
//        System.out.print("Masukkan nama lengkap: ");
//        String name = scanner.nextLine();
//
//        System.out.print("Masukkan email: ");
//        String email = scanner.nextLine();
//
//        System.out.print("Masukkan jurusan (major): ");
//        String major = scanner.nextLine();
//
//        System.out.print("Masukkan Student ID: ");
//        String studentId = scanner.nextLine();
//
//        // Membuat objek Member baru. ID akan di-generate oleh DB.
//        // Konstruktor Member kedua (tanpa userId) digunakan.
//        Member newMember = new Member(newUsername, newPassword, name, email, major, studentId);
//
//        if (userDAO.registerMember(newMember)) { // Panggil metode registerMember dari UserDAO
//            System.out.println("Registrasi berhasil untuk member: " + newUsername + " (Nama: " + name + ")");
//            System.out.println("Silakan login untuk melanjutkan.");
//        } else {
//            // Pesan error sudah ditangani di dalam userDAO.registerMember() atau bisa ditambahkan di sini
//            System.out.println("Registrasi gagal. Username mungkin sudah digunakan atau terjadi kesalahan lain.");
//        }
//    }
//}