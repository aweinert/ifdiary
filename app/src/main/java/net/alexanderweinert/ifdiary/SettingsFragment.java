package net.alexanderweinert.ifdiary;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.widget.BaseAdapter;
import android.widget.TimePicker;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import net.alexanderweinert.ifdiary.settings.SettingsService;

public class SettingsFragment extends PreferenceFragmentCompat {

    private TimePreference timePreference = null;

    final private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePreference.setTime(hourOfDay, minute);
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(context);

        SwitchPreferenceCompat notificationPreference = new SwitchPreferenceCompat(context);
        notificationPreference.setKey(SettingsService.SHOULD_SHOW_REMINDER_PREFERENCES_KEY);
        notificationPreference.setTitle("Show daily reminder");

        preferenceScreen.addPreference(notificationPreference);

        this.timePreference = new TimePreference(context);
        timePreference.setKey(SettingsService.REMINDER_TIME_KEY);
        timePreference.setTitle("Time for daily reminder");

        preferenceScreen.addPreference(timePreference);

        setPreferenceScreen(preferenceScreen);

        timePreference.setDependency(SettingsService.SHOULD_SHOW_REMINDER_PREFERENCES_KEY);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if(preference == timePreference) {
            final int previousHour = timePreference.getHourOfDay();
            final int previousMinutes = timePreference.getMinutes();
            new TimePickerDialog(this.getContext(), timeSetListener, previousHour, previousMinutes, false).show();
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
