package com.iotta.calendarseekbar.viewpresenter.calendar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iotta.calendarseekbar.R;
import com.iotta.calendarseekbar.view.CalendarSeekBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarContract.IView{

    private CalendarContract.IPresenter mPresenter;

    private OnCalendarInteractionListener mListener;

    // Container Activity must implement this interface
    public interface OnCalendarInteractionListener {
        void onDetailsPageSelected();
    }



    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        Button goToDetails = root.findViewById(R.id.btnDetails);

        CalendarSeekBar spinner = root.findViewById(R.id.daysSpinner);
        spinner.setMax(31);
        spinner.setMin(1);
        spinner.setStep(1);

        goToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDetailsPageSelected();
            }
        });
        return root;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalendarInteractionListener) {
            mListener = (OnCalendarInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCalendarInteractionListener");
        }
    }

    @Override
    public void setPresenter(@NonNull CalendarContract.IPresenter IPresenter) {
        mPresenter = IPresenter;
    }
}
