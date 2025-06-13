package controller.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Menyimpan data peminjaman buku untuk halaman History.
 */
public class SharedHistoryData {
    private static final ObservableList<HistoryRecord> historyRecords = FXCollections.observableArrayList();

    private SharedHistoryData() {
        // Mencegah instansiasi
    }

    public static ObservableList<HistoryRecord> getHistoryRecords() {
        return historyRecords;
    }

    public static void addHistoryRecord(HistoryRecord record) {
        historyRecords.add(record);
    }

    public static void removeHistoryRecord(HistoryRecord record) {
        historyRecords.remove(record);
    }
}
