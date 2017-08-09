package com.iotta.mydays;

/**
 * Created by Galya on 05/08/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.iotta.mydays.model.IMyDaysManager;
import com.iotta.mydays.model.MyDaysManager;

/**
 * Enables injection of mock implementations for
 * {@link com.iotta.mydays.model.MyDaysManager} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static IMyDaysManager provideDatesData(@NonNull Context context) {
        return MyDaysManager.getInstance(context);
    }
}
