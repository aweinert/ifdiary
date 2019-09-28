package net.alexanderweinert.ifdiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.alexanderweinert.recurrenceservice.RecurrenceService;

import java.util.Calendar;

public class BootSignalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DataEntryNotificationDisplayer.ensureDailyNotification(context);
    }
}
