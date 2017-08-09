package com.iotta.mydays.viewpresenter.calendar;

import android.support.annotation.NonNull;
import android.util.Log;

import com.iotta.mydays.model.IMyDaysManager;
import com.iotta.mydays.model.MyDate;
import com.iotta.mydays.utils.Logger;

/**
 * Created by Galina Litkin on 05/08/2017.
 */

/**
 * Listens to user actions from the UI ({@link com.iotta.mydays.MainActivity}), retrieves the data and updates the
 * UI as required.
 */
public class CalendarPresenter implements CalendarContract.IPresenter {

    //UI component
    private final CalendarContract.IView mCalendarView;
    private IMyDaysManager mDataManager;

    public CalendarPresenter(@NonNull IMyDaysManager dataManager, @NonNull CalendarContract.IView calendarIView) {
        mCalendarView = calendarIView;
        mCalendarView.setPresenter(this);
        mDataManager = dataManager;
    }

    @Override
    public void start() {
        //No data needed to be loaded from model, do nothing
    }

    @Override
    public void addDate(MyDate date) {
        Logger.debug("New date is selected "+date.toString());
        mDataManager.addDate(date);
    }


}
