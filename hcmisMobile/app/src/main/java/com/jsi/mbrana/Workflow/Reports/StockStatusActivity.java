package com.jsi.mbrana.Workflow.Reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.Reports.Adapter_Stockstatus;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.UserActivityCodeHelper;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.SoH_Model;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockStatusActivity extends AppCompatActivity implements IDataNotification {
    ArrayList<SoH_Model> SOHList;
    ListView sohtablecontainer;
    ProgressDialog progress;
    android.support.v7.app.ActionBar ab;
    CheckBox IsCampain;
    SwipeRefreshLayout swipeContainer;
    String ActivityCode;
    boolean isProgressVisible = false;
    private Tracker _mTracker;
    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_status);

        progress = new ProgressDialog(this);

        final SharedPreferences prefs = getSharedPreferences("userCredentials", 0);

        sohtablecontainer = (ListView) findViewById(R.id.StockStatusList_TableContainer);
        ab = getSupportActionBar();
        ab.setSubtitle(prefs.getString("Environment", ""));
        ActivityCode = prefs.getString("ActivityCode", "");

        IsCampain = (CheckBox) findViewById(R.id.isCampinCheckBoxSOH);
        if (UserActivityCodeHelper.getAssignedTo().equals("Malaria"))
            IsCampain.setVisibility(View.GONE);
        IsCampain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadStockStatusReport(prefs.getString("EnvironmentCode", ""), IsCampain.isChecked());
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeStockStatusContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                LoadStockStatusReport(prefs.getString("EnvironmentCode", ""), IsCampain.isChecked());
            }
        });

        LoadStockStatusReport(prefs.getString("EnvironmentCode", ""), IsCampain.isChecked());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("GA-Screen", "Stock Status");
        _mTracker.setScreenName("Stock Status");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void LoadStockStatusReport(String environmentCode, Boolean IsCampaign) {
        start = System.currentTimeMillis();
        new DataServiceTask(this, 1, false).execute("StockStatus/GetSOH?EnvironmentCode=" + environmentCode + "&IsCampaign=" + IsCampaign + "&ActivityCode=" + ActivityCode, "");
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        try {
            SOHList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                SoH_Model item = new SoH_Model();
                item.setFullitemname(jo.getString("fullitemname"));
                item.setProductCN(jo.getString("ProductCN"));
                item.setUnit(jo.getString("Unit"));
                item.setItemId(jo.getInt("ItemId"));
                item.setUnitId(jo.getInt("UnitId"));
                item.setActivityID(jo.getInt("ActivityID"));
                item.setSOH(jo.getInt("SOH"));
                item.setUsableSOH(jo.getInt("UsableSOH"));
                item.setAWC(jo.getDouble("AWC"));
                item.setWOS(jo.getDouble("WOS"));
                item.setGIT(jo.getInt("GIT"));
                item.setGITMOS(jo.getString("GITMOS"));
                item.setOrdered(jo.getInt("Ordered"));
                item.setOrderedMos(jo.getInt("OrderedMos"));
                item.setExpiredQuantity(jo.getInt("ExpiredQuantity"));
                item.setNearExpiredQuantity(jo.getInt("NearExpiredQuantity"));
                item.setVVMExpired(jo.getInt("VVMExpiredQuantity"));
                SOHList.add(item);
            }
            Adapter_Stockstatus adapter = new Adapter_Stockstatus(this, R.layout.layout_stock_status_report, SOHList);
            sohtablecontainer.setAdapter(adapter);
            long stop = System.currentTimeMillis();
            long interval = (stop - start) / 1000;
            _mTracker.send(new HitBuilders.TimingBuilder()
                    .setCategory("Loading")
                    .setValue(interval)
                    .setVariable("StockStatusActivity")
                    .setLabel("StockStatusActivity/GetSOH")
                    .build());
            Log.d("GA-Timer", "Loading Time for StockStatusActivity:(StockStatusActivity/GetSOH) is " + interval + "Seconds");
        } catch (Exception e) {
            e.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build());
        }
    }

    @Override
    public void handelNotification(String message) {

    }

    @Override
    public void handelProgressDialog(Boolean showprogress, String Message) {
        try {
            if (progress == null || showprogress == null)
                return;
            if (showprogress) {
                //Setting the Message
                if (Message != null) {
                    if (Message.length() > 0)
                        progress.setMessage(Message);
                } else
                    progress.setMessage("Loading");
                //Showing the Dialog
                if (!progress.isShowing())
                    progress.show();
            } else {
                //Hiding the Dialog
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(this, null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build());
        }
    }

    @Override
    public void readFromDatabase(int requestCode) {

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
            Intent intent = new Intent(StockStatusActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Report")) {
                Intent intent = new Intent(StockStatusActivity.this, ReportListActivity.class);
                startActivity(intent);
                return true;
            } else if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Dashboard")) {
                Intent i = new Intent(StockStatusActivity.this, MainNavigationActivity.class);
                startActivity(i);
                return true;
            } else if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("MainOptionPage")) {
                Intent i = new Intent(StockStatusActivity.this, MainNavigationActivity.class);
                startActivity(i);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
