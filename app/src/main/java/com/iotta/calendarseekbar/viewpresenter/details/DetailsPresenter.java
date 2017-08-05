package com.iotta.calendarseekbar.viewpresenter.details;

import android.support.annotation.NonNull;

import com.iotta.calendarseekbar.model.IMyDaysManager;
import com.iotta.calendarseekbar.viewpresenter.calendar.CalendarContract;

/**
 * Created by Galina Litkin on 05/08/2017.
 */

public class DetailsPresenter implements DetailsContract.IPresenter{

    //UI component
    private final DetailsContract.IView mCalendarView;
    private IMyDaysManager dataManager;

    public DetailsPresenter(@NonNull IMyDaysManager dataManager, @NonNull DetailsContract.IView repositoriesIView) {
        mCalendarView = repositoriesIView;
        mCalendarView.setPresenter(this);
        dataManager = dataManager;
    }

    @Override
    public void start() {

    }
}
