package com.jsi.mbrana.Workflow.Issue.Depreciated;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Adapter.Dashboard.ViewPagerAdapter;
import com.jsi.mbrana.Workflow.Issue.Depreciated.Fragments.PendingOrderFragment;
import com.jsi.mbrana.Workflow.Issue.Depreciated.Fragments.NewOrder_Fragment;

public class OrdersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Tracker _mTracker;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen",this.getLocalClassName());
        _mTracker.setScreenName("WorkFlow-Issue: Orders");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewOrder_Fragment(), "New");
        adapter.addFragment(new PendingOrderFragment(), "Submit");
        viewPager.setAdapter(adapter);
    }
}