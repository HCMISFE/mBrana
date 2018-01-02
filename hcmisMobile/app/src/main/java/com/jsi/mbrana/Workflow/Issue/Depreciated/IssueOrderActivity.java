package com.jsi.mbrana.Workflow.Issue.Depreciated;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.Issue.IssueOrderAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Models.ApiOrderModel;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Issue.ApproveIssueActivity;
import com.jsi.mbrana.Workflow.Issue.IssueMenuActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class IssueOrderActivity extends AppCompatActivity implements IDataNotification {
    ArrayList<ItemModel> items = new ArrayList<ItemModel>();
    ProgressDialog progress;
    OrderModel Header;
    ListView itemsTable;
    TextView facilityNameTV;
    Button cancelBtn;
    Button confirmBtn;
    Button returnBtn;
    int actionType = -1;
    private Tracker _mTracker;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen",this.getLocalClassName());
        _mTracker.setScreenName("WorkFlow-Issue: Issue Confirmation");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Issue Confirmation");

        Header = (OrderModel) getIntent().getSerializableExtra("Order");
        facilityNameTV = (TextView) findViewById(R.id.IssueOrder_FacilityName);
        if (Header != null) {
            facilityNameTV.setText(Header.getFacility());
            getSupportActionBar().setSubtitle("Ref.No: " + Header.getRefNo());
        }

        //load detail data
        new DataServiceTask(this, 1).execute("Order/GetOrderDetail?OrderID=" + Header.getID() + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        //set Action Listerner for buttons
        cancelBtn = (Button) findViewById(R.id.IssueOrder_CancelBtn);
        confirmBtn = (Button) findViewById(R.id.IssueOrder_IssueBtn);
        returnBtn = (Button) findViewById(R.id.IssueOrder_ReturnBtn);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> IssuedValues = GlobalVariables.getOrdersToBeIssued();
                OrderModel header = new OrderModel();
                header.setID(Header.getID());
                header.setRequestedBy(Header.getRequestedBy());
                header.setFacility(Header.getFacility());
                header.setOrderStatusCode(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode());
                header.setEnvironmentID(Header.getEnvironmentID());
                header.setEnvironmentCode(Header.getEnvironmentCode());

                ApiOrderModel orderData = new ApiOrderModel();
                orderData.setOrder(header);
                orderData.setOrderDetail(IssuedValues);

                Gson gson = new Gson();
                String itemstring = gson.toJson(orderData);

                handelDataSave(itemstring, "Order/UpDateOrder");
                actionType = 0;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> IssuedValues = GlobalVariables.getOrdersToBeIssued();
                OrderModel header = new OrderModel();
                header.setID(Header.getID());
                header.setRequestedBy(Header.getRequestedBy());
                header.setFacility(Header.getFacility());
                header.setOrderStatusCode(Constants.OrderStatus.CANCELED.getOrderStatusCode());
                header.setEnvironmentID(Header.getEnvironmentID());
                header.setEnvironmentCode(Header.getEnvironmentCode());

                ApiOrderModel orderData = new ApiOrderModel();
                orderData.setOrder(header);
                orderData.setOrderDetail(IssuedValues);

                Gson gson = new Gson();
                String itemstring = gson.toJson(orderData);

                handelDataSave(itemstring, "Order/UpDateOrder");
                actionType = 1;

            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> IssuedValues = GlobalVariables.getOrdersToBeIssued();

                OrderModel header = new OrderModel();
                header.setID(Header.getID());
                header.setRequestedBy(Header.getRequestedBy());
                header.setFacility(Header.getFacility());
                header.setOrderStatusCode(Constants.OrderStatus.ISSUED.getOrderStatusCode());
                header.setEnvironmentID(Header.getEnvironmentID());
                header.setEnvironmentCode(Header.getEnvironmentCode());

                ApiOrderModel orderData = new ApiOrderModel();
                orderData.setOrder(header);
                orderData.setOrderDetail(IssuedValues);

                Gson gson = new Gson();
                String itemstring = gson.toJson(orderData);

                handelDataSave(itemstring, "Issue/SaveIssue");

                actionType = 2;
            }
        });

    }


    public void handelDataSave(String objString, String api) {
        new DataServiceTaskForObjects(this, objString).execute(api, "");
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {

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
                item.setApprovedQuantity(jo.getInt("ApprovedQuantity"));
                items.add(item);
            }

            // set the table values to global varaiable for further processing
            GlobalVariables.setOrdersToBeIssued(items);

            //set Adapter to itemsListTable
            itemsTable = (ListView) findViewById(R.id.IssueOrder_ItemTable);
            IssueOrderAdapter adapter = new IssueOrderAdapter(IssueOrderActivity.this, R.layout.layout_approvalissueorder, items);
            itemsTable.setAdapter(adapter);

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
    public void onBackPressed() {

    }

    @Override
    public void handelNotification(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();

        if (message.equalsIgnoreCase("Change is successful")) {
            if (actionType == 0) {
                Intent intent = new Intent(IssueOrderActivity.this, ApproveIssueActivity.class);
                intent.putExtra("Order", Header);
                startActivity(intent);

            } else if (actionType == 1) {
                Intent intent = new Intent(IssueOrderActivity.this, IssueMenuActivity.class);
                intent.putExtra("Order", Header);
                startActivity(intent);
            } else if (actionType == 2) {
                Intent intent = new Intent(IssueOrderActivity.this, IssueMenuActivity.class);
                intent.putExtra("Order", Header);
                startActivity(intent);
            }
        }
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
