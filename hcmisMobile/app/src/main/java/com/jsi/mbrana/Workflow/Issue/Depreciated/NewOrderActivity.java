package com.jsi.mbrana.Workflow.Issue.Depreciated;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.Lookup.InstitutionCustomAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ApiOrderModel;
import com.jsi.mbrana.Models.InstitutionModel;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Issue.ApproveIssueActivity;
import com.jsi.mbrana.Workflow.Issue.IssueMenuActivity;
import com.jsi.mbrana.Workflow.Issue.UpdateIssueActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewOrderActivity extends AppCompatActivity implements IDataNotification {
    final ArrayList<ItemModel> items = new ArrayList<ItemModel>();
    final ArrayList<InstitutionModel> institutions = new ArrayList<InstitutionModel>();
    ProgressDialog progress;
    int actionType = -1;
    Date selectedDate;
    Spinner facilitiesListSpinner;
    ListView itemsTable;
    Button btnSave;
    Button btnSubmit;

    private Tracker _mTracker;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen",this.getLocalClassName());
        _mTracker.setScreenName("WorkFlow-Issue: Create Issue");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Create Issue");
        getSupportActionBar().setSubtitle(getIntent().getStringExtra("InstitutionName"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Assign values to date variable
        Date date = new Date();
        selectedDate = date;

        // Load item unit list
        new DataServiceTask(this, 1, false, true).execute("LookUp/GetItemUnitList?InstitutionID=" +
                getIntent().getIntExtra("InstitutionID", 0)
                + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        // Initialize item table
        itemsTable = (ListView) findViewById(R.id.newOrder_ItemsTable);

        // Initialize  add action listener to Action Buttons
        btnSave = (Button) findViewById(R.id.neworder_buttonSave);
        btnSubmit = (Button) findViewById(R.id.neworder_buttonSubmit);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> itemsToBeSaved = (ArrayList<ItemModel>) getIntent().getSerializableExtra("DraftOrderItems");
                if (isProperData(itemsToBeSaved)) {
                    OrderModel header = new OrderModel();
                    header.setRequestedBy(getIntent().getIntExtra("InstitutionID", 0));
                    header.setFacility(getIntent().getStringExtra("InstitutionName"));
                    header.setOrderStatusCode(Constants.OrderStatus.DRAFT.getOrderStatusCode());
                    header.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                    header.setEnvironmentID(GlobalVariables.getSelectedEnvironmentID());
                    header.setDate(selectedDate);

                    ApiOrderModel orderData = new ApiOrderModel();
                    orderData.setOrder(header);
                    orderData.setOrderDetail(itemsToBeSaved);

                    Gson gson = new Gson();
                    final String itemstring = gson.toJson(orderData);

                    new AlertDialog.Builder(NewOrderActivity.this)
                            .setMessage(Html.fromHtml(getString(R.string.draftSaveMessage)))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Save the data
                                    handelDataSave(itemstring, 3);
                                    actionType = 0;
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else {
                    dataErrorMessage();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> itemsToBeSaved = (ArrayList<ItemModel>) getIntent().getSerializableExtra("DraftOrderItems");

                if (isProperData(itemsToBeSaved)) {
                    OrderModel header = new OrderModel();
                    header.setRequestedBy(getIntent().getIntExtra("InstitutionID", 0));
                    header.setFacility(getIntent().getStringExtra("InstitutionName"));
                    header.setOrderStatusCode(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode());
                    header.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                    header.setEnvironmentID(GlobalVariables.getSelectedEnvironmentID());
                    header.setDate(selectedDate);

                    ApiOrderModel orderData = new ApiOrderModel();
                    orderData.setOrder(header);
                    orderData.setOrderDetail(itemsToBeSaved);
                    Gson gson = new Gson();
                    final String itemstring = gson.toJson(orderData);

                    new AlertDialog.Builder(NewOrderActivity.this)
                            .setMessage(Html.fromHtml(getString(R.string.submitMessage)))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Submit data
                                    handelDataSave(itemstring, 4);
                                    actionType = 1;
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else {
                    dataErrorMessage();
                }
            }
        });
    }

    public void dataErrorMessage() {
        new AlertDialog.Builder(NewOrderActivity.this)
                .setMessage(Html.fromHtml(getString(R.string.invalidDataMessage)))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public boolean isProperData(ArrayList<ItemModel> itemsToBeSaved) {
        int count = 0;
        for (ItemModel x : itemsToBeSaved) {
            if (x.getQuantity() == 0) {
                count++;
            }
        }

        return !(count == itemsToBeSaved.size());
    }

    public void handelDataSave(String objString, int requestCode) {
        new DataServiceTaskForObjects(this, objString, requestCode).execute("Order/InsertOrder", "");
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        switch (requestCode) {
            case 1: {
                try {
                    if (items.size() != 0) items.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        ItemModel item = new ItemModel();
                        item.setItemName(jo.getString("FullItemName"));
                        item.setItemCode(jo.getString("ProductCN"));
                        item.setItemID(jo.getInt("ItemID"));
                        item.setUnit(jo.getString("Unit"));
                        item.setUnitID(jo.getInt("UnitID"));
                        item.setQuantity(jo.getInt("Quantity"));
                        items.add(item);
                    }
                    // Set initial new order values to global variable, intent
                    getIntent().putExtra("DraftOrderItems", items);
                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
                break;
            }
            case 2: {
                try {
                    ArrayList<String> institutionNames = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        InstitutionModel institutionModel = new InstitutionModel();
                        institutionModel.setInstitutionName(jo.getString("Institution"));
                        institutionModel.setInstitutionID(jo.getInt("InstitutionID"));
                        institutionModel.setIssueDate(jo.getString("IssueDate"));

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                        Date issuedate = null;

                        try {
                            issuedate = simpleDateFormat.parse(institutionModel.getIssueDate().replace("T", " "));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            _mTracker.send(new HitBuilders.ExceptionBuilder()
                                    .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                            .getDescription(Thread.currentThread().getName(), e))
                                    .setFatal(false)
                                    .build());
                        }

                        if (institutionModel.getIssueDate().equalsIgnoreCase("0001-01-01T00:00:00"))
                            institutionNames.add(institutionModel.getInstitutionName() + " - NA");
                        else
                            institutionNames.add(institutionModel.getInstitutionName() + " - " + Helper.getMomentFromNow(issuedate));

                        institutions.add(institutionModel);
                    }

                    InstitutionCustomAdapter customAdapter = new InstitutionCustomAdapter(this, R.layout.layout_spinneritem, institutions);
                    customAdapter.setDropDownViewResource(R.layout.layout_spinneritem);
                    facilitiesListSpinner.setAdapter(customAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
                break;
            }
            case 3: {
                try {
                    if (items.size() != 0) items.clear();
                    OrderModel Header = new OrderModel();
                    if (jsonArray.length() > 0) {
                        Header.setID(jsonArray.getJSONObject(0).getInt("ID"));
                        Header.setRefNo(jsonArray.getJSONObject(0).getString("RefNo"));
                        Header.setOrderStatus(jsonArray.getJSONObject(0).getString("OrderStatus"));
                        Header.setOrderStatusCode(jsonArray.getJSONObject(0).getString("OrderStatusCode"));
                        Header.setFacility(jsonArray.getJSONObject(0).getString("Facility"));
                        Header.setContactPerson(jsonArray.getJSONObject(0).getString("ContactPerson"));
                        Header.setContactPersonMobileNumber(jsonArray.getJSONObject(0).getString("ContactPersonMobileNumber"));
                        Header.setRequestedNoOfItems(jsonArray.getJSONObject(0).getInt("RequestedNoOfItems"));
                        Header.setProcessedNoOfItems(jsonArray.getJSONObject(0).getInt("ProcessedNoOfItems"));
                        Header.setRequestedBy(jsonArray.getJSONObject(0).getInt("RequestedBy"));
                        Header.setDateCreated(jsonArray.getJSONObject(0).getString("Date"));
                        Header.setModifiedDate(jsonArray.getJSONObject(0).getString("ModifiedDate"));
                        Header.setEnvironmentCode(jsonArray.getJSONObject(0).getString("EnvironmentCode"));
                        Header.setEnvironmentID(jsonArray.getJSONObject(0).getInt("EnvironmentID"));

                        Intent intent = new Intent(this, UpdateIssueActivity.class);
                        intent.putExtra("Order", Header);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
                break;
            }
            case 4: {
                try {
                    if (items.size() != 0) items.clear();
                    OrderModel Header = new OrderModel();
                    if (jsonArray.length() > 0) {
                        Header.setID(jsonArray.getJSONObject(0).getInt("ID"));
                        Header.setRefNo(jsonArray.getJSONObject(0).getString("RefNo"));
                        Header.setOrderStatus(jsonArray.getJSONObject(0).getString("OrderStatus"));
                        Header.setOrderStatusCode(jsonArray.getJSONObject(0).getString("OrderStatusCode"));
                        Header.setFacility(jsonArray.getJSONObject(0).getString("Facility"));
                        Header.setContactPerson(jsonArray.getJSONObject(0).getString("ContactPerson"));
                        Header.setContactPersonMobileNumber(jsonArray.getJSONObject(0).getString("ContactPersonMobileNumber"));
                        Header.setRequestedNoOfItems(jsonArray.getJSONObject(0).getInt("RequestedNoOfItems"));
                        Header.setProcessedNoOfItems(jsonArray.getJSONObject(0).getInt("ProcessedNoOfItems"));
                        Header.setRequestedBy(jsonArray.getJSONObject(0).getInt("RequestedBy"));
                        Header.setDateCreated(jsonArray.getJSONObject(0).getString("Date"));
                        Header.setModifiedDate(jsonArray.getJSONObject(0).getString("ModifiedDate"));
                        Header.setEnvironmentCode(jsonArray.getJSONObject(0).getString("EnvironmentCode"));
                        Header.setEnvironmentID(jsonArray.getJSONObject(0).getInt("EnvironmentID"));

                        Intent intent = new Intent(this, ApproveIssueActivity.class);
                        intent.putExtra("Order", Header);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                            startActivity(intent, options.toBundle());
                        } else
                            startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
                break;
            }
        }
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
            Intent intent = new Intent(NewOrderActivity.this, MainNavigationActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NewOrderActivity.this);
                startActivity(intent, options.toBundle());
            } else startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(NewOrderActivity.this, IssueMenuActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NewOrderActivity.this);
                startActivity(intent, options.toBundle());
            } else startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handelNotification(String message) {
        if (message.equalsIgnoreCase("Change is successful")) {
            if (actionType == 0) {
                // save button
                message = "Draft Issue Saved.";
                Intent intent = new Intent(NewOrderActivity.this, IssueMenuActivity.class);
                intent.putExtra("Type", "Draft");
                // startActivity(intent);
            } else if (actionType == 1) {
                // submit button
                message = "Issue Sent to Approval.";
                Intent intent = new Intent(NewOrderActivity.this, IssueMenuActivity.class);
                intent.putExtra("Type", "Approval");
                // startActivity(intent);
            } else {
                Intent intent = new Intent(NewOrderActivity.this, IssueMenuActivity.class);
                startActivity(intent);
            }

        }

        //change Toast to snack bar
        Snackbar snackbar1 = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(message), Snackbar.LENGTH_LONG);
        snackbar1.setActionTextColor(getResources().getColor(R.color.white));
        View view = snackbar1.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.excess));
        snackbar1.show();
    }

    @Override
    public void handelProgressDialog(Boolean showprogress, String Message) {
        if (showprogress) {
            progress = new ProgressDialog(this);
            if (Message.length() > 0) progress.setMessage(Message);
            else progress.setMessage("Loading");
            progress.show();
        } else progress.dismiss();
    }

    @Override
    public void readFromDatabase(int requestCode) {

    }
}
