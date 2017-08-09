package com.iotta.mydays;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.iotta.mydays.model.IMyDaysManager;
import com.iotta.mydays.model.MyDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    IMyDaysManager mDataManager;


    @Before
    public void setUp() {
        mDataManager = Injection.provideDatesData(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mDataManager);
    }

    @Test
    public void testShouldAddExpenseType() throws Exception {

        mDataManager.addDate(new MyDate(1, "January"));
        mDataManager.addDate(new MyDate(1, "January"));

        List<MyDate> datesFromDb = mDataManager.getDates();

        assertThat(datesFromDb.size(), is(1));
        assertTrue(datesFromDb.get(0).getMonth().equals("January"));
        assertEquals(datesFromDb.get(0).getDay(), 1);
    }
}
