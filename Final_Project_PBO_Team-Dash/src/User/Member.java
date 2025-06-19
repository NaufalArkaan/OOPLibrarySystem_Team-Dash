
package User;

import Data.Loan; // Tetap dibutuhkan jika Member masih menyimpan daftar pinjamannya setelah login
import java.util.ArrayList; // Tetap dibutuhkan untuk borrowedLoans

public class Member extends User {
    private double fee;
    private ArrayList<Loan> borrowedLoans;

    public Member(int userId, String username, String password, String name, String email, String major, String studentId) {
        super(userId, username, password, "Member", name, email, major, studentId);
        this.fee = 0.0;
        this.borrowedLoans = new ArrayList<>();
    }

    public Member(String username, String password, String name, String email, String major, String studentId) {
        super(username, password, "Member", name, email, major, studentId);
        this.fee = 0.0;
        this.borrowedLoans = new ArrayList<>();
    }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
    public void addFee(double additionalFee) {
        this.fee += additionalFee;
    }

    public ArrayList<Loan> getBorrowedLoans() {
        return borrowedLoans;
    }

    public void setBorrowedLoans(ArrayList<Loan> loans) {
        this.borrowedLoans = loans;
    }

    public void addLoan(Loan loan) {
        this.borrowedLoans.add(loan);
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
        return this.borrowedLoans.remove(loan);
    }


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