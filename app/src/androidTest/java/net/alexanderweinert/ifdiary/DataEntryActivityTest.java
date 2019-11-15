package net.alexanderweinert.ifdiary;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import net.alexanderweinert.dateservice.Date;
import net.alexanderweinert.dateservice.DateService;
import net.alexanderweinert.ifdiary.persistence.PersistenceService;
import net.alexanderweinert.ifdiary.persistence.PersistenceServiceException;
import net.alexanderweinert.logging.LoggingService;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.function.Function;

@RunWith(AndroidJUnit4.class)
public class DataEntryActivityTest {

    private DateService mockedDateService = Mockito.mock(DateService.class);
    private PersistenceService mockedPersistenceService = Mockito.mock(PersistenceService.class);
    private Function<Context, PersistenceService> mockedPersistenceServiceFactory = new Function<Context, PersistenceService>() {
        @Override
        public PersistenceService apply(Context context) {
            return mockedPersistenceService;
        }
    };
    private LoggingService mockedLoggingService = Mockito.mock(LoggingService.class);

    final IntentsTestRule<DataEntryActivity> activity = new IntentsTestRule<DataEntryActivity>(DataEntryActivity.class) {
        DateService originalDateService;
        Function<Context, PersistenceService> originalPersistenceServiceFactory;
        LoggingService originalLoggingService;

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            this.originalDateService = DataEntryActivity.bindDateService(mockedDateService);
            this.originalPersistenceServiceFactory = DataEntryActivity.bindPersistenceServiceFactory(mockedPersistenceServiceFactory);
            this.originalLoggingService = DataEntryActivity.bindLoggingService(mockedLoggingService);
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            DataEntryActivity.bindDateService(this.originalDateService);
            DataEntryActivity.bindPersistenceServiceFactory(this.originalPersistenceServiceFactory);
            DataEntryActivity.bindLoggingService(this.originalLoggingService);

        }
    };

    @Before
    public void resetMocks() {
        Mockito.reset(mockedDateService, mockedLoggingService, mockedPersistenceService);
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void pressYesButton() throws PersistenceServiceException {
        final Date yesterday = Date.builder().year(2019).month(11).day(5).build();
        Mockito.when(mockedDateService.getYesterday()).thenReturn(yesterday);
        Mockito.when(mockedPersistenceService.hasFasted(yesterday)).thenReturn(Optional.<Boolean>ofNullable(true));
        activity.launchActivity(new Intent());

        Intents.intending(IntentMatchers.anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        onView(withId(R.id.button_yes)).perform(click());

        Mockito.verify(mockedPersistenceService).setFasting(yesterday, true);
        Intents.intended(IntentMatchers.hasComponent("net.alexanderweinert.ifdiary.ShowStatisticsActivity"));
    }

    @Test
    public void pressNoButton() throws PersistenceServiceException {
        final Date yesterday = Date.builder().year(2019).month(11).day(5).build();
        Mockito.when(mockedDateService.getYesterday()).thenReturn(yesterday);
        Mockito.when(mockedPersistenceService.hasFasted(yesterday)).thenReturn(Optional.<Boolean>ofNullable(true));
        activity.launchActivity(new Intent());

        Intents.intending(IntentMatchers.anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        onView(withId(R.id.button_no)).perform(click());

        Mockito.verify(mockedPersistenceService).setFasting(yesterday, false);
        Intents.intended(IntentMatchers.hasComponent("net.alexanderweinert.ifdiary.ShowStatisticsActivity"));
    }

}