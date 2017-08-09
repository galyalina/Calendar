package com.iotta.mydays.viewpresenter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.iotta.mydays.R;
import com.iotta.mydays.utils.Logger;

/**
 * This class is an extension of the SwagPoints project's class
 * https://github.com/enginebai/SwagPoints
 * Licensed under  Apache License Version 2.0, January 2004
 * http://www.apache.org/licenses/
 * You may not use this file except in compliance with this License.
 */

public class CalendarSeekBar extends View {

    public static int INVALID_VALUE = -1;
    public static final int MAX = 31;
    public static final int MIN = 1;

    /**
     * Offset = -90 indicates that the progress starts from 12 o'clock.
     */
    private static final int ANGLE_OFFSET = -90;

    /**
     * The current data value.
     */
    private int mData = MIN;

    /**
     * The min value of progress value.
     */
    private int mMin = MIN;

    /**
     * The Maximum value that this SeekArc can be set to
     */
    private int mMax = MAX;

    /**
     * The increment/decrement value for each movement of progress.
     */
    private int mStep = 1;

    /**
     * The Drawable for the seek arc thumbnail
     */
    private Drawable mIndicatorIcon;


    private int mProgressWidth = 12;
    private int mArcWidth = 12;
    private boolean mClockwise = true;
    private boolean mEnabled = true;

    /**
     * The counts of data update to determine whether to change previous progress.
     */
    private int mUpdateTimes = 0;
    private float mPreviousProgress = -1;
    private float mCurrentProgress = 0;

    /**
     * Determine whether reach max of data.
     */
    private boolean isMax = false;

    /**
     * Determine whether reach min of data.
     */
    private boolean isMin = false;

    private int mArcRadius = 0;
    private RectF mArcRect = new RectF();
    private Paint mArcPaint;

    private float mProgressSweep = 0;
    private Paint mProgressPaint;

    private float mTextSize = 72;
    private float mTitleTextSize = 8;

    private int mTranslateX;
    private int mTranslateY;

    // the (x, y) coordinator of indicator icon
    private int mIndicatorIconX;
    private int mIndicatorIconY;

    /**
     * The current touch angle of arc.
     */
    private double mTouchAngle;
    private OnProgressChangeListener mOnProgressChangeListener;

    public CalendarSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public CalendarSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        float density = getResources().getDisplayMetrics().density;

        // Defaults, may need to link this into theme settings
        int arcColor = ContextCompat.getColor(context, R.color.seekbar_arc);
        int progressColor = ContextCompat.getColor(context, R.color.seekbar_progress);
        int textColor = ContextCompat.getColor(context, R.color.seekbar_text);
        mProgressWidth = (int) (mProgressWidth * density);
        mArcWidth = (int) (mArcWidth * density);
        mTextSize = (int) (mTextSize * density);
        mTitleTextSize = (int) (mTitleTextSize * density);

        mIndicatorIcon = ContextCompat.getDrawable(context, R.drawable.indicator);

        if (attrs != null) {
            // Attribute initialization
            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.CalendarSeekBar, 0, 0);

            Drawable indicatorIcon = a.getDrawable(R.styleable.CalendarSeekBar_indicatorIcon);
            if (indicatorIcon != null)
                mIndicatorIcon = indicatorIcon;

            int indicatorIconHalfWidth = mIndicatorIcon.getIntrinsicWidth() / 2;
            int indicatorIconHalfHeight = mIndicatorIcon.getIntrinsicHeight() / 2;
            mIndicatorIcon.setBounds(-indicatorIconHalfWidth, -indicatorIconHalfHeight, indicatorIconHalfWidth,
                    indicatorIconHalfHeight);

            mData = a.getInteger(R.styleable.CalendarSeekBar_data, mData);
            mMin = a.getInteger(R.styleable.CalendarSeekBar_min, mMin);
            mMax = a.getInteger(R.styleable.CalendarSeekBar_max, mMax);
            mStep = a.getInteger(R.styleable.CalendarSeekBar_step, mStep);

            mProgressWidth = (int) a.getDimension(R.styleable.CalendarSeekBar_progressWidth, mProgressWidth);
            progressColor = a.getColor(R.styleable.CalendarSeekBar_progressColor, progressColor);

            mArcWidth = (int) a.getDimension(R.styleable.CalendarSeekBar_arcWidth, mArcWidth);
            arcColor = a.getColor(R.styleable.CalendarSeekBar_arcColor, arcColor);

