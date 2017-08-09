package com.iotta.mydays.viewpresenter.calendar;

import com.iotta.mydays.model.MyDate;

import java.util.List;

/**
 * Created by Galina Litkin on 07/08/2017.
 * CB used for asynchronous loading of data
 */

public interface IGetDatesCB {
    void onSuccess(List<MyDate> dates);
}

