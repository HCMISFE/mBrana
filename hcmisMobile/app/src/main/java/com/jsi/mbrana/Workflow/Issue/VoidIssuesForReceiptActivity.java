package com.jsi.mbrana.Workflow.Issue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.jsi.mbrana.Workflow.Adapter.Issue.IssuesTovoidAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Modules.Receive.Activity.ConfirmReceiveActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VoidIssuesForReceiptActivity extends AppCompatActivity implements IDataNotification {
    ArrayList<OrderModel> issuesToVoidList = new ArrayList<>();
    ProgressDialog progress;
    ListView issuesToVoidTable;
    android.support.v7.app.ActionBar ab;
    boolean isProgressVisible = false;
    private Tracker _mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_tovoid);
        ab = getSupportActionBar();
        SharedPreferences prefs = getSharedPreferences("userCredentials", 0);
        ab.setTitle("Issues");
        ab.setSubtitle(prefs.getString("Environment", ""));
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);

        issuesToVoidTable = (ListView) findViewById(R.id.issuestovoid_Table);
        issuesToVoidTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderModel selectedOrder = issuesToVoidList.get(position);
                if (selectedOrder.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.ISSUED.getOrderStatusCode())) {
                    Intent intent = new Intent(VoidIssuesForReceiptActivity.this, PrintIssueActivity.class);
                    intent.putExtra("Order", selectedOrder);
                    startActivity(intent);
                }
            }
        });

        new DataServiceTask(this, 1, false).execute("Order/GetOrder?ReceiptID=" + getIntent().getIntExtra("ReceiptID", 0)
                + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", this.getLocalClassName());
        _mTracker.setScreenName("Issue: Issues To Void");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                OrderModel order = new OrderModel();
                order.setID(jo.getInt("ID"));
                order.setRefNo(jo.getString("RefNo"));
                order.setDateCreated(jo.getString("Date"));
                order.setOrderStatus(jo.getString("OrderStatus"));
                order.setOrderStatusCode(jo.getString("OrderStatusCode"));
                order.setFacility(jo.getString("Facility"));
                order.setRequestedBy(jo.getInt("RequestedBy"));
                order.setContactPerson(jo.getString("ContactPerson"));
                order.setContactPersonMobileNumber(jo.getString("ContactPersonMobileNumber"));
                order.setRequestedNoOfItems(jo.getInt("RequestedNoOfItems"));
                order.setProcessedNoOfItems(jo.getInt("ProcessedNoOfItems"));
                order.setEnvironmentCode(jo.getString("EnvironmentCode"));
                order.setEnvironmentID(jo.getInt("EnvironmentID"));
                order.setModifiedDate(jo.getString("ModifiedDate"));
                issuesToVoidList.add(order);
            }
            Log.d("issuesToVoid", issuesToVoidList.toString());
            IssuesTovoidAdapter adapter = new IssuesTovoidAdapter(VoidIssuesForReceiptActivity.this, R.layout.layout_issues_tovoid, issuesToVoidList);
            issuesToVoidTable.setAdapter(adapter);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
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

    public void onBackPressed() {
        Intent intent = new Intent(VoidIssuesForReceiptActivity.this, ConfirmReceiveActivity.class);
        intent.putExtra("ReceiptID", getIntent().getIntExtra("ReceiptID", 0));
        intent.putExtra("ReceiptInvoiceID", getIntent().getIntExtra("ReceiptInvoiceID", 0));
        intent.putExtra("EnvironmentID", getIntent().getIntExtra("EnvironmentID", 0));
        intent.putExtra("EnvironmentCode", getIntent().getStringExtra("EnvironmentCode"));
        intent.putExtra("ModifiedInvoice", getIntent().getStringExtra("ModifiedInvoice"));
        intent.putExtra("DocumentTypesCode", getIntent().getStringExtra("DocumentTypesCode"));
        intent.putExtra("Supplier", getIntent().getStringExtra("Supplier"));
        intent.putExtra("ReceiptStatusCode", getIntent().getStringExtra("ReceiptStatusCode"));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        switch (items.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(VoidIssuesForReceiptActivity.this, ConfirmReceiveActivity.class);
                intent.putExtra("ReceiptID", getIntent().getIntExtra("ReceiptID", 0));
                intent.putExtra("ReceiptInvoiceID", getIntent().getIntExtra("ReceiptInvoiceID", 0));
                intent.putExtra("EnvironmentID", getIntent().getIntExtra("EnvironmentID", 0));
                intent.putExtra("EnvironmentCode", getIntent().getStringExtra("EnvironmentCode"));
                intent.putExtra("ModifiedInvoice", getIntent().getStringExtra("ModifiedInvoice"));
                intent.putExtra("DocumentTypesCode", getIntent().getStringExtra("DocumentTypesCode"));
                intent.putExtra("Supplier", getIntent().getStringExtra("Supplier"));
                intent.putExtra("ReceiptStatusCode", getIntent().getStringExtra("ReceiptStatusCode"));
                startActivity(intent);
                return true;
            case R.id.action_home:
                Intent i = new Intent(VoidIssuesForReceiptActivity.this, MainNavigationActivity.class);
                i.putExtra("ReceiptID", getIntent().getIntExtra("ReceiptID", 0));
                i.putExtra("ReceiptInvoiceID", getIntent().getIntExtra("ReceiptInvoiceID", 0));
                i.putExtra("EnvironmentID", getIntent().getIntExtra("EnvironmentID", 0));
                i.putExtra("EnvironmentCode", getIntent().getStringExtra("EnvironmentCode"));
                i.putExtra("ModifiedInvoice", getIntent().getStringExtra("ModifiedInvoice"));
                i.putExtra("DocumentTypesCode", getIntent().getStringExtra("DocumentTypesCode"));
                i.putExtra("Supplier", getIntent().getStringExtra("Supplier"));
                i.putExtra("ReceiptStatusCode", getIntent().getStringExtra("ReceiptStatusCode"));
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(items);
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
