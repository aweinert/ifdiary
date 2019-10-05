package net.alexanderweinert.ifdiary.reminder;

import android.content.Context;
import android.content.SharedPreferences;

import net.alexanderweinert.ifdiary.settings.SettingsService;

public abstract class ReminderService {
    public static ReminderService getInstance() {
        return new ReminderServiceImpl();
    }

    /**
     * @param context The context with which the alarm should be set, if necessary.
     */
    abstract public void applySettings(Context context, SettingsService settingsService);

    /**
     * @param alarmContext The context with which the alarm should be set, if necessary.
     */
    abstract public SharedPreferences.OnSharedPreferenceChangeListener getPreferenceChangeListener(Context alarmContext);
}
