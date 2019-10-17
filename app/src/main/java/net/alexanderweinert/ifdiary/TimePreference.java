package net.alexanderweinert.ifdiary;

import android.content.Context;
import android.view.View;

import androidx.preference.DialogPreference;

class TimePreference extends DialogPreference {

    private final static int DEFAULT_VALUE = 6 * 60;

    public TimePreference(Context context) {
        super(context);
    }

    @Override
    public CharSequence getSummary() {
        final int minutes = this.getMinutes();
        final int hour = this.getHourOfDay();
        final String summary = String.format("%02d:%02d", hour, minutes);
        return summary;
    }

    public int getHourOfDay() {
        return this.getPersistedInt(DEFAULT_VALUE) / 60;
    }

    public int getMinutes() {
        return this.getPersistedInt(DEFAULT_VALUE) % 60;
    }

    public void setTime(int hour, int minutes) {
        this.persistInt(hour * 60 + minutes);
        this.notifyChanged();
    }
}
