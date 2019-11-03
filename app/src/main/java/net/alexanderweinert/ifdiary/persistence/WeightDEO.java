package net.alexanderweinert.ifdiary.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = DateDEO.class, parentColumns = "id", childColumns = "date_id"))
class WeightDEO {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = "date_id")
    public int dateId;

    // Given in dekagrams, e.g., a value of 942 denotes a weight of 94.2kg.
    @ColumnInfo
    public int weight;


}
