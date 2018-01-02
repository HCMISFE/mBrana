package com.jsi.mbrana.Workflow.Reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.Reports.SSOHAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SupplierSOHActivity extends AppCompatActivity implements IDataNotification {
    ArrayList<ItemModel> sSOH;
    ListView sSOHContainer;
    ProgressDialog progress;
    String EnvironmentCode = GlobalVariables.getSelectedEnvironmentCode();
    String ActivityCode = GlobalVariables.getActivityCode();
    private Tracker _mTracker;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_soh);
        setTitle("Supplier SOH");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences("userCredentials", 0);
        getSupportActionBar().setSubtitle(prefs.getString("Environment", ""));

        progress = new ProgressDialog(this);

        sSOHContainer = (ListView) findViewById(R.id.sSOH_tableContainer);
        new DataServiceTask(this, 1, false).execute("StockStatus/SohBySupplier?EnvironmentCode=" + EnvironmentCode + "&ActivityCode=" + ActivityCode, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        _mTracker.setScreenName("WorkFlow-Reports: Supplier SOH");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itemUnitObjs to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(SupplierSOHActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Report")) {
                Intent intent = new Intent(SupplierSOHActivity.this, ReportListActivity.class);
                startActivity(intent);
                return true;
            } else if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Dashboard")) {
                Intent i = new Intent(SupplierSOHActivity.this, MainNavigationActivity.class);
                startActivity(i);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        if (requestCode == 1) {
            sSOH = new ArrayList<>();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel item = new ItemModel();
                    item.setItemNameSH(jo.getString("FullItemName"));
                    item.setItemNameSH(jo.getString("ProductCN"));
                    item.setUnit(jo.getString("Unit"));
                    item.setStockStatus(jo.getString("Supplier"));
                    item.setSOH(jo.getInt("SOH"));
                    sSOH.add(item);
                }
                SSOHAdapter adapter = new SSOHAdapter(SupplierSOHActivity.this, R.layout.layout_supplierlist, sSOH);
                sSOHContainer.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
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
}
