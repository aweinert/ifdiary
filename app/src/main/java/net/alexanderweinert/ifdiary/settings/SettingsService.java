package net.alexanderweinert.ifdiary.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class SettingsService {
    public static final String SHOULD_SHOW_REMINDER_PREFERENCES_KEY = "shouldShowReminder";
    public static final String REMINDER_TIME_KEY = "reminderTime";

    public static SettingsService getInstance(final Context context) {
        return new SettingsServiceImpl(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static SettingsService getInstance(final SharedPreferences preferences) {
        return new SettingsServiceImpl(preferences);
    }

    abstract public boolean shouldShowReminder();
    abstract public void setShouldShowReminder(boolean shouldShowReminder);

    abstract public int getReminderHour();
    abstract public int getReminderMinutes();
    abstract public void setReminderTime(int hours, int minutes);
}
