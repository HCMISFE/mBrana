package com.jsi.mbrana.Workflow.Order;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.PO.CreateOrderAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ApiRRFModel;
import com.jsi.mbrana.Models.PeriodModel;
import com.jsi.mbrana.Models.RRFHeaderModel;
import com.jsi.mbrana.Models.RRFModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.jsi.mbrana.Helpers.Helper.dateFormatter;
import static com.jsi.mbrana.Helpers.Helper.getDateDifference;

public class CreateOrderActivity extends AppCompatActivity implements IDataNotification {
    final ArrayList<RRFModel> rrf_model = new ArrayList<>();
    final ArrayList<PeriodModel> periods = new ArrayList<>();
    ProgressDialog progress;
    View customToastLayout;
    RecyclerView recycler_order;
    Button btnSubmit;
    Button btnDraft;
    Button btnConfirm;
    int actionType = -1;
    Tracker _mTracker;
    boolean isProgressVisible = false;
    SharedPreferences prefs_user;
    TextView tv_date_start, tv_date_next, tv_required_header;
    boolean isEmergency = false, isEditable = false;
    CreateOrderAdapter adapter;
    Boolean showEmergency = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        // Setting the content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vrf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Create VRF");
        LayoutInflater inflater = getLayoutInflater();

        customToastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        toolbar.setSubtitle(GlobalVariables.getSelectedEnvironment());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progress = new ProgressDialog(this);

        prefs_user = getSharedPreferences("userCredentials", 0);

        showEmergency = getIntent().getExtras().getBoolean("isEmergency");

        tv_date_start = (TextView) findViewById(R.id.tv_date_start);
        tv_date_next = (TextView) findViewById(R.id.tv_date_next);
        tv_required_header = (TextView) findViewById(R.id.tv_required_header);

