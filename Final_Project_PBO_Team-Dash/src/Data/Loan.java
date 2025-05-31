// File: src/Data/Loan.java (Anda bisa meletakkannya di package Data atau package baru) // Komentar mengenai lokasi file.
package Data; // Mendefinisikan package tempat kelas ini berada, yaitu 'Data'.

import java.time.LocalDate; // Import kelas LocalDate untuk bekerja dengan tanggal (misalnya, tanggal pinjam, jatuh tempo).
import java.time.temporal.ChronoUnit; // Import ChronoUnit untuk menghitung selisih antar tanggal (misalnya, jumlah hari keterlambatan).

public class Loan { // Deklarasi kelas Loan, yang merepresentasikan satu transaksi peminjaman buku oleh seorang member.
    private Book book; // Variabel untuk menyimpan objek Book yang dipinjam dalam transaksi ini.
    private LocalDate borrowDate; // Variabel untuk menyimpan tanggal ketika buku ini dipinjam.
    private LocalDate dueDate; // Variabel untuk menyimpan tanggal jatuh tempo pengembalian buku.
    // Tambahkan Member jika perlu melacak siapa yang meminjam langsung di objek Loan,
    // tapi karena Loan akan disimpan dalam list di objek Member, mungkin tidak perlu.

    public static final int DEFAULT_LOAN_PERIOD_DAYS = 7; // Konstanta untuk periode peminjaman default, yaitu 7 hari. Bersifat final (tidak bisa diubah) dan statis (milik kelas).
    public static final double FINE_PER_DAY = 5000.0;   // Konstanta untuk besar denda per hari keterlambatan, yaitu Rp 5000.

    /**
     * Konstruktor untuk membuat objek Loan baru dengan periode peminjaman default.
     * Tanggal jatuh tempo dihitung otomatis berdasarkan tanggal pinjam dan DEFAULT_LOAN_PERIOD_DAYS.
     * @param book Objek Book yang dipinjam.
     * @param borrowDate Tanggal buku dipinjam.
     */
    public Loan(Book book, LocalDate borrowDate) {
        this.book = book; // Menginisialisasi buku yang dipinjam.
        this.borrowDate = borrowDate; // Menginisialisasi tanggal pinjam.
        this.dueDate = borrowDate.plusDays(DEFAULT_LOAN_PERIOD_DAYS); // Menghitung dan menginisialisasi tanggal jatuh tempo dengan menambahkan periode pinjam default ke tanggal pinjam.
    }

    /**
     * Konstruktor untuk membuat objek Loan baru dengan periode peminjaman yang ditentukan.
     * @param book Objek Book yang dipinjam.
     * @param borrowDate Tanggal buku dipinjam.
     * @param loanPeriodDays Durasi peminjaman dalam hari.
     */
    public Loan(Book book, LocalDate borrowDate, int loanPeriodDays) {
        this.book = book; // Menginisialisasi buku yang dipinjam.
        this.borrowDate = borrowDate; // Menginisialisasi tanggal pinjam.
        this.dueDate = borrowDate.plusDays(loanPeriodDays); // Menghitung dan menginisialisasi tanggal jatuh tempo dengan menambahkan periode pinjam yang ditentukan ke tanggal pinjam.
    }

    // Getter
    // Metode getter digunakan untuk mengakses nilai atribut privat dari luar kelas.
    public Book getBook() { // Mengembalikan objek Book yang dipinjam.
        return book;
    }

    public LocalDate getBorrowDate() { // Mengembalikan tanggal buku dipinjam.
        return borrowDate;
    }

    public LocalDate getDueDate() { // Mengembalikan tanggal jatuh tempo pengembalian buku.
        return dueDate;
    }

    // Setter untuk dueDate jika ada fitur perpanjangan
    // Metode setter digunakan untuk mengubah nilai atribut privat dari luar kelas.
    public void setDueDate(LocalDate dueDate) { // Mengatur atau mengubah tanggal jatuh tempo. Berguna jika ada fitur perpanjangan pinjaman.
        this.dueDate = dueDate;
    }

    /**
     * Memeriksa apakah peminjaman sudah melewati tanggal jatuh tempo.
     * @param currentDate Tanggal saat ini untuk perbandingan.
     * @return true jika tanggal saat ini setelah tanggal jatuh tempo, false jika sebaliknya.
     */
    public boolean isOverdue(LocalDate currentDate) { // Metode untuk mengecek apakah peminjaman sudah terlambat.
        return currentDate.isAfter(this.dueDate); // Mengembalikan true jika tanggal saat ini (currentDate) sudah melewati tanggal jatuh tempo (this.dueDate).
    }

    /**
     * Menghitung jumlah hari keterlambatan.
     * @param currentDate Tanggal saat ini untuk perhitungan.
     * @return Jumlah hari keterlambatan. Jika tidak terlambat, mengembalikan 0.
     */
    public long getOverdueDays(LocalDate currentDate) { // Metode untuk mendapatkan jumlah hari keterlambatan.
        if (isOverdue(currentDate)) { // Jika peminjaman memang sudah terlambat.
            return ChronoUnit.DAYS.between(this.dueDate, currentDate); // Menghitung selisih hari antara tanggal jatuh tempo dan tanggal saat ini.
        }
        return 0; // Jika tidak terlambat, kembalikan 0 hari.
    }

    /**
     * Menghitung total denda berdasarkan jumlah hari keterlambatan.
     * @param currentDate Tanggal saat ini untuk perhitungan denda.
     * @return Jumlah total denda. Jika tidak ada keterlambatan, mengembalikan 0.0.
     */
    public double calculateFine(LocalDate currentDate) { // Metode untuk menghitung denda.
        long overdueDays = getOverdueDays(currentDate); // Mendapatkan jumlah hari keterlambatan.
        if (overdueDays > 0) { // Jika ada hari keterlambatan.
            return overdueDays * FINE_PER_DAY; // Mengalikan jumlah hari terlambat dengan denda per hari.
        }
        return 0.0; // Jika tidak terlambat, tidak ada denda.
    }

    @Override // Menandakan bahwa metode ini meng-override metode toString() dari kelas Object.
    public String toString() { // Metode untuk menghasilkan representasi String dari objek Loan, berguna untuk tampilan daftar pinjaman.
        return "Buku: " + book.getTitle() + " (ISBN: " + book.getCode() + ")" + // Menambahkan judul dan ISBN buku.
                ", Dipinjam: " + borrowDate + // Menambahkan tanggal pinjam.
                ", Jatuh Tempo: " + dueDate; // Menambahkan tanggal jatuh tempo.
    }
}