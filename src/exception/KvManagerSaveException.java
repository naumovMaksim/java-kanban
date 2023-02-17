package exception;

public class KvManagerSaveException extends RuntimeException {

    public KvManagerSaveException(String message) {
            super(message);
    }

    public KvManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public KvManagerSaveException(Throwable cause) {
        super(cause);
    }
}
