package net.alexanderweinert.ifdiary.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface DBEntryDAO {
    @Query("SELECT * FROM dbentry")
    List<DBEntry> getAllDbEntries();

    @Query("SELECT * FROM DBEntry ORDER BY year DESC, month DESC, day DESC")
    DBEntry getLastEntry();

    @Query("SELECT * FROM DBEntry WHERE year = :year AND month = :month AND day = :day")
    DBEntry getEntryForDate(int year, int month, int day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DBEntry dbEntry);

    @Query("DELETE FROM dbentry")
    void deleteAll();
}
