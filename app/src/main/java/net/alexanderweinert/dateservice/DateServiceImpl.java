package net.alexanderweinert.dateservice;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Iterator;

class DateServiceImpl extends DateService {
    private Date fromCalendar(Calendar calendar) {
        return Date.builder()
                .year(calendar.get(Calendar.YEAR))
                .month(calendar.get(Calendar.MONTH))
                .day(calendar.get(Calendar.DAY_OF_MONTH))
                .build();
    }

    private Calendar fromDate(Date date) {
       final Calendar calendar = Calendar.getInstance();
       calendar.set(date.getYear(), date.getMonth(), date.getDay());
       return calendar;
    }

    @Override
    public Date getToday() {
        final Calendar today = Calendar.getInstance();
        return fromCalendar(today);
    }

    @Override
    public Date getTomorrow() {
        final Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        return fromCalendar(tomorrow);
    }

    @Override
    public Date getTomorrow(final Date today) {
        final Calendar tomorrow = fromDate(today);
        tomorrow.add(Calendar.DATE, 1);
        return fromCalendar(tomorrow);
    }

    @Override
    public Date getYesterday() {
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        return fromCalendar(yesterday);
    }

    @Override
    public Date getYesterday(final Date today) {
        final Calendar yesterday = fromDate(today);
        yesterday.add(Calendar.DATE, -1);
        return fromCalendar(yesterday);
    }

    @Override
    public Date decrementDays(final Date start, int amount) {
        final Calendar returnValue = fromDate(start);
        returnValue.add(Calendar.DATE, -1 * amount);
        return fromCalendar(returnValue);
    }

    @Override
    /**
     * Returns a date range including the given start and end dates.
     */
    public Iterable<Date> dateRange(final Date start, final Date stop) {
        return new Iterable<Date>() {
            @NonNull
            @Override
            public Iterator<Date> iterator() {
                return new Iterator<Date>() {
                    Date today = getYesterday(start);

                    @Override
                    public boolean hasNext() {
                        return !today.equals(stop);
                    }

                    @Override
                    public Date next() {
                        this.today = getTomorrow(today);
                        return this.today;
                    }
                };
            }
        };
    }
}
