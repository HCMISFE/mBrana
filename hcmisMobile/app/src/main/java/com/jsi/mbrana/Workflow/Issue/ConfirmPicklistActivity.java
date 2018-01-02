package com.jsi.mbrana.Workflow.Issue;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.Issue.PicklistConfirmationAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.API_PicklistDetailModel;
import com.jsi.mbrana.Models.IssueHeaderModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.Models.PicklistHeaderModel;
import com.jsi.mbrana.Models.PicklistModel;
import com.jsi.mbrana.CommonModels.VVMModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConfirmPicklistActivity extends AppCompatActivity implements IDataNotification {
    OrderModel Header;
    ArrayList<PicklistModel> picklists = new ArrayList<>();
    ProgressDialog progress;
    ListView itemsTable;
    Button cancelBtn;
    Button confirmBtn;
    Button returnBtn;
    int actionType = -1;
    Dialog datepickerDialog;
    Date selectedDate;
    TextView IsCampain;
    View customToastLayout;
    TextView customToastext;
    private Tracker _mTracker;
    boolean isProgressVisible = false;
    ArrayList<VVMModel> VVmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picklist_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //custom toast definition
        LayoutInflater inflater = getLayoutInflater();

        progress = new ProgressDialog(this);

        customToastLayout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a message
        customToastext = (TextView) customToastLayout.findViewById(R.id.customToastText);
        IsCampain = (TextView) findViewById(R.id.IsCampaignText);

        setTitle("Picklist Confirmation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        new DataServiceTask(this, 5, false).execute("LookUp/GetVVMs?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        itemsTable = (ListView) findViewById(R.id.pkl_ItemTable);

        // set date to date tv
        Date date = new Date();
        selectedDate = date;

        // intialize date picker
        Calendar calendar = Calendar.getInstance();
        datepickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date cratedDate = null;
                    cratedDate = simpleDateFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    selectedDate = cratedDate;
                } catch (ParseException e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }
                datepickerDialog.dismiss();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // get selected order from intent
        Header = (OrderModel) getIntent().getSerializableExtra("Order");
        if (Header.isCampaign())
            IsCampain.setVisibility(View.VISIBLE);
        if (Header != null) {
            if (Html.fromHtml(Header.getFacility().toString()).length() > 35)
                getSupportActionBar().setSubtitle(Html.fromHtml(Header.getFacility().toString().substring(0, 35)));
            else
                getSupportActionBar().setSubtitle(Html.fromHtml(Header.getFacility().toString()));
        }

        //load detail data
        new DataServiceTask(this, 1, false).execute("PickList/GetPickListDetail?OrderID=" + Header.getID() + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        //set Action Listerner for buttons
        cancelBtn = (Button) findViewById(R.id.pkl_CancelBtn);
        confirmBtn = (Button) findViewById(R.id.pkl_IssueBtn);
        returnBtn = (Button) findViewById(R.id.pkl_ReturnBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Cancel")
                        .setLabel("Delete Issue")
                        .build());
                new AlertDialog.Builder(ConfirmPicklistActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.canncelMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<PicklistModel> pickListToBeSaved = (ArrayList<PicklistModel>) getIntent().getSerializableExtra("PicklistsToBeSaved");
                                API_PicklistDetailModel dataToBeSaved = new API_PicklistDetailModel();

                                for (int i = 0; i < pickListToBeSaved.size(); i++) {
                                    pickListToBeSaved.get(i).setOrderStatusCode(Constants.OrderStatus.CANCELED.getOrderStatusCode());
                                    pickListToBeSaved.get(i).setPickedDate(selectedDate);
                                    pickListToBeSaved.get(i).setEnvironmentCode(Header.getEnvironmentCode());
                                }
                                dataToBeSaved.setPickListDetails(pickListToBeSaved);

                                PicklistHeaderModel picklistHeader = new PicklistHeaderModel();
                                picklistHeader.setEnvironmentCode(Header.getEnvironmentCode());
                                picklistHeader.setEnvironmentID(Header.getEnvironmentID());
                                picklistHeader.setOrderId(Header.getID());
                                picklistHeader.setRequestedBy(Header.getRequestedBy());
                                if (pickListToBeSaved.size() > 0)
                                    picklistHeader.setID(pickListToBeSaved.get(0).getPickListId());

                                dataToBeSaved.setPickList(picklistHeader);

                                Gson gson = new Gson();
                                final String itemstring = gson.toJson(dataToBeSaved);
                                // continue with Issue
                                handelDataSave(itemstring, "PickList/CancelPickList", 6);
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

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Confirm")
                        .setLabel("Confirm Picklist for Issue")
                        .build());
                ArrayList<PicklistModel> pickListToBeSaved = (ArrayList<PicklistModel>) getIntent().getSerializableExtra("PicklistsToBeSaved");

                API_PicklistDetailModel dataToBeSaved = new API_PicklistDetailModel();

                if (pickListToBeSaved == null)
                    return;
                for (int i = 0; i < pickListToBeSaved.size(); i++) {
                    pickListToBeSaved.get(i).setPickedDate(selectedDate);
                    pickListToBeSaved.get(i).setEnvironmentCode(Header.getEnvironmentCode());
                }
                dataToBeSaved.setPickListDetails(pickListToBeSaved);

                PicklistHeaderModel picklistHeader = new PicklistHeaderModel();
                picklistHeader.setEnvironmentCode(Header.getEnvironmentCode());
                picklistHeader.setEnvironmentID(Header.getEnvironmentID());
                picklistHeader.setOrderId(Header.getID());
                picklistHeader.setRequestedBy(Header.getRequestedBy());
                if (pickListToBeSaved.size() > 0)
                    picklistHeader.setID(pickListToBeSaved.get(0).getPickListId());
                dataToBeSaved.setPickList(picklistHeader);
                Gson gson = new Gson();
                final String itemstring = gson.toJson(dataToBeSaved);

                new AlertDialog.Builder(ConfirmPicklistActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.issueMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handelDataSave(itemstring, "Issue/SaveIssue", 7);
                                actionType = 1;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Return")
                        .setLabel("Return Picklist to Approval")
                        .build());
                ArrayList<PicklistModel> pickListToBeSaved = (ArrayList<PicklistModel>) getIntent().getSerializableExtra("PicklistsToBeSaved");

                API_PicklistDetailModel dataToBeSaved = new API_PicklistDetailModel();

                if (pickListToBeSaved == null)
                    return;

                for (int i = 0; i < pickListToBeSaved.size(); i++) {
                    pickListToBeSaved.get(i).setOrderStatusCode(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode());
                    pickListToBeSaved.get(i).setPickedDate(selectedDate);
                    pickListToBeSaved.get(i).setEnvironmentCode(Header.getEnvironmentCode());
                }
                dataToBeSaved.setPickListDetails(pickListToBeSaved);

                PicklistHeaderModel picklistHeader = new PicklistHeaderModel();
                picklistHeader.setEnvironmentCode(Header.getEnvironmentCode());
                picklistHeader.setEnvironmentID(Header.getEnvironmentID());
                picklistHeader.setOrderId(Header.getID());
                picklistHeader.setRequestedBy(Header.getRequestedBy());
                if (pickListToBeSaved.size() > 0)
                    picklistHeader.setID(pickListToBeSaved.get(0).getPickListId());

                dataToBeSaved.setPickList(picklistHeader);

                Gson gson = new Gson();
                final String itemstring = gson.toJson(dataToBeSaved);

                new AlertDialog.Builder(ConfirmPicklistActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.returnMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with Issue
                                handelDataSave(itemstring, "PickList/CancelPickList", 8);
                                actionType = 2;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Issues: Picklist Confirmation");
        _mTracker.setScreenName("Issues: Picklist Confirmation");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void handelDataSave(String objString, String api, int requestCode) {
        new DataServiceTaskForObjects(this, objString, requestCode).execute(api, "");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, IssueMenuActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
        } else startActivity(intent);
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
                    PicklistModel picklist = new PicklistModel();
                    picklist.setProductCN(jo.getString("ProductCN"));
                    picklist.setBatchNumber(jo.getString("BatchNumber"));
                    picklist.setExpireDate(jo.getString("ExpireDate"));
                    picklist.setFullItemName(jo.getString("FullItemName"));
                    picklist.setUnit(jo.getString("Unit"));
                    picklist.setQuantityInBU(jo.getInt("QuantityInBU"));
                    picklist.setItemID(jo.getInt("ItemID"));
                    picklist.setUnitID(jo.getInt("UnitID"));
                    picklist.setPickListId(jo.getInt("PickListId"));
                    picklist.setOrderId(jo.getInt("OrderId"));
                    picklist.setEnvironmentID(jo.getInt("EnvironmentID"));
                    picklist.setEnvironmentCode(jo.getString("EnvironmentCode"));
                    picklist.setManufacturerID(jo.getInt("ManufacturerID"));
                    picklist.setPhysicalStoreID(jo.getInt("PhysicalStoreID"));
                    picklist.setQtyPerPack(jo.getInt("QtyPerPack"));
                    picklist.setPickListDetailId(jo.getInt("PickListDetailId"));
                    picklist.setReceiveDocID(jo.getInt("ReceiveDocID"));
                    picklist.setStoreID(jo.getInt("StoreID"));
                    picklist.setUnitCost(jo.getInt("UnitCost"));
                    picklist.setSellingPrice(jo.getInt("SellingPrice"));
                    picklist.setVVMID(jo.getInt("VVMID"));
                    picklist.setVVMCode(jo.getString("VVMCode"));
                    picklist.setManufacturer(jo.getString("Manufacturer"));
                    picklist.setManufacturerSH(jo.getString("ManufacturerSH"));
                    picklists.add(picklist);
                }
                getIntent().putExtra("PicklistsToBeSaved", picklists);
                //set Adapter to itemsListTable
                PicklistConfirmationAdapter adapter = new PicklistConfirmationAdapter(ConfirmPicklistActivity.this, R.layout.layout_picklistconfirmation, picklists, VVmList);
                itemsTable.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
        } else if (requestCode == 5) {
            try {
                VVmList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    VVMModel vvm = new VVMModel();
                    vvm.setCode(jo.getString("VVMCode"));
                    vvm.setId(jo.getInt("VVMID"));
                    VVmList.add(vvm);
                }
            } catch (Exception e) {
                this.handelNotification("Error found");
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
        } else if (requestCode == 7) {
            try {
                if (jsonArray.length() == 0) return;

                if (jsonArray.length() > 1) {
                    // if the issue has multiple issue show dialog to choose the approprate issue
                    try {
                        ArrayList<IssueHeaderModel> issuesList = new ArrayList<>();
                        ArrayList<String> stvsList = new ArrayList<String>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject currentvalue = jsonArray.getJSONObject(0);
                            IssueHeaderModel issue = new IssueHeaderModel();
                            issue.setIssNo(currentvalue.getString("IDPrinted"));
                            issue.setID(currentvalue.getInt("ID"));
                            issuesList.add(issue);
                            stvsList.add(issue.getIssNo());
                        }
                        showMultipleIssueDialog(stvsList.toArray(new String[0]), issuesList, Header);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        _mTracker.send(new HitBuilders.ExceptionBuilder()
                                .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                        .getDescription(Thread.currentThread().getName(), ex))
                                .setFatal(false)
                                .build());
                    }
                } else {
                    JSONObject currentvalue = jsonArray.getJSONObject(0);
                    final IssueHeaderModel issue = new IssueHeaderModel();
                    issue.setIssNo(currentvalue.getString("IssNo"));
                    issue.setID(currentvalue.getInt("ID"));
                    new AlertDialog.Builder(ConfirmPicklistActivity.this)
                            .setMessage(Html.fromHtml(getString(R.string.printMessage)))
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ConfirmPicklistActivity.this, PrintIssueActivity.class);
                                    intent.putExtra("Order", Header);
                                    intent.putExtra("IssueID", issue.getID());
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ConfirmPicklistActivity.this, IssueMenuActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
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

    private void showMultipleIssueDialog(String[] values, final ArrayList<IssueHeaderModel> issues, final OrderModel selectedOrder) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("STVs")

                .setItems(values, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("itemClicked=>", issues.get(which).getIssNo());
                        Intent intent = new Intent(ConfirmPicklistActivity.this, PrintIssueActivity.class);
                        intent.putExtra("Order", selectedOrder);
                        intent.putExtra("IssueID", issues.get(which).getID());
                        startActivity(intent);
                    }
                });
        builder.show();
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
            Intent intent = new Intent(ConfirmPicklistActivity.this, MainNavigationActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(intent, options.toBundle());
            } else startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handelNotification(String message) {
        Intent intent = null;
        if (message.equalsIgnoreCase("Change is successful")) {
            if (actionType == 0) {
                message = "Issue Cancelled";
                ShowSnackNotification(message, R.color.successMessage);
                intent = new Intent(ConfirmPicklistActivity.this, IssueMenuActivity.class);
            } else if (actionType == 1) {
                message = "Issue Confirmed";
                ShowSnackNotification(message, R.color.successMessage);
            } else if (actionType == 2) {
                message = "Issue Sent to Approval.";
                ShowSnackNotification(message, R.color.successMessage);
                intent = new Intent(ConfirmPicklistActivity.this, ApproveIssueActivity.class);
                intent.putExtra("Order", Header);
                intent.putExtra(getString(R.string.transitionDirection), getString(R.string.leftTranstion));
            }

            if (intent != null) {
                startActivity(intent);
            }
        } else {
            ShowSnackNotification(message, R.color.errorMessage);
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
