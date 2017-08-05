package com.iotta.calendarseekbar.viewpresenter.calendar;

import com.iotta.calendarseekbar.BasePresenter;
import com.iotta.calendarseekbar.BaseView;

/**
 * Created by Galina Litkin on 05/08/2017.
 */
public class CalendarContract {

    /**
     * This specifies the contract between the view and the presenter.
     */
    interface IView extends BaseView<IPresenter> {
    }

    interface IPresenter extends BasePresenter {
    }
}