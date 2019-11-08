package net.alexanderweinert.ifdiary.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = DateDEO.class, parentColumns = "id", childColumns = "date_id"))
class FastingDEO {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = "date_id")
    public int dateId;

    @ColumnInfo
    public int firstMealStartInMinutes;

    @ColumnInfo
    public int lastMealEndInMinutes;
}
