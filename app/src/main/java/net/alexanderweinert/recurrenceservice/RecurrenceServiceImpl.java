package net.alexanderweinert.recurrenceservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

class RecurrenceServiceImpl extends RecurrenceService {
    @Override
    public void ensureOnceDaily(final Context context, final Intent intent, final Calendar notificationTime) {
        PendingIntent recurringNotificationDisplay =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringNotificationDisplay);

    }

    @Override
    public void cancelRecurrence(Context context, Intent intent) {
        PendingIntent recurringNotificationDisplay =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(recurringNotificationDisplay);
    }
}
