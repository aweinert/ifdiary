package net.alexanderweinert.ifdiary.persistence;

public class PersistenceServiceException extends Exception {
    public PersistenceServiceException() {
    }

    public PersistenceServiceException(String message) {
        super(message);
    }

    public PersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceServiceException(Throwable cause) {
        super(cause);
    }
}
