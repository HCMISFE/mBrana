package com.jsi.mbrana.Workflow.Reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.Reports.GITAdapter;
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

public class GitItemActivity extends AppCompatActivity implements IDataNotification {

    ArrayList<ItemModel> GitItem = new ArrayList<>();
    ListView GitContainer;
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
        setContentView(R.layout.activity_git_item);
        setTitle("GIT Item");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences("userCredentials", 0);
        getSupportActionBar().setSubtitle(prefs.getString("Environment", ""));

        progress = new ProgressDialog(this);

        GitContainer = (ListView) findViewById(R.id.git_tableContainer);

        // Make a call to populate the grid
        new DataServiceTask(this, 1, false).execute("StockStatus/GITByItem?EnvironmentCode=" + EnvironmentCode + "&ActivityCode=" + ActivityCode, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        _mTracker.setScreenName("Reports: GIT By Item");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent intent = new Intent(GitItemActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Report")) {
                Intent intent = new Intent(GitItemActivity.this, ReportListActivity.class);
                startActivity(intent);
                return true;
            } else if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Dashboard")) {
                Intent i = new Intent(GitItemActivity.this, MainNavigationActivity.class);
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
            GitItem = new ArrayList<>();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel item = new ItemModel();
                    item.setItemNameSH(jo.getString("ProductCN"));
                    item.setUnit(jo.getString("Unit"));
                    item.setQuantity(jo.getInt("QuantityInvoiced"));
                    item.setReceivedQuantity(jo.getInt("QuantityReceived"));
                    item.setGIT(jo.getInt("GIT"));
                    GitItem.add(item);
                }
                GITAdapter adapter = new GITAdapter(GitItemActivity.this, R.layout.layout_gititem, GitItem);
                GitContainer.setAdapter(adapter);
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
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
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
