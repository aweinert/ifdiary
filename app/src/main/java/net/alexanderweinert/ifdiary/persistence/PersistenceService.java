package net.alexanderweinert.ifdiary.persistence;

import android.content.Context;

import net.alexanderweinert.dateservice.Date;

import java.util.Optional;

public abstract class PersistenceService {
    public static PersistenceService instance(Context context) {
        return new PersistenceServiceImpl(context);
    }

    public abstract Optional<Boolean> hasFasted(Date date) throws PersistenceServiceException;

    public boolean fastingStored(Date date) throws PersistenceServiceException {
        return hasFasted(date).isPresent();
    }

    public abstract void setFasting(Date date, boolean hasFasted) throws PersistenceServiceException;
}
