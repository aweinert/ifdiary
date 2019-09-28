package net.alexanderweinert.logging;

import net.alexanderweinert.ifdiary.persistence.PersistenceServiceException;

public abstract class LoggingService {
    public static LoggingService instance() {
        return new LoggingServiceImpl();
    }

    public abstract void error(String message, Throwable exception);
}
