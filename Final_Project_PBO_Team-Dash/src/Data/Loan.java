package Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private int userId;
    private int loanId;
    private double fine;
    private int quantity; // Jumlah buku yang dipinjam
    private String status;
    private LocalDate actualReturnDate;

    public static final int DEFAULT_LOAN_PERIOD_DAYS = 7;
    public static final double FINE_PER_DAY = 10000.0;

    public Loan(Book book, LocalDate borrowDate, int quantity) {
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(DEFAULT_LOAN_PERIOD_DAYS);
        this.fine = 0.0;
        this.quantity = quantity;
        this.status = "Borrowed";
    }

    public Loan(Book book, LocalDate borrowDate) {
        this(book, borrowDate, 1);
    }

    // --- Getters ---
    public Book getBook() { return book; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public int getUserId() { return userId; }
    public int getLoanId() { return loanId; }
    public double getFine() { return fine; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }

    // --- Setters ---
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    public void setFine(double fine) { this.fine = fine; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setStatus(String status) { this.status = status; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public boolean isOverdue(LocalDate currentDate) {
        return currentDate.isAfter(this.dueDate);
    }

    public long getOverdueDays(LocalDate currentDate) {
        if (isOverdue(currentDate)) {
            return ChronoUnit.DAYS.between(this.dueDate, currentDate);
        }
        return 0;
    }

    public double calculateFine(LocalDate currentDate) {
        long overdueDays = getOverdueDays(currentDate);
        return overdueDays * FINE_PER_DAY;
    }

    public double calculateFine() {
        return calculateFine(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Buku: " + book.getTitle() + ", Jatuh Tempo: " + dueDate;
    }
}