package net.alexanderweinert.dateservice;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateServiceImplTest {

    @Test
    public void dateRange() {
        final DateServiceImpl dateService = new DateServiceImpl();

        final Date start = Date.builder().year(2019).month(1).day(1).build();
        final Date end = Date.builder().year(2019).month(1).day(5).build();

        int days = 0;

        for(final Date date : dateService.dateRange(start, end)) {
            days += 1;
        }

        Assert.assertEquals(5, days);
    }
}