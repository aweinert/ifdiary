package net.alexanderweinert.ifdiary.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
class DateDEO {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo
    public int year;

    @ColumnInfo
    public int month;

    @ColumnInfo
    public int day;

    @Override
    public String toString() {
        return String.format("DBEntry { id = %d, date = %s-%s-%s }", id, year, month, day);
    }
}
