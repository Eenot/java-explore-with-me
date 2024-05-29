package http.client.exception;

public class IpIsNullException extends RuntimeException {
    public IpIsNullException() {
        super("IP-адрес пуст!");
    }
}
