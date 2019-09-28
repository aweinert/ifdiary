package net.alexanderweinert.ifdiary.persistence;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities =  {DBEntry.class}, version = 1)
abstract class IFDatabase extends RoomDatabase {
    public abstract DBEntryDAO dbEntryDAO();
}
