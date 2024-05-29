package http.client.exception;

public class IncorrectDataException extends RuntimeException {
    public IncorrectDataException() {
        super("Дата некорректна!");
    }
}
