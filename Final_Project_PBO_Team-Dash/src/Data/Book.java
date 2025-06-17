package Data;

public class Book {
    private String code;
    private String title;
    private String author;
    private String category;
    private byte[] image;
    private String status;
    private int quantity; // Stok buku yang tersedia

    public Book(String code, String title, String author, String category, byte[] image, String status, int quantity) {
        this.code = code;
        this.title = title;
        this.author = author;
        this.category = category;
        this.image = image;
        this.status = status;
        this.quantity = quantity;
    }

    public Book(String code, String title, String author, String category, byte[] image, String status) {
        this(code, title, author, category, image, status, 0);
    }

    // --- Getters ---
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public byte[] getImage() { return image; }
    public String getStatus() { return status; }
    public int getQuantity() { return quantity; }

    // --- Setters ---
    public void setCode(String code) { this.code = code; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setImage(byte[] image) { this.image = image; }
    public void setStatus(String status) { this.status = status; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "[" + code + "] " + title;
    }
}