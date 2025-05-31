// File: src/User/Admin.java
package User;

public class Admin extends User {

    /**
     * Konstruktor untuk membuat objek Admin dari data database.
     * @param userId ID unik admin.
     * @param username Username untuk login.
     * @param password Password untuk login.
     * @param name Nama lengkap admin.
     * @param email Alamat email admin.
     */
    public Admin(int userId, String username, String password, String name, String email) {
        super(userId, username, password, "Admin", name, email, null, null);
    }

    /**
     * Konstruktor untuk membuat objek Admin baru sebelum disimpan ke database.
     * @param username Username untuk login.
     * @param password Password untuk login.
     * @param name Nama lengkap admin.
     * @param email Alamat email admin.
     */
    public Admin(String username, String password, String name, String email) {
        // userId akan di-generate oleh database
        super(username, password, "Admin", name, email, null, null);
    }

    // Metode loginAsAdmin statis dihapus. Logika login akan ada di UserDAO atau AuthService.
    // Contoh: public static Admin loginAsAdmin(String username, String password) { ... }

    // Metode displayMenu() bisa tetap ada jika hanya untuk menampilkan teks menu,
    // namun tidak akan lagi menjadi tanggung jawab kelas Admin untuk memproses login.
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