package com.iotta.mydays.viewpresenter.view;

/**
 * Created by Galina Litkin on 05/08/2017.
 * Utility class to perform calculations
 */

public class MathHelper {

    public static double convertTouchEventDataToAngle(float xPos, float yPos, float mTranslateX, float mTranslateY, boolean mClockwise) {
        // transform touch coordinate into component coordinate
        float x = xPos - mTranslateX;
        float y = yPos - mTranslateY;

        x = (mClockwise) ? x : -x;
        double angle = Math.toDegrees(Math.atan2(y, x) + (Math.PI / 2));
        angle = (angle < 0) ? (angle + 360) : angle;
        return angle;
    }

    public static int convertAngleToProgress(double angle, int maxValue) {
        return (int) Math.round(valuePerDegree(maxValue) * angle);
    }

    public static float valuePerDegree(int maxValue) {
        return (float) (maxValue) / 360.0f;
    }
}
