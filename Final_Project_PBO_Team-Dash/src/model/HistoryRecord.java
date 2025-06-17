package model;

/**
 * Model data untuk menyimpan riwayat peminjaman buku oleh mahasiswa.
 */
public class HistoryRecord {
    private String name;
    private String className;
    private String title;
    private String isbn;

    public HistoryRecord(String name, String className, String title, String isbn) {
        this.name = name;
        this.className = className;
        this.title = title;
        this.isbn = isbn;
    }

    // --- Getter ---
    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    // --- Setter ---
    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
