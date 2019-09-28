package net.alexanderweinert.ifdiary.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
class DBEntry {

    public static class Builder {
        private int year, month, day;
        private boolean fasted;

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder month(int month) {
            this.month = month;
            return this;
        }

        public Builder day(int day) {
            this.day = day;
            return this;
        }

        public Builder fasted(boolean fasted) {
            this.fasted = fasted;
            return this;
        }

        public DBEntry build() {
            final DBEntry retVal = new DBEntry();
            retVal.date = new Date(year, month, day);
            retVal.fasted = this.fasted;
            return retVal;
        }
    }

    public static class Date {
        public int year, month, day;

        public Date(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public int getYear() {
            return this.year;
        }

        public int getMonth() {
            return this.month;
        }

        public int getDay() {
            return this.day;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Embedded
    @PrimaryKey
    @NonNull
    private Date date;

    @ColumnInfo
    private boolean fasted;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFasted(boolean fasted) {
        this.fasted = fasted;
    }

    public Date getDate() {
        return date;
    }

    public boolean isFasted() {
        return fasted;
    }

    public String toString() {
        return String.format("DBEntry { date = %s-%s-%s, fasted = %s }", date.year, date.month, date.day, fasted);
    }
}
