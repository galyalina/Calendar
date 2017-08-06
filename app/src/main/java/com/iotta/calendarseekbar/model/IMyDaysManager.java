package com.iotta.calendarseekbar.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Galina Litkin on 05/08/2017.
 */

public interface IMyDaysManager{
    public void addDate(Date date);
    public List<Date> getDates();
}
