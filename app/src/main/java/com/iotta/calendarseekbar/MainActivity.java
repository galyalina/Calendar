package com.iotta.calendarseekbar;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iotta.calendarseekbar.utils.AndroidUtils;
import com.iotta.calendarseekbar.utils.Logger;
import com.iotta.calendarseekbar.viewpresenter.calendar.CalendarFragment;
import com.iotta.calendarseekbar.viewpresenter.calendar.CalendarPresenter;
import com.iotta.calendarseekbar.viewpresenter.details.DetailsFragment;

public class MainActivity extends AppCompatActivity implements CalendarFragment.OnCalendarInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (calendarFragment == null) {
            // Create the fragment
            calendarFragment = CalendarFragment.newInstance();
            AndroidUtils.addFragmentToActivity(getSupportFragmentManager(), calendarFragment, R.id.contentFrame);
        }

        // Create the presenter
        new CalendarPresenter(Injection.provideDatesData(), calendarFragment);
        Logger.debug("Details Fragment created");
    }

    @Override
    public void onDetailsPageSelected() {
        DetailsFragment calendarFragment = DetailsFragment.newInstance();
        AndroidUtils.replaceFragmentInActivity(getSupportFragmentManager(), calendarFragment, R.id.contentFrame);
    }
}
