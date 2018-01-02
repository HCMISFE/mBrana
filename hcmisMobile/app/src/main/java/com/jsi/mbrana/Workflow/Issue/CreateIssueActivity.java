package com.jsi.mbrana.Workflow.Issue;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.Issue.OrderAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Helpers.UserActivityCodeHelper;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ApiOrderModel;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateIssueActivity extends AppCompatActivity implements IDataNotification {
    RecyclerView new_issue_recycler;
    ArrayList<ItemModel> ItemModelMaster = new ArrayList<>();
    ArrayList<Integer> QuantityList = new ArrayList<>();
    OrderModel Header;
    Button btnDraftSave;
    Button btnSubmitIssue;
    SharedPreferences prefs;
    CheckBox IsCampain;
    int actionType;
    TextView customToastext;
    View customToastLayout;
    ProgressDialog progress;
    ImageButton resetIssueQuantity;
    ImageButton historyIssueQuantity;
    InputMethodManager imm;
    private Tracker _mTracker;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_issue_form);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle(getIntent().getStringExtra("InstitutionName"));

        progress = new ProgressDialog(this);

        prefs = getSharedPreferences("userCredentials", 0);
        IsCampain = (CheckBox) findViewById(R.id.isCampinCheckBoxNewIssue);
        if (UserActivityCodeHelper.getAssignedTo().equals("Malaria"))
            IsCampain.setVisibility(View.GONE);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        new_issue_recycler = (RecyclerView) findViewById(R.id.newIssueOrder_Recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        new_issue_recycler.setLayoutManager(mLayoutManager);
        new_issue_recycler.setItemAnimator(new DefaultItemAnimator());
        new_issue_recycler.addItemDecoration(new SimpleDividerItemDecoration(this));

        new DataServiceTask(this, 1, false).execute("LookUp/GetItemUnitList?InstitutionID=" +
                getIntent().getIntExtra("InstitutionID", 0)
                + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode()
                + "&ActivityCode=" + UserActivityCodeHelper.getDefaultActivityCode(), "");
        LayoutInflater inflater = getLayoutInflater();

        customToastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        customToastext = (TextView) customToastLayout.findViewById(R.id.customToastText);

        historyIssueQuantity = (ImageButton) findViewById(R.id.historyIssueQuantity);
        historyIssueQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHistory();
            }
        });

        resetIssueQuantity = (ImageButton) findViewById(R.id.resetIssueQuantity);
        resetIssueQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetHistory();
            }
        });

        btnDraftSave = (Button) findViewById(R.id.neworder_draftSave);
        btnDraftSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Save Draft")
                        .setLabel("Save Issue as draft")
                        .build());
                if (imm.isAcceptingText()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // hide keyboard first
                    draftIssueSave();
                } else {
                    draftIssueSave();
                }
            }
        });

        btnSubmitIssue = (Button) findViewById(R.id.SubmitNewIssueOrder);
        btnSubmitIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User Action")
                        .setAction("Submit")
                        .setLabel("Submit Issue")
                        .build());
                if (imm.isAcceptingText()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // hide keyboard first
                    submitIssue();
                } else {
                    submitIssue();
                }
            }
        });
    }

    public void ShowHistory() {
        //Resetting the history back
        for (int x = 0; x < ItemModelMaster.size(); x++) {
            ItemModelMaster.get(x).setQuantity(QuantityList.get(x));
        }
        // Then pass the reset model to the recycler view
        OrderAdapter adapter = new OrderAdapter(this, ItemModelMaster, new_issue_recycler);
        new_issue_recycler.setAdapter(adapter);
        historyIssueQuantity.setVisibility(View.GONE);
        resetIssueQuantity.setVisibility(View.VISIBLE);
    }

    public void ResetHistory() {
        // Resetting quantity to zero
        for (int x = 0; x < ItemModelMaster.size(); x++) {
            ItemModelMaster.get(x).setQuantity(0);
        }
        // Then pass the reset model to the recycler view
        OrderAdapter adapter = new OrderAdapter(this, ItemModelMaster, new_issue_recycler);
        new_issue_recycler.setAdapter(adapter);
        historyIssueQuantity.setVisibility(View.VISIBLE);
        resetIssueQuantity.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Issues: Create Issue");
        _mTracker.setScreenName("Issues: Create Issue");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void draftIssueSave() {
        Header = new OrderModel();
        Header.setUserID(prefs.getInt("userid", 0));
        Header.setUserName(prefs.getString("username", ""));
        Header.setCampaign(IsCampain.isChecked());
        Header.setFacility(getIntent().getStringExtra("InstitutionName"));
        Header.setRequestedBy(getIntent().getIntExtra("InstitutionID", 0));
        Header.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
        Header.setEnvironmentID(GlobalVariables.getSelectedEnvironmentID());
        Header.setOrderStatusCode(Constants.OrderStatus.DRAFT.getOrderStatusCode());
        ApiOrderModel orderData = new ApiOrderModel();
        orderData.setOrder(Header);
        ArrayList<ItemModel> headerDetail = (ArrayList<ItemModel>) getIntent().getSerializableExtra("NewIssueDataSet");

        for (int i = 0; i < new_issue_recycler.getChildCount(); i++) {
            headerDetail.get(i).setAMC(0);
            headerDetail.get(i).setApprovedQuantity(0);
            headerDetail.get(i).setBalance(0);
            headerDetail.get(i).setID(0);
            headerDetail.get(i).setMOS(0);
            headerDetail.get(i).setPOQuantity(0);
            headerDetail.get(i).setPurchaseOrderDetailID(0);
            headerDetail.get(i).setReceivedQuantity(0);
            headerDetail.get(i).setSOH(0);
            headerDetail.get(i).setUnitOfIssueID(0);
            headerDetail.get(i).setActivityCode(getIntent().getStringExtra("ActivityCode"));
            headerDetail.get(i).setIssuedQuantity(0);
            headerDetail.get(i).setItemSN(0);
        }

        orderData.setOrderDetail(headerDetail);
        Gson gson = new Gson();
        final String itemstring = gson.toJson(orderData);

        if (isProperData(headerDetail)) {
            handelDataSave(itemstring, 3);
            actionType = 0;
        } else {
            String msg = "";
            msg = getString(R.string.invalidDataMessage);
            ShowSnackNotification(msg, R.color.errorMessage);
        }
    }

    public void submitIssue() {
        new AlertDialog.Builder(CreateIssueActivity.this)
                .setMessage(Html.fromHtml(getString(R.string.submitMessage)))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Header = new OrderModel();
                        Header.setActivityCode(getIntent().getStringExtra("ActivityCode"));
                        Header.setUserID(prefs.getInt("userid", 0));
                        Header.setUserName(prefs.getString("username", ""));
                        Header.setCampaign(IsCampain.isChecked());
                        Header.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                        Header.setEnvironmentID(GlobalVariables.getSelectedEnvironmentID());
                        Header.setOrderStatusCode(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode());
                        Header.setFacility(getIntent().getStringExtra("InstitutionName"));
                        Header.setRequestedBy(getIntent().getIntExtra("InstitutionID", 0));
                        ApiOrderModel orderData = new ApiOrderModel();
                        orderData.setOrder(Header);
                        ArrayList<ItemModel> headerDetail = (ArrayList<ItemModel>) getIntent().getSerializableExtra("NewIssueDataSet");

                        for (int i = 0; i < new_issue_recycler.getChildCount(); i++) {
                            headerDetail.get(i).setAMC(0);
                            headerDetail.get(i).setApprovedQuantity(0);
                            headerDetail.get(i).setBalance(0);
                            headerDetail.get(i).setID(0);
                            headerDetail.get(i).setMOS(0);
                            headerDetail.get(i).setPOQuantity(0);
                            headerDetail.get(i).setPurchaseOrderDetailID(0);
                            headerDetail.get(i).setReceivedQuantity(0);
                            headerDetail.get(i).setSOH(0);
                            headerDetail.get(i).setUnitOfIssueID(0);
                            headerDetail.get(i).setIssuedQuantity(0);
                            headerDetail.get(i).setItemSN(0);
                            headerDetail.get(i).setActivityCode(getIntent().getStringExtra("ActivityCode"));
                        }

                        orderData.setOrderDetail(headerDetail);
                        Gson gson = new Gson();
                        final String itemstring = gson.toJson(orderData);

                        if (isProperData(headerDetail)) {
                            handelDataSave(itemstring, 4);
                            actionType = 1;
                        } else {
                            String msg = "";
                            msg = getString(R.string.invalidDataMessage);
                            ShowSnackNotification(msg, R.color.errorMessage);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public void handelDataSave(String objString, int requestCode) {
        new DataServiceTaskForObjects(this, objString, requestCode).execute("Order/InsertOrder", "");
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    public boolean isProperData(ArrayList<ItemModel> itemsToBeSaved) {
        int count = 0;
        if (itemsToBeSaved != null) {
            for (ItemModel x : itemsToBeSaved) {
                if (x.getQuantity() == 0) {
                    count++;
                }
            }
            return !(count == itemsToBeSaved.size());
        }
        return false;
    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        if (requestCode == 1) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel itemHeader = new ItemModel();
                    itemHeader.setItemName(jo.getString("FullItemName"));
                    itemHeader.setItemCode(jo.getString("ProductCN"));
                    itemHeader.setItemID(jo.getInt("ItemID"));
                    itemHeader.setUnit(jo.getString("Unit").trim());
                    itemHeader.setUnitID(jo.getInt("UnitID"));
                    itemHeader.setQuantity(jo.getInt("Quantity"));
                    QuantityList.add(jo.getInt("Quantity"));
                    ItemModelMaster.add(itemHeader);
                }
                getIntent().putExtra("NewIssueDataSet", ItemModelMaster);
                ResetHistory();
            } catch (Exception e) {
                this.handelNotification("Error found");
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }

        } else if (requestCode == 4) {
            try {
                Header.setID(jsonArray.getJSONObject(0).getInt("ID"));
                Log.d("GA ID", String.valueOf(jsonArray.getJSONObject(0).getInt("ID")));
            } catch (JSONException e) {
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
        Log.d("message", message);
        if (message.equalsIgnoreCase("Change is successful")) {
            if (actionType == 0) {
                ShowSnackNotification("Draft issue saved successfully!", R.color.successMessage);
                Intent intent = new Intent(this, IssueMenuActivity.class);
                startActivity(intent);
            } else if (actionType == 1) {
                // Submit button
                ShowSnackNotification("Issue sent to approval successfully!", R.color.successMessage);
                Intent intent = new Intent(CreateIssueActivity.this, ApproveIssueActivity.class);
                intent.putExtra("ActivityCode", prefs.getString("ActivityCode", ""));
                intent.putExtra("Order", Header);
                intent.putExtra("Type", "Approval");
                startActivity(intent);
            } else {
                Intent intent = new Intent(CreateIssueActivity.this, IssueMenuActivity.class);
                intent.putExtra("ActivityCode", prefs.getString("ActivityCode", ""));
                startActivity(intent);
            }
        } else {
            if (actionType == 0) {
                ShowSnackNotification("Error saving draft issue!", R.color.errorMessage);
            } else if (actionType == 1) {
                ShowSnackNotification("Error submitting issue for approval!", R.color.errorMessage);
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
    public boolean onOptionsItemSelected(MenuItem items) {
        switch (items.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                GlobalVariables.setSelectedEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                Intent intent = new Intent(this, FacilitySelectionActivity.class);
                intent.putExtra("ActivityCode", prefs.getString("ActivityCode", ""));
                startActivity(intent);
                return true;
            case R.id.action_home:
                Intent i = new Intent(CreateIssueActivity.this, MainNavigationActivity.class);
                i.putExtra("ActivityCode", prefs.getString("ActivityCode", ""));
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    public void onBackPressed() {
        GlobalVariables.setSelectedEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
        Intent intent = new Intent(this, FacilitySelectionActivity.class);
        intent.putExtra("ActivityCode", prefs.getString("ActivityCode", ""));
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
