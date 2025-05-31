// File: src/Data/Transaction.java // Penamaan file, sebaiknya path disesuaikan jika file ada di src/Action
package Action; // Mendefinisikan package tempat kelas ini berada. Sepertinya lebih cocok di package Data jika ini adalah entitas data.

import User.Member; // Import kelas Member dari package User, karena transaksi berelasi dengan Member.
import java.time.LocalDateTime; // Import kelas LocalDateTime untuk mencatat waktu transaksi secara presisi (tanggal dan waktu).
import java.util.UUID; // Import kelas UUID untuk menghasilkan ID transaksi yang unik.

public class Transaction { // Deklarasi kelas Transaction, merepresentasikan sebuah transaksi pembayaran denda.
    private String transactionId; // Variabel untuk menyimpan ID unik dari transaksi.
    private Member member; // Variabel untuk menyimpan objek Member yang melakukan transaksi ini.
    private LocalDateTime transactionDateTime; // Variabel untuk menyimpan tanggal dan waktu ketika transaksi terjadi.
    private double amountPaid; // Variabel untuk menyimpan jumlah uang yang dibayarkan dalam transaksi ini.
    private double fineBeforePayment; // Variabel untuk menyimpan jumlah total denda member sebelum pembayaran ini dilakukan.
    private double fineAfterPayment; // Variabel untuk menyimpan jumlah sisa denda member setelah pembayaran ini dilakukan.

    /**
     * Konstruktor untuk membuat objek Transaction baru.
     * ID Transaksi dan Waktu Transaksi akan dibuat secara otomatis.
     *
     * @param member Member yang melakukan pembayaran.
     * @param amountPaid Jumlah uang yang diterapkan untuk membayar denda.
     * @param fineBeforePayment Total denda member sebelum pembayaran ini.
     * @param fineAfterPayment Total denda member setelah pembayaran ini.
     */
    public Transaction(Member member, double amountPaid, double fineBeforePayment, double fineAfterPayment) {
        this.transactionId = UUID.randomUUID().toString(); // Membuat ID unik secara otomatis untuk setiap transaksi menggunakan UUID.
        this.member = member; // Menginisialisasi member yang melakukan transaksi.
        this.transactionDateTime = LocalDateTime.now(); // Menginisialisasi waktu transaksi dengan waktu saat ini secara otomatis.
        this.amountPaid = amountPaid; // Menginisialisasi jumlah yang dibayarkan.
        this.fineBeforePayment = fineBeforePayment; // Menginisialisasi denda sebelum pembayaran.
        this.fineAfterPayment = fineAfterPayment; // Menginisialisasi denda setelah pembayaran.
    }

    // Getter untuk semua atribut
    // Metode getter digunakan untuk mengakses nilai atribut privat dari luar kelas.
    public String getTransactionId() { // Mengembalikan ID transaksi.
        return transactionId;
    }

    public Member getMember() { // Mengembalikan objek Member yang terkait dengan transaksi ini.
        return member;
    }

    public LocalDateTime getTransactionDateTime() { // Mengembalikan waktu terjadinya transaksi.
        return transactionDateTime;
    }

    public double getAmountPaid() { // Mengembalikan jumlah yang dibayarkan dalam transaksi.
        return amountPaid;
    }

    public double getFineBeforePayment() { // Mengembalikan jumlah denda sebelum pembayaran.
        return fineBeforePayment;
    }

    public double getFineAfterPayment() { // Mengembalikan jumlah denda setelah pembayaran.
        return fineAfterPayment;
    }


    @Override // Menandakan bahwa metode ini meng-override metode toString() dari kelas Object.
    public String toString() { // Metode untuk menghasilkan representasi String dari objek Transaction, berguna untuk logging atau tampilan.
        return "Transaction {\n" + // Memulai format string.
                "  ID Transaksi    : " + transactionId + "\n" + // Menambahkan ID transaksi ke string.
                "  Member          : " + member.getName() + " (ID: " + member.getStudentId() + ")\n" + // Menambahkan nama dan ID member.
                "  Waktu Transaksi : " + transactionDateTime.toLocalDate() + " " + transactionDateTime.toLocalTime().withNano(0) + "\n" + // Menambahkan tanggal dan waktu transaksi (tanpa nanodetik).
                "  Denda Sebelumnya: Rp " + fineBeforePayment + "\n" + // Menambahkan informasi denda sebelum pembayaran.
                "  Jumlah Dibayar  : Rp " + amountPaid + "\n" + // Menambahkan jumlah yang dibayarkan.
                "  Sisa Denda      : Rp " + fineAfterPayment + "\n" + // Menambahkan sisa denda setelah pembayaran.
                "}"; // Menutup format string.
    }
    public Transaction(String transactionId, Member member, LocalDateTime transactionDateTime, double amountPaid, double fineBeforePayment, double fineAfterPayment) {
        this.transactionId = transactionId;
        this.member = member;
        this.transactionDateTime = transactionDateTime;
        this.amountPaid = amountPaid;
        this.fineBeforePayment = fineBeforePayment;
        this.fineAfterPayment = fineAfterPayment;
    }
}