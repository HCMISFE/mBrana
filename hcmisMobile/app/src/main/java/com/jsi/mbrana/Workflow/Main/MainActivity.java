package com.jsi.mbrana.Workflow.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Tracker _mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _mTracker.setScreenName("WorkFlow-Main: Main");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
