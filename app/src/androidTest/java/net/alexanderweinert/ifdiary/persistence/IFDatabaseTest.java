package net.alexanderweinert.ifdiary.persistence;

import android.database.Cursor;

import androidx.room.migration.Migration;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class IFDatabaseTest {
    private static final String TEST_DB = "migration-test";

    @Rule
    public MigrationTestHelper helper;

    public IFDatabaseTest() {
        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), IFDatabase.class.getCanonicalName(), new FrameworkSQLiteOpenHelperFactory());
    }

    @Test
    public void migrate1To2() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 1);

        final int expectedYear = 2019;
        final int expectedMonth = 4;
        final int expectedDay = 8;

        db.execSQL("INSERT INTO DBEntry (fasted, year, month, day) VALUES (1, ?, ?, ?);", new Object[]{expectedYear, expectedMonth, expectedDay});

        db.close();

        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, IFDatabase.Migration1To2);

        final Cursor dateCursor = db.query("SELECT * FROM DateDEO;");
        assertEquals(4, dateCursor.getColumnCount());

        final int idIndexDate = dateCursor.getColumnIndex("id");
        final int yearIndex = dateCursor.getColumnIndex("year");
        final int monthIndex = dateCursor.getColumnIndex("month");
        final int dayIndex = dateCursor.getColumnIndex("day");

        assertTrue("Table DateDEO contains no entries after migration", dateCursor.moveToNext());

        final int dateId = dateCursor.getInt(idIndexDate);
        assertEquals(expectedYear, dateCursor.getInt(yearIndex));
        assertEquals(expectedMonth, dateCursor.getInt(monthIndex));
        assertEquals(expectedDay, dateCursor.getInt(dayIndex));

        assertFalse("Table DateDEO contains more than one entry after migration", dateCursor.moveToNext());

        final Cursor fastingCursor = db.query("SELECT * FROM FastingDEO;");

        final int dateIdIndexFasting = fastingCursor.getColumnIndex("date_id");
        final int firstMealStartIndex = fastingCursor.getColumnIndex("firstMealStartInMinutes");
        final int lastMealEndIndex = fastingCursor.getColumnIndex("lastMealEndInMinutes");

        assertTrue("Table FastingDEO contains no entries after migration", fastingCursor.moveToNext());

        assertEquals("The single entry in FastingDEO does not reference the single entry in DateDEO", dateId, dateIdIndexFasting);
        assertEquals(8*60, fastingCursor.getInt(firstMealStartIndex));
        assertEquals(15*60, fastingCursor.getInt(lastMealEndIndex));

        assertFalse("Table FastingDEO contains more than one entry after migration", fastingCursor.moveToNext());
    }
}