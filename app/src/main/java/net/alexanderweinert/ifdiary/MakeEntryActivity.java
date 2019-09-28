package net.alexanderweinert.ifdiary;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import net.alexanderweinert.dateservice.Date;
import net.alexanderweinert.dateservice.DateService;
import net.alexanderweinert.ifdiary.persistence.PersistenceService;
import net.alexanderweinert.ifdiary.persistence.PersistenceServiceException;
import net.alexanderweinert.logging.LoggingService;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Optional;

public class MakeEntryActivity extends AppCompatActivity {

    Date relevantDate = null;

    private final static String EXTRA_NAME_DATE = "net.alexanderweinert.ifdiary.date";
    private final static String EXTRA_NAME_FASTED = "net.alexanderweinert.ifdiary.fasted";

    /**
     * @param date The date for which the fasting status shall be queried
     * @return An Intent that may be passed to startActivity in order to launch this Activity such
     * that it queries for the fasting status for the given date
     */
    public static Intent buildIntent(Context context, Date date) {
        final Intent returnValue = new Intent(context, MakeEntryActivity.class);

        returnValue.putExtra(EXTRA_NAME_DATE, date);

        return returnValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_entry);

        relevantDate = DateService.instance().getYesterday();

        boolean entryExistsForYesterday = false;
        try {
            entryExistsForYesterday =
                    PersistenceService.instance(this.getApplicationContext()).fastingStored(relevantDate);
        } catch (PersistenceServiceException e) {
            LoggingService.instance().error("Error ", e);
        }

        if (entryExistsForYesterday) {
            startActivity(buildIntent());
        }

        final TextView questionView = findViewById(R.id.question);
        questionView.setText(generateQueryString(relevantDate));
    }

    /**
     * @return A human-readable string that asks the user whether they fasted on the given date.
     */
    private String generateQueryString(final Date date) {
        // We add one to the given month since the date-class counts months starting at zero.
        return String.format("Gefastet am %s.%s.%s?", date.getDay(), date.getMonth() + 1, date.getYear());
    }

    public void onYesButton(View context) {
        storeFasting(true);
        startActivity(buildIntent());
    }

    private void storeFasting(final boolean hasFasted) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PersistenceService.instance(getApplicationContext()).setFasting(relevantDate, hasFasted);
                } catch (PersistenceServiceException e) {
                    LoggingService.instance().error("Error", e);
                }
            }
        });
    }

    public void onNoButton(View context) {
        storeFasting(false);
        startActivity(buildIntent());
    }

    /**
     * @return An intent calling the {@link ShowStatisticsActivity}.
     */
    public Intent buildIntent() {
        return new Intent(this, ShowStatisticsActivity.class);
    }
}
