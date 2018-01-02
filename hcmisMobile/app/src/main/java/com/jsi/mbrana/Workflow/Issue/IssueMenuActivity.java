package com.jsi.mbrana.Workflow.Issue;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.Issue.PendingOrderAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.IssueHeaderModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Issue.Depreciated.IssueOrderActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class IssueMenuActivity extends AppCompatActivity implements IDataNotification {
    ArrayList<OrderModel> pendingOrders = new ArrayList<>();
    ProgressDialog progress;
    ListView pendingOrderTable;
    SwipeRefreshLayout myRefreshLayout;
    LinearLayout nodata;
    SharedPreferences mBranaPrefs, prefs_environment;
    FloatingActionButton addNewIssueButton;
    private Tracker _mTracker;
    private long start;
    boolean isProgressVisible = false;
    Toolbar toolbar;
    PendingOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Issues");
        getSupportActionBar().setSubtitle(GlobalVariables.getSelectedEnvironment());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);

        nodata = (LinearLayout) this.findViewById(R.id.PendingIssuedMainNoDataLayout);

        mBranaPrefs = getSharedPreferences("mBranaPrefs", 0);
        prefs_environment = getSharedPreferences("Environment", 0);

        // Initialize swipe refresh
        myRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        myRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRefreshLayout.setRefreshing(false);
                start = System.currentTimeMillis();
                new DataServiceTask(IssueMenuActivity.this, 1).execute("Order/GetOrder"
                        + "?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode()
                        + "&UserID=" + mBranaPrefs.getInt("UserID", 0)
                        + "&ActivityCode=" + mBranaPrefs.getString("DefaultActivityId", ""), "");
            }
        });

        // Add New Issue Button
        addNewIssueButton = (FloatingActionButton) findViewById(R.id.addNewIssueButton);
        addNewIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Create")
                        .setLabel("Create New Issue")
                        .build());

                Intent intent = new Intent(IssueMenuActivity.this, FacilitySelectionActivity.class);
                intent.putExtra("ActivityCode", mBranaPrefs.getString("ActivityCode", ""));
                startActivity(intent);
            }
        });

        // Handel Item click for list view
        pendingOrderTable = (ListView) findViewById(R.id.pendingOrder_PendingTable);
        pendingOrderTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderModel selectedOrder = pendingOrders.get(position);
                Intent intent = null;
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User Action")
                        .setAction("Issue Selection")
                        .setLabel("Selected Issue:(" + selectedOrder.getID() + ")")
                        .build());
                if (selectedOrder.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.DRAFT.getOrderStatusCode())) {
                    intent = new Intent(IssueMenuActivity.this, UpdateIssueActivity.class);
                    intent.putExtra("ActivityCode", mBranaPrefs.getString("ActivityCode", ""));
                    intent.putExtra("Order", selectedOrder);
                } else if (selectedOrder.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode())) {
                    intent = new Intent(IssueMenuActivity.this, ApproveIssueActivity.class);
                    intent.putExtra("ActivityCode", mBranaPrefs.getString("ActivityCode", ""));
                    intent.putExtra("IsSyncZoneToWoreda", pendingOrders.get(0).getWoredaToZoneSync());
                    intent.putExtra("Order", selectedOrder);
                } else if (selectedOrder.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.APPROVED.getOrderStatusCode())) {
                    intent = new Intent(IssueMenuActivity.this, IssueOrderActivity.class);
                    intent.putExtra("ActivityCode", mBranaPrefs.getString("ActivityCode", ""));
                    intent.putExtra("Order", selectedOrder);
                } else if (selectedOrder.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.PickListed.getOrderStatusCode())) {
                    intent = new Intent(IssueMenuActivity.this, ConfirmPicklistActivity.class);
                    intent.putExtra("ActivityCode", mBranaPrefs.getString("ActivityCode", ""));
                    intent.putExtra("Order", selectedOrder);
                } else if (selectedOrder.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.ISSUED.getOrderStatusCode()) || selectedOrder.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.VOID.getOrderStatusCode())) {
                    // check if the order has multiple issues
                    String issues = selectedOrder.getSTVs();
                    if (issues == null) return;
                    else if (issues.split("\\|").length > 1) {
                        // if the issue has multiple issue show dialog to choose the approprate issue
                        try {
                            ArrayList<IssueHeaderModel> issuesList = new ArrayList<>();
                            ArrayList<String> stvsList = new ArrayList<String>();
                            for (int i = 0; i < issues.split("\\|").length; i++) {
                                String currentvalue = issues.split("\\|")[i];
                                IssueHeaderModel issue = new IssueHeaderModel();
                                issue.setIssNo(currentvalue.split("\\:")[1]);
                                issue.setID(Integer.parseInt(currentvalue.split("\\:")[0]));
                                issuesList.add(issue);
                                stvsList.add(issue.getIssNo());
                            }
                            showMultipleIssueDialog(stvsList.toArray(new String[0]), issuesList, selectedOrder);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            _mTracker.send(new HitBuilders.ExceptionBuilder()
                                    .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                            .getDescription(Thread.currentThread().getName(), ex))
                                    .setFatal(false)
                                    .build());
                        }
                    } else {
                        // if the issue has single issue
                        intent = new Intent(IssueMenuActivity.this, PrintIssueActivity.class);
                        intent.putExtra("Order", selectedOrder);
                    }
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        start = System.currentTimeMillis();
        new DataServiceTask(this, 1, false).execute("Order/GetOrder" + "?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode() + "&UserID=" + mBranaPrefs.getInt("UserID", 0) + "&ActivityCode=" + mBranaPrefs.getString("DefaultActivityId", ""), "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Issues: Issue Menu");
        _mTracker.setScreenName("Issues: Issue Menu");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void showMultipleIssueDialog(String[] values, final ArrayList<IssueHeaderModel> issues, final OrderModel selectedOrder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("STVs")
                .setItems(values, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("itemClicked=>", issues.get(which).getIssNo());
                        Intent intent = new Intent(IssueMenuActivity.this, PrintIssueActivity.class);
                        intent.putExtra("Order", selectedOrder);
                        intent.putExtra("IssueID", issues.get(which).getID());
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        nodata.setVisibility(LinearLayout.GONE);
        pendingOrderTable.setVisibility(View.VISIBLE);
        try {
            pendingOrders = new ArrayList<>();
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
                order.setSTVs(jo.getString("STVs"));
                order.setCampaign(jo.getBoolean("IsCampaign"));
                order.setWoredaToZoneSync(jo.getBoolean("isWoredaToZoneSync"));
                pendingOrders.add(order);
            }
            adapter = new PendingOrderAdapter(IssueMenuActivity.this, R.layout.layout_pendingorder, pendingOrders);
            pendingOrderTable.setAdapter(adapter);
            long stop = System.currentTimeMillis();
            long interval = (stop - start) / 1000;
            _mTracker.send(new HitBuilders.TimingBuilder()
                    .setCategory("Loading")
                    .setValue(interval)
                    .setVariable("Issue")
                    .setLabel("Order/GetOrder")
                    .build());
            Log.d("GA-Timer", "Loading Time for Issue:(Order/GetOrder) is " + interval + "Seconds");
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
        if (message.equals("No Data")) {
            nodata.setVisibility(LinearLayout.VISIBLE);
            pendingOrderTable.setVisibility(View.GONE);
        } else if (message.equals(getResources().getString(R.string.connectionErrorMessage))) {
            Toast.makeText(getApplicationContext(), "Please Check your Internet Connection.", Toast.LENGTH_LONG).show();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_switch, menu);
        // Get the action view used in your switch service item
        final MenuItem item_switch = menu.findItem(R.id.item_switch);

        if (item_switch != null) {
            final Switch actionView = (Switch) item_switch.getActionView();
            //Only zonal environments and users on malaria are not suppposed to see this
            if (prefs_environment.getString("EnvironmentTypeCode", "").equals("ZNLCR") && prefs_environment.getBoolean("IsZonalAndHasChildEnvironment", false))
                item_switch.setVisible(true); //Show it
            actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (getSupportActionBar() != null) {
                        //Check the sub title
                        if (isChecked)
                            getSupportActionBar().setSubtitle(GlobalVariables.getSelectedEnvironment() + " - Request from Woreda");
                        else
                            getSupportActionBar().setSubtitle(GlobalVariables.getSelectedEnvironment());
                    }
                    //then filter out the list
                    adapter.filter(isChecked);
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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
