package net.alexanderweinert.ifdiary.persistence;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities =  {DateDEO.class, FastingDEO.class, WeightDEO.class}, version = 2)
abstract class IFDatabase extends RoomDatabase {
    public abstract DateDAO dateDAO();
    public abstract FastingDAO fastingDAO();
    public abstract WeightDAO weightDAO();
}
