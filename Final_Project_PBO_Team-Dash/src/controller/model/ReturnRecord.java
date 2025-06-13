package controller.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReturnRecord {
    private int no;
    private String studentId;
    private String memberName;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String fine;
    private String status; // "Sudah Kembali" / "Belum Kembali"
    private int totalBorrowed;

    // Format tanggal: 11/06/2025
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReturnRecord(int no, String studentId, String memberName, String bookTitle,
                        LocalDate borrowDate, LocalDate returnDate,
                        String fine, String status, int totalBorrowed) {
        this.no = no;
        this.studentId = studentId;
        this.memberName = memberName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.fine = fine;
        this.status = status;
        this.totalBorrowed = totalBorrowed;
    }

    // ========================
    // Getter methods
    // ========================
    public int getNo() {
        return no;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getFine() {
        return fine;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    // ========================
    // Formatted date strings
    // ========================
    public String getBorrowDateFormatted() {
        return borrowDate != null ? borrowDate.format(formatter) : "";
    }

    public String getReturnDateFormatted() {
        return returnDate != null ? returnDate.format(formatter) : "";
    }

    // ========================
    // Setter methods
    // ========================
    public void setNo(int no) {
        this.no = no;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalBorrowed(int totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    // ========================
    // Debug toString
    // ========================
    @Override
    public String toString() {
        return "ReturnRecord{" +
                "no=" + no +
                ", studentId='" + studentId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", fine='" + fine + '\'' +
                ", status='" + status + '\'' +
                ", totalBorrowed=" + totalBorrowed +
                '}';
    }
}
