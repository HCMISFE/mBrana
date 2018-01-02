package com.jsi.mbrana.Modules.Receive.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.CommonUIComponents.mBranaNotification;
import com.jsi.mbrana.CommonUIComponents.mBranaProgressDialog;
import com.jsi.mbrana.DataAccessLayer.DataServiceAgent;
import com.jsi.mbrana.DataAccessLayer.DataServiceInterface;
import com.jsi.mbrana.Extensions.RecyclerItemClickListener;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Modules.Receive.Adapter.ReceiptMenuAdapter;
import com.jsi.mbrana.Modules.Receive.Helper.Status.StatusHelperModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;
import com.jsi.mbrana.Modules.UserManagement.LoginActivity;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Preferences.PreferenceKey;
import com.jsi.mbrana.Preferences.PreferenceManager;
import com.jsi.mbrana.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ReceiveMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fBtn_Add;
    Tracker _mTracker;
    PreferenceManager preference;
    DataServiceInterface dataService;
    mBranaNotification notification;
    mBranaProgressDialog progressDialog;
    int ReceiveInvoiceId;
    android.support.v7.app.ActionBar ab;
    List<ReceiveModel> ReceivesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_invoice_list_detail_receives);

        //Toolbar
        ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Receives");
            ab.setSubtitle("Place Holder");
        }

        //Preference
        preference = new PreferenceManager(this);

        //DataService
        dataService = DataServiceAgent.getDataService(this);

        //Notification
        notification = new mBranaNotification(this, findViewById(android.R.id.content));

        //ProgressDialog
        progressDialog = new mBranaProgressDialog(this);

        //Intent Selected
        ReceiveInvoiceId = getIntent().getIntExtra("ReceiveInvoiceId", 0);

        //Recycler View
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.ReceiptInvloceListDetail_TableContainer);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (ReceivesData.size() > 0 && ReceivesData != null) {
                    if (ReceivesData.get(position).getReceiveStatusCode().equals(StatusHelperModel.ReceiveStatus.CONFIRMED.getStatusCode())) {
                        Intent intent = new Intent(ReceiveMenuActivity.this, ConfirmReceiveActivity.class);
                        intent.putExtra("ReceiveId", ReceivesData.get(position).getId());
                        intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                        intent.putExtra("ReceiveStatusCode", ReceivesData.get(position).getReceiveStatusCode());
                        startActivity(intent);
                    } else if (ReceivesData.get(position).getReceiveStatusCode().equals(StatusHelperModel.ReceiveStatus.SUBMITTED.getStatusCode())) {
                        Intent intent = new Intent(ReceiveMenuActivity.this, ConfirmReceiveActivity.class);
                        intent.putExtra("ReceiveId", ReceivesData.get(position).getId());
                        intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                        intent.putExtra("ReceiveStatusCode", ReceivesData.get(position).getReceiveStatusCode());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ReceiveMenuActivity.this, UpdateReceiveActivity.class);
                        intent.putExtra("ReceiveId", ReceivesData.get(position).getId());
                        intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                        intent.putExtra("ReceiveStatusCode", ReceivesData.get(position).getReceiveStatusCode());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //TODO
            }
        }));

        //
        fBtn_Add = (FloatingActionButton) this.findViewById(R.id.addDetailRIButton);
        fBtn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("User Action")
                        .setAction("Create")
                        .setLabel("Create New Receipt")
                        .build());

                //Create Receive
                Intent intent = new Intent(getApplicationContext(), CreateReceiveActivity.class);
                intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                startActivity(intent);
            }
        });

        //ShipmentDetail
        GetReceives();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Receipts: Receipts");
        _mTracker.setScreenName("Receipts: Receipts");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void GetReceives() {
        progressDialog.showProgressDialog();
        try {
            dataService.GetReceives(preference.getInt(PreferenceKey.EnvironmentId), ReceiveInvoiceId).enqueue(new Callback<List<ReceiveModel>>() {
                @Override
                public void onResponse(Call<List<ReceiveModel>> call, retrofit2.Response<List<ReceiveModel>> response) {
                    progressDialog.hideProgressDialog();
                    //Response
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Convert Model
                            ReceivesData = response.body();

                            if (ReceivesData.size() == 0) {
                                Intent intent = new Intent(getApplicationContext(), CreateReceiveActivity.class);
                                intent.putExtra("ReceiveInvoiceId", getIntent().getIntExtra("ReceiveInvoiceId", 0));
                                intent.putExtra("hasPreviousReceive", false);
                                startActivity(intent);
                            } else {
                                ReceiptMenuAdapter adapter = new ReceiptMenuAdapter(ReceivesData);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(ReceiveMenuActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 406) {
                        notification.showHttp406Notification();
                    }
                }

                @Override
                public void onFailure(Call<List<ReceiveModel>> call, Throwable t) {
                    progressDialog.hideProgressDialog();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(ReceiveMenuActivity.this, MainNavigationActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, ShipmentsMenuActivity.class);
        startActivity(intent);
    }
}