package net.alexanderweinert.ifdiary;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import net.alexanderweinert.ifdiary.reminder.ReminderService;
import net.alexanderweinert.ifdiary.settings.SettingsService;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(context);

        SwitchPreferenceCompat notificationPreference = new SwitchPreferenceCompat(context);
        notificationPreference.setKey(SettingsService.SHOULD_SHOW_REMINDER_PREFERENCES_KEY);
        notificationPreference.setTitle("Show daily reminder");

        preferenceScreen.addPreference(notificationPreference);

        setPreferenceScreen(preferenceScreen);
    }
}
