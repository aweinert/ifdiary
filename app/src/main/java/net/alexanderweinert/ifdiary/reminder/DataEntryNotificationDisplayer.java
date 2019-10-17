package net.alexanderweinert.ifdiary.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.alexanderweinert.ifdiary.MakeEntryActivity;
import net.alexanderweinert.ifdiary.R;
import net.alexanderweinert.ifdiary.settings.SettingsService;
import net.alexanderweinert.recurrenceservice.RecurrenceService;

import java.util.Calendar;

public class DataEntryNotificationDisplayer extends BroadcastReceiver {

    public static void ensureDailyNotification(final Context context) {

        Log.i("DataEntryNotificationDisplayer", "Ensuring that notification is shown daily");

        final SettingsService settingsService = SettingsService.getInstance(context);
        Calendar updateTime = Calendar.getInstance();
        updateTime.set(Calendar.HOUR_OF_DAY, settingsService.getReminderHour());
        updateTime.set(Calendar.MINUTE, settingsService.getReminderMinutes());

        Intent notificationDisplayIntent = new Intent(context, DataEntryNotificationDisplayer.class);

        RecurrenceService.getInstance().ensureOnceDaily(context, notificationDisplayIntent, updateTime);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ReminderServiceImpl.channelId)
                .setSmallIcon(R.drawable.panda)
                .setContentTitle("Intermittent Fasting")
                .setContentText("Did you fast yesterday?")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        final Intent tapIntent = new Intent(context, MakeEntryActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, tapIntent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify(0, builder.build());
    }
}
