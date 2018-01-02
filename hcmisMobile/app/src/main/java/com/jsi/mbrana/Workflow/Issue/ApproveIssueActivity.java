package com.jsi.mbrana.Workflow.Issue;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jsi.mbrana.Workflow.Adapter.Issue.ApprovalIssueOrderAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Helpers.SnackbarNotifications;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ApiOrderModel;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApproveIssueActivity extends AppCompatActivity implements IDataNotification, SnackbarNotifications {
    ArrayList<ItemModel> items = new ArrayList<>();
    ProgressDialog progress;
    OrderModel Header;
    RecyclerView itemsTable;
    Button cancelBtn;
    Button approveBtn;
    Button returnBtn;
    int actionType = -1;
    TextView IsCampain;
    View customToastLayout;
    TextView customToastext;
    String itemstring;
    SharedPreferences prefs, prefs_environment;
    private Tracker _mTracker;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_issue_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Approve Quantity");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);

        IsCampain = (TextView) findViewById(R.id.IsCampaignText);
        itemsTable = (RecyclerView) findViewById(R.id.approveIssueOrder_ItemTable);
        itemsTable.addItemDecoration(new SimpleDividerItemDecoration(this));

        // Custom toast definition
        LayoutInflater inflater = getLayoutInflater();

        prefs = getSharedPreferences("userCredentials", 0);
        customToastLayout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // Set a message
        customToastext = (TextView) customToastLayout.findViewById(R.id.customToastText);

        // Get selected Issue
        Header = (OrderModel) getIntent().getSerializableExtra("Order");
        if (Header != null) {
            if (Html.fromHtml(Header.getFacility().toString()).length() > 35)
                getSupportActionBar().setSubtitle(Html.fromHtml(Header.getFacility().toString().substring(0, 35)));
            else
                getSupportActionBar().setSubtitle(Html.fromHtml(Header.getFacility().toString()));
        }

        // Load detail data
        new DataServiceTask(this, 1, false).execute("Order/GetOrderDetail?OrderID=" + Header.getID() + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");
        new DataServiceTask(this, 2, false).execute("Order/GetOrder?OrderID=" + Header.getID() + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        // Set Action Listener for buttons
        cancelBtn = (Button) findViewById(R.id.approveIssueOrder_CancelBtn);
        approveBtn = (Button) findViewById(R.id.approveIssueOrder_ApproveBtn);
        returnBtn = (Button) findViewById(R.id.approveIssueOrder_ReturnBtn);

        //Parse out the flag
        prefs_environment = getSharedPreferences("Environment", 0);

        //Handling button for those issues that are synced from zone to woreda
        if (prefs_environment.getString("EnvironmentTypeCode", "ZNLCR").equals("ZNLCR") || !prefs_environment.getBoolean("IsZoneSupplied", false)) {
            cancelBtn.setVisibility(View.GONE);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User Action")
                        .setAction("Cancel")
                        .setLabel("Delete Issue")
                        .build());

                ArrayList<ItemModel> approvedValues = (ArrayList<ItemModel>) getIntent().getSerializableExtra("OrdersToBeApporved");
                for (int i = 0; i < itemsTable.getChildCount(); i++) {
                    approvedValues.get(i).setActivityCode(getIntent().getStringExtra("ActivityCode"));
                }

                OrderModel header = new OrderModel();
                header.setID(Header.getID());
                header.setRequestedBy(Header.getRequestedBy());
                header.setFacility(Header.getFacility());
                header.setOrderStatusCode(Constants.OrderStatus.CANCELED.getOrderStatusCode());
                header.setEnvironmentID(Header.getEnvironmentID());
                header.setEnvironmentCode(Header.getEnvironmentCode());
                header.setCampaign(Header.isCampaign());
                header.setUserID(Header.getUserID());
                header.setUserName(Header.getUserName());

                ApiOrderModel orderData = new ApiOrderModel();
                orderData.setOrder(header);
                orderData.setOrderDetail(approvedValues);

                Gson gson = new Gson();
                final String itemstring = gson.toJson(orderData);

                new AlertDialog.Builder(ApproveIssueActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.canncelMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(itemstring, "Order/UpDateOrder", 7);
                                actionType = 0;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User Action")
                        .setAction("Return")
                        .setLabel("Return Issue to Draft")
                        .build());

                ArrayList<ItemModel> approvedValues = (ArrayList<ItemModel>) getIntent().getSerializableExtra("OrdersToBeApporved");
                for (int i = 0; i < itemsTable.getChildCount(); i++) {
                    approvedValues.get(i).setActivityCode(getIntent().getStringExtra("ActivityCode"));
                }

                OrderModel header = new OrderModel();
                header.setID(Header.getID());
                header.setRequestedBy(Header.getRequestedBy());
                header.setFacility(Header.getFacility());
                header.setOrderStatusCode(Constants.OrderStatus.DRAFT.getOrderStatusCode());
                header.setEnvironmentID(Header.getEnvironmentID());
                header.setEnvironmentCode(Header.getEnvironmentCode());
                header.setCampaign(Header.isCampaign());
                header.setUserID(Header.getUserID());
                header.setUserName(Header.getUserName());

                ApiOrderModel orderData = new ApiOrderModel();
                orderData.setOrder(header);
                orderData.setOrderDetail(approvedValues);

                Gson gson = new Gson();
                final String itemstring = gson.toJson(orderData);

                new AlertDialog.Builder(ApproveIssueActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.returnMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(itemstring, "Order/UpDateOrder", 8);
                                actionType = 1;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

            }
        });

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Approve")
                        .setLabel("Approve Issue")
                        .build());

                ArrayList<ItemModel> approvedValues = (ArrayList<ItemModel>) getIntent().getSerializableExtra("OrdersToBeApporved");
                for (int i = 0; i < itemsTable.getChildCount(); i++) {
                    View view = itemsTable.getChildAt(i);
                    EditText editText = (EditText) view.findViewById(R.id.approveIssueOrder_approvedQtyColumn_enabled);
                    approvedValues.get(i).setActivityCode(getIntent().getStringExtra("ActivityCode"));
                    if (editText.getText().toString().isEmpty()) {
                        approvedValues.get(i).setApprovedQuantity(0);
                    } else {
                        approvedValues.get(i).setApprovedQuantity(Integer.parseInt(editText.getText().toString()));
                    }
                }

                if (isProperData(approvedValues)) {
                    OrderModel header = new OrderModel();
                    header.setID(Header.getID());
                    header.setRequestedBy(Header.getRequestedBy());
                    header.setFacility(Header.getFacility());
                    header.setOrderStatusCode(Constants.OrderStatus.APPROVED.getOrderStatusCode());
                    header.setEnvironmentID(Header.getEnvironmentID());
                    header.setEnvironmentCode(Header.getEnvironmentCode());
                    header.setCampaign(Header.isCampaign());
                    header.setUserID(Header.getUserID());
                    header.setUserName(Header.getUserName());

                    ApiOrderModel orderData = new ApiOrderModel();
                    orderData.setOrder(header);
                    orderData.setOrderDetail(approvedValues);

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    itemstring = gson.toJson(orderData);

                    new AlertDialog.Builder(ApproveIssueActivity.this)
                            .setMessage(Html.fromHtml(getString(R.string.approveMessage)))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    handelDataSave(itemstring, "PickList/GeneratePickList", 6);
                                    actionType = 2;
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else {
                    String msg = "";
                    if (isStockAvailable(approvedValues))
                        msg = getString(R.string.stockIsNotAvailableForApprovalMessaage);
                    else
                        msg = getString(R.string.invalidDataMessage);
                    dataErrorMessage(msg);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Issues: Approve Quantity");
        _mTracker.setScreenName("Issues: Approve Quantity");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void dataErrorMessage(String msg) {
        new AlertDialog.Builder(ApproveIssueActivity.this)
                .setMessage(Html.fromHtml(msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public boolean isProperData(ArrayList<ItemModel> itemsToBeSaved) {
        int count = 0;
        if (itemsToBeSaved != null) {
            for (ItemModel x : itemsToBeSaved) {
                if (x.getApprovedQuantity() == 0) {
                    count++;
                }
            }
            return !(count == itemsToBeSaved.size());
        }
        return false;
    }

    public boolean isStockAvailable(ArrayList<ItemModel> itemsToBeSaved) {

        int count = 0;
        for (ItemModel x : itemsToBeSaved) {
            if (x.getSOH() == 0) {
                count++;
            }
        }

        return (count == itemsToBeSaved.size());
    }

    public void handelDataSave(String objString, String api, int requestcode) {
        new DataServiceTaskForObjects(this, objString, requestcode).execute(api, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(ApproveIssueActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        if (requestCode == 1) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel item = new ItemModel();
                    item.setItemID(jo.getInt("ItemID"));
                    item.setID(jo.getInt("ID"));
                    item.setItemName(jo.getString("fullitemname"));
                    item.setItemCode(jo.getString("ProductCN"));
                    item.setUnit(jo.getString("Unit"));
                    item.setBulkUnits(jo.getString("BulkUnit"));
                    item.setUnitID(jo.getInt("UnitID"));
                    item.setQuantity(jo.getInt("Quantity"));
                    item.setSOH(jo.getInt("SOH"));
                    item.setMOS(jo.getInt("MOS"));
                    item.setAMC(jo.getInt("AMC"));
                    item.setApprovedQuantity(jo.getInt("ApprovedQuantity"));
                    if (item.getQuantity() > 0)
                        items.add(item);
                }
                getIntent().putExtra("OrdersToBeApporved", items);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                ApprovalIssueOrderAdapter adapter = new ApprovalIssueOrderAdapter(this, items, itemsTable);
                itemsTable.setLayoutManager(mLayoutManager);
                itemsTable.setItemAnimator(new DefaultItemAnimator());
                itemsTable.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
        } else if (requestCode == 2) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                if (jo.getBoolean("IsCampaign"))
                    IsCampain.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
        } else if (requestCode == 6) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                }
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
        if (message.equalsIgnoreCase("Change is successful")) {
            Intent intent = null;
            if (actionType == 0) {
                message = "Issue Cancelled";
                ShowSnackNotification(message, R.color.successMessage);
                intent = new Intent(ApproveIssueActivity.this, IssueMenuActivity.class);
                intent.putExtra("Order", Header);
            } else if (actionType == 1) {
                message = "Issue Sent to Draft";
                ShowSnackNotification(message, R.color.successMessage);
                intent = new Intent(ApproveIssueActivity.this, UpdateIssueActivity.class);
                intent.putExtra("Order", Header);
                intent.putExtra(getString(R.string.transitionDirection), getString(R.string.leftTranstion));
            } else if (actionType == 2) {
                message = "Issue Sent to Picklist";
                ShowSnackNotification(message, R.color.successMessage);
                intent = new Intent(ApproveIssueActivity.this, ConfirmPicklistActivity.class);
                intent.putExtra("Order", Header);
            }

            if (intent != null) {
                startActivity(intent);
            }

        } else {
            ShowSnackNotification(message, R.color.errorMessage);
        }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, IssueMenuActivity.class);
        startActivity(intent);
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
    public void showMessage(String message) {

    }
}