        // Get the Period & Supplier
        new DataServiceTask(this, 8, false).execute("Lookup/GetPeriod?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        // Recycler view
        recycler_order = (RecyclerView) findViewById(R.id.newpo_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_order.setLayoutManager(mLayoutManager);
        recycler_order.setItemAnimator(new DefaultItemAnimator());

        // confirm button
        btnSubmit = (Button) findViewById(R.id.newpo_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handelProgressDialog(true, "");
                // Pause for 3 Seconds
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 3000ms
                        handelProgressDialog(false, "");
                    }
                }, 3000);
                // Show or hide buttons
                btnSubmit.setVisibility(View.GONE);
                btnDraft.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
                // Confirm Page
                getSupportActionBar().setTitle("Confirm VRF");
                // Set adapter to disable edit quantity
                adapter = new CreateOrderAdapter(getApplicationContext(), rrf_model, "Non-Editable", recycler_order);
                recycler_order.setAdapter(null);
                recycler_order.setAdapter(adapter);
            }
        });

        // Submit button
        btnConfirm = (Button) findViewById(R.id.newpo_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Submit")
                        .setLabel("Submit New VRF")
                        .build());
                ArrayList<RRFModel> itemsToBeSaved = rrf_model;
                String _username = prefs_user.getString("username", "");
                int _userid = prefs_user.getInt("UserID", 0);

                RRFHeaderModel header = new RRFHeaderModel();
                header.setEnvironmentCode(prefs_user.getString("EnvironmentCode", null));
                header.setEnvironmentID(prefs_user.getInt("EnvironmentID", 0));
                header.setActivityCode(prefs_user.getString("ActivityCode", null));
                header.setUserID(_userid);
                header.setUserName(_username);
                header.setPurchaseOrderStatusCode(Constants.PurchaseOrderStatus.Requested.getOrderStatusCode());
                header.setFilled(false);
                header.setPeriodID(periods.get(0).getPeriodID());
                header.setSupplierID(0);
                header.setMonthsToSupply(0);

                if (isEmergency) {
                    header.setPurchaseOrderType("Emergency");
                } else {
                    header.setPurchaseOrderType("Internal");
                }

                ApiRRFModel order = new ApiRRFModel();
                order.setVrfHeader(header);
                order.setVrfDetailModels(itemsToBeSaved);

                Gson gson = new Gson();
                final String vrf_model = gson.toJson(order);

                new AlertDialog.Builder(CreateOrderActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.submitMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(vrf_model, 99);
                                actionType = 99;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        //Save as draft
        btnDraft = (Button) findViewById(R.id.neworder_draftSave);
        btnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Submit")
                        .setLabel("Submit New VRF AS Draft")
                        .build());
                ArrayList<RRFModel> itemsToBeSaved = rrf_model;
                String _username = prefs_user.getString("username", "");
                int _userid = prefs_user.getInt("UserID", 0);

                RRFHeaderModel header = new RRFHeaderModel();
                header.setEnvironmentCode(prefs_user.getString("EnvironmentCode", null));
                header.setEnvironmentID(prefs_user.getInt("EnvironmentID", 0));
                header.setActivityCode(prefs_user.getString("ActivityCode", null));
                header.setUserID(_userid);
                header.setUserName(_username);
                header.setPurchaseOrderStatusCode(Constants.PurchaseOrderStatus.Draft.getOrderStatusCode());
                header.setFilled(false);
                header.setPeriodID(periods.get(0).getPeriodID());
                header.setSupplierID(0);
                header.setMonthsToSupply(0);

                if (isEmergency) {
                    header.setPurchaseOrderType("Emergency");
                } else {
                    header.setPurchaseOrderType("Internal");
                }

                ApiRRFModel order = new ApiRRFModel();
                order.setVrfHeader(header);
                order.setVrfDetailModels(itemsToBeSaved);

                Gson gson = new Gson();
                final String vrf_model = gson.toJson(order);

                new AlertDialog.Builder(CreateOrderActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.draftSaveMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(vrf_model, 66);
                                actionType = 66;
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

    public void GenerateVRF() {
        new DataServiceTask(this, 6, false).execute("PurchaseOrder/GenerateVRF?PeriodId=" + periods.get(0).getPeriodID() + "&PeriodStart=" + periods.get(0).getPeriodStart()
                + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode() + "&ActivityCode=" + GlobalVariables.getActivityCode(), "");
    }

    public void showVRFByType(String type) {
        switch (type) {
            case "Editable": {
                // Order is submitted
                btnSubmit.setVisibility(View.VISIBLE);
                btnDraft.setVisibility(View.VISIBLE);
            }
            break;
            case "Non-Editable": {
                // Show the notification
                handelNotification("Order is filled for this period.");
                // Order is filled
                btnSubmit.setVisibility(View.GONE);
                btnDraft.setVisibility(View.GONE);
            }
            break;
            case "Emergency": {
                isEmergency = true;
                tv_required_header.setVisibility(View.GONE);
                // Order is submitted
                btnSubmit.setVisibility(View.VISIBLE);
                btnDraft.setVisibility(View.VISIBLE);
                //Set requested quantity to zero
                for (int k = 0; k < rrf_model.size(); k++)
                    rrf_model.get(k).setRequestedQuantity(0);
            }
            break;
        }
        adapter = new CreateOrderAdapter(getApplicationContext(), rrf_model, type, recycler_order);
        recycler_order.setAdapter(null);
        recycler_order.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "VRFs: Create VRF");
        _mTracker.setScreenName("VRFs: Create VRF");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void handelDataSave(String objString, int requestCode) {
        new DataServiceTaskForObjects(this, objString, requestCode).execute("PurchaseOrder/SaveVRF", "");
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
            Intent i = new Intent(CreateOrderActivity.this, MainNavigationActivity.class);
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
            case 6: {
                try {
                    JSONObject jo = jsonArray.getJSONObject(0);
                    boolean isFilled = jo.getBoolean("IsFilled");
                    JSONArray VrfDetail = jo.getJSONArray("VrfDetail");

                    // Set adapter for the filled period
                    for (int i = 0; i < VrfDetail.length(); i++) {
                        JSONObject detail = VrfDetail.getJSONObject(i);
                        RRFModel model = new RRFModel();
                        model.setProductCN(detail.getString("ProductCN"));
                        model.setEnvironmentID(detail.getInt("EnvironmentID"));
                        model.setItemID(detail.getInt("ItemID"));
                        model.setUnitOfIssueID(detail.getInt("UnitOfIssueID"));
                        model.setFullItemName(detail.getString("FullItemName"));
                        model.setEnvironment(detail.getString("Environment"));
                        model.setUnit(detail.getString("Unit"));
                        model.setBeginningBalance(detail.getInt("BeginningBalance"));
                        model.setQuantityReceived(detail.getInt("QuantityReceived"));
                        model.setDOS(detail.getInt("DOS"));
                        model.setLoss(detail.getInt("Loss"));
                        model.setAdjustment(detail.getInt("Adjustment"));
                        model.setEndingBalance(detail.getInt("EndingBalance"));
                        model.setWasteFactor(detail.getDouble("WasteFactor"));
                        model.setTargetCoverage(detail.getInt("TargetCoverage"));
                        model.setDose(detail.getInt("Dose"));
                        model.setRequiredForNextSupplyPeriod(detail.getInt("RequiredForNextSupplyPeriod"));
                        model.setRequestedQuantity(detail.getInt("RequestedQuantity"));
                        model.setConsumption(detail.getInt("Consumption"));
                        model.setMaxStockQuantity(detail.getInt("MaxStockQuantity"));
                        model.setQuantityToReachMax(detail.getInt("QuantityToReachMax"));
                        rrf_model.add(model);
                    }

                    // Handling the form type
                    if (showEmergency) {
                        showVRFByType("Emergency");
                    } else if (!isFilled && isEditable) {
                        showVRFByType("Editable");
                    } else {
                        showVRFByType("Non-Editable");
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
            case 8: {
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        PeriodModel period = new PeriodModel();
                        period.setPeriodID(jo.getInt("PeriodID"));
                        period.setPeriodStart(jo.getString("PeriodStart"));
                        period.setNextPeriodStart(jo.getString("NextPeriodStart"));
                        periods.add(period);
                    }
                    tv_date_start.setText(dateFormatter(periods.get(0).getPeriodStart()) + "");

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                    Date next_date = simpleDateFormat.parse(periods.get(0).getNextPeriodStart().replace("T", " "));
                    tv_date_next.setText(Helper.getMomentFromNow(next_date));

                    String today_string = simpleDateFormat.format(new Date());
                    Date today = simpleDateFormat.parse(today_string);

                    int remaining_days = getDateDifference(today, next_date);

                    isEditable = remaining_days <= 0;

                    GenerateVRF();
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
        switch (message) {
            case "Change is successful": {
                ShowSnackNotification(message, R.color.successMessage);
                if (actionType == 99 || actionType == 66) {
                    Intent intent = new Intent(this, OrderMenuActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case "Change failed": {
                ShowSnackNotification(message, R.color.errorMessage);
                break;
            }
            default: {
                ShowSnackNotification(message, R.color.errorMessage);
                break;
            }
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
