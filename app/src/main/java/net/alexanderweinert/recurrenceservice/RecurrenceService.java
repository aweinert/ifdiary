package net.alexanderweinert.recurrenceservice;

import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public abstract class RecurrenceService {
    public static RecurrenceService getInstance() {
        return new RecurrenceServiceImpl();
    }

    /**
     * @param intent Must refer to a class that implements {@link android.content.BroadcastReceiver}
     */
    abstract public void ensureOnceDaily(Context context, Intent intent, Calendar time);

    abstract public void cancelRecurrence(Context context, Intent intent);
}
