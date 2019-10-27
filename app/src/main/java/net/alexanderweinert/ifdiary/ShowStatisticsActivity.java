package net.alexanderweinert.ifdiary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import net.alexanderweinert.dateservice.Date;
import net.alexanderweinert.dateservice.DateService;
import net.alexanderweinert.ifdiary.persistence.PersistenceService;
import net.alexanderweinert.ifdiary.persistence.PersistenceServiceException;
import net.alexanderweinert.ifdiary.reminder.ReminderService;
import net.alexanderweinert.ifdiary.settings.SettingsService;
import net.alexanderweinert.logging.LoggingService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import pl.droidsonroids.gif.GifImageView;

/**
 * This activity should:
 * If there is a fasting logged for yesterday, display the appropriate panda image
 * If no fasting status has been logged for yesterday, do nothing
 */
public class ShowStatisticsActivity extends AppCompatActivity {

    private static final String SUCCESS_FILE_NAME = "success.gif";
    private static final String FAILURE_FILE_NAME = "failure.gif";
    private final String COLOR_GREEN = "228B22";
    private final String COLOR_RED = "B22222";
    private final String COLOR_GRAY = "C0C0C0";

    final Handler handler = new Handler();

    final Runnable pandaViewUpdater = new Runnable() {
            @Override
            public void run() {
                queryYesterdayAndUpdateView();
                handler.postDelayed(this, updateInterval);
            }
        };

    final Runnable pieChartUpdater = new Runnable() {
        @Override
        public void run() {
            updatePieChart((PieChart) findViewById(R.id.pieChart7), 7);
            updatePieChart((PieChart) findViewById(R.id.pieChart30), 30);
            handler.postDelayed(this, updateInterval);
        }
    };

    // The update interval in milliseconds, i.e., the time between subsequent database queries.
    private final long updateInterval = 1000;

    private void queryYesterdayAndUpdateView() {
        final Date yesterday = DateService.instance().getYesterday();
        boolean fastingStored = false;
        try {
            fastingStored = PersistenceService.instance(this.getApplicationContext())
                    .fastingStored(yesterday);
        } catch (PersistenceServiceException e) {
            LoggingService.instance().error("Exception thrown when querying whether fasting value was stored", e);
        }

        if (!fastingStored) {
            hidePandaView();
        } else {
            final boolean fasted;
            try {
                fasted = PersistenceService.instance(this.getApplicationContext())
                        .hasFasted(yesterday).get();
            } catch (PersistenceServiceException e) {
                LoggingService.instance().error("Exception thrown when querying whether user has fasted yesterday", e);
                return;
            }

            /*
            try {
                this.getApplicationContext().openFileInput("happy.gif");
            } catch (FileNotFoundException e) {
                final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
            */
            final int pandaImageId = (fasted ? R.drawable.panda_happy : R.drawable.panda_sad);
            setPandaImage(pandaImageId);
            showPandaView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
        }
    }

    private void updatePieChart(PieChart chart, int dateRangeSize) {

        final Date yesterday = DateService.instance().getYesterday();
        final Date startDate = DateService.instance().decrementDays(yesterday, dateRangeSize - 1);

        int daysFasted = 0, daysNotFasted = 0, daysUnknown = 0;

        final PersistenceService persistenceService = PersistenceService.instance(getApplicationContext());

        for(final Date date : DateService.instance().dateRange(startDate, yesterday)) {
            Optional<Boolean> fasted;
            try {
                fasted = persistenceService.hasFasted(date);
            } catch (PersistenceServiceException e) {
                fasted = Optional.empty();
            }
            if (fasted.isPresent()) {
                if(fasted.get()) {
                    daysFasted += 1;
                } else {
                    daysNotFasted += 1;
                }
            } else {
                daysUnknown += 1;
            }
        }

        final ArrayList<PieEntry> entries = new ArrayList<>();
        final ArrayList<Integer> colors = new ArrayList<>();

        if(daysFasted > 0) {
            entries.add(new PieEntry(daysFasted, "gefastet"));
            colors.add(ColorTemplate.rgb(COLOR_GREEN));
        }
        if(daysNotFasted > 0) {
            entries.add(new PieEntry(daysNotFasted, "nicht gefastet"));
            colors.add(ColorTemplate.rgb(COLOR_RED));
        }
        if(daysUnknown > 0) {
            entries.add(new PieEntry(daysUnknown, "unbekannt"));
            colors.add(ColorTemplate.rgb(COLOR_GRAY));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Fastendaten");

        dataSet.setColors(colors);

        final PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
                                   @Override
                                   public String getFormattedValue(float value) {
                                       final int intValue = (int) value;
                                       final String daysString = (intValue == 1 ? "Tag" : "Tage");
                                       return String.format("%d %s", intValue, daysString);
                                   }
                               });
        data.setValueTextSize(13f);
        chart.setData(data);
        chart.invalidate();

        chart.setCenterText(String.format("Fastenquote %d Tage", dateRangeSize));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistics);

        final Context applicationContext = this.getApplicationContext();

        ReminderService.getInstance().applySettings(applicationContext, SettingsService.getInstance(applicationContext));

        //if (!fileExistsForReading(SUCCESS_FILE_NAME)) {
            //queryUserForFile(SUCCESS_FILE_NAME);
        //}

        //if (!fileExistsForReading(FAILURE_FILE_NAME)) {
            //queryUserForFile(FAILURE_FILE_NAME);
        //}

        configurePieChart((PieChart) findViewById(R.id.pieChart7));
        configurePieChart((PieChart) findViewById(R.id.pieChart30));

        handler.post(this.pandaViewUpdater);
        handler.post(this.pieChartUpdater);
    }

    private boolean fileExistsForReading(String successFileName) throws IOException {
        try (final FileInputStream successFile = this.getApplicationContext().openFileInput(successFileName)) {
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    private void configurePieChart(PieChart chart) {
        chart.setRotationEnabled(false);
        chart.animateY(1000);
        chart.getLegend().setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.pandaViewUpdater);
        this.handler.removeCallbacks(this.pieChartUpdater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch(itemId) {
            case R.id.action_settings:
                final Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPandaImage(int imageId) {
        final GifImageView pandaView = findViewById(R.id.pandaView);
        pandaView.setImageResource(0);
        pandaView.setImageResource(imageId);
    }

    private void hidePandaView() {
        final GifImageView pandaView = findViewById(R.id.pandaView);
        pandaView.setVisibility(View.GONE);
    }

    private void showPandaView() {
        final GifImageView pandaView = findViewById(R.id.pandaView);
        pandaView.setVisibility(View.VISIBLE);
    }

}
