package com.jsi.mbrana.Workflow.Issue.Depreciated;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Issue.IssueMenuActivity;

public class IssueActivity extends AppCompatActivity {
    LinearLayout optionOrder;
    LinearLayout optionDraft;
    LinearLayout optionApprove;
    LinearLayout optionIssue;
    FloatingActionButton fab;

    private Tracker _mTracker;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", this.getLocalClassName());
        _mTracker.setScreenName("WorkFlow-Issue: Issue");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // enable <- arrow on the toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set title
        getSupportActionBar().setTitle("Issue");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IssueActivity.this, NewOrderActivity.class);
                startActivity(intent);
            }
        });

        optionOrder = (LinearLayout) findViewById(R.id.IssueOptions_newOrder);
        optionDraft = (LinearLayout) findViewById(R.id.IssueOptions_pendingOrder);
        optionApprove = (LinearLayout) findViewById(R.id.IssueOptions_approveOrder);
        optionIssue = (LinearLayout) findViewById(R.id.IssueOptions_IssueOrder);

        optionOrder.setOnClickListener(new IssueOptionsClickHandeler());
        optionDraft.setOnClickListener(new IssueOptionsClickHandeler());
        optionApprove.setOnClickListener(new IssueOptionsClickHandeler());
        optionIssue.setOnClickListener(new IssueOptionsClickHandeler());
    }

    public class IssueOptionsClickHandeler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (v == optionOrder) {
                Intent intent = new Intent(IssueActivity.this, NewOrderActivity.class);
                startActivity(intent);
            } else if (v == optionDraft) {
                Intent intent = new Intent(IssueActivity.this, IssueMenuActivity.class);
                intent.putExtra("Type", "Draft");
                GlobalVariables.setPendingOrderType("Draft");
                startActivity(intent);
            } else if (v == optionApprove) {
                Intent intent = new Intent(IssueActivity.this, IssueMenuActivity.class);
                intent.putExtra("Type", "Approval");
                GlobalVariables.setPendingOrderType("Approval");
                startActivity(intent);
            } else if (v == optionIssue) {
                Intent intent = new Intent(IssueActivity.this, IssueMenuActivity.class);
                intent.putExtra("Type", "Issue");
                GlobalVariables.setPendingOrderType("Issue");
                startActivity(intent);
            }
        }
    }
}
