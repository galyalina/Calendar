package com.iotta.mydays.viewpresenter.details;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotta.mydays.R;
import com.iotta.mydays.model.MyDate;
import com.iotta.mydays.viewpresenter.calendar.IGetDatesCB;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class DetailsFragment extends Fragment implements DetailsContract.IView, IGetDatesCB {

    private DetailsContract.IPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private Handler mHandle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
            if (mPresenter != null) {
                //Function is async
                mPresenter.getDates(this);
            }
        }

        mHandle = new Handler(Looper.getMainLooper());
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(DetailsContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSuccess(final List<MyDate> dates) {
        if (mRecyclerView != null) {
            mHandle.post(new Runnable() {
                @Override
                public void run() {
                    //TODO add month sorting
                    Collections.sort(dates, new Comparator<MyDate>() {
                        @Override
                        public int compare(MyDate myDate1, MyDate myDate2) {
                            return myDate1.getDay() - myDate2.getDay();
                        }
                    });
                    mRecyclerView.setAdapter(new MyItemRecyclerViewAdapter(dates));
                }
            });
        }
    }
}
