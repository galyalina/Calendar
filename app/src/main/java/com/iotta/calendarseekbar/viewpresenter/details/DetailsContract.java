package com.iotta.calendarseekbar.viewpresenter.details;

import com.iotta.calendarseekbar.BasePresenter;
import com.iotta.calendarseekbar.BaseView;
import com.iotta.calendarseekbar.viewpresenter.calendar.CalendarContract;

/**
 * Created by Galina Litkin on 05/08/2017.
 */

public class DetailsContract {

    /**
     * This specifies the contract between the view and the presenter.
     */
    interface IView extends BaseView<IPresenter> {
    }

    interface IPresenter extends BasePresenter {
    }

}
