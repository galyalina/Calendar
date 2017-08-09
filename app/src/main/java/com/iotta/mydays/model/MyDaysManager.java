package com.iotta.mydays.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Galina Litkin on 05/08/2017.
 * <p>
 * Class to store, manipulate and retrieve data, currently uses SQlite db
 */
public class MyDaysManager implements IMyDaysManager {

    private DatabaseHandler m_db = null;
    private static MyDaysManager INSTANCE;
    private HashSet<MyDate> mDates;

    // Prevent direct instantiation.
    private MyDaysManager(@NonNull Context context) {
        m_db = new DatabaseHandler(context);
    }

    public static IMyDaysManager getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MyDaysManager(context);
        }
        return INSTANCE;
    }

    @Override
    public void addDate(MyDate date) {
        if (mDates == null) {
            mDates = new HashSet<>();
        }

        //Set collection takes care of duplications
        mDates.add(date);
        //DB table constraint takes case of duplications
        m_db.addDate(date);
    }


    @Override
    public List<MyDate> getDates() {
        if (mDates == null) {
            mDates = new HashSet<>();
            ArrayList<MyDate> list = m_db.getAllDates();
            mDates = new HashSet<>(list);
            return list;
        }

        return new ArrayList<MyDate>(mDates);
    }
}
