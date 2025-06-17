package model;

/**
 * Model class untuk merepresentasikan data Buku.
 */
public class Book {
    private int no;             // Nomor urut buku
    private String title;       // Judul buku
    private String author;      // Penulis buku
    private String category;    // Kategori (Fiction / Non-Fiction)
    private String isbn;        // Nomor ISBN
    private String status;      // Status (Available / Borrowed)
    private String imagePath;   // Path atau nama file gambar buku

    // Constructor lengkap
    public Book(int no, String title, String author, String category, String isbn, String status, String imagePath) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.status = status;
        this.imagePath = imagePath;
    }

    // Constructor tanpa gambar (jika tidak butuh imagePath)
    public Book(int no, String title, String author, String category, String isbn, String status) {
        this(no, title, author, category, isbn, status, null);
    }

    // Getter
    public int getNo() {
        return no;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getStatus() {
        return status;
    }

    public String getImagePath() {
        return imagePath;
    }

    // Setter
    public void setNo(int no) {
        this.no = no;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return no + ". " + title;
    }
}
