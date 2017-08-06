package com.iotta.calendarseekbar.viewpresenter.calendar;

import android.support.annotation.NonNull;

import com.iotta.calendarseekbar.model.IMyDaysManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Galina Litkin on 05/08/2017.
 */

/**
 * Listens to user actions from the UI ({@link com.iotta.calendarseekbar.MainActivity}), retrieves the data and updates the
 * UI as required.
 */
public class CalendarPresenter implements CalendarContract.IPresenter {

    //UI component
    private final CalendarContract.IView mCalendarView;
    private IMyDaysManager dataManager;

    public CalendarPresenter(@NonNull IMyDaysManager dataManager, @NonNull CalendarContract.IView calendarIView) {
        mCalendarView = calendarIView;
        mCalendarView.setPresenter(this);
        dataManager = dataManager;
    }

    @Override
    public void start() {

    }

    @Override
    public void addDate(Date date) {
        dataManager.addDate(date);
    }

    @Override
    public List<Date> getDates(Date date) {
        return dataManager.getDates();
    }
}
