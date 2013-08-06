
package com.uwetrottmann.shopr.eval;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.google.analytics.tracking.android.EasyTracker;
import com.uwetrottmann.shopr.provider.ShoprContract.Stats;

/**
 * Stores data about the current task.
 */
public class Statistics {

    private static Statistics _instance;

    private long mStartTime;
    private int mCycleCount;
    private int mCyclePositiveCount;
    private String mUserName;
    private boolean mIsDiversity;

    private boolean mIsStarted;

    private Statistics() {

    }

    public synchronized static Statistics get() {
        if (_instance == null) {
            _instance = new Statistics();
        }
        return _instance;
    }

    /**
     * Saves the current time, user name and task type until
     * {@link #finishTask(Context)} is called.
     */
    public synchronized void startTask(String username, boolean isDiversity) {
        mIsStarted = true;
        mUserName = username;
        mIsDiversity = isDiversity;
        mStartTime = System.currentTimeMillis();
        mCycleCount = 0;
        mCyclePositiveCount = 0;
    }

    /**
     * Increases the recommendation cycle count by 1. Also the positive cycle
     * count if isPositive is true.
     */
    public synchronized void incrementCycleCount(boolean isPositive) {
        mCycleCount++;
        if (isPositive) {
            mCyclePositiveCount++;
        }
    }

    /**
     * Stops the task and writes all data to the database.
     * 
     * @return The {@link Uri} pointing to the new data set or {@code null} if
     *         {@link #startTask(String, boolean)} was not called before.
     */
    public synchronized Uri finishTask(Context context) {
        if (!mIsStarted) {
            return null;
        }

        mIsStarted = false;
        long duration = System.currentTimeMillis() - mStartTime;

        // Write to database
        ContentValues statValues = new ContentValues();
        statValues.put(Stats.USERNAME, mUserName);
        statValues.put(Stats.TASK_TYPE, mIsDiversity ? "div" : "sim");
        statValues.put(Stats.CYCLE_COUNT, mCycleCount + "(" + mCyclePositiveCount + "+)");
        statValues.put(Stats.DURATION, duration);
        final Uri inserted = context.getContentResolver().insert(Stats.CONTENT_URI, statValues);

        EasyTracker.getTracker().sendEvent("Results", "Type",
                mIsDiversity ? "Diversity" : "Similarity",
                (long) 0);
        EasyTracker.getTracker().sendEvent("Results", "Value", "Cycles", (long) mCycleCount);
        EasyTracker.getTracker().sendEvent("Results", "Value", "Cycles (positive)",
                (long) mCyclePositiveCount);
        EasyTracker.getTracker().sendEvent("Results", "Value", "Duration", duration);

        return inserted;
    }

    public synchronized String getUserName() {
        return mUserName;
    }

}
