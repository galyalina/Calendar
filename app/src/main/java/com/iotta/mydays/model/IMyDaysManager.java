package com.iotta.mydays.model;

import java.util.List;

/**
 * Created by Galina Litkin on 05/08/2017.
 */

public interface IMyDaysManager {

    void addDate(MyDate date);

    List<MyDate> getDates();
}
