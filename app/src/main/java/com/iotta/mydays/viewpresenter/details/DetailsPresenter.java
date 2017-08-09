package com.iotta.mydays.viewpresenter.details;

import android.support.annotation.NonNull;

import com.iotta.mydays.viewpresenter.calendar.IGetDatesCB;
import com.iotta.mydays.model.IMyDaysManager;

/**
 * Created by Galina Litkin on 05/08/2017.
 */

public class DetailsPresenter implements DetailsContract.IPresenter{

    //UI component
    private final DetailsContract.IView mCalendarView;
    private IMyDaysManager mDataManager;

    public DetailsPresenter(@NonNull IMyDaysManager dataManager, @NonNull DetailsContract.IView repositoriesIView) {
        mCalendarView = repositoriesIView;
        mCalendarView.setPresenter(this);
        mDataManager = dataManager;
    }

    @Override
    public void start() {

    }

    @Override
    public void getDates(final IGetDatesCB cb) {

        //In this case the action doesn't really have to be performed on separate thread since it's a very short interaction with DB in case the model is empty,
        //but just in case of later extension good to have it asynchronously
        new Thread(new Runnable() {
            @Override
            public void run() {
                cb.onSuccess(mDataManager.getDates());
            }
        }).start();
    }
}
