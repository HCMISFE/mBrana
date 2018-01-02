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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.Issue.DraftOrderAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ApiOrderModel;
import com.jsi.mbrana.Models.InstitutionModel;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpdateIssueActivity extends AppCompatActivity implements IDataNotification {
    final ArrayList<InstitutionModel> institutions = new ArrayList<>();
    ArrayList<ItemModel> items = new ArrayList<>();
    ProgressDialog progress;
    OrderModel Header;
    RecyclerView itemsTable;
    Button cancelBtn;
    Button updateBtn;
    Button submitBtn;
    int actionType = -1;
    ImageButton editFacilityForIssue;
    TextView FacilityForIssue;
    View customToastLayout;
    TextView customToastext;
    CheckBox IsCampain;
    SharedPreferences prefs;
    private Tracker _mTracker;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Update Issue");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progress = new ProgressDialog(this);

        prefs = getSharedPreferences("userCredentials", 0);
        LayoutInflater inflater = getLayoutInflater();

        customToastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a message
        customToastext = (TextView) customToastLayout.findViewById(R.id.customToastText);

        // get selected Draft Order
        Header = (OrderModel) getIntent().getSerializableExtra("Order");
        FacilityForIssue = (TextView) findViewById(R.id.FacilityForIssue);

        // Is Campain checkbox
        IsCampain = (CheckBox) findViewById(R.id.isCampinCheckBoxUpdateIssue);
        IsCampain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Header.setCampaign(((CheckBox) v).isChecked());
            }
        });

        IsCampain.setChecked(Header.isCampaign());
        if (Header != null) {
            Log.d("header", Header.getFacility());
            String htmlString = "<u>" + Header.getFacility() + "</u>";
            FacilityForIssue.setText(Html.fromHtml(htmlString));
            FacilityForIssue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UpdateIssueActivity.this, FacilitySelectionUpdateActivity.class);
                    intent.putExtra("SelectedFacility", Header.getFacility());
                    intent.putExtra("Order", Header);
                    startActivity(intent);
                }
            });
        }

        // Load detail data
        new DataServiceTask(this, 1, false).execute("Order/GetOrderDetail?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode() + "&OrderID=" + Header.getID(), "");

        // Load lookups
        new DataServiceTask(this, 2, false).execute("LookUp/GetInstitution?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        // Initialize facilitiesListSpinner
        // FacilitiesListSpinner = (Spinner) findViewById(R.id.draftOrderFaclitiesSpinner);
        editFacilityForIssue = (ImageButton) findViewById(R.id.editFacilityForIssue);
        editFacilityForIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateIssueActivity.this, FacilitySelectionUpdateActivity.class);
                intent.putExtra("SelectedFacility", Header.getFacility());
                intent.putExtra("Order", Header);
                startActivity(intent);
            }
        });

        // Initialize item table
        itemsTable = (RecyclerView) findViewById(R.id.draftOrderItemsTable);
        itemsTable.addItemDecoration(new SimpleDividerItemDecoration(this));

        // Initialize and add action listener to Action Buttons
        updateBtn = (Button) findViewById(R.id.draftOrderbuttonSave);
        submitBtn = (Button) findViewById(R.id.draftOrderbuttonSubmit);
        cancelBtn = (Button) findViewById(R.id.draftOrderbuttonCancel);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Update")
                        .setLabel("Update Issue")
                        .build());
                ArrayList<ItemModel> itemsToBeSaved = items;
                getIntent().putExtra("DraftOrderItems", itemsToBeSaved);

                if (isProperData(itemsToBeSaved)) {
                    OrderModel header = new OrderModel();
                    header.setID(Header.getID());
                    header.setRequestedBy(Header.getRequestedBy());
                    header.setFacility(Header.getFacility());
                    header.setOrderStatusCode(Constants.OrderStatus.DRAFT.getOrderStatusCode());
                    header.setEnvironmentID(Header.getEnvironmentID());
                    header.setEnvironmentCode(Header.getEnvironmentCode());
                    header.setCampaign(IsCampain.isChecked());
                    header.setUserID(prefs.getInt("userid", 0));
                    header.setUserName(prefs.getString("username", ""));

                    ApiOrderModel orderData = new ApiOrderModel();
                    orderData.setOrder(header);
                    orderData.setOrderDetail(itemsToBeSaved);

                    Gson gson = new Gson();
                    final String itemstring = gson.toJson(orderData);

                    new AlertDialog.Builder(UpdateIssueActivity.this)
                            .setMessage(Html.fromHtml(getString(R.string.updateMessage)))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    handelDataSave(itemstring, "Order/UpDateOrder");
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

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> itemsToBeSaved = items;
                getIntent().putExtra("DraftOrderItems", itemsToBeSaved);

                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User Action")
                        .setAction("Submit")
                        .setLabel("Submit Issue")
                        .build());
                if (isProperData(itemsToBeSaved)) {
                    OrderModel header = new OrderModel();
                    header.setID(Header.getID());
                    header.setRequestedBy(Header.getRequestedBy());
                    header.setFacility(Header.getFacility());
                    header.setOrderStatusCode(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode());
                    header.setEnvironmentID(Header.getEnvironmentID());
                    header.setEnvironmentCode(Header.getEnvironmentCode());
                    header.setCampaign(IsCampain.isChecked());
                    header.setUserID(prefs.getInt("userid", 0));
                    header.setUserName(prefs.getString("username", ""));
                    ApiOrderModel orderData = new ApiOrderModel();
                    orderData.setOrder(header);
                    orderData.setOrderDetail(itemsToBeSaved);

                    Gson gson = new Gson();
                    final String itemstring = gson.toJson(orderData);

                    new AlertDialog.Builder(UpdateIssueActivity.this)
                            .setMessage(Html.fromHtml(getString(R.string.submitMessage)))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    handelDataSave(itemstring, "Order/UpDateOrder");
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


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> itemsToBeSaved = items;
                getIntent().putExtra("DraftOrderItems", itemsToBeSaved);

                if (isProperData(itemsToBeSaved)) {
                    OrderModel header = new OrderModel();
                    header.setID(Header.getID());
                    header.setRequestedBy(Header.getRequestedBy());
                    header.setFacility(Header.getFacility());
                    header.setOrderStatusCode(Constants.OrderStatus.CANCELED.getOrderStatusCode());
                    header.setEnvironmentID(Header.getEnvironmentID());
                    header.setEnvironmentCode(Header.getEnvironmentCode());
                    header.setCampaign(IsCampain.isChecked());
                    header.setUserID(prefs.getInt("userid", 0));
                    header.setUserName(prefs.getString("username", ""));

                    ApiOrderModel orderData = new ApiOrderModel();
                    orderData.setOrder(header);
                    orderData.setOrderDetail(itemsToBeSaved);

                    Gson gson = new Gson();
                    final String itemstring = gson.toJson(orderData);

                    new AlertDialog.Builder(UpdateIssueActivity.this)
                            .setMessage(Html.fromHtml(getString(R.string.canncelMessage)))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    handelDataSave(itemstring, "Order/UpDateOrder");
                                    actionType = 2;
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Issues: Edit Issue");
        _mTracker.setScreenName("Issues: Edit Issue");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void dataErrorMessage() {
        new AlertDialog.Builder(UpdateIssueActivity.this)
                .setMessage(Html.fromHtml("Invalid Data! Please check the entries"))
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

    public void handelDataSave(String objString, String api) {
        new DataServiceTaskForObjects(this, objString).execute(api, "");
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(UpdateIssueActivity.this, MainNavigationActivity.class);
            startActivity(i);
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(UpdateIssueActivity.this, IssueMenuActivity.class);
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
        switch (requestCode) {
            case 1: {
                //populate detail data
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
                        item.setcStockOnHand(jo.getInt("cStockOnHand"));
                        item.setActivityCode(jo.getString("ActivityCode"));
                        items.add(item);
                    }
                    getIntent().putExtra("DraftOrderItems", items);
                    //set Adapter to itemsListTable
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    DraftOrderAdapter adapter = new DraftOrderAdapter(this, items, itemsTable);
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

    public void showCustomToast() {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customToastLayout);
        toast.show();
    }

    @Override
    public void handelNotification(String message) {
        //this has to be modified
        if (message.equalsIgnoreCase("Change is successful")) {
            Intent intent = null;
            if (actionType == 0) {
                intent = new Intent(UpdateIssueActivity.this, IssueMenuActivity.class);
                intent.putExtra("Order", Header);
                message = "Issue Updated";
            } else if (actionType == 1) {
                intent = new Intent(UpdateIssueActivity.this, ApproveIssueActivity.class);
                intent.putExtra("Order", Header);
                message = "Issue Sent to Approval";
                startActivity(intent);
            } else if (actionType == 2) {
                intent = new Intent(UpdateIssueActivity.this, IssueMenuActivity.class);
                intent.putExtra("Order", Header);
                message = "Issue Cancelled";
                startActivity(intent);
            }

            //change Toast to snack bar
            customToastext.setText(Html.fromHtml(message));
            customToastLayout.setBackgroundColor(getResources().getColor(R.color.successMessage));
            // Toast...
            showCustomToast();
        } else {
            Log.d("mytag", "handelNotification: Change Failed");
            customToastext.setText(Html.fromHtml("Change Failed"));
            customToastLayout.setBackgroundColor(getResources().getColor(R.color.errorMessage));
            showCustomToast();
        }
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
