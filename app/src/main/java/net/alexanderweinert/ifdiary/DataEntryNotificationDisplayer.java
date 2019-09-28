package net.alexanderweinert.ifdiary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.alexanderweinert.recurrenceservice.RecurrenceService;

import java.util.Calendar;

public class DataEntryNotificationDisplayer extends BroadcastReceiver {

    private final static String channelId = "IFNotificationChannel_Id";

    private final static String channelName = "IFNotificationChannel";

    private final static String channelDescription = "IFNotificationChannelDescription";

    private static void ensureNotificationChannelExists(final Context context) {
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

    public static void ensureDailyNotification(final Context context) {
        ensureNotificationChannelExists(context);

        Log.i("DataEntryNotificationDisplayer", "Ensuring that notification is shown daily");

        Calendar updateTime = Calendar.getInstance();
        updateTime.set(Calendar.HOUR_OF_DAY,6);
        updateTime.set(Calendar.MINUTE,0);

        Intent notificationDisplayIntent = new Intent(context, DataEntryNotificationDisplayer.class);

        RecurrenceService.getInstance().ensureOnceDaily(context, notificationDisplayIntent, updateTime);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
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
