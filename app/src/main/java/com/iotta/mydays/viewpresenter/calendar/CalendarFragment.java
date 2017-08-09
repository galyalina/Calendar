package com.iotta.mydays.viewpresenter.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iotta.mydays.R;
import com.iotta.mydays.model.MyDate;
import com.iotta.mydays.utils.Logger;
import com.iotta.mydays.viewpresenter.view.CalendarSeekBar;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarContract.IView {

    private CalendarContract.IPresenter mPresenter;
    private TextView mDate;
    private CalendarSeekBar mSpinner;
    private OnCalendarInteractionListener mListener;
    private String mMonthName;

    // Container Activity must implement this interface in order to allow replace mechanism for fragments
    public interface OnCalendarInteractionListener {
        void onDetailsPageSelected();
    }

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
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

    /**
     * Update central button
     * @param date - progress of the seek bar
     */
    private void setText(int date) {
        if (!TextUtils.isEmpty(mMonthName)) {
            final SpannableString text = new SpannableString(mMonthName + "\n" + date);
            text.setSpan(new RelativeSizeSpan(1.3f), 0, mMonthName.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            mDate.setText(text);
        }
    }

    /**
     * Get system date and use current month and number of days
     */
    private void init() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DAY_OF_MONTH);
        mMonthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        if (mSpinner != null) {
            mSpinner.setMax(c.getActualMaximum(Calendar.DAY_OF_MONTH));
            mSpinner.setData(day);
        }
        mSpinner.setData(day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        Button goToDetails = root.findViewById(R.id.btnDetails);
        root.findViewById(R.id.dateLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.debug("onClick ");
                if (mSpinner != null) {
                    MyDate date = new MyDate(mSpinner.getData(), mMonthName);
                    Logger.debug("Add " + date.toString());
                    mPresenter.addDate(date);
                }
            }
        });

        mSpinner = root.findViewById(R.id.daysSpinner);
        mSpinner.setMin(1);
        mSpinner.setStep(1);

        mDate = root.findViewById(R.id.txtDate);
        mSpinner.setOnProgressChangeListener(new DateChangeListener());
        goToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDetailsPageSelected();
            }
        });

        init();
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

    /**
     *  Seekbar events CB
     */
    class DateChangeListener implements CalendarSeekBar.OnProgressChangeListener {

        @Override
        public void onProgressChanged(CalendarSeekBar calendarSeekBar, int date, boolean fromUser) {
            setText(date);
        }

        @Override
        public void onStartTrackingTouch(CalendarSeekBar calendarSeekBar) {

        }
        @Override
        public void onStopTrackingTouch(CalendarSeekBar calendarSeekBar) {

        }
    }
}
