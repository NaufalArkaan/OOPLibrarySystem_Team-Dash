//// File: src/User/Register.java
//package User;
//
//import SQL_DATA.UserDAO;
//import java.util.Scanner;
//
//public class Register {
//
//
//    public static void performRegistration(Scanner scanner, UserDAO userDAO) {
//        System.out.println("\n=== MENU REGISTRASI PENGGUNA BARU ===");
//
//        System.out.print("Masukkan username: ");
//        String newUsername = scanner.nextLine();
//
//
//        System.out.print("Masukkan password: ");
//        String newPassword = scanner.nextLine();
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
//
//        Member newMember = new Member(newUsername, newPassword, name, email, major, studentId);
//
//        if (userDAO.registerMember(newMember)) {
//            System.out.println("Registrasi berhasil untuk member: " + newUsername + " (Nama: " + name + ")");
//            System.out.println("Silakan login untuk melanjutkan.");
//        } else {
//            System.out.println("Registrasi gagal. Username mungkin sudah digunakan atau terjadi kesalahan lain.");
//        }
//    }
//}