package ExceptionHandle;

public class BookOutOfStockException extends Exception {
    public BookOutOfStockException(String message) {
        super(message);
    }
}