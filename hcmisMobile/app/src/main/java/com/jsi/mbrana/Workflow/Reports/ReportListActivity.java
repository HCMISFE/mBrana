package com.jsi.mbrana.Workflow.Reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.Reports.ReportsListAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Order.OrderMenuActivity;

public class ReportListActivity extends AppCompatActivity {
    private Tracker _mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setTitle("Report");

        ListView reportsList = (ListView) findViewById(R.id.report_ReportListContainer);
        ReportsListAdapter reportsListAdapter = new ReportsListAdapter(this, R.layout.layout_reportlistview, getResources().getStringArray(R.array.ReporstList));
        reportsList.setAdapter(reportsListAdapter);

        reportsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(ReportListActivity.this, StockStatusActivity.class);
                        intent.putExtra("NavigationFrom", "Report");
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(ReportListActivity.this, BincardActivity.class);
                        intent.putExtra("NavigationFrom", "Report");
                        startActivity(intent);
                        break;
                    }
                    case 2: {
//                        Intent intent = new Intent(ReportListActivity.this, VRFReportActivity.class);
//                        intent.putExtra("NavigationFrom", "Report");
//                        startActivity(intent);
                        Intent intent = new Intent(ReportListActivity.this, OrderMenuActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Intent intent = new Intent(ReportListActivity.this, GitItemActivity.class);
                        intent.putExtra("NavigationFrom", "Report");
                        startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(ReportListActivity.this, SupplierSOHActivity.class);
                        intent.putExtra("NavigationFrom", "Report");
                        startActivity(intent);
                        break;
                    }
                    case 5: {
                        Intent intent = new Intent(ReportListActivity.this, ListofFacilityWithIssueActivity.class);
                        intent.putExtra("NavigationFrom", "Report");
                        startActivity(intent);
                        break;
                    }
                    case 6: {
                        Intent intent = new Intent(ReportListActivity.this, FacilitiesWithNoReorderActivity.class);
                        intent.putExtra("NavigationFrom", "Report");
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Reports: Report Menu");
        _mTracker.setScreenName("Reports: Report Menu");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(ReportListActivity.this,  MainNavigationActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(ReportListActivity.this, MainNavigationActivity.class);
        startActivity(intent);
    }
}
