package net.alexanderweinert.ifdiary.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import net.alexanderweinert.ifdiary.settings.SettingsService;
import net.alexanderweinert.recurrenceservice.RecurrenceService;

import java.util.Calendar;

class ReminderServiceImpl extends ReminderService {

    final static String channelId = "IFNotificationChannel_Id";

    private final static String channelName = "IFNotificationChannel";

    private final static String channelDescription = "IFNotificationChannelDescription";

    private final static String loggerTag = "ReminderService";

    private void ensureNotificationChannelExists(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channelName;
            String description = channelDescription;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void applySettings(final Context context, final SettingsService settingsService) {

        final RecurrenceService recurrenceService = RecurrenceService.getInstance();
        final Intent notificationDisplayIntent = new Intent(context, DataEntryNotificationDisplayer.class);

        final boolean shouldShowReminder = settingsService.shouldShowReminder();
        if (shouldShowReminder) {
            ensureNotificationChannelExists(context);
            Calendar updateTime = Calendar.getInstance();
            updateTime.set(Calendar.HOUR_OF_DAY, settingsService.getReminderHour());
            updateTime.set(Calendar.MINUTE, settingsService.getReminderMinutes());

            Log.d(loggerTag, String.format("Ensuring daily reminder at %02d:%02d", settingsService.getReminderHour(), settingsService.getReminderMinutes()));
            recurrenceService.ensureOnceDaily(context, notificationDisplayIntent, updateTime);
        } else {
            Log.d(loggerTag, "Cancelling daily reminder");
            recurrenceService.cancelRecurrence(context, notificationDisplayIntent);
        }
    }

    @Override
    public SharedPreferences.OnSharedPreferenceChangeListener getPreferenceChangeListener(final Context alarmContext) {
        return new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
                Log.d(loggerTag, "Shared preferences changed");
                final SettingsService settingsService = SettingsService.getInstance(preferences);
                applySettings(alarmContext, settingsService);
            }
        };
    }
}
