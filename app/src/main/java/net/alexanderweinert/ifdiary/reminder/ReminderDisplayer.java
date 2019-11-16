package net.alexanderweinert.ifdiary.reminder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.alexanderweinert.dateservice.DateService;
import net.alexanderweinert.ifdiary.DataEntryActivity;
import net.alexanderweinert.ifdiary.R;
import net.alexanderweinert.ifdiary.persistence.PersistenceService;
import net.alexanderweinert.ifdiary.persistence.PersistenceServiceException;
import net.alexanderweinert.ifdiary.settings.SettingsService;
import net.alexanderweinert.recurrenceservice.RecurrenceService;

import java.util.Calendar;

public class ReminderDisplayer extends BroadcastReceiver {

    public static void ensureDailyNotification(final Context context) {

        Log.i("ReminderDisplayer", "Ensuring that notification is shown daily");

        final SettingsService settingsService = SettingsService.getInstance(context);
        Calendar updateTime = Calendar.getInstance();
        updateTime.set(Calendar.HOUR_OF_DAY, settingsService.getReminderHour());
        updateTime.set(Calendar.MINUTE, settingsService.getReminderMinutes());

        Intent notificationDisplayIntent = new Intent(context, ReminderDisplayer.class);

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

        final Intent tapIntent = new Intent(context, DataEntryActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, tapIntent, 0);
        builder.setContentIntent(pendingIntent);

        boolean fastingStored;
        try {
            fastingStored = PersistenceService.instance(context).fastingStored(DateService.instance().getYesterday());
        } catch (PersistenceServiceException e) {
            Log.e(this.getClass().getCanonicalName(), "Exception during querying for whether fasting is stored", e);
            fastingStored = false;
        }
        if(!fastingStored) {
            NotificationManagerCompat.from(context).notify(0, builder.build());
        }
    }
}
