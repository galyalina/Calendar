package com.iotta.calendarseekbar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Galina Litkin on 05/08/2017.
 *
 *
 * Class to store, manipulate and retrieve data
 */
public class MyDaysManager implements IMyDaysManager {

    private static MyDaysManager INSTANCE;
    private List<Date> mDates;

    // Prevent direct instantiation.
    private MyDaysManager() {
    }

    public static IMyDaysManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyDaysManager();
        }
        return INSTANCE;
    }

    public void addDate(Date date) {
        if (mDates == null) {
            mDates.add(date);
        } else {
            mDates = new ArrayList<>();
        }
    }

    public List<Date> getDates() {
        return mDates;
    }
}
