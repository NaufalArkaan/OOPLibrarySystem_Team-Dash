package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model class ini berfungsi sebagai DTO (Data Transfer Object) untuk menampilkan
 * data gabungan dari berbagai sumber (Loan, Member, Book) di dalam tabel admin.
 * Ini BUKAN representasi langsung dari satu tabel di database.
 */
public class ReturnRecord {
    private int no;
    private String studentId;
    private String memberName;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate; // Diisi dengan DueDate atau ActualReturnDate tergantung konteks
    private String fine;
    private String status;
    private int totalBorrowed;

    // ID penting untuk melakukan aksi update atau delete
    private int userId;
    private String bookCode;
    private int loanId;
    private LocalDate actualReturnDate; // Tanggal pengembalian yang sebenarnya

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

    // --- Getters ---
    public int getNo() { return no; }
    public String getStudentId() { return studentId; }
    public String getMemberName() { return memberName; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getFine() { return fine; }
    public String getStatus() { return status; }
    public int getTotalBorrowed() { return totalBorrowed; }
    public int getUserId() { return userId; }
    public String getBookCode() { return bookCode; }
    public int getLoanId() { return loanId; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }

    // Getter untuk format tanggal yang akan digunakan oleh TableView
    public String getBorrowDateFormatted() {
        return borrowDate != null ? borrowDate.format(formatter) : "";
    }
    public String getReturnDateFormatted() {
        return returnDate != null ? returnDate.format(formatter) : "";
    }

    // --- Setters ---
    public void setNo(int no) { this.no = no; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setFine(String fine) { this.fine = fine; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalBorrowed(int totalBorrowed) { this.totalBorrowed = totalBorrowed; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setBookCode(String bookCode) { this.bookCode = bookCode; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    @Override
    public String toString() {
        return "ReturnRecord{" + "no=" + no + ", studentId='" + studentId + '\'' + ", bookTitle='" + bookTitle + '\'' + '}';
    }
}