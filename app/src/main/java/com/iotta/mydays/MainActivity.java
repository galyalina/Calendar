package com.iotta.mydays;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iotta.mydays.utils.AndroidUtils;
import com.iotta.mydays.utils.Logger;
import com.iotta.mydays.viewpresenter.calendar.CalendarFragment;
import com.iotta.mydays.viewpresenter.calendar.CalendarPresenter;
import com.iotta.mydays.viewpresenter.details.DetailsFragment;
import com.iotta.mydays.viewpresenter.details.DetailsPresenter;

/**
 * Main and only Activity in MyDays application.
 * Used as a container for two fragments {@link CalendarFragment}  and  {@link DetailsFragment}
 */
public class MainActivity extends AppCompatActivity implements CalendarFragment.OnCalendarInteractionListener {

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            return;
        }

        if (fragment.getClass().getName() == CalendarFragment.class.getName()) {
            new CalendarPresenter(Injection.provideDatesData(MainActivity.this.getApplicationContext()), (CalendarFragment) fragment);
            Logger.debug("Calendar Fragment resumed");
        } else {
            new DetailsPresenter(Injection.provideDatesData(MainActivity.this.getApplicationContext()), (DetailsFragment) fragment);
            Logger.debug("Details Fragment resumed");
        }
    }

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
        if (fragment.getClass().getName() == CalendarFragment.class.getName()) {
            new CalendarPresenter(Injection.provideDatesData(MainActivity.this.getApplicationContext()), (CalendarFragment) fragment);
            Logger.debug("Calendar Fragment created");
        } else {
            new DetailsPresenter(Injection.provideDatesData(MainActivity.this.getApplicationContext()), (DetailsFragment) fragment);
            Logger.debug("Details Fragment created");
        }
        // Create the presenter
    }

    @Override
    public void onDetailsPageSelected() {
        DetailsFragment detailsFragment = DetailsFragment.newInstance();
        AndroidUtils.replaceFragmentInActivity(getSupportFragmentManager(), detailsFragment, R.id.contentFrame);
        new DetailsPresenter(Injection.provideDatesData(MainActivity.this.getApplicationContext()), (DetailsFragment) detailsFragment);
    }
}
