package Action;

import User.Member;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private Member member;
    private LocalDateTime transactionDateTime;
    private double amountPaid;
    private double fineBeforePayment;
    private double fineAfterPayment;

    public Transaction(Member member, double amountPaid, double fineBeforePayment, double fineAfterPayment) {
        this.transactionId = UUID.randomUUID().toString();
        this.member = member;
        this.transactionDateTime = LocalDateTime.now();
        this.amountPaid = amountPaid;
        this.fineBeforePayment = fineBeforePayment;
        this.fineAfterPayment = fineAfterPayment;
    }

    public Transaction(String transactionId, Member member, LocalDateTime transactionDateTime, double amountPaid, double fineBeforePayment, double fineAfterPayment) {
        this.transactionId = transactionId;
        this.member = member;
        this.transactionDateTime = transactionDateTime;
        this.amountPaid = amountPaid;
        this.fineBeforePayment = fineBeforePayment;
        this.fineAfterPayment = fineAfterPayment;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getFineBeforePayment() {
        return fineBeforePayment;
    }

    public double getFineAfterPayment() {
        return fineAfterPayment;
    }

    @Override
    public String toString() {
        return "Transaction {\n" +
                "  ID Transaksi    : " + transactionId + "\n" +
                "  Member          : " + member.getName() + " (ID: " + member.getStudentId() + ")\n" +
                "  Waktu Transaksi : " + transactionDateTime.toLocalDate() + " " + transactionDateTime.toLocalTime().withNano(0) + "\n" +
                "  Denda Sebelumnya: Rp " + fineBeforePayment + "\n" +
                "  Jumlah Dibayar  : Rp " + amountPaid + "\n" +
                "  Sisa Denda      : Rp " + fineAfterPayment + "\n" +
                "}";
    }
}
