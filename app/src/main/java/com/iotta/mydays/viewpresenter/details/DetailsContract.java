package com.iotta.mydays.viewpresenter.details;

import com.iotta.mydays.BasePresenter;
import com.iotta.mydays.BaseView;
import com.iotta.mydays.viewpresenter.calendar.IGetDatesCB;

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
        void getDates(IGetDatesCB cb);
    }

}
