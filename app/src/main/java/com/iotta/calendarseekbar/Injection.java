package com.iotta.calendarseekbar;

/**
 * Created by Galya on 05/08/2017.
 */

import com.iotta.calendarseekbar.model.IMyDaysManager;
import com.iotta.calendarseekbar.model.MyDaysManager;
/**
 * Enables injection of mock implementations for
 * {@link com.iotta.calendarseekbar.model.MyDaysManager} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static IMyDaysManager provideDatesData() {
        return MyDaysManager.getInstance();
    }
}
