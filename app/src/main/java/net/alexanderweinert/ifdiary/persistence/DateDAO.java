package net.alexanderweinert.ifdiary.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface DateDAO {
    @Query("SELECT * FROM datedeo")
    List<DateDEO> getAllDates();

    @Query("SELECT * FROM datedeo ORDER BY year DESC, month DESC, day DESC LIMIT 1")
    DateDEO getLatestDate();

    @Query("SELECT * FROM datedeo WHERE year = :year AND month = :month AND day = :day LIMIT 1")
    DateDEO getEntryForDate(int year, int month, int day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DateDEO dbEntry);

    @Query("DELETE FROM datedeo")
    void deleteAll();
}
