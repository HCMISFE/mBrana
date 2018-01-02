package com.jsi.mbrana.Workflow.Order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.PO.POAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.POModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderMenuActivity extends AppCompatActivity implements IDataNotification {
    ArrayList<POModel> pos = new ArrayList<>();
    ProgressDialog progress;
    ListView poslist;
    SwipeRefreshLayout myRefreshLayout;
    SharedPreferences prefs;
    FloatingActionButton fab;
    private Tracker _mTracker;
    private long start;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("VRFs");
        prefs = getSharedPreferences("userCredentials", 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle(GlobalVariables.getSelectedEnvironment());

        progress = new ProgressDialog(this);

        // Intialize swipe refresh
        myRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        myRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRefreshLayout.setRefreshing(false);
                start = System.currentTimeMillis();
                new DataServiceTask(OrderMenuActivity.this, 1, false).execute("PurchaseOrder/GetPurchaseOrder" + "?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");
            }
        });

        //
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Create")
                        .setLabel("Create New VRF")
                        .build());
                Intent intent = new Intent(OrderMenuActivity.this, CreateOrderActivity.class);
                intent.putExtra("ActivityCode", prefs.getString("ActivityCode", ""));
                startActivity(intent);
            }
        });

        // Set listener to po list
        poslist = (ListView) findViewById(R.id.po_polisttable);
        poslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("VRF Selected")
                        .setLabel("Selected VRF:(" + pos.get(position).getID() + ")")
                        .build());
                if (pos.get(position).getPurchaseOrderStatusCode().equalsIgnoreCase(Constants.PurchaseOrderStatus.Draft.getOrderStatusCode())) {
                    Intent intent = new Intent(OrderMenuActivity.this, UpdateOrderActivity.class);
                    intent.putExtra("ActivityCode", prefs.getString("ActivityCode", ""));
                    intent.putExtra("Header", pos.get(position));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OrderMenuActivity.this, ViewOrderActivity.class);
                    intent.putExtra("Header", pos.get(position));
                    startActivity(intent);
                }
            }
        });

        //
        start = System.currentTimeMillis();

        // Load orders
        new DataServiceTask(this, 1, false).execute("PurchaseOrder/GetPurchaseOrder" + "?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("GA-Screen", "VRFs: VRFs Menu");
        _mTracker.setScreenName("VRFs: VRFs Menu");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OrderMenuActivity.this, MainNavigationActivity.class);
        startActivity(i);
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        try {
            pos = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                POModel po = new POModel();
                po.setID(jo.getInt("ID"));
                po.setPONumber(jo.getString("PONumber"));
                po.setEnvironmentCode(jo.getString("EnvironmentCode"));
                po.setEnvironmentID(jo.getInt("EnvironmentID"));
                po.setPurchaseOrderStatus(jo.getString("PurchaseOrderStatus"));
                po.setPurchaseOrderStatusCode(jo.getString("PurchaseOrderStatusCode"));
                po.setDetailCount(jo.getInt("DetailCount"));
                po.setSupplier(jo.getString("Supplier"));
                po.setModifiedDate(jo.getString("ModifiedDate"));
                po.setSupplierID(jo.getInt("SupplierID"));
                po.setFillRate(jo.getInt("FillRate"));
                pos.add(po);
            }
            POAdapter adapter = new POAdapter(OrderMenuActivity.this, R.layout.layout_polist, pos);
            poslist.setAdapter(adapter);
            long stop = System.currentTimeMillis();
            long interval = (stop - start) / 1000;
            _mTracker.send(new HitBuilders.TimingBuilder()
                    .setCategory("Loading")
                    .setValue(interval)
                    .setVariable("VRFs")
                    .setLabel("PurchaseOrder/GetPurchaseOrder")
                    .build());
            Log.d("GA-Timer", "Loading Time for VRFs:(PurchaseOrder/GetPurchaseOrder) is " + interval + "Seconds");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(OrderMenuActivity.this, MainNavigationActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handelNotification(String message) {
        if (message.equals("No Data"))
            ShowSnackNotification("VRF not filled yet. Please go to Add.", R.color.successMessage);
        else
            ShowSnackNotification(message, R.color.successMessage);
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

    public void ShowSnackNotification(String message, int color) {
        Snackbar snack_bar;
        snack_bar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(message), Snackbar.LENGTH_LONG);
        snack_bar.setActionTextColor(getResources().getColor(R.color.white));
        View view = snack_bar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        view.setBackgroundColor(ContextCompat.getColor(this, color));
        snack_bar.show();
    }
}
