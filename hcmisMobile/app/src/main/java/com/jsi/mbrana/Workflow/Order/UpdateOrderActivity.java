package com.jsi.mbrana.Workflow.Order;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.PO.EditPOAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ApiPoModel;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.POModel;
import com.jsi.mbrana.Models.SupplierModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdateOrderActivity extends AppCompatActivity implements IDataNotification {
    final ArrayList<ItemModel> items = new ArrayList<ItemModel>();
    final ArrayList<SupplierModel> suppliers = new ArrayList<>();
    Button updateBtn;
    Button submitBtn;
    Button cancelBtn;
    RecyclerView itemsTable;
    POModel headerPoModel;
    ProgressDialog progress;
    Spinner supplierListSpinner;
    int selectedSupplierIndex = 0;
    private int actionType;
    private Tracker _mTracker;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_po);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Update VRF");
        toolbar.setSubtitle(GlobalVariables.getSelectedEnvironment());
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);

        // Intialize supplier list
        supplierListSpinner = (Spinner) findViewById(R.id.editpo_supplierSpinner);

        // Load supplier look up
        new DataServiceTask(this, 3, false).execute("LookUp/GetSupplier?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        // Handel supplier selected changed
        supplierListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSupplierIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Initialize action buttons
        updateBtn = (Button) findViewById(R.id.editpo_save);
        submitBtn = (Button) findViewById(R.id.editpo_submit);
        cancelBtn = (Button) findViewById(R.id.editpo_cancel);

        // Intialize item table
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        itemsTable = (RecyclerView) findViewById(R.id.editpo_itemslistTable);
        itemsTable.addItemDecoration(new SimpleDividerItemDecoration(this));
        itemsTable.setLayoutManager(mLayoutManager);
        itemsTable.setItemAnimator(new DefaultItemAnimator());

        headerPoModel = (POModel) getIntent().getSerializableExtra("Header");
        new DataServiceTask(this, 1, false).execute("PurchaseOrder/GetPurchaseOrderDetail?PurchaseOrderID=" + headerPoModel.getID() + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Update")
                        .setLabel("Update VRF")
                        .build());
                ArrayList<ItemModel> itemsToBeSaved = (ArrayList<ItemModel>) getIntent().getSerializableExtra("DraftPOItems");

//                if (isProperData(itemsToBeSaved)) {
                POModel header = new POModel();
                header.setSupplierID(suppliers.get(selectedSupplierIndex).getSupplierID());
                header.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                header.setPurchaseOrderStatusCode(Constants.PurchaseOrderStatus.Draft.getOrderStatusCode());
                header.setID(headerPoModel.getID());
                ApiPoModel orderData = new ApiPoModel();
                orderData.setPurchaseOrder(header);
                orderData.setPurchaseOrderDetail(itemsToBeSaved);
                Gson gson = new Gson();
                final String itemstring = gson.toJson(orderData);

                new AlertDialog.Builder(UpdateOrderActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.updateMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(itemstring, 5);
                                actionType = 0;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
//                } else {
//                    dataErrorMessage();
//                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Submit")
                        .setLabel("Submit VRF")
                        .build());
                ArrayList<ItemModel> itemsToBeSaved = (ArrayList<ItemModel>) getIntent().getSerializableExtra("DraftPOItems");

//                if (isProperData(itemsToBeSaved)) {
                POModel header = new POModel();
                header.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                header.setSupplierID(suppliers.get(selectedSupplierIndex).getSupplierID());
                header.setPurchaseOrderStatusCode(Constants.PurchaseOrderStatus.Requested.getOrderStatusCode());
                header.setID(headerPoModel.getID());
                ApiPoModel orderData = new ApiPoModel();
                orderData.setPurchaseOrder(header);
                orderData.setPurchaseOrderDetail(itemsToBeSaved);
                Gson gson = new Gson();
                final String itemstring = gson.toJson(orderData);

                new AlertDialog.Builder(UpdateOrderActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.submitMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(itemstring, 6);
                                actionType = 1;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
//                } else {
//                    dataErrorMessage();
//                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Cancel")
                        .setLabel("Delete VRF")
                        .build());
                ArrayList<ItemModel> itemsToBeSaved = GlobalVariables.getDraftPOItems();
                POModel header = new POModel();
                header.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                header.setSupplierID(suppliers.get(selectedSupplierIndex).getSupplierID());
                header.setPurchaseOrderStatusCode(Constants.PurchaseOrderStatus.Canceled.getOrderStatusCode());
                header.setID(headerPoModel.getID());
                ApiPoModel orderData = new ApiPoModel();
                orderData.setPurchaseOrder(header);
                orderData.setPurchaseOrderDetail(itemsToBeSaved);

                Gson gson = new Gson();
                final String itemstring = gson.toJson(orderData);
                new AlertDialog.Builder(UpdateOrderActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.canncelMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(itemstring, 7);
                                actionType = 2;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "VRFs: Edit VRF");
        _mTracker.setScreenName("VRFs: Edit VRF");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void dataErrorMessage() {
        new AlertDialog.Builder(UpdateOrderActivity.this)
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
        new DataServiceTaskForObjects(this, objString, requestCode).execute("PurchaseOrder/UpdatePurchaseOrder", "");
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
            Intent i = new Intent(UpdateOrderActivity.this, MainNavigationActivity.class);
            startActivity(i);
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
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        ItemModel item = new ItemModel();
                        item.setItemName(jo.getString("FullItemName"));
                        item.setItemCode(jo.getString("ProductCN"));
                        item.setItemID(jo.getInt("ItemID"));
                        item.setUnitOfIssue(jo.getString("Unit"));
                        item.setUnitOfIssueID(jo.getInt("UnitOfIssueID"));
                        item.setQuantity(jo.getInt("Quantity"));
                        item.setPurchaseOrderDetailID(jo.getInt("PurchaseOrderDetailID"));
                        items.add(item);
                    }
                    getIntent().putExtra("DraftPOItems", items);
                    EditPOAdapter adapter = new EditPOAdapter(this, items, itemsTable);
                    itemsTable.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
                break;
            }
            case 3: {
                try {
                    ArrayList<String> suppliersNameList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        SupplierModel supplier = new SupplierModel();
                        supplier.setSupplier(jo.getString("Supplier"));
                        supplier.setSupplierID(jo.getInt("SupplierID"));
                        suppliersNameList.add(supplier.getSupplier());
                        suppliers.add(supplier);
                    }
                    //set adapter to suppliers spinner
                    ArrayAdapter customAdapter = new ArrayAdapter(this, R.layout.layout_spinneritem, suppliersNameList);
                    customAdapter.setDropDownViewResource(R.layout.layout_spinneritem);
                    supplierListSpinner.setAdapter(customAdapter);

                    // set previously selected supplier
                    for (int i = 0; i < suppliers.size(); i++) {
                        if (headerPoModel.getSupplierID() == suppliers.get(i).getSupplierID()) {
                            supplierListSpinner.setSelection(i);
                            break;
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
                break;
            }
            case 6: {
                try {
                    JSONObject jo = jsonArray.getJSONObject(0);
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
                    Intent intent = new Intent(UpdateOrderActivity.this, ViewOrderActivity.class);
                    intent.putExtra("Header", po);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
                break;
            }
        }
    }

    @Override
    public void handelNotification(String message) {
        ShowSnackNotification(message, R.color.successMessage);
        if (message.equalsIgnoreCase("Change is successful") && actionType != 0 && actionType != 1) {
            Intent intent = new Intent(UpdateOrderActivity.this, OrderMenuActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateOrderActivity.this, OrderMenuActivity.class);
        startActivity(i);
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
