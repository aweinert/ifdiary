package net.alexanderweinert.ifdiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.alexanderweinert.ifdiary.reminder.DataEntryNotificationDisplayer;
import net.alexanderweinert.ifdiary.reminder.ReminderService;
import net.alexanderweinert.ifdiary.settings.SettingsService;

public class BootSignalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Context applicationContext = context.getApplicationContext();
        ReminderService.getInstance().applySettings(applicationContext, SettingsService.getInstance(applicationContext));
    }
}
