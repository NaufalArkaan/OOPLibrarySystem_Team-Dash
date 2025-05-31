package Data;

import java.util.ArrayList;

public class Book {
    private String code;
    private String title;
    private String author;
    private String category;
    private int quantity;

    public Book(String code, String title, String author, String category, int quantity) {
        this.code = code;
        this.title = title;
        this.author = author;
        this.category = category;
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            this.quantity = 0;
            System.out.println("Peringatan: Kuantitas awal untuk buku '" + title + "' negatif, diatur ke 0.");
        }
    }

    // Getter
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }

    // Setter
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }

    /**
     * Sets the quantity of the book.
     * Ensures quantity is not negative.
     * @param newQuantity The new quantity.
     */
    public void setQuantity(int newQuantity) { // <--- THIS METHOD MUST EXIST
        if (newQuantity >= 0) {
            this.quantity = newQuantity;
        } else {
            this.quantity = 0;
            System.out.println("Peringatan: Upaya mengatur kuantitas buku '" + title + "' ke nilai negatif, diatur ke 0.");
        }
    }

    /**
     * Metode internal untuk memperbarui kuantitas buku secara langsung.
     * Biasanya dipanggil oleh AdminAction setelah validasi.
     * @param newQuantity Kuantitas baru buku.
     */
    public void _updateRawQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    public boolean borrowBook() {
        if (this.quantity > 0) {
            this.quantity--;
            return true;
        }
        return false;
    }

    public void returnBook() {
        this.quantity++;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + title + " oleh " + author + " (Kategori: " + category + ", Stok: " + quantity + ")";
    }

    public static ArrayList<Book> getDefaultBooks() {
        ArrayList<Book> defaultBooks = new ArrayList<>();
        defaultBooks.add(new Book("0000", "Otodidak Desain dan Pemrograman Website", "Jubilee Enterprise", "Fiction", 1));
        defaultBooks.add(new Book("0001", "World History Sejarah Dunia Lengkap", "Hutton Webster PHD", "Fiction", 1));
        defaultBooks.add(new Book("0002", "Pulang", "Tere Liye", "Non-Fiction", 1));
        return defaultBooks;
    }
}