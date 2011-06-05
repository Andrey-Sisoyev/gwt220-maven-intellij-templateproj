package utils;

public class EntityExistsNotException extends Exception {
    // ================================
    // CONSTRUCTORS

    public EntityExistsNotException() {
    }

    public EntityExistsNotException(String message) {
        super(message);
    }

    public EntityExistsNotException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityExistsNotException(Throwable cause) {
        super(cause);
    }
}

