package net.alexanderweinert.ifdiary.persistence;

import android.content.Context;

import androidx.room.Room;

import net.alexanderweinert.dateservice.Date;

import java.util.List;
import java.util.Optional;

class PersistenceServiceImpl extends PersistenceService {

    private final IFDatabase database;

    public PersistenceServiceImpl(Context context) {
        this.database = Room.databaseBuilder(context, IFDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.database.close();
    }

    @Override
    public Optional<Boolean> hasFasted(Date date) throws PersistenceServiceException {
        /*
        final FastingDEO deo = this.database.fastingDao().getEntryForDate(date.getYear(), date.getMonth(), date.getDay());
        if(deo == null) {
            return Optional.empty();
        } else {
            return Optional.of(deo.isFasted());
        }
         */
        return null;
    }

    @Override
    public void setFasting(Date date, boolean hasFasted) throws PersistenceServiceException {
        /*
        final DBEntry entry = DBEntry.builder()
                .year(date.getYear())
                .month(date.getMonth())
                .day(date.getDay())
                .fasted(hasFasted)
                .build();

        final List<DBEntry> list = this.database.dbEntryDAO().getAllDbEntries();
        this.database.dbEntryDAO().insert(entry);
         */
    }
}