            mClockwise = a.getBoolean(R.styleable.CalendarSeekBar_clockwise,
                    mClockwise);
            mEnabled = a.getBoolean(R.styleable.CalendarSeekBar_enabled, mEnabled);
            a.recycle();
        }

        // range check
        mData = (mData > mMax) ? mMax : mData;
        mData = (mData < mMin) ? mMin : mData;

        mProgressSweep = (float) mData / MathHelper.valuePerDegree(mMax);

        mArcPaint = new Paint();
        mArcPaint.setColor(arcColor);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressWidth);

    }

    public void setStep(int step) {
        mStep = step;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int min = Math.min(width, height);

        mTranslateX = (int) (width * 0.5f);
        mTranslateY = (int) (height * 0.5f);

        int arcDiameter = min - getPaddingLeft();
        mArcRadius = arcDiameter / 2;
        float top = height / 2 - (arcDiameter / 2);
        float left = width / 2 - (arcDiameter / 2);
        mArcRect.set(left, top, left + arcDiameter, top + arcDiameter);

        updateIndicatorIconPosition();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mClockwise) {
            canvas.scale(-1, 1, mArcRect.centerX(), mArcRect.centerY());
        }


        // draw the arc and progress
        canvas.drawArc(mArcRect, ANGLE_OFFSET, 360, false, mArcPaint);
        canvas.drawArc(mArcRect, ANGLE_OFFSET, mProgressSweep, false, mProgressPaint);

        if (mEnabled) {
            // draw the indicator icon
            canvas.translate(mTranslateX - mIndicatorIconX, mTranslateY - mIndicatorIconY);
            mIndicatorIcon.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mEnabled) {
            this.getParent().requestDisallowInterceptTouchEvent(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mOnProgressChangeListener != null) {
                        mOnProgressChangeListener.onStartTrackingTouch(this);
                    }
                    //Logger.debug("MotionEvent.ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateOnTouch(event);
                    //Logger.debug("MotionEvent.ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    if (mOnProgressChangeListener != null) {
                        mOnProgressChangeListener.onStopTrackingTouch(this);
                    }
                    updateOnTouch(event);
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    //Logger.debug("MotionEvent.ACTION_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (mOnProgressChangeListener != null)
                        mOnProgressChangeListener.onStopTrackingTouch(this);
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    //Logger.debug("MotionEvent.ACTION_CANCEL");
                    break;
            }
            return true;
        } else {
            Logger.debug("Do nothing view is disabled");
            return false;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mIndicatorIcon != null && mIndicatorIcon.isStateful()) {
            int[] state = getDrawableState();
            mIndicatorIcon.setState(state);
        }
        invalidate();
    }

    /**
     * Update all the UI components on touch events.
     *
     * @param event MotionEvent
     */
    private void updateOnTouch(MotionEvent event) {
        setPressed(true);
        mTouchAngle = MathHelper.convertTouchEventDataToAngle(event.getX(), event.getY(), mTranslateX, mTranslateY, mClockwise);
        int progress = MathHelper.convertAngleToProgress(mTouchAngle, mMax);
        updateProgress(progress, true);
    }


    private void updateIndicatorIconPosition() {
        int thumbAngle = (int) (mProgressSweep + 90);
        mIndicatorIconX = (int) (mArcRadius * Math.cos(Math.toRadians(thumbAngle)));
        mIndicatorIconY = (int) (mArcRadius * Math.sin(Math.toRadians(thumbAngle)));
    }

    private void updateProgress(int progress, boolean fromUser) {

        mUpdateTimes++;
        if (progress == INVALID_VALUE) {
            Logger.debug("Some error occurred during update, the value is invalid");
            return;
        }


        // record previous and current progress change
        if (mUpdateTimes == 1) {
            mCurrentProgress = progress;
        } else {
            mPreviousProgress = mCurrentProgress;
            mCurrentProgress = progress;
        }

        if (mCurrentProgress > mMax) {
            mCurrentProgress = mCurrentProgress % mMax;
        }

        if (mCurrentProgress < mMin) {
            mCurrentProgress = (mCurrentProgress + mMax) % mMax;
        }

        mData = (int) (mCurrentProgress - (mCurrentProgress % mStep));
        if (mMin != 0) {
            if (mData == 0) {
                mData = mMax;
            }
        }
        if (mOnProgressChangeListener != null) {
            mProgressSweep = (float) mCurrentProgress / MathHelper.valuePerDegree(mMax);
            mOnProgressChangeListener.onProgressChanged(this, mData, fromUser);
            Logger.debug("Update Progress:  " + mData);
            updateIndicatorIconPosition();
            invalidate();
            return;
        }

    }

    public interface OnProgressChangeListener {

        /**
         * Notification that the data value has changed.
         *
         * @param calendarSeekBar The CalendarSeekBar view whose value has changed
         * @param data            The current data value.
         * @param fromUser        True if the data change was triggered by the user.
         */
        void onProgressChanged(CalendarSeekBar calendarSeekBar, int data, boolean fromUser);

        void onStartTrackingTouch(CalendarSeekBar calendarSeekBar);

        void onStopTrackingTouch(CalendarSeekBar calendarSeekBar);
    }

    public void setData(int data) {
        data = data > mMax ? mMax : data;
        data = data < mMin ? mMin : data;
        updateProgress(data, false);
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public void setMax(int mMax) {
        if (mMax <= mMin)
            throw new IllegalArgumentException("Max should not be less than min.");
        this.mMax = mMax;
    }

    public int getData() {
        return mData;
    }

    public void setMin(int min) {
        if (mMax <= mMin)
            throw new IllegalArgumentException("Min should not be greater than max.");
        mMin = min;
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        mOnProgressChangeListener = onProgressChangeListener;
    }
}
