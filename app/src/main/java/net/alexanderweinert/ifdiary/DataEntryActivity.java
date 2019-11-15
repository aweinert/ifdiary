package net.alexanderweinert.ifdiary;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.alexanderweinert.dateservice.Date;
import net.alexanderweinert.dateservice.DateService;
import net.alexanderweinert.ifdiary.persistence.PersistenceService;
import net.alexanderweinert.ifdiary.persistence.PersistenceServiceException;
import net.alexanderweinert.logging.LoggingService;

import java.util.function.Function;

public class DataEntryActivity extends AppCompatActivity {

    private static DateService dateService = DateService.instance();

    private static Function<Context, PersistenceService> persistenceServiceFactory = new Function<Context, PersistenceService>() {
        @Override
        public PersistenceService apply(Context context) {
            return PersistenceService.instance(context);
        }
    };

    private static LoggingService loggingService = LoggingService.instance();

    public static DateService bindDateService(final DateService dateService) {
        final DateService oldService = DataEntryActivity.dateService;
        DataEntryActivity.dateService = dateService;
        return oldService;
    }

    public static Function<Context,PersistenceService> bindPersistenceServiceFactory(Function<Context, PersistenceService> persistenceServiceFactory) {
        final Function<Context, PersistenceService> previousPersistenceServiceFactory = DataEntryActivity.persistenceServiceFactory;
        DataEntryActivity.persistenceServiceFactory = persistenceServiceFactory;
        return previousPersistenceServiceFactory;
    }

    public static LoggingService bindLoggingService(LoggingService newLoggingService) {
        final LoggingService oldLoggingService = DataEntryActivity.loggingService;
        DataEntryActivity.loggingService = newLoggingService;
        return oldLoggingService;
    }

    Date relevantDate = null;

    private final static String EXTRA_NAME_DATE = "net.alexanderweinert.ifdiary.date";
    private final static String EXTRA_NAME_FASTED = "net.alexanderweinert.ifdiary.fasted";

    /**
     * @param date The date for which the fasting status shall be queried
     * @return An Intent that may be passed to startActivity in order to launch this Activity such
     * that it queries for the fasting status for the given date
     */
    public static Intent buildIntent(Context context, Date date) {
        final Intent returnValue = new Intent(context, DataEntryActivity.class);

        returnValue.putExtra(EXTRA_NAME_DATE, date);

        return returnValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        relevantDate = this.dateService.getYesterday();

        boolean entryExistsForYesterday = false;
        try {
            entryExistsForYesterday =
                    this.persistenceServiceFactory.apply(this.getApplicationContext()).fastingStored(relevantDate);
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
                    persistenceServiceFactory.apply(getApplicationContext()).setFasting(relevantDate, hasFasted);
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
