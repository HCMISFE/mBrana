package com.jsi.mbrana.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String SETTINGS_NAME = "mBranaPrefs";
    private static PreferenceManager sSharedPrefs;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;

    public PreferenceManager(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceManager getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new PreferenceManager(context.getApplicationContext());
        }
        return sSharedPrefs;
    }

    public static PreferenceManager getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }

        //Option 1:
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");

        //Option 2:
        // Alternatively, you can create a new instance here
        // with something like this:
        // getInstance(MyCustomApplication.getAppContext());
    }

    public void put(PreferenceKey preferenceKey, String val) {
        doEdit();
        mEditor.putString(preferenceKey.name(), val);
        doCommit();
    }

    public void put(PreferenceKey preferenceKey, int val) {
        doEdit();
        mEditor.putInt(preferenceKey.name(), val);
        doCommit();
    }

    public void put(PreferenceKey preferenceKey, boolean val) {
        doEdit();
        mEditor.putBoolean(preferenceKey.name(), val);
        doCommit();
    }

    public void put(PreferenceKey preferenceKey, float val) {
        doEdit();
        mEditor.putFloat(preferenceKey.name(), val);
        doCommit();
    }

    /**
     * Convenience method for storing doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param preferenceKey The enum of the preference to store.
     * @param val The new value for the preference.
     */
    public void put(PreferenceKey preferenceKey, double val) {
        doEdit();
        mEditor.putString(preferenceKey.name(), String.valueOf(val));
        doCommit();
    }

    public void put(PreferenceKey preferenceKey, long val) {
        doEdit();
        mEditor.putLong(preferenceKey.name(), val);
        doCommit();
    }

    public String getString(PreferenceKey preferenceKey, String defaultValue) {
        return mPref.getString(preferenceKey.name(), defaultValue);
    }

    public String getString(PreferenceKey preferenceKey) {
        return mPref.getString(preferenceKey.name(), "");
    }

    public int getInt(PreferenceKey preferenceKey) {
        return mPref.getInt(preferenceKey.name(), 0);
    }

    public int getInt(PreferenceKey preferenceKey, int defaultValue) {
        return mPref.getInt(preferenceKey.name(), defaultValue);
    }

    public long getLong(PreferenceKey preferenceKey) {
        return mPref.getLong(preferenceKey.name(), 0);
    }

    public long getLong(PreferenceKey preferenceKey, long defaultValue) {
        return mPref.getLong(preferenceKey.name(), defaultValue);
    }

    public float getFloat(PreferenceKey preferenceKey) {
        return mPref.getFloat(preferenceKey.name(), 0);
    }

    public float getFloat(PreferenceKey preferenceKey, float defaultValue) {
        return mPref.getFloat(preferenceKey.name(), defaultValue);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param preferenceKey The enum of the preference to fetch.
     */
    public double getDouble(PreferenceKey preferenceKey) {
        return getDouble(preferenceKey, 0);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p/>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param preferenceKey The enum of the preference to fetch.
     */
    public double getDouble(PreferenceKey preferenceKey, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(preferenceKey.name(), String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(PreferenceKey preferenceKey, boolean defaultValue) {
        return mPref.getBoolean(preferenceKey.name(), defaultValue);
    }

    public boolean getBoolean(PreferenceKey preferenceKey) {
        return mPref.getBoolean(preferenceKey.name(), false);
    }

    /**
     * Remove preferenceKeys from SharedPreferences.
     *
     * @param preferenceKeys The enum of the key(s) to be removed.
     */
    public void remove(PreferenceKey... preferenceKeys) {
        doEdit();
        for (PreferenceKey preferenceKey : preferenceKeys) {
            mEditor.remove(preferenceKey.name());
        }
        doCommit();
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    public void edit() {
        mBulkUpdate = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }
}
