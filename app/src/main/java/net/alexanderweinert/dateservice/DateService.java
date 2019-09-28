package net.alexanderweinert.dateservice;

public abstract class DateService {
    public static DateService instance() {
        return new DateServiceImpl();
    }

    public abstract Date getToday();

    public abstract Date getTomorrow();

    public abstract Date getTomorrow(Date today);

    public abstract Date getYesterday();

    public abstract Date getYesterday(Date today);

    public abstract Date decrementDays(Date start, int amount);

    public abstract Iterable<Date> dateRange(Date start, Date stop);
}
