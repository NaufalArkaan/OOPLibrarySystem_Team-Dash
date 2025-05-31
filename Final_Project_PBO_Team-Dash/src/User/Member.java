// File: src/User/Member.java
package User;

import Data.Loan; // Tetap dibutuhkan jika Member masih menyimpan daftar pinjamannya setelah login
import java.util.ArrayList; // Tetap dibutuhkan untuk borrowedLoans

public class Member extends User {
    private double fee;
    private ArrayList<Loan> borrowedLoans; // Ini akan di-populate dari database saat Member login

    /**
     * Konstruktor untuk membuat objek Member dari data database.
     * @param userId ID unik member.
     * @param username Username untuk login.
     * @param password Password untuk login.
     * @param name Nama lengkap member.
     * @param email Alamat email member.
     * @param major Jurusan member.
     * @param studentId Nomor Induk Mahasiswa (NIM) atau ID unik member.
     */
    public Member(int userId, String username, String password, String name, String email, String major, String studentId) {
        super(userId, username, password, "Member", name, email, major, studentId);
        this.fee = 0.0; // Fee awal, bisa di-load dari database jika ada kolomnya
        this.borrowedLoans = new ArrayList<>(); // Akan diisi dari tabel 'loans' saat login
    }

    /**
     * Konstruktor untuk membuat objek Member baru sebelum disimpan ke database (saat registrasi).
     * @param username Username untuk login.
     * @param password Password untuk login.
     * @param name Nama lengkap member.
     * @param email Alamat email member.
     * @param major Jurusan member.
     * @param studentId Nomor Induk Mahasiswa (NIM) atau ID unik member.
     */
    public Member(String username, String password, String name, String email, String major, String studentId) {
        super(username, password, "Member", name, email, major, studentId); // Peran otomatis "Member"
        this.fee = 0.0;
        this.borrowedLoans = new ArrayList<>();
    }

    // Getter dan Setter spesifik Member
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; } // Mungkin akan di-update oleh DAO setelah operasi DB

    public void addFee(double additionalFee) {
        this.fee += additionalFee;
        // Pertimbangkan untuk langsung update ke database via DAO di sini,
        // atau tandai bahwa fee perlu di-update saat objek Member di-persist.
    }

    public ArrayList<Loan> getBorrowedLoans() {
        return borrowedLoans;
    }

    // Metode ini penting untuk diisi oleh DAO setelah Member login
    public void setBorrowedLoans(ArrayList<Loan> loans) {
        this.borrowedLoans = loans;
    }

    public void addLoan(Loan loan) {
        this.borrowedLoans.add(loan);
        // Operasi addLoan ke database akan ditangani oleh LoanDAO.
        // ArrayList ini hanya cache sisi klien setelah data diambil.
    }

    public Loan findLoanByBookCode(String bookCode) {
        for (Loan loan : borrowedLoans) {
            if (loan.getBook().getCode().equalsIgnoreCase(bookCode)) {
                return loan;
            }
        }
        return null;
    }

    public boolean removeLoan(Loan loan) {
        // Operasi removeLoan dari database akan ditangani oleh LoanDAO.
        return this.borrowedLoans.remove(loan);
    }

    // Metode loginAsMember statis dihapus. Logika login akan ada di UserDAO atau AuthService.
    // Contoh: public static Member loginAsMember(String username, String password) { ... }


    // displayMenu() bisa tetap ada
    public void displayMenu() {
        System.out.println("=== Menu Member ===");
        System.out.println("1. Lihat Profil");
        System.out.println("2. Lihat Daftar Buku (Semua)");
        System.out.println("3. Lihat Buku yang Saya Pinjam");
        System.out.println("4. Bayar Denda");
        System.out.println("5. Pinjam Buku");
        System.out.println("6. Kembalikan Buku");
        System.out.println("7. Logout");
        System.out.println("=====================");
    }
}