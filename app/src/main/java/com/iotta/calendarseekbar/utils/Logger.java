package com.iotta.calendarseekbar.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.iotta.calendarseekbar.BuildConfig;

import java.util.Set;

/**
 * Wrapper class that formats output los
 */

public class Logger {

    private static final int ASSERT = Log.ASSERT;

    private static int mCurrentLevel = ASSERT;

    private static String mAppLogTag = BuildConfig.App_Log_String;

    public static void setAppTag(String appTag) {

        mAppLogTag = appTag;
    }

    public static void setLevel(int level) {

        mCurrentLevel = level;
    }

    public static int getLevel() {

        return mCurrentLevel;
    }

    public static boolean isEnabled(int level) {

        return level >= mCurrentLevel;
    }

    public static void debug(String tag, String message) {
        Log.d(tag, formatMessage(message));
    }

    public static void debug(String message) {

        Log.d(mAppLogTag, formatMessage(message));
    }

    public static void debug(String message, Throwable throwable) {

        Log.d(mAppLogTag, formatMessage(message));
    }

    public static void debug(Intent intent) {

        debug("Intent action=" + intent.getAction());

        // log extras

        Bundle extras = intent.getExtras();

        if (extras != null) {
            Set<String> keys = extras.keySet();

            if (keys == null ||  keys.isEmpty()) {
                debug("    extras: none");
            } else {

                debug("    extras:");

                for (String key : keys) {
                    debug("       " + key + "=" + extras.get(key));
                }

            }
        }
    }

    public static void info(String message) {

        Log.i(mAppLogTag, formatMessage(message));
    }

    public static void info(String tag, String message) {
        Log.i(tag, formatMessage(message));
    }

    public static void error(String error) {

        Log.e(mAppLogTag, formatMessage(error));
    }

    public static void error(String tag, String message) {
        Log.e(tag, formatMessage(message));
    }

    public static void error(String error, Throwable throwable) {

        Log.e(mAppLogTag, formatMessage(error), throwable);
    }

    public static void warn(String message) {

        Log.w(mAppLogTag, formatMessage(message));
    }

    public static void warn(String tag, String message) {

        Log.w(tag, formatMessage(message));
    }

    public static void warn(String message, Throwable throwable) {

        Log.w(mAppLogTag, formatMessage(message), throwable);
    }

    /**
     * Formats a log message
     * @param message message to write
     * @return formatted string of the log message
     */
    private static String formatMessage(String message) {

        String strClass = Thread.currentThread().getStackTrace()[4].getClassName();
        String strMethod = Thread.currentThread().getStackTrace()[4].getMethodName();
        int iLine = Thread.currentThread().getStackTrace()[4].getLineNumber();

        StringBuilder builder = new StringBuilder();

        String tag = "[" + strClass + ":" + strMethod + ":" + iLine + "]";

        String prefix = String.format("%-22s ", tag);

        builder.append(prefix).append(message);
        return builder.toString();
    }

}
