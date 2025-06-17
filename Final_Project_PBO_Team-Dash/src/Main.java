//// Di dalam file Main.java
//import User.Admin;
//import User.Member;
//// User.User tidak lagi digunakan untuk list statis
//import User.Register;
//import Action.AdminAction;
//import Action.MemberAction;
//import SQL_DATA.UserDAO; // Import UserDAO
//
//import java.util.InputMismatchException;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        UserDAO userDAO = new UserDAO(); // Inisialisasi UserDAO
//
//        // Data awal pengguna tidak lagi ditambahkan ke ArrayList statis.
//        // Data ini seharusnya sudah ada di database Anda.
//        // Jika belum, Anda perlu script SQL untuk memasukkannya sekali,
//        // atau metode setup di aplikasi Anda yang dijalankan sekali.
//        // Contoh:
//        // Jika database kosong, Anda bisa menjalankan metode untuk menambah admin default:
//        // if (userDAO.isUserTableEmpty()) { // Anda perlu membuat metode ini di UserDAO
//        //     userDAO.createDefaultAdmin("admin", "passadmin", "Adinda Admin", "admin@example.com");
//        // }
//
//        boolean running = true;
//
//        while (running) {
//            try {
//                System.out.println("\n=== SELAMAT DATANG DI SISTEM PERPUSTAKAAN ===");
//                System.out.println("Pilih aksi:");
//                System.out.println("1. Login");
//                System.out.println("2. Registrasi Member Baru");
//                System.out.println("3. Keluar");
//                System.out.print("Masukkan pilihan: ");
//                int initialChoice = -1;
//
//                if (scanner.hasNextInt()) {
//                    initialChoice = scanner.nextInt();
//                } else {
//                    System.err.println("Input tidak valid. Harap masukkan angka (1-3).");
//                    scanner.nextLine(); // Membersihkan buffer
//                    continue;
//                }
//                scanner.nextLine(); // Membersihkan newline
//
//                switch (initialChoice) {
//                    case 1:
//                        System.out.println("\nLogin sebagai:");
//                        System.out.println("1. Admin");
//                        System.out.println("2. Member");
//                        System.out.print("Masukkan pilihan: ");
//                        int loginChoice = -1;
//
//                        if (scanner.hasNextInt()) {
//                            loginChoice = scanner.nextInt();
//                        } else {
//                            System.err.println("Input tidak valid untuk pilihan login. Harap masukkan angka (1-2).");
//                            scanner.nextLine(); // Membersihkan buffer
//                            continue;
//                        }
//                        scanner.nextLine(); // Membersihkan newline
//
//                        System.out.print("Username: ");
//                        String username = scanner.nextLine();
//                        System.out.print("Password: ");
//                        String password = scanner.nextLine(); // INGAT: implementasi hashing password!
//
//                        Object loggedInUser = userDAO.findUserByCredentials(username, password);
//
//                        if (loggedInUser != null) {
//                            if (loginChoice == 1 && loggedInUser instanceof Admin) {
//                                Admin loggedInAdmin = (Admin) loggedInUser;
//                                System.out.println("Login Admin berhasil! Selamat datang, " + loggedInAdmin.getName());
//                                AdminAction adminAction = new AdminAction();
//                                adminAction.processMenu(loggedInAdmin); // processMenu sekarang menerima Admin
//                            } else if (loginChoice == 2 && loggedInUser instanceof Member) {
//                                Member loggedInMember = (Member) loggedInUser;
//                                System.out.println("Login Member berhasil! Selamat datang, " + loggedInMember.getName());
//                                // Anda mungkin perlu memuat data pinjaman member di sini
//                                // LoanDAO loanDAO = new LoanDAO();
//                                // loggedInMember.setBorrowedLoans(loanDAO.getActiveLoansByUserId(loggedInMember.getUserId()));
//
//                                MemberAction memberAction = new MemberAction();
//                                memberAction.processMenu(loggedInMember); // processMenu sekarang menerima Member
//                            } else {
//                                // Role tidak sesuai dengan pilihan login
//                                System.out.println("Login gagal. Peran pengguna tidak sesuai dengan pilihan login Anda atau kredensial salah.");
//                            }
//                        } else {
//                            System.out.println("Login gagal. Username atau password salah.");
//                        }
//                        break;
//                    case 2:
//                        // Kelas Register akan menggunakan UserDAO untuk menyimpan data
//                        Register.performRegistration(scanner, userDAO); // Modifikasi performRegistration
//                        // Pesan sukses akan ditangani di dalam performRegistration
//                        break;
//                    case 3:
//                        System.out.println("Keluar dari sistem.");
//                        running = false;
//                        break;
//                    default:
//                        System.out.println("Pilihan tidak valid. Harap masukkan angka antara 1 dan 3.");
//                        break;
//                }
//            } catch (InputMismatchException e) {
//                System.err.println("Kesalahan Input Sistem: Format input tidak sesuai. Harap masukkan angka.");
//                if (scanner.hasNextLine()) scanner.nextLine();
//            } catch (IllegalStateException e) {
//                System.err.println("Kesalahan Sistem: Terjadi masalah dengan state aplikasi. " + e.getMessage());
//                running = false;
//            } catch (Exception e) {
//                System.err.println("Terjadi kesalahan umum yang tidak terduga dalam sistem: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//        System.out.println("\nTerima kasih telah menggunakan sistem perpustakaan.");
//        scanner.close();
//    }
//}