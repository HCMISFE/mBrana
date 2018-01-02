package com.jsi.mbrana;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by Baakal Tesfaye & Surafel Nigussie on 3/8/2017.
 */

public class AnalyticsApplication extends Application {
    private static final String APP_TRACKER_ID = "UA-93220873-2";
    private static final String GLOBAL_TRACKER_ID = "UA-93220873-2";
    private HashMap<TrackerName, Tracker> _mTrackers = new HashMap<TrackerName, Tracker>();

    synchronized public Tracker getDefaultTracker() {
        return getTracker(TrackerName.APP_TRACKER);
    }

    synchronized public Tracker getTracker(TrackerName trackerID) {
        if (!_mTrackers.containsKey(trackerID)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker mTracker;

            if (trackerID == TrackerName.APP_TRACKER) {
                mTracker = analytics.newTracker(APP_TRACKER_ID);
                mTracker.enableExceptionReporting(true);

            } else {
                mTracker = analytics.newTracker(GLOBAL_TRACKER_ID);
            }

            _mTrackers.put(trackerID, mTracker);
        }
        return _mTrackers.get(trackerID);
    }

    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
    }
}
