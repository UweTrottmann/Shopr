
package com.uwetrottmann.shopr.utils;

/**
 * Stores data about the current task.
 */
public class Statistics {

    private static Statistics _instance;

    private long mStartTime;
    private int mCycleCount;
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

    public synchronized void startTask(String username, boolean isDiversity) {
        mIsStarted = true;
        mUserName = username;
        mIsDiversity = isDiversity;
        mStartTime = System.currentTimeMillis();
        mCycleCount = 0;
    }

    public void incrementCycleCount() {
        mCycleCount++;
    }

    public synchronized void stopTask() {
        if (!mIsStarted) {
            return;
        }
        mIsStarted = false;
        long duration = System.currentTimeMillis() - mStartTime;
        // TODO: write to database
    }

}
