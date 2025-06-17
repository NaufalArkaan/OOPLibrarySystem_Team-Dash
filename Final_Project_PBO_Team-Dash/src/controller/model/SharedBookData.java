package controller.model;

import Data.Book; // Menggunakan kelas Book dari package Data

/**
 * Kelas utilitas untuk MENYIMPAN SEMENTARA buku yang dipilih dari katalog
 * untuk dibuka di halaman peminjaman.
 */
public final class SharedBookData {

    // Field untuk menyimpan buku yang dipilih saat klik "Pinjam"
    private static Book selectedBook = null;

    private SharedBookData() {
        // Mencegah instansiasi kelas ini
    }

    /**
     * Menyimpan objek buku yang dipilih oleh pengguna.
     * @param book Objek buku dari kelas Data.Book.
     */
    public static void setSelectedBook(Book book) {
        selectedBook = book;
    }

    /**
     * Mengambil objek buku yang sebelumnya telah disimpan.
     * @return Objek buku yang dipilih, atau null jika tidak ada.
     */
    public static Book getSelectedBook() {
        return selectedBook;
    }
}