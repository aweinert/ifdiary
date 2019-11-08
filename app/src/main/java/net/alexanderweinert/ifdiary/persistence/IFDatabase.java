package net.alexanderweinert.ifdiary.persistence;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities =  {DateDEO.class, FastingDEO.class, WeightDEO.class}, version = 2)
abstract class IFDatabase extends RoomDatabase {
    static final Migration Migration1To2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE DateDEO (" +
                    "id INTEGER NOT NULL," +
                    "year INTEGER NOT NULL," +
                    "month INTEGER NOT NULL," +
                    "day INTEGER NOT NULL," +
                    "PRIMARY KEY(id));");
            database.execSQL("CREATE TABLE FastingDEO (" +
                    "id INTEGER NOT NULL," +
                    "date_id INTEGER NOT NULL," +
                    "firstMealStartInMinutes INTEGER NOT NULL," +
                    "lastMealEndInMinutes INTEGER NOT NULL," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY (date_id) REFERENCES DateDEO(id));");
            database.execSQL("CREATE TABLE WeightDEO (" +
                    "weight INTEGER NOT NULL," +
                    "id INTEGER NOT NULL," +
                    "date_id INTEGER NOT NULL," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY (date_id) REFERENCES DateDEO(id));");

            final Cursor dbEntryCursor = database.query("SELECT * FROM DBEntry");

            final int yearIndex = dbEntryCursor.getColumnIndex("year");
            final int monthIndex = dbEntryCursor.getColumnIndex("month");
            final int dayIndex = dbEntryCursor.getColumnIndex("day");
            final int fastedIndex = dbEntryCursor.getColumnIndex("fasted");

            while (dbEntryCursor.moveToNext()) {
                final int year = dbEntryCursor.getInt(yearIndex);
                final int month = dbEntryCursor.getInt(monthIndex);
                final int day = dbEntryCursor.getInt(dayIndex);
                final int fasted = dbEntryCursor.getInt(fastedIndex);

                database.execSQL("INSERT INTO DateDEO (year, month, day) VALUES (?,?,?);", new Object[]{ year, month, day});
                final Cursor idCursor = database.query("SELECT last_insert_rowid();");
                idCursor.moveToNext();
                final int dateId = idCursor.getInt(idCursor.getColumnIndex("last_insert_rowid()"));

                final int firstMealStartInMinutes = (fasted == 1) ? 8*60 : 0;
                final int lastMealEndedInMinutes = (fasted == 1) ? 15*60 : 23*60;
                database.execSQL("INSERT INTO FastingDEO (date_id, firstMealStartInMinutes, lastMealEndInMinutes) VALUES (?,?,?);", new Object[] { dateId, firstMealStartInMinutes, lastMealEndedInMinutes });
            }

            database.execSQL("DROP TABLE DBEntry;");
        }
    };
    public abstract DateDAO dateDAO();
    public abstract FastingDAO fastingDAO();
    public abstract WeightDAO weightDAO();
}
