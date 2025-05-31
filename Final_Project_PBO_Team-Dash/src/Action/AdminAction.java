// File: src/Action/AdminAction.java
package Action;

import Data.Book; //
import Data.Loan; //
import SQL_DATA.BookDAO; //
import SQL_DATA.LoanDAO; //
import SQL_DATA.TransactionDAO; //
import SQL_DATA.UserDAO; //
import ExceptionHandle.BookNotFoundException; //
import ExceptionHandle.MemberNotFoundException; //
import ExceptionHandle.NoDataFoundException; //
import User.Admin; //
import User.Member; //

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminAction {
    private Scanner scanner = new Scanner(System.in);
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private LoanDAO loanDAO;
    private TransactionDAO transactionDAO;

    public AdminAction() {
        this.bookDAO = new BookDAO();
        this.userDAO = new UserDAO();
        this.loanDAO = new LoanDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public int displayMenu() {
        System.out.println("\n=== Menu Admin Perpustakaan UMM ==="); //
        System.out.println("0. Dashboard (Statistik Cepat)"); //
        System.out.println("1. Kelola Buku"); //
        System.out.println("2. Proses Pengembalian Buku (oleh Admin)"); //
        System.out.println("3. Lihat Daftar Member"); //
        System.out.println("4. Lihat Denda Member (Perhitungan Saat Ini)");
        System.out.println("5. Lihat Laporan Peminjaman Buku Aktif"); //
        System.out.println("6. Lihat Riwayat Transaksi Pembayaran Denda"); //
        System.out.println("7. Logout"); //
        System.out.print("Masukkan pilihan Anda: ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Consume invalid input
            System.out.println("Input tidak valid. Harap masukkan angka sebagai pilihan.");
            return -1; // Indicates an invalid input type
        }
    }

    public void processMenu(Admin admin) {
        int pilihan;
        do {
            pilihan = displayMenu();

            if (pilihan == -1) { // Invalid input type from displayMenu
                continue;
            }
            try {
                switch (pilihan) {
                    case 0: displayDashboard(); break;
                    case 1: manageBook(); break;
                    case 2: processAdminBookReturn(); break;
                    case 3: listMembers(); break;
                    case 4: viewMemberFines(); break;
                    case 5: viewLoanReports(); break;
                    case 6: viewTransactionHistory(); break;
                    case 7: logout(admin); break;
                    default: System.out.println("Pilihan tidak valid, coba lagi."); break;
                }
            } catch (MemberNotFoundException | NoDataFoundException | BookNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Error Input: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.err.println("Critical Error: " + e.getMessage() + " Harap hubungi administrator.");
            } catch (Exception e) {
                System.err.println("Terjadi kesalahan tak terduga: " + e.getMessage());
                e.printStackTrace();
            }
        } while (pilihan != 7);
    }

    public void displayDashboard() {
        System.out.println("\n=== Dashboard Admin ===");
        System.out.println("--- Statistik Cepat ---");

        ArrayList<Book> allBooks = bookDAO.getAllBooks(); //
        int totalUniqueTitles = allBooks.size();
        int totalAllCopies = 0;
        for (Book book : allBooks) {
            totalAllCopies += book.getQuantity(); //
        }
        int totalBorrowedCount = loanDAO.getTotalActiveLoans(); //

        System.out.println("Total Judul Buku Unik : " + totalUniqueTitles);
        System.out.println("Total Eksemplar Buku  : " + totalAllCopies);
        System.out.println("Buku Sedang Dipinjam  : " + totalBorrowedCount);
        System.out.println("-----------------------");
    }

    public void manageBook() throws BookNotFoundException, NoDataFoundException {
        int bookActionChoice = 0; // Initialize at declaration
        do {
            System.out.println("\n=== Kelola Buku ===");
            System.out.println("1. Tambah Buku Baru / Tambah Stok Buku Lama");
            System.out.println("2. Edit Detail Buku");
            System.out.println("3. Ubah Kuantitas Buku Tertentu");
            System.out.println("4. Hapus Buku");
            System.out.println("5. Kembali ke Menu Admin");
            System.out.print("Masukkan pilihan Anda (Kelola Buku): ");
            try {
                if (scanner.hasNextInt()) { // Check if the next input is an integer
                    bookActionChoice = scanner.nextInt();
                } else {
                    System.out.println("Input tidak valid. Harap masukkan angka.");
                    bookActionChoice = 0; // Default to loop again on non-integer input
                }
                scanner.nextLine(); // Consume newline or the invalid input

                switch (bookActionChoice) {
                    case 1: addBookOrStock(); break;
                    case 2: editBookDetails(); break;
                    case 3: updateBookQuantityMenu(); break;
                    case 4: removeBook(); break;
                    case 5: System.out.println("Kembali ke Menu Admin..."); break;
                    default:
                        // Avoid printing "tidak valid" if user choice is not 5 but simply out of menu range,
                        // as the loop condition handles continuation.
                        if (bookActionChoice != 5) {
                            System.out.println("Pilihan tidak valid untuk Kelola Buku. Silakan coba lagi.");
                        }
                        break;
                }
            } catch (InputMismatchException e) { // This catch handles cases where nextInt() fails unexpectedly
                scanner.nextLine(); // Consume invalid input
                System.out.println("Input tidak valid. Harap masukkan angka untuk pilihan Kelola Buku.");
                bookActionChoice = 0; // Reset choice to loop again
            } catch (IllegalArgumentException iae) {
                System.err.println("Error Input Data Buku: " + iae.getMessage());
            }
            // BookNotFoundException and NoDataFoundException are declared to be thrown by manageBook
            // and will be caught by the processMenu method's catch blocks.
        } while (bookActionChoice != 5);
    }

    public void addBookOrStock() throws IllegalArgumentException {
        System.out.println("\n=== Tambah Buku Baru / Tambah Stok Buku Lama ===");
        System.out.print("Masukkan Kode Buku (ISBN): ");
        String kode = scanner.nextLine();
        if (kode.trim().isEmpty()) {
            System.out.println("Kode Buku (ISBN) tidak boleh kosong.");
            return;
        }

        Book existingBook = bookDAO.findBookByCode(kode); //

        if (existingBook != null) {
            System.out.println("Buku dengan kode " + kode + " (\"" + existingBook.getTitle() + "\") sudah ada."); //
            System.out.print("Masukkan jumlah stok yang ingin ditambahkan: ");
            int tambahanKuantitas = getInputPositiveInt("Jumlah tambahan stok");
            int kuantitasBaru = existingBook.getQuantity() + tambahanKuantitas; //
            if (bookDAO.updateBookQuantity(kode, kuantitasBaru)) { //
                System.out.println("Stok buku \"" + existingBook.getTitle() + "\" berhasil diperbarui menjadi " + kuantitasBaru + "."); //
            } else {
                System.out.println("Gagal memperbarui stok buku di database.");
            }
            return;
        }

        System.out.println("Buku dengan kode " + kode + " belum ada. Menambahkan sebagai buku baru.");
        System.out.print("Masukkan Judul Buku: ");
        String judul = scanner.nextLine();
        if (judul.trim().isEmpty()) { System.out.println("Judul tidak boleh kosong."); return; }

        System.out.print("Masukkan Penulis Buku: ");
        String penulis = scanner.nextLine();
        if (penulis.trim().isEmpty()) { System.out.println("Penulis tidak boleh kosong."); return; }

        System.out.print("Masukkan Kategori Buku: ");
        String kategori = scanner.nextLine();
        if (kategori.trim().isEmpty()) { System.out.println("Kategori tidak boleh kosong."); return; }

        System.out.print("Masukkan Jumlah Awal (Kuantitas): ");
        int kuantitasAwal = getInputPositiveInt("Jumlah awal");

        Book newBook = new Book(kode, judul, penulis, kategori, kuantitasAwal); //
        if (bookDAO.addBook(newBook)) { //
            System.out.println("Buku baru \"" + judul + "\" (Kategori: " + kategori + ") dengan kode " + kode + " sebanyak " + kuantitasAwal + " buah berhasil ditambahkan!");
        } else {
            System.out.println("Gagal menambahkan buku baru ke database. Kode buku mungkin sudah ada atau terjadi kesalahan lain.");
        }
    }

    public void editBookDetails() throws BookNotFoundException {
        System.out.println("\n=== Edit Detail Buku ===");
        System.out.print("Masukkan Kode Buku (ISBN) yang akan diedit: ");
        String code = scanner.nextLine();
        if (code.trim().isEmpty()) {
            System.out.println("Kode buku tidak boleh kosong.");
            return;
        }

        Book book = bookDAO.findBookByCode(code); //
        if (book == null) {
            throw new BookNotFoundException("Buku dengan kode " + code + " tidak ditemukan.");
        }

        System.out.println("Detail buku saat ini: " + book.toString()); //

        System.out.print("Masukkan judul baru (kosongkan jika tidak ingin mengubah '" + book.getTitle() + "'): "); //
        String newTitle = scanner.nextLine();
        if (!newTitle.trim().isEmpty()) {
            book.setTitle(newTitle); //
        }

        System.out.print("Masukkan penulis baru (kosongkan jika tidak ingin mengubah '" + book.getAuthor() + "'): "); //
        String newAuthor = scanner.nextLine();
        if (!newAuthor.trim().isEmpty()) {
            book.setAuthor(newAuthor); //
        }

        System.out.print("Masukkan kategori baru (kosongkan jika tidak ingin mengubah '" + book.getCategory() + "'): "); //
        String newCategory = scanner.nextLine();
        if (!newCategory.trim().isEmpty()) {
            book.setCategory(newCategory); //
        }

        if (bookDAO.updateBook(book)) { //
            System.out.println("Detail buku berhasil diperbarui.");
        } else {
            System.out.println("Gagal memperbarui detail buku di database.");
        }
    }

    public void updateBookQuantityMenu() throws BookNotFoundException {
        System.out.println("\n=== Ubah Kuantitas Buku Tertentu ===");
        System.out.print("Masukkan Kode Buku (ISBN) yang kuantitasnya akan diubah: ");
        String code = scanner.nextLine();
        if (code.trim().isEmpty()) {
            System.out.println("Kode buku tidak boleh kosong.");
            return;
        }

        Book book = bookDAO.findBookByCode(code); //
        if (book == null) {
            throw new BookNotFoundException("Buku dengan kode " + code + " tidak ditemukan.");
        }

        System.out.println("Buku: " + book.getTitle() + ", Kuantitas saat ini: " + book.getQuantity()); //
        System.out.print("Masukkan kuantitas baru: ");
        int newQuantity = getInputPositiveInt("Kuantitas baru");

        if (bookDAO.updateBookQuantity(code, newQuantity)) { //
            System.out.println("Kuantitas buku berhasil diperbarui menjadi " + newQuantity + ".");
        } else {
            System.out.println("Gagal memperbarui kuantitas buku di database.");
        }
    }

    public void removeBook() throws BookNotFoundException {
        System.out.println("\n=== Hapus Buku ===");
        System.out.print("Masukkan Kode Buku (ISBN) yang akan dihapus: ");
        String code = scanner.nextLine();
        if (code.trim().isEmpty()) {
            System.out.println("Kode buku tidak boleh kosong.");
            return;
        }

        Book book = bookDAO.findBookByCode(code); //
        if (book == null) {
            throw new BookNotFoundException("Buku dengan kode " + code + " tidak ditemukan.");
        }

        System.out.println("Anda akan menghapus buku: " + book.toString()); //
        System.out.print("Apakah Anda yakin? (Y/N): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {
            if (bookDAO.deleteBook(code)) { //
                System.out.println("Buku berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus buku. Buku mungkin sedang dipinjam atau terjadi kesalahan lain.");
            }
        } else {
            System.out.println("Penghapusan buku dibatalkan.");
        }
    }

    public void processAdminBookReturn() throws MemberNotFoundException, BookNotFoundException, NoDataFoundException {
        System.out.println("\n=== Proses Pengembalian Buku (oleh Admin) ===");

        System.out.println("Daftar Member:");
        listMembers();

        System.out.print("Masukkan User ID member yang mengembalikan buku: ");
        int userId;
        try {
            userId = scanner.nextInt(); // This can throw InputMismatchException
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Consume the invalid input if nextInt() failed partway
            System.err.println("User ID tidak valid. Harap masukkan angka.");
            return;
        }

        ArrayList<Member> members = userDAO.getAllMembers(); //
        Member returningMember = null;
        for (Member m : members) {
            if (m.getUserId() == userId) { //
                returningMember = m;
                break;
            }
        }
        if (returningMember == null) {
            throw new MemberNotFoundException("Member dengan User ID " + userId + " tidak ditemukan.");
        }
        System.out.println("Member yang dipilih: " + returningMember.getName()); //


        ArrayList<Loan> activeLoans = loanDAO.getActiveLoansByUserId(userId); //
        if (activeLoans.isEmpty()) {
            System.out.println("Member dengan User ID " + userId + " tidak memiliki buku yang sedang dipinjam.");
            return;
        }

        System.out.println("\nBuku yang sedang dipinjam oleh member (User ID: " + userId + "):");
        for (int i = 0; i < activeLoans.size(); i++) {
            System.out.println((i + 1) + ". " + activeLoans.get(i).toString()); //
        }

        System.out.print("Masukkan kode buku (ISBN) yang dikembalikan: ");
        String bookCode = scanner.nextLine();
        if (bookCode.trim().isEmpty()) {
            System.out.println("Kode buku tidak boleh kosong.");
            return;
        }

        Loan loanToReturn = null;
        for (Loan loan : activeLoans) {
            if (loan.getBook().getCode().equalsIgnoreCase(bookCode)) { //
                loanToReturn = loan;
                break;
            }
        }

        if (loanToReturn == null) {
            throw new BookNotFoundException("Buku dengan kode " + bookCode + " tidak ditemukan dalam daftar pinjaman member ini.");
        }

        double fineAmount = loanToReturn.calculateFine(LocalDate.now()); //
        if (fineAmount > 0) {
            System.out.println("DENDA TERDETEKSI!");
            System.out.println("Jumlah hari terlambat: " + loanToReturn.getOverdueDays(LocalDate.now())); //
            System.out.println("Total denda: Rp " + String.format("%,.2f", fineAmount));
            System.out.println("Harap informasikan member untuk membayar denda melalui menu member.");
        }

        if (loanDAO.removeLoan(userId, bookCode)) { //
            System.out.println("Data peminjaman buku '" + loanToReturn.getBook().getTitle() + "' berhasil dihapus."); //

            Book returnedBook = bookDAO.findBookByCode(bookCode); //
            if (returnedBook != null) {
                int newQuantity = returnedBook.getQuantity() + 1; //
                if (bookDAO.updateBookQuantity(bookCode, newQuantity)) { //
                    System.out.println("Stok buku '" + returnedBook.getTitle() + "' berhasil diperbarui menjadi " + newQuantity + "."); //
                } else {
                    System.err.println("Peringatan: Gagal memperbarui stok buku '" + returnedBook.getTitle() + "' di database."); //
                }
            } else {
                System.err.println("Peringatan: Buku dengan kode " + bookCode + " tidak ditemukan untuk pembaruan stok (seharusnya ada).");
            }
            System.out.println("Pengembalian buku selesai diproses.");
        } else {
            System.err.println("Gagal menghapus data peminjaman dari database.");
        }
    }


    public void listMembers() throws NoDataFoundException {
        System.out.println("\n=== Daftar Member ===");
        ArrayList<Member> members = userDAO.getAllMembers(); //

        if (members.isEmpty()) {
            throw new NoDataFoundException("Belum ada member terdaftar.");
        }
        System.out.printf("%-5s | %-25s | %-15s | %-20s | %-30s\n", "ID", "Nama", "Student ID", "Jurusan", "Email");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Member m : members) {
            System.out.printf("%-5d | %-25s | %-15s | %-20s | %-30s\n",
                    m.getUserId(), //
                    m.getName(), //
                    m.getStudentId(), //
                    m.getMajor(), //
                    m.getEmail()); //
        }
    }

    public void viewMemberFines() throws NoDataFoundException {
        System.out.println("\n=== Denda Member (Perhitungan Saat Ini Berdasarkan Keterlambatan) ===");
        ArrayList<Member> members = userDAO.getAllMembers(); //

        if (members.isEmpty()) {
            throw new NoDataFoundException("Belum ada member terdaftar untuk dicek dendanya.");
        }

        boolean anyFineFound = false;
        System.out.printf("%-5s | %-25s | %-15s | %-15s\n", "ID", "Nama Member", "Student ID", "Total Denda");
        System.out.println("------------------------------------------------------------------------");

        for (Member member : members) {
            ArrayList<Loan> memberLoans = loanDAO.getActiveLoansByUserId(member.getUserId()); //
            double totalFineForMember = 0;
            if (!memberLoans.isEmpty()) {
                for (Loan loan : memberLoans) {
                    totalFineForMember += loan.calculateFine(LocalDate.now()); //
                }
            }

            if (totalFineForMember > 0) {
                System.out.printf("%-5d | %-25s | %-15s | Rp %,-12.2f\n",
                        member.getUserId(), //
                        member.getName(), //
                        member.getStudentId(), //
                        totalFineForMember);
                anyFineFound = true;
            }
        }

        if (!anyFineFound) {
            System.out.println("Tidak ada member yang memiliki denda keterlambatan saat ini.");
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("* Total Denda dihitung berdasarkan keterlambatan buku yang sedang dipinjam saat ini.");
    }


    public void viewLoanReports() throws NoDataFoundException {
        System.out.println("\n=== Laporan Peminjaman Buku Aktif ===");

        ArrayList<Member> members = userDAO.getAllMembers(); //
        if (members.isEmpty()) {
            throw new NoDataFoundException("Tidak ada member terdaftar, maka tidak ada laporan peminjaman.");
        }

        boolean anyLoanFound = false;
        System.out.printf("%-25s (ID: %-5s) | %-30s (Kode: %-10s) | Pinjam: %-10s | Tempo: %-10s\n",
                "Nama Member", "User ID", "Judul Buku", "Kode Buku", "Tgl Pinjam", "Jatuh Tempo");
        System.out.println(new String(new char[120]).replace('\0', '-'));


        for (Member member : members) {
            ArrayList<Loan> memberLoans = loanDAO.getActiveLoansByUserId(member.getUserId()); //
            if (!memberLoans.isEmpty()) {
                anyLoanFound = true;
                for (Loan loan : memberLoans) {
                    System.out.printf("%-25s (ID: %-5d) | %-30s (Kode: %-10s) | %-10s | %-10s\n",
                            member.getName(), //
                            member.getUserId(), //
                            loan.getBook().getTitle(), //
                            loan.getBook().getCode(), //
                            loan.getBorrowDate().toString(), //
                            loan.getDueDate().toString()); //
                }
            }
        }

        if (!anyLoanFound) {
            System.out.println("Tidak ada buku yang sedang dipinjam saat ini.");
        }
        System.out.println(new String(new char[120]).replace('\0', '-'));
    }


    public void viewTransactionHistory() throws NoDataFoundException {
        System.out.println("\n=== Riwayat Transaksi Pembayaran Denda ===");
        ArrayList<Transaction> transactions = transactionDAO.getAllTransactions(); //

        if (transactions.isEmpty()) {
            throw new NoDataFoundException("Belum ada riwayat transaksi pembayaran denda.");
        }
        int count = 1;
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            System.out.println("--- Transaksi Ke-" + count + " ---");
            System.out.println(transaction.toString()); //
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            count++;
        }
    }

    private int getInputPositiveInt(String fieldName) {
        int number;
        while (true) {
            System.out.print("Masukkan " + fieldName + ": ");
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                scanner.nextLine(); // Bersihkan buffer
                if (number >= 0) {
                    break;
                } else {
                    System.out.println(fieldName + " tidak boleh negatif. Masukkan lagi.");
                }
            } else {
                System.out.println("Input tidak valid untuk " + fieldName + ". Masukkan angka.");
                scanner.nextLine(); // Bersihkan buffer
            }
        }
        return number;
    }

    private void logout(Admin admin) {
        System.out.println("\nAdmin " + admin.getName() + " telah logout."); //
    }
}