//package Action;
//
//import Data.Book;
//import SQL_DATA.BookDAO;
//import Data.Loan;
//import SQL_DATA.LoanDAO;
//import SQL_DATA.TransactionDAO;
//import SQL_DATA.UserDAO;
//import User.Member;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.InputMismatchException;
//import java.util.Scanner;
//import ExceptionHandle.BookNotFoundException;
//import ExceptionHandle.NoDataFoundException;
//import ExceptionHandle.BookOutOfStockException;
//import Action.Transaction;
//
//public class MemberAction {
//
//    private final Scanner scanner = new Scanner(System.in);
//    private final BookDAO bookDAO = new BookDAO();
//    private final LoanDAO loanDAO = new LoanDAO();
//    private final TransactionDAO transactionDAO = new TransactionDAO();
//    private final UserDAO userDAO = new UserDAO();
//
//    public MemberAction() {
//
//    }
//
//    public int displayMenu() {
//        System.out.println("\n=== Menu Member ===");
//        System.out.println("1. Lihat Profil");
//        System.out.println("2. Lihat Daftar Buku (Semua)");
//        System.out.println("3. Lihat Buku yang Saya Pinjam");
//        System.out.println("4. Bayar Denda");
//        System.out.println("5. Pinjam Buku");
//        System.out.println("6. Kembalikan Buku");
//        System.out.println("7. Logout");
//        System.out.print("Masukkan pilihan Anda: ");
//        try {
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//            return choice;
//        } catch (InputMismatchException e) {
//            scanner.nextLine();
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
//
//        try {
//            ArrayList<Loan> activeLoans = loanDAO.getActiveLoansByUserId(member.getUserId());
//            member.setBorrowedLoans(activeLoans);
//        } catch (NoDataFoundException e) {
//            System.out.println("Info: " + e.getMessage());
//            member.setBorrowedLoans(new ArrayList<>());
//        } catch (RuntimeException e) {
//            System.err.println("Error memuat pinjaman aktif untuk member: " + e.getMessage());
//            e.printStackTrace();
//            member.setBorrowedLoans(new ArrayList<>());
//        }
//
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
//                    case 6: returnBookAction(member); break;
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
//        System.out.println("Nama        : " + member.getName());
//        System.out.println("Email       : " + member.getEmail());
//        System.out.println("Jurusan     : " + member.getMajor());
//        System.out.println("Student ID  : " + member.getStudentId());
//        ArrayList<Loan> activeLoans = new ArrayList<>();
//        try {
//            activeLoans = loanDAO.getActiveLoansByUserId(member.getUserId());
//        } catch (NoDataFoundException e) {
//            System.out.println("Info: " + e.getMessage());
//        } catch (RuntimeException e) {
//            System.err.println("Error memuat pinjaman aktif untuk profil: " + e.getMessage());
//            e.printStackTrace();
//        }
//        double totalCurrentFine = 0;
//        for (Loan loan : activeLoans) {
//            totalCurrentFine += loan.calculateFine(LocalDate.now());
//        }
//        System.out.println("Total Denda : Rp " + String.format("%,.2f", totalCurrentFine));
//    }
//
//    public void displayAllBookList() throws NoDataFoundException {
//        System.out.println("\n=== Daftar Semua Buku di Perpustakaan ===");
//        ArrayList<Book> books = bookDAO.getAllBooks();
//        if (books.isEmpty()) {
//            throw new NoDataFoundException("Belum ada data buku di perpustakaan.");
//        }
//        for (Book book : books) {
//            System.out.println(book.toString() + " (Stok: " + book.getQuantity() + ")");
//        }
//    }
//
//    public void displayBorrowedLoans(Member member) throws NoDataFoundException {
//        System.out.println("\n=== Buku yang Saya Pinjam ===");
//        ArrayList<Loan> borrowedLoans = loanDAO.getActiveLoansByUserId(member.getUserId());
//        member.setBorrowedLoans(borrowedLoans);
//
//        if (borrowedLoans == null || borrowedLoans.isEmpty()) {
//            throw new NoDataFoundException("Anda belum meminjam buku.");
//        }
//
//        for (Loan loan : borrowedLoans) {
//            System.out.println("Buku: " + loan.getBook().getTitle() + " (Kode: " + loan.getBook().getCode() + ")");
//            System.out.println("  Tanggal Pinjam: " + loan.getBorrowDate());
//            System.out.println("  Tanggal Jatuh Tempo: " + loan.getDueDate());
//            if (loan.isOverdue(LocalDate.now())) {
//                System.out.println("  Status: TERLAMBAT (Denda Rp " + String.format("%,.2f", loan.calculateFine(LocalDate.now())) + ")");
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
//        Book bookToBorrow = bookDAO.findBookByCode(isbn);
//        if (bookToBorrow == null) {
//            throw new BookNotFoundException("Buku dengan kode ISBN \"" + isbn + "\" tidak ditemukan.");
//        }
//
//        if (bookToBorrow.getQuantity() <= 0) {
//            throw new BookOutOfStockException("Buku \"" + bookToBorrow.getTitle() + "\" ditemukan tetapi stok habis.");
//        }
//
//        if (member.findLoanByBookCode(isbn) != null) {
//            System.out.println("Anda sudah meminjam buku dengan ISBN '" + isbn + "' dan belum mengembalikannya.");
//            return;
//        }
//
//        int newStock = bookToBorrow.getQuantity() - 1;
//        if (!bookDAO.updateBookQuantity(bookToBorrow.getCode(), newStock)) {
//            System.err.println("Gagal memperbarui stok buku di database. Peminjaman dibatalkan.");
//            return;
//        }
//        if (newStock == 0) {
//            bookDAO.updateBookStatus(bookToBorrow.getCode(), "Borrowed");
//        }
//        bookToBorrow.setQuantity(newStock);
//
//        Loan newLoan = new Loan(bookToBorrow, LocalDate.now());
//        newLoan.setQuantity(1);
//        newLoan.setUserId(member.getUserId());
//
//        if (loanDAO.addLoan(newLoan, member)) {
//            member.addLoan(newLoan);
//            System.out.println("\n=== Buku Berhasil Dipinjam ===");
//            System.out.println("Judul      : " + bookToBorrow.getTitle());
//            System.out.println("Kode ISBN  : " + bookToBorrow.getCode());
//            System.out.println("Tgl Pinjam : " + newLoan.getBorrowDate());
//            System.out.println("Jatuh Tempo: " + newLoan.getDueDate());
//            System.out.println("==================================");
//        } else {
//            System.err.println("Gagal mencatat peminjaman di database. Stok buku perlu dikembalikan manual.");
//            bookDAO.updateBookQuantity(bookToBorrow.getCode(), bookToBorrow.getQuantity() + 1);
//            bookToBorrow.setQuantity(bookToBorrow.getQuantity() + 1);
//            if (bookToBorrow.getQuantity() > 0) {
//                bookDAO.updateBookStatus(bookToBorrow.getCode(), "Available");
//            }
//        }
//    }
//
//    public void returnBookAction(Member member) throws NoDataFoundException, BookNotFoundException {
//        System.out.println("\n=== Kembalikan Buku ===");
//        displayBorrowedLoans(member);
//
//        ArrayList<Loan> borrowedLoans = member.getBorrowedLoans();
//
//        if (borrowedLoans.isEmpty()) {
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
//        Loan loanToReturn = null;
//        for (Loan loan : borrowedLoans) {
//            if (loan.getBook().getCode().equalsIgnoreCase(isbn)) {
//                loanToReturn = loan;
//                break;
//            }
//        }
//
//        if (loanToReturn == null) {
//            throw new BookNotFoundException("Buku dengan kode ISBN \"" + isbn + "\" tidak ditemukan dalam daftar pinjaman Anda.");
//        }
//
//        Book bookReturned = loanToReturn.getBook();
//
//        double fine = loanToReturn.calculateFine(LocalDate.now());
//        if (fine > 0) {
//            System.out.println("-----------------------------------------------------");
//            System.out.println("ANDA TERLAMBAT MENGEMBALIKAN BUKU INI!");
//            System.out.println("Judul Buku       : " + bookReturned.getTitle());
//            System.out.println("Tanggal Pinjam   : " + loanToReturn.getBorrowDate());
//            System.out.println("Tanggal J. Tempo : " + loanToReturn.getDueDate());
//            System.out.println("Tanggal Kembali  : " + LocalDate.now());
//            System.out.println("Jumlah Keterlambatan: " + loanToReturn.getOverdueDays(LocalDate.now()) + " hari");
//            System.out.println("Denda per hari   : Rp " + String.format("%,.2f", Loan.FINE_PER_DAY));
//            System.out.println("TOTAL DENDA      : Rp " + String.format("%,.2f", fine));
//            System.out.println("-----------------------------------------------------");
//            System.out.println("Denda ini telah ditambahkan ke total denda Anda. Silakan lakukan pembayaran melalui menu 'Bayar Denda'.");
//            loanDAO.updateLoanFine(loanToReturn.getLoanId(), fine);
//        }
//
//        if (loanDAO.returnLoan(loanToReturn.getLoanId())) {
//            member.removeLoan(loanToReturn);
//
//            Book currentBookState = bookDAO.findBookByCode(bookReturned.getCode());
//            if (currentBookState != null) {
//                int newQuantity = currentBookState.getQuantity() + loanToReturn.getQuantity();
//                if (bookDAO.updateBookQuantity(currentBookState.getCode(), newQuantity)) {
//                    System.out.println("\n=== Buku Berhasil Dikembalikan ===");
//                    System.out.println("Judul: " + bookReturned.getTitle());
//                    if (fine > 0) {
//                        System.out.println("Status: Dikembalikan dengan denda Rp " + String.format("%,.2f", fine));
//                    } else {
//                        System.out.println("Status: Dikembalikan tepat waktu.");
//                    }
//                    bookDAO.updateBookStatus(currentBookState.getCode(), "Available");
//                } else {
//                    System.err.println("Gagal memperbarui stok buku di database setelah pengembalian.");
//                }
//            } else {
//                System.err.println("Error: Tidak dapat menemukan buku " + bookReturned.getCode() + " untuk memperbarui stoknya.");
//            }
//        } else {
//            System.err.println("Gagal menghapus catatan peminjaman dari database.");
//        }
//    }
//
//
//    public void payFine(Member member) {
//        System.out.println("\n=== Bayar Denda ===");
//        ArrayList<Loan> activeLoansWithFine = new ArrayList<>();
//        double totalFineBeforePayment = 0;
//
//        ArrayList<Loan> memberLoans = new ArrayList<>();
//        try {
//            memberLoans = loanDAO.getActiveLoansByUserId(member.getUserId());
//        } catch (NoDataFoundException e) {
//            System.out.println("Info: " + e.getMessage() + " (Tidak ada pinjaman aktif untuk denda awal)");
//
//        } catch (RuntimeException e) {
//            System.err.println("Error memuat daftar pinjaman untuk denda: " + e.getMessage());
//            e.printStackTrace();
//
//        }
//
//
//        for(Loan loan : memberLoans) {
//            double loanFine = loan.calculateFine(LocalDate.now());
//            if (loanFine > 0 || loan.getFine() > 0) {
//                activeLoansWithFine.add(loan);
//                totalFineBeforePayment += Math.max(loanFine, loan.getFine());
//            }
//        }
//
//        if (totalFineBeforePayment == 0) {
//            System.out.println("Anda tidak memiliki denda.");
//            return;
//        }
//        System.out.println("Total denda yang harus dibayar: Rp " + String.format("%,.2f", totalFineBeforePayment));
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
//        double amountAppliedToFine = 0;
//        double remainingFine = totalFineBeforePayment;
//
//        if (nominalPayment >= totalFineBeforePayment) {
//            amountAppliedToFine = totalFineBeforePayment;
//            remainingFine = 0;
//            System.out.println("Pembayaran berhasil! Denda lunas.");
//            if (nominalPayment > totalFineBeforePayment) {
//                System.out.println("Kembalian: Rp " + String.format("%,.2f", (nominalPayment - totalFineBeforePayment)));
//            }
//        } else {
//            amountAppliedToFine = nominalPayment;
//            remainingFine = totalFineBeforePayment - nominalPayment;
//            member.setFee(remainingFine);
//            System.out.println("Pembayaran sebagian berhasil. Sisa denda: Rp " + String.format("%,.2f", remainingFine));
//        }
//
//        Transaction transaction = new Transaction(member, amountAppliedToFine, totalFineBeforePayment, remainingFine);
//        if (transactionDAO.addTransaction(transaction)) {
//            System.out.println("Transaksi pembayaran telah dicatat. ID Transaksi: " + transaction.getTransactionId());
//        } else {
//            System.err.println("Gagal mencatat transaksi pembayaran di database.");
//            member.setFee(totalFineBeforePayment);
//        }
//    }
//
//    public void logout() {
//        System.out.println("\nAnda telah logout dari sesi Member.");
//    }
//}