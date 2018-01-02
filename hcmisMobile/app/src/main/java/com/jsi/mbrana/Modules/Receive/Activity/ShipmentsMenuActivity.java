package com.jsi.mbrana.Modules.Receive.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.CommonUIComponents.mBranaNotification;
import com.jsi.mbrana.CommonUIComponents.mBranaProgressDialog;
import com.jsi.mbrana.DataAccessLayer.DataServiceAgent;
import com.jsi.mbrana.DataAccessLayer.DataServiceInterface;
import com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice.ReceiveInvoiceModel;
import com.jsi.mbrana.Modules.UserManagement.LoginActivity;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Preferences.PreferenceKey;
import com.jsi.mbrana.Preferences.PreferenceManager;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Adapter.Receipt.Adapter_Receipt;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ShipmentsMenuActivity extends AppCompatActivity {
    ListView tablecontainer;
    EditText searchText;
    Adapter_Receipt adapter;
    List<ReceiveInvoiceModel> ReceiptInvoicesData;
    android.support.v7.app.ActionBar ab;
    FloatingActionButton searchRIButton;
    private SwipeRefreshLayout swipeContainer;
    private Tracker _mTracker;
    private long start;
    PreferenceManager preference;
    DataServiceInterface dataService;
    private mBranaNotification notification;
    private mBranaProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_invoices_list);

        preference = new PreferenceManager(this);

        ab = getSupportActionBar();
        if (ab != null) {
            ab.setSubtitle(preference.getString(PreferenceKey.EnvironmentCode));
        }

        dataService = DataServiceAgent.getDataService(this);

        //Notification
        notification = new mBranaNotification(this, findViewById(android.R.id.content));

        //ProgressDialog
        progressDialog = new mBranaProgressDialog(this);

        searchText = (EditText) this.findViewById(R.id.search);

        searchRIButton = (FloatingActionButton) this.findViewById(R.id.searchRIButton);
        searchRIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (searchText.getVisibility() == View.GONE) {
                    searchText.setVisibility(View.VISIBLE);
                    searchText.requestFocus();
                    imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);
                    searchText.setText("");
                } else if (searchText.getVisibility() == View.VISIBLE) {
                    searchText.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    searchText.setText("");
                }
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Search")
                        .setLabel("Search Clicked")
                        .build());
            }
        });

        //
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                getReceiveInvoiceData();
            }
        });

        //
        tablecontainer = (ListView) findViewById(R.id.ReceiptInvloceList_TableContainer);
        tablecontainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShipmentsMenuActivity.this, ReceiveMenuActivity.class);
                intent.putExtra("ReceiveInvoiceId", ReceiptInvoicesData.get(position).getId());
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User Action")
                        .setAction("Shipment Selection")
                        .setLabel("selected shipment (" + ReceiptInvoicesData.get(position).getId() + ")")
                        .build());
                startActivity(intent);
            }
        });

        getReceiveInvoiceData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Receipts: Shipments");
        _mTracker.setScreenName("Receipts: Shipments");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void getReceiveInvoiceData() {
        start = System.currentTimeMillis();
        progressDialog.showProgressDialog();
        try {
            dataService.GetReceiveInvoices(preference.getInt(PreferenceKey.EnvironmentId)).enqueue(new Callback<List<ReceiveInvoiceModel>>() {
                @Override
                public void onResponse(Call<List<ReceiveInvoiceModel>> call, retrofit2.Response<List<ReceiveInvoiceModel>> response) {
                    progressDialog.hideProgressDialog();
                    //Response
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Convert Model
                            ReceiptInvoicesData = response.body();

                            //Set Header Title
                            ab.setTitle(ReceiptInvoicesData.size() + " Shipments");

                            long stop = System.currentTimeMillis();
                            long interval = (stop - start) / 1000;
                            _mTracker.send(new HitBuilders.TimingBuilder()
                                    .setCategory("Loading")
                                    .setValue(interval)
                                    .setVariable("Receipts")
                                    .setLabel("ReceiptInvoice/GetReceiptInvoice")
                                    .build());

                            adapter = new Adapter_Receipt(ShipmentsMenuActivity.this, R.layout.layout_viewreceiptinvoices, ReceiptInvoicesData);
                            tablecontainer.setAdapter(adapter);
                        }
                    }  else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(ShipmentsMenuActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 406) {
                        notification.showHttp406Notification();
                    }
                }

                @Override
                public void onFailure(Call<List<ReceiveInvoiceModel>> call, Throwable t) {
                    progressDialog.hideProgressDialog();
                    //Fail Message
                    notification.showFailureNotification(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(ShipmentsMenuActivity.this, MainNavigationActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}