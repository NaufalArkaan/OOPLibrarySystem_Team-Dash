//// File: src/Action/MemberAction.java
//package Action;
//
//import Data.Book; //
//import SQL_DATA.BookDAO; //
//import Data.Loan; //
//import SQL_DATA.LoanDAO; //
//import SQL_DATA.TransactionDAO; //
//import SQL_DATA.UserDAO; //
//import User.Member; //
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.InputMismatchException;
//import java.util.Scanner;
//import ExceptionHandle.BookNotFoundException; //
//import ExceptionHandle.NoDataFoundException; //
//import ExceptionHandle.BookOutOfStockException; //
//
//public class MemberAction {
//    private Scanner scanner = new Scanner(System.in);
//    private BookDAO bookDAO;
//    private LoanDAO loanDAO;
//    private TransactionDAO transactionDAO;
//    private UserDAO userDAO;
//
//    public MemberAction() {
//        this.bookDAO = new BookDAO();
//        this.loanDAO = new LoanDAO();
//        this.transactionDAO = new TransactionDAO();
//        this.userDAO = new UserDAO();
//    }
//
//    public int displayMenu() {
//        System.out.println("\n=== Menu Member ==="); //
//        System.out.println("1. Lihat Profil"); //
//        System.out.println("2. Lihat Daftar Buku (Semua)"); //
//        System.out.println("3. Lihat Buku yang Saya Pinjam"); //
//        System.out.println("4. Bayar Denda"); //
//        System.out.println("5. Pinjam Buku"); //
//        System.out.println("6. Kembalikan Buku"); //
//        System.out.println("7. Logout"); //
//        System.out.print("Masukkan pilihan anda: ");
//        try {
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // Consume newline
//            return choice;
//        } catch (InputMismatchException e) {
//            scanner.nextLine(); // Consume invalid input
//            System.out.println("Input tidak valid. Harap masukkan angka sebagai pilihan.");
//            return -1;
//        }
//    }
//
//    public void processMenu(Member member) {
//        if (member == null) {
//            System.err.println("Error: Tidak ada sesi member yang aktif untuk memproses menu.");
//            return;
//        }
//        // Ensure member's loans are loaded
//        if (member.getBorrowedLoans() == null || member.getBorrowedLoans().isEmpty()) { //
//            ArrayList<Loan> activeLoans = loanDAO.getActiveLoansByUserId(member.getUserId()); //
//            member.setBorrowedLoans(activeLoans); //
//        }
//
//        int pilihan;
//        do {
//            pilihan = displayMenu();
//
//            if (pilihan == -1) {
//                continue;
//            }
//            try {
//                switch (pilihan) {
//                    case 1: displayProfile(member); break;
//                    case 2: displayAllBookList(); break;
//                    case 3: displayBorrowedLoans(member); break;
//                    case 4: payFine(member); break;
//                    case 5: borrowBookAction(member); break;
//                    case 6: returnBookAction(member); break; //
//                    case 7: logout(); break;
//                    default: System.out.println("Pilihan tidak valid, coba lagi."); break;
//                }
//            } catch (NoDataFoundException | BookNotFoundException | BookOutOfStockException e) {
//                System.err.println("Error: " + e.getMessage());
//            } catch (IllegalArgumentException | IllegalStateException e) {
//                System.err.println("System Error: " + e.getMessage());
//            } catch (Exception e) {
//                System.err.println("Terjadi kesalahan yang tidak diharapkan: " + e.getMessage());
//                e.printStackTrace();
//            }
//        } while (pilihan != 7);
//    }
//
//
//    public void displayProfile(Member member) {
//        System.out.println("\n=== Profil Member ===");
//        System.out.println("Nama        : " + member.getName()); //
//        System.out.println("Email       : " + member.getEmail()); //
//        System.out.println("Jurusan     : " + member.getMajor()); //
//        System.out.println("Student ID  : " + member.getStudentId()); //
//        System.out.println("Total Denda : Rp " + String.format("%,.2f", member.getFee())); //
//    }
//
//    public void displayAllBookList() throws NoDataFoundException {
//        System.out.println("\n=== Daftar Semua Buku di Perpustakaan ===");
//        ArrayList<Book> books = bookDAO.getAllBooks(); //
//        if (books.isEmpty()) {
//            throw new NoDataFoundException("Belum ada data buku di perpustakaan.");
//        }
//        for (Book book : books) {
//            System.out.println(book.toString()); //
//        }
//    }
//
//    public void displayBorrowedLoans(Member member) throws NoDataFoundException {
//        System.out.println("\n=== Buku yang Saya Pinjam ===");
//        //
//        ArrayList<Loan> borrowedLoans = loanDAO.getActiveLoansByUserId(member.getUserId()); //
//        member.setBorrowedLoans(borrowedLoans); //
//
//
//        if (borrowedLoans == null || borrowedLoans.isEmpty()) {
//            throw new NoDataFoundException("Anda belum meminjam buku.");
//        }
//
//        for (Loan loan : borrowedLoans) {
//            System.out.println(loan.toString()); //
//            if (loan.isOverdue(LocalDate.now())) { //
//                System.out.println("  Status: TERLAMBAT (Denda Rp " + String.format("%,.2f", loan.calculateFine(LocalDate.now())) + ")"); //
//            } else {
//                System.out.println("  Status: Dipinjam");
//            }
//        }
//    }
//
//    public void borrowBookAction(Member member) throws BookNotFoundException, NoDataFoundException, BookOutOfStockException {
//        System.out.println("\n=== Pinjam Buku ===");
//        displayAllBookList();
//
//        System.out.print("Masukkan Kode Buku (ISBN) yang ingin dipinjam: ");
//        String isbn = scanner.nextLine();
//        if (isbn.trim().isEmpty()){
//            System.out.println("ISBN tidak boleh kosong.");
//            return;
//        }
//
//        Book bookToBorrow = bookDAO.findBookByCode(isbn); //
//
//        if (bookToBorrow == null) {
//            throw new BookNotFoundException("Buku dengan kode ISBN \"" + isbn + "\" tidak ditemukan.");
//        }
//
//        if (bookToBorrow.getQuantity() <= 0) { //
//            throw new BookOutOfStockException("Buku \"" + bookToBorrow.getTitle() + "\" ditemukan tetapi stok habis."); //
//        }
//
//        //
//        if (member.findLoanByBookCode(isbn) != null) { //
//            System.out.println("Anda sudah meminjam buku dengan ISBN '" + isbn + "' dan belum mengembalikannya.");
//            return;
//        }
//
//
//        if (!bookDAO.updateBookQuantity(bookToBorrow.getCode(), bookToBorrow.getQuantity() - 1)) { //
//            System.err.println("Gagal memperbarui stok buku di database. Peminjaman dibatalkan.");
//            return;
//        }
//        //
//        bookToBorrow.setQuantity(bookToBorrow.getQuantity() - 1); //
//
//
//        Loan newLoan = new Loan(bookToBorrow, LocalDate.now()); //
//        if (loanDAO.addLoan(newLoan, member.getUserId())) { //
//            member.addLoan(newLoan); //
//            System.out.println("\n=== Buku Berhasil Dipinjam ===");
//            System.out.println("Judul      : " + bookToBorrow.getTitle()); //
//            System.out.println("Kode ISBN  : " + bookToBorrow.getCode()); //
//            System.out.println("Tgl Pinjam : " + newLoan.getBorrowDate()); //
//            System.out.println("Jatuh Tempo: " + newLoan.getDueDate()); //
//            System.out.println("==================================");
//        } else {
//            System.err.println("Gagal mencatat peminjaman di database. Stok buku mungkin perlu dikembalikan manual.");
//            // Rollback stok
//            bookDAO.updateBookQuantity(bookToBorrow.getCode(), bookToBorrow.getQuantity() + 1); //
//            bookToBorrow.setQuantity(bookToBorrow.getQuantity() + 1); //  //
//        }
//    }
//
//    public void returnBookAction(Member member) throws NoDataFoundException, BookNotFoundException {
//        System.out.println("\n=== Kembalikan Buku ===");
//        //
//        displayBorrowedLoans(member); //
//
//        ArrayList<Loan> borrowedLoans = member.getBorrowedLoans(); //
//        if (borrowedLoans.isEmpty()) {
//
//            System.out.println("Anda tidak memiliki buku untuk dikembalikan.");
//            return;
//        }
//
//        System.out.print("Masukkan Kode Buku (ISBN) yang ingin dikembalikan: ");
//        String isbn = scanner.nextLine();
//        if (isbn.trim().isEmpty()){
//            System.out.println("ISBN tidak boleh kosong.");
//            return;
//        }
//
//        Loan loanToReturn = member.findLoanByBookCode(isbn); //
//
//        if (loanToReturn == null) {
//            throw new BookNotFoundException("Buku dengan kode ISBN \"" + isbn + "\" tidak ditemukan dalam daftar pinjaman Anda.");
//        }
//
//        Book bookReturned = loanToReturn.getBook(); //
//
//        // Calculate fine
//        double fine = loanToReturn.calculateFine(LocalDate.now()); //
//        if (fine > 0) {
//            System.out.println("-----------------------------------------------------");
//            System.out.println("ANDA TERLAMBAT MENGEMBALIKAN BUKU INI!");
//            System.out.println("Judul Buku       : " + bookReturned.getTitle()); //
//            System.out.println("Tanggal Pinjam   : " + loanToReturn.getBorrowDate()); //
//            System.out.println("Tanggal J. Tempo : " + loanToReturn.getDueDate()); //
//            System.out.println("Tanggal Kembali  : " + LocalDate.now());
//            System.out.println("Jumlah Keterlambatan: " + loanToReturn.getOverdueDays(LocalDate.now()) + " hari"); //
//            System.out.println("Denda per hari   : Rp " + String.format("%,.2f", Loan.FINE_PER_DAY)); //
//            System.out.println("TOTAL DENDA      : Rp " + String.format("%,.2f", fine));
//            System.out.println("-----------------------------------------------------");
//            System.out.println("Denda ini telah ditambahkan ke total denda Anda.");
//            System.out.println("Silakan lakukan pembayaran melalui menu 'Bayar Denda'.");
//            member.addFee(fine); //  //
//
//        }
//
//
//        if (loanDAO.removeLoan(member.getUserId(), bookReturned.getCode())) { //
//            member.removeLoan(loanToReturn); //
//
//            Book currentBookState = bookDAO.findBookByCode(bookReturned.getCode()); //
//            if (currentBookState != null) {
//                if (bookDAO.updateBookQuantity(currentBookState.getCode(), currentBookState.getQuantity() + 1)) { //
//                    System.out.println("\n=== Buku Berhasil Dikembalikan ===");
//                    System.out.println("Judul: " + bookReturned.getTitle()); //
//                    if (fine > 0) {
//                        System.out.println("Status: Dikembalikan dengan denda Rp " + String.format("%,.2f", fine));
//                    } else {
//                        System.out.println("Status: Dikembalikan tepat waktu.");
//                    }
//                } else {
//                    System.err.println("Gagal memperbarui stok buku di database setelah pengembalian.");
//                }
//            } else {
//                System.err.println("Error: Tidak dapat menemukan buku " + bookReturned.getCode() + " untuk memperbarui stoknya."); //
//            }
//        } else {
//            System.err.println("Gagal menghapus catatan peminjaman dari database.");
//        }
//    }
//
//
//    public void payFine(Member member) {
//        System.out.println("\n=== Bayar Denda ===");
//        double fineBeforePayment = member.getFee(); //
//
//        if (fineBeforePayment == 0) {
//            System.out.println("Anda tidak memiliki denda.");
//            return;
//        }
//        System.out.println("Total denda yang harus dibayar: Rp " + String.format("%,.2f", fineBeforePayment));
//
//        double nominalPayment;
//        while (true) {
//            System.out.print("Masukkan nominal pembayaran: Rp ");
//            try {
//                nominalPayment = scanner.nextDouble();
//                scanner.nextLine();
//                if (nominalPayment >= 0) {
//                    break;
//                } else {
//                    System.out.println("Nominal pembayaran tidak boleh negatif. Masukkan lagi.");
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Input tidak valid. Masukkan angka.");
//                scanner.nextLine();
//            }
//        }
//
//        double amountAppliedToFine;
//        double fineAfterPayment;
//
//        if (nominalPayment >= fineBeforePayment) {
//            amountAppliedToFine = fineBeforePayment;
//            fineAfterPayment = 0;
//            member.setFee(0); //
//            System.out.println("Pembayaran berhasil! Denda lunas.");
//            if (nominalPayment > fineBeforePayment) {
//                System.out.println("Kembalian: Rp " + String.format("%,.2f", (nominalPayment - fineBeforePayment)));
//            }
//        } else {
//            amountAppliedToFine = nominalPayment;
//            fineAfterPayment = fineBeforePayment - nominalPayment;
//            member.setFee(fineAfterPayment); //
//            System.out.println("Pembayaran sebagian berhasil. Sisa denda: Rp " + String.format("%,.2f", fineAfterPayment));
//        }
//
//        Transaction transaction = new Transaction(member, amountAppliedToFine, fineBeforePayment, fineAfterPayment); //
//        if (transactionDAO.addTransaction(transaction)) { //
//            System.out.println("Transaksi pembayaran telah dicatat. ID Transaksi: " + transaction.getTransactionId()); //
//        } else {
//            System.err.println("Gagal mencatat transaksi pembayaran di database.");
//            member.setFee(fineBeforePayment);
//        }
//    }
//
//    public void logout() {
//        System.out.println("\nAnda telah logout dari sesi Member.");
//    }
//}