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
import android.widget.TextView;

import com.iotta.calendarseekbar.R;
import com.iotta.calendarseekbar.view.CalendarSeekBar;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarContract.IView {

    private CalendarContract.IPresenter mPresenter;
    private TextView mDate;
    private TextView mMonth;
    CalendarSeekBar mSpinner;
    private OnCalendarInteractionListener mListener;
    private int mMonthIndex;

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
     *
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


    void setMonthAndInitDay(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
        if(mSpinner!=null){
            mSpinner.setPoints(dayOfWeek);
        }
        mMonthIndex = c.get(Calendar.MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        Button goToDetails = root.findViewById(R.id.btnDetails);
        root.findViewById(R.id.dateLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSpinner!=null){
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_MONTH, mSpinner.getPoint());
                    mPresenter.addDate(c.getTime());
                }
            }
        });

        mDate  = root.findViewById(R.id.txtDate);
        mMonth  = root.findViewById(R.id.txtMonth);

        mMonth.setText("August");
        mSpinner = root.findViewById(R.id.daysSpinner);
        mSpinner.setMax(31);
        mSpinner.setMin(1);
        mSpinner.setStep(1);
        setMonthAndInitDay();

        mSpinner.setOnSwagPointsChangeListener(new DateChangeListener());
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


    class DateChangeListener implements CalendarSeekBar.OnSwagPointsChangeListener {
        @Override
        public void onPointsChanged(CalendarSeekBar swagPoints, int points, boolean fromUser) {
            mDate.setText(""+points);
        }

        @Override
        public void onStartTrackingTouch(CalendarSeekBar swagPoints) {

        }

        @Override
        public void onStopTrackingTouch(CalendarSeekBar swagPoints) {

        }
    }
}
