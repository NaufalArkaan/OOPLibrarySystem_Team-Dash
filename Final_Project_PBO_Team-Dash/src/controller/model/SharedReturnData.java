package controller.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedReturnData {

    // Singleton ObservableList: data yang dibagikan ke semua controller
    private static final ObservableList<ReturnRecord> returnRecords = FXCollections.observableArrayList();

    // Private constructor: mencegah instansiasi
    private SharedReturnData() {}

    // Akses data pengembalian
    public static ObservableList<ReturnRecord> getReturnRecords() {
        return returnRecords;
    }

    // Tambah record baru
    public static void addReturnRecord(ReturnRecord record) {
        returnRecords.add(record);
    }

    // Hapus record tertentu
    public static void removeReturnRecord(ReturnRecord record) {
        returnRecords.remove(record);
    }

    // Hapus semua data (opsional, untuk reset/debug)
    public static void clear() {
        returnRecords.clear();
    }

    // Cari record berdasarkan student ID dan judul buku (optional helper)
    public static ReturnRecord findRecord(String studentId, String bookTitle) {
        for (ReturnRecord record : returnRecords) {
            if (record.getStudentId().equals(studentId) &&
                    record.getBookTitle().equalsIgnoreCase(bookTitle)) {
                return record;
            }
        }
        return null;
    }

    // Update status dan tanggal kembali (untuk sinkronisasi)
    public static void updateStatus(ReturnRecord updatedRecord) {
        for (int i = 0; i < returnRecords.size(); i++) {
            ReturnRecord r = returnRecords.get(i);
            if (r.getNo() == updatedRecord.getNo()) {
                returnRecords.set(i, updatedRecord);
                break;
            }
        }
    }
}
