// File: src/User/Admin.java
package User;

public class Admin extends User {


    public Admin(int userId, String username, String password, String name, String email) {
        super(userId, username, password, "Admin", name, email, null, null);
    }


    public Admin(String username, String password, String name, String email) {
        super(username, password, "Admin", name, email, null, null);
    }


    public void displayMenu() {
        System.out.println("\n=== Menu Admin Perpustakaan UMM (dari Admin.java) ===");
        System.out.println("0. Dashboard (Statistik Cepat)");
        System.out.println("1. Kelola Buku");
        System.out.println("2. Proses Pengembalian Buku (oleh Admin)");
        System.out.println("3. Lihat Daftar Member");
        System.out.println("4. Lihat Denda Member");
        System.out.println("5. Lihat Laporan Peminjaman Buku Aktif");
        System.out.println("6. Lihat Riwayat Transaksi Pembayaran Denda");
        System.out.println("7. Logout");
        System.out.println("================================================");
    }
}