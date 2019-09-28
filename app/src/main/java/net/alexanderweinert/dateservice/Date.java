package net.alexanderweinert.dateservice;

import java.io.Serializable;

public class Date implements Serializable {

    public static class Builder {
        private Integer year = null;
        private Integer month = null;
        private Integer day = null;

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

        public Date build() {
            return new Date(year, month, day);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    final private int year;
    final private int month;
    final private int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Date)) {
            return false;
        }

        final Date otherDate = (Date) other;

        return
                otherDate.day == this.day &&
                otherDate.month == this.month &&
                otherDate.year == this.year;
    }
}
