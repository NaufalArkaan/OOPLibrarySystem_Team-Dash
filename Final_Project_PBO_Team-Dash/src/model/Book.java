package model;


public class Book {
    private int no;
    private String title;
    private String author;
    private String category;
    private String isbn;
    private String status;
    private String imagePath;


    public Book(int no, String title, String author, String category, String isbn, String status, String imagePath) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.status = status;
        this.imagePath = imagePath;
    }


    public Book(int no, String title, String author, String category, String isbn, String status) {
        this(no, title, author, category, isbn, status, null);
    }

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
