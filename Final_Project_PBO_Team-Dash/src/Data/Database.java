package Data; // Mendefinisikan package tempat kelas ini berada, yaitu 'Data' yang berfokus pada entitas dan penyimpanan data.

import Action.Transaction; // Import kelas Transaction dari package Action, karena database akan menyimpan daftar transaksi.
import java.util.ArrayList; // Import kelas ArrayList untuk digunakan sebagai struktur data penyimpan buku dan transaksi.
// Kelas Book berada dalam package yang sama (Data), jadi import eksplisit Data.Book tidak wajib,
// tetapi bisa ditambahkan untuk kejelasan jika diinginkan.

public class Database { // Deklarasi kelas Database, yang berfungsi sebagai tempat penyimpanan data utama aplikasi.
    // ArrayList statis untuk menyimpan semua objek Book yang ada di perpustakaan.
    // 'static' berarti list ini milik kelas Database itu sendiri, bukan instance spesifik,
    // sehingga bisa diakses secara global dari mana saja di aplikasi.
    public static ArrayList<Book> books = new ArrayList<>(); // Inisialisasi list kosong untuk buku.

    // ArrayList statis untuk menyimpan semua objek Transaction (riwayat pembayaran denda).
    public static ArrayList<Transaction> transactions = new ArrayList<>(); // Inisialisasi list kosong untuk transaksi.

    // Blok inisialisasi statis.
    // Blok ini akan dieksekusi hanya sekali ketika kelas Database pertama kali dimuat oleh Java Virtual Machine (JVM).
    // Tujuannya adalah untuk mengisi data awal ke dalam database, seperti daftar buku default.
    static {
        // Memastikan data hanya dimuat sekali jika blok ini berpotensi dijalankan lagi
        // (meskipun blok statis biasanya hanya berjalan sekali per classloader).
        if (books.isEmpty()) { // Cek apakah list buku masih kosong untuk menghindari duplikasi data jika kelas dimuat ulang (jarang terjadi dalam aplikasi standar).
            ArrayList<Book> defaultBooks = Book.getDefaultBooks(); // Memanggil metode statis dari kelas Book untuk mendapatkan daftar buku default.
            if (defaultBooks != null) { // Memastikan daftar buku default tidak null sebelum menambahkannya.
                books.addAll(defaultBooks); // Menambahkan semua buku dari daftar defaultBooks ke list 'books' utama di Database.
            }
        }
    }
    // Anda mungkin juga ingin menambahkan ArrayList untuk Loan jika ingin menyimpan semua histori peminjaman
    // public static ArrayList<Loan> loanHistory = new ArrayList<>(); // Contoh komentar untuk potensi penambahan list histori peminjaman.
}