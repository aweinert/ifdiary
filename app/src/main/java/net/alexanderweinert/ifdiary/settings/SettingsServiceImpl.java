package net.alexanderweinert.ifdiary.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class SettingsServiceImpl extends SettingsService {
    final SharedPreferences preferences;

    public SettingsServiceImpl(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean shouldShowReminder() {
        return this.preferences.getBoolean(SettingsService.SHOULD_SHOW_REMINDER_PREFERENCES_KEY, false);
    }

    @Override
    public void setShouldShowReminder(boolean shouldShowReminder) {
        final SharedPreferences.Editor editor = this.preferences.edit();
        editor.putBoolean(SettingsService.SHOULD_SHOW_REMINDER_PREFERENCES_KEY, shouldShowReminder);
        editor.commit();
    }
}
