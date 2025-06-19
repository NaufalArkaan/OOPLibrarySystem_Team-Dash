package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedReturnData {

    private static final ObservableList<ReturnRecord> returnRecords = FXCollections.observableArrayList();

    private SharedReturnData() {}

    public static ObservableList<ReturnRecord> getReturnRecords() {
        return returnRecords;
    }

    public static void addReturnRecord(ReturnRecord record) {
        returnRecords.add(record);
    }

    public static void removeReturnRecord(ReturnRecord record) {
        returnRecords.remove(record);
    }

    public static void clear() {
        returnRecords.clear();
    }

    public static ReturnRecord findRecord(String studentId, String bookTitle) {
        for (ReturnRecord record : returnRecords) {
            if (record.getStudentId().equals(studentId) &&
                    record.getBookTitle().equalsIgnoreCase(bookTitle)) {
                return record;
            }
        }
        return null;
    }

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
