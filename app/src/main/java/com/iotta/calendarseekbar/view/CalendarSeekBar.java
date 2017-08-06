package com.iotta.calendarseekbar.view;

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

import com.iotta.calendarseekbar.R;
import com.iotta.calendarseekbar.utils.Logger;

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
     * The current points value.
     */
    private int mPoints = MIN;

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
    private int mStep = 10;

    /**
     * The Drawable for the seek arc thumbnail
     */
    private Drawable mIndicatorIcon;


    private int mProgressWidth = 12;
    private int mArcWidth = 12;
    private boolean mClockwise = true;
    private boolean mEnabled = true;

    /**
     * The counts of point update to determine whether to change previous progress.
     */
    private int mUpdateTimes = 0;
    private float mPreviousProgress = -1;
    private float mCurrentProgress = 0;

    /**
     * Determine whether reach max of point.
     */
    private boolean isMax = false;

    /**
     * Determine whether reach min of point.
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
    private OnSwagPointsChangeListener mOnSwagPointsChangeListener;

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
                    R.styleable.SwagPoints, 0, 0);

            Drawable indicatorIcon = a.getDrawable(R.styleable.SwagPoints_indicatorIcon);
            if (indicatorIcon != null)
                mIndicatorIcon = indicatorIcon;

            int indicatorIconHalfWidth = mIndicatorIcon.getIntrinsicWidth() / 2;
            int indicatorIconHalfHeight = mIndicatorIcon.getIntrinsicHeight() / 2;
            mIndicatorIcon.setBounds(-indicatorIconHalfWidth, -indicatorIconHalfHeight, indicatorIconHalfWidth,
                    indicatorIconHalfHeight);

            mPoints = a.getInteger(R.styleable.SwagPoints_points, mPoints);
            mMin = a.getInteger(R.styleable.SwagPoints_min, mMin);
            mMax = a.getInteger(R.styleable.SwagPoints_max, mMax);
            mStep = a.getInteger(R.styleable.SwagPoints_step, mStep);

            mProgressWidth = (int) a.getDimension(R.styleable.SwagPoints_progressWidth, mProgressWidth);
            progressColor = a.getColor(R.styleable.SwagPoints_progressColor, progressColor);

            mArcWidth = (int) a.getDimension(R.styleable.SwagPoints_arcWidth, mArcWidth);
            arcColor = a.getColor(R.styleable.SwagPoints_arcColor, arcColor);

            mClockwise = a.getBoolean(R.styleable.SwagPoints_clockwise,
                    mClockwise);
            mEnabled = a.getBoolean(R.styleable.SwagPoints_enabled, mEnabled);
            a.recycle();
        }

        // range check
        mPoints = (mPoints > mMax) ? mMax : mPoints;
        mPoints = (mPoints < mMin) ? mMin : mPoints;

        mProgressSweep = (float) mPoints / MathHelper.valuePerDegree(mMax);

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
                    if (mOnSwagPointsChangeListener != null) {
                        mOnSwagPointsChangeListener.onStartTrackingTouch(this);
                    }
                    Logger.debug("MotionEvent.ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateOnTouch(event);
                    Logger.debug("MotionEvent.ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    if (mOnSwagPointsChangeListener != null) {
                        mOnSwagPointsChangeListener.onStopTrackingTouch(this);
                    }
                    updateOnTouch(event);
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    Logger.debug("MotionEvent.ACTION_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (mOnSwagPointsChangeListener != null)
                        mOnSwagPointsChangeListener.onStopTrackingTouch(this);
                    setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    Logger.debug("MotionEvent.ACTION_CANCEL");
                    break;
            }
            return true;
        }else{
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
        mTouchAngle = MathHelper.convertTouchEventPointToAngle(event.getX(), event.getY(),  mTranslateX,  mTranslateY, mClockwise);
        int progress =  MathHelper.convertAngleToProgress(mTouchAngle, mMax);
        updateProgress(progress, true);
    }


    private void updateIndicatorIconPosition() {
        int thumbAngle = (int) (mProgressSweep + 90);
        mIndicatorIconX = (int) (mArcRadius * Math.cos(Math.toRadians(thumbAngle)));
        mIndicatorIconY = (int) (mArcRadius * Math.sin(Math.toRadians(thumbAngle)));
    }

    private void updateProgress(int progress, boolean fromUser) {

        Logger.debug("updateProgress");

        // detect points change closed to max or min
        final int maxDetectValue = (int) ((double) mMax * 0.95);
        final int minDetectValue = (int) ((double) mMax * 0.05) + mMin;

        mUpdateTimes++;
        if (progress == INVALID_VALUE) {
            Logger.debug("Some error occurred during update, the value is invalid");
            return;
        }

        // avoid accidentally touch to become max from original point
        if (progress > maxDetectValue && mPreviousProgress == INVALID_VALUE) {
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

        mPoints = progress - (progress % mStep);

        /**
         * Determine whether reach max or min to lock point update event.
         *
         * When reaching max, the progress will drop from max (or maxDetectPoints ~ max
         * to min (or min ~ minDetectPoints) and vice versa.
         *
         * If reach max or min, stop increasing / decreasing to avoid exceeding the max / min.
         */
        if (mUpdateTimes > 1 && !isMin && !isMax) {
            if (mPreviousProgress >= maxDetectValue && mCurrentProgress <= minDetectValue &&
                    mPreviousProgress > mCurrentProgress) {
                isMax = true;
                progress = mMax;
                mPoints = mMax;
                if (mOnSwagPointsChangeListener != null) {
                    mOnSwagPointsChangeListener
                            .onPointsChanged(this, progress, fromUser);
                    Logger.debug("Update Progress:  "+progress);
                    updateIndicatorIconPosition();
                    invalidate();
                    return;
                }
            } else if ((mCurrentProgress >= maxDetectValue
                    && mPreviousProgress <= minDetectValue
                    && mCurrentProgress > mPreviousProgress) || mCurrentProgress <= mMin) {
                isMin = true;
                progress = mMin;
                mPoints = mMin;
                if (mOnSwagPointsChangeListener != null) {
                    mOnSwagPointsChangeListener
                            .onPointsChanged(this, progress, fromUser);
                    Logger.debug("Update Progress:  "+progress);
                    updateIndicatorIconPosition();
                    invalidate();
                    return;
                }
            }
        } else {

            // Detect whether decreasing from max or increasing from min, to unlock the update event.
            // Make sure to check in detect range only.
            if (isMax & (mCurrentProgress < mPreviousProgress) && mCurrentProgress >= maxDetectValue) {
                isMax = false;
            }
            if (isMin
                    && (mPreviousProgress < mCurrentProgress)
                    && mPreviousProgress <= minDetectValue && mCurrentProgress <= minDetectValue
                    && mPoints >= mMin) {
                isMin = false;
            }
        }

        if (!isMax && !isMin) {
            progress = (progress > mMax) ? mMax : progress;
            progress = (progress < mMin) ? mMin : progress;

            if (mOnSwagPointsChangeListener != null) {
                progress = progress - (progress % mStep);

                mOnSwagPointsChangeListener
                        .onPointsChanged(this, progress, fromUser);
                Logger.debug("Update Progress:  "+progress);
            }

            mProgressSweep = (float) progress /  MathHelper.valuePerDegree(mMax);
            updateIndicatorIconPosition();
            invalidate();
        }
    }

    public interface OnSwagPointsChangeListener {

        /**
         * Notification that the point value has changed.
         *
         * @param swagPoints The SwagPoints view whose value has changed
         * @param points     The current point value.
         * @param fromUser   True if the point change was triggered by the user.
         */
        void onPointsChanged(CalendarSeekBar swagPoints, int points, boolean fromUser);

        void onStartTrackingTouch(CalendarSeekBar swagPoints);

        void onStopTrackingTouch(CalendarSeekBar swagPoints);
    }

    public void setPoints(int points) {
        points = points > mMax ? mMax : points;
        points = points < mMin ? mMin : points;
        updateProgress(points, false);
    }

    public void setProgressWidth(int mProgressWidth) {
        this.mProgressWidth = mProgressWidth;
        mProgressPaint.setStrokeWidth(mProgressWidth);
    }

    public void setArcWidth(int mArcWidth) {
        this.mArcWidth = mArcWidth;
        mArcPaint.setStrokeWidth(mArcWidth);
    }

    public void setClockwise(boolean isClockwise) {
        mClockwise = isClockwise;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public void setProgressColor(int color) {
        mProgressPaint.setColor(color);
        invalidate();
    }

    public void setArcColor(int color) {
        mArcPaint.setColor(color);
        invalidate();
    }

    public void setMax(int mMax) {
        if (mMax <= mMin)
            throw new IllegalArgumentException("Max should not be less than min.");
        this.mMax = mMax;
    }

    public int getMin() {
        return mMin;
    }

    public int getPoint(){
        return mPoints;
    }

    public void setMin(int min) {
        if (mMax <= mMin)
            throw new IllegalArgumentException("Min should not be greater than max.");
        mMin = min;
    }

    public int getStep() {
        return mStep;
    }

    public void setOnSwagPointsChangeListener(OnSwagPointsChangeListener onSwagPointsChangeListener) {
        mOnSwagPointsChangeListener = onSwagPointsChangeListener;
    }
}
