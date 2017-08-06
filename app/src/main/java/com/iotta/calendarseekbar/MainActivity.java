package com.iotta.calendarseekbar;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iotta.calendarseekbar.utils.AndroidUtils;
import com.iotta.calendarseekbar.utils.Logger;
import com.iotta.calendarseekbar.viewpresenter.calendar.CalendarContract;
import com.iotta.calendarseekbar.viewpresenter.calendar.CalendarFragment;
import com.iotta.calendarseekbar.viewpresenter.calendar.CalendarPresenter;
import com.iotta.calendarseekbar.viewpresenter.details.DetailsFragment;
import com.iotta.calendarseekbar.viewpresenter.details.DetailsPresenter;

public class MainActivity extends AppCompatActivity implements CalendarFragment.OnCalendarInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = CalendarFragment.newInstance();
            AndroidUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        if(fragment.getClass().getName() == CalendarFragment.class.getName()){
            new CalendarPresenter(Injection.provideDatesData(), (CalendarFragment) fragment);
            Logger.debug("Calendar Fragment created");
        }else{
            new DetailsPresenter(Injection.provideDatesData(), (DetailsFragment) fragment);
            Logger.debug("Details Fragment created");
        }
        // Create the presenter
    }

    @Override
    public void onDetailsPageSelected() {
        DetailsFragment detailsFragment = DetailsFragment.newInstance();
        AndroidUtils.replaceFragmentInActivity(getSupportFragmentManager(), detailsFragment, R.id.contentFrame);
        new DetailsPresenter(Injection.provideDatesData(), (DetailsFragment) detailsFragment);
    }
}
