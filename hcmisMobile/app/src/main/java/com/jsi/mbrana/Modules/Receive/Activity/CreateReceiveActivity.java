package com.jsi.mbrana.Modules.Receive.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.CommonModels.VVMModel;
import com.jsi.mbrana.CommonUIComponents.mBranaNotification;
import com.jsi.mbrana.CommonUIComponents.mBranaProgressDialog;
import com.jsi.mbrana.DataAccessLayer.DataServiceAgent;
import com.jsi.mbrana.DataAccessLayer.DataServiceInterface;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Modules.Receive.Adapter.CreateReceiptInvoiceAdapter;
import com.jsi.mbrana.Modules.Receive.Helper.Status.StatusHelperModel;
import com.jsi.mbrana.Modules.Receive.Helper.WorkFlow.WorkFlowModel;
import com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice.ReceiveInvoiceModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveDetailModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;
import com.jsi.mbrana.Modules.UserManagement.LoginActivity;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Preferences.PreferenceKey;
import com.jsi.mbrana.Preferences.PreferenceManager;
import com.jsi.mbrana.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateReceiveActivity extends AppCompatActivity {
    RecyclerView NewReceiveList;
    Button saveAsDraftButton;
    Button submitButton;
    CheckBox IsCampain;
    Tracker _mTracker;
    PreferenceManager preference;
    DataServiceInterface dataService;
    mBranaNotification notification;
    mBranaProgressDialog progressDialog;
    int ReceiveInvoiceId, ReceiveId;
    ReceiveInvoiceModel receiveInvoiceModelData;
    ReceiveModel receiveModelData;
    List<VVMModel> vvmModelData;
    WorkFlowModel workFlowModel = new WorkFlowModel();
    boolean hasPreviousReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createreceive);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Create Receive");
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

        //Has Previous Receive
        hasPreviousReceive = getIntent().getBooleanExtra("hasPreviousReceive", false);

        //Recycler View
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        NewReceiveList = (RecyclerView) findViewById(R.id.NewReceiveList);
        NewReceiveList.addItemDecoration(new SimpleDividerItemDecoration(this));
        NewReceiveList.setLayoutManager(mLayoutManager);
        NewReceiveList.setItemAnimator(new DefaultItemAnimator());

        IsCampain = (CheckBox) findViewById(R.id.isCampinCheckBoxNewReceipt);
        if (!preference.getBoolean(PreferenceKey.isMultipleActivityAllowed)) //HIde for case of Malaria
            IsCampain.setVisibility(View.GONE);

        saveAsDraftButton = (Button) findViewById(R.id.SaveAsDraftRIButton);
        saveAsDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Save Draft")
                        .setLabel("Save Receipt as draft")
                        .build());
                if (receiveInvoiceModelData != null) {
                    //Cast
                    receiveModelData = new ReceiveModel();
                    receiveModelData.setId(0);
                    receiveModelData.setReceiveInvoiceId(receiveInvoiceModelData.getId());
                    receiveModelData.setEnvironmentId(receiveInvoiceModelData.getEnvironmentId());
                    receiveModelData.setActivityId(receiveInvoiceModelData.getActivityId());
                    receiveModelData.setReceiveStatusCode(StatusHelperModel.ReceiveStatus.DRAFT.getStatusCode());

                    ArrayList<ReceiveDetailModel> receiveDetailModel = new ArrayList<>();
                    for (int y = 0; y < receiveInvoiceModelData.getReceiveInvoiceDetail().size(); y++) {
                        ReceiveDetailModel model = new ReceiveDetailModel();
                        model.setItemUnitId(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getItemUnitId());
                        model.setFullItemName(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getFullItemName());
                        model.setItemName(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getItemName());
                        model.setUnitOfIssue(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getUnitOfIssue());
                        model.setManufacturerId(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getManufacturerId());
                        model.setBatchNumber(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getBatchNumber());
                        model.setQuantity(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getQuantity());

                        receiveDetailModel.add(model);
                    }
                    receiveModelData.setReceiveDetail(receiveDetailModel);

                    //Workflow
                    workFlowModel.setAction("Save");

                    saveReceive(receiveModelData);
                }
            }
        });

        submitButton = (Button) findViewById(R.id.SubmitRIButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Submit")
                        .setLabel("Submit Receipt")
                        .build());
                if (receiveInvoiceModelData != null) {
                    //Cast
                    receiveModelData = new ReceiveModel();
                    receiveModelData.setId(0);
                    receiveModelData.setReceiveInvoiceId(receiveInvoiceModelData.getId());
                    receiveModelData.setEnvironmentId(receiveInvoiceModelData.getEnvironmentId());
                    receiveModelData.setActivityId(receiveInvoiceModelData.getActivityId());
                    receiveModelData.setReceiveStatusCode(StatusHelperModel.ReceiveStatus.SUBMITTED.getStatusCode());

                    ArrayList<ReceiveDetailModel> receiveDetailModel = new ArrayList<>();
                    for (int y = 0; y < receiveInvoiceModelData.getReceiveInvoiceDetail().size(); y++) {
                        ReceiveDetailModel model = new ReceiveDetailModel();
                        model.setItemUnitId(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getItemUnitId());
                        model.setFullItemName(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getFullItemName());
                        model.setItemName(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getItemName());
                        model.setUnitOfIssue(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getUnitOfIssue());
                        model.setManufacturerId(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getManufacturerId());
                        model.setBatchNumber(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getBatchNumber());
                        model.setQuantity(receiveInvoiceModelData.getReceiveInvoiceDetail().get(y).getQuantity());

                        receiveDetailModel.add(model);
                    }
                    receiveModelData.setReceiveDetail(receiveDetailModel);

                    //Workflow
                    workFlowModel.setAction("Submit");

                    saveReceive(receiveModelData);
                }
            }
        });

        getReceiveInvoiceById();

        getVVMS();
    }

    private void getVVMS() {
        progressDialog.showProgressDialog();
        try {
            dataService.GetVVMs().enqueue(new Callback<List<VVMModel>>() {
                @Override
                public void onResponse(Call<List<VVMModel>> call, retrofit2.Response<List<VVMModel>> response) {
                    progressDialog.hideProgressDialog();
                    //Response from Authenticate
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Convert Model
                            vvmModelData = response.body();

                            //Populate Data
                            populateData(vvmModelData, receiveInvoiceModelData);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(CreateReceiveActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 406) {
                        notification.showHttp406Notification();
                    }
                }

                @Override
                public void onFailure(Call<List<VVMModel>> call, Throwable t) {
                    progressDialog.hideProgressDialog();
                    notification.showFailureNotification(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReceiveInvoiceById() {
        progressDialog.showProgressDialog();
        try {
            dataService.GetReceiveInvoiceById(preference.getInt(PreferenceKey.EnvironmentId), ReceiveInvoiceId).enqueue(new Callback<ReceiveInvoiceModel>() {
                @Override
                public void onResponse(Call<ReceiveInvoiceModel> call, retrofit2.Response<ReceiveInvoiceModel> response) {
                    progressDialog.hideProgressDialog();
                    //Response
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Convert Model
                            receiveInvoiceModelData = response.body();

                            //Populate Data
                            populateData(vvmModelData, receiveInvoiceModelData);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(CreateReceiveActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 406) {
                        notification.showHttp406Notification();
                    }
                }

                @Override
                public void onFailure(Call<ReceiveInvoiceModel> call, Throwable t) {
                    progressDialog.hideProgressDialog();
                    notification.showFailureNotification(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateData(List<VVMModel> vvmModelData, ReceiveInvoiceModel receiveInvoiceModelData) {
        if (vvmModelData != null && receiveInvoiceModelData != null) {
            CreateReceiptInvoiceAdapter adapter = new CreateReceiptInvoiceAdapter(this, vvmModelData, receiveInvoiceModelData, NewReceiveList);
            NewReceiveList.setAdapter(adapter);
        }
    }

    private void saveReceive(final ReceiveModel model) {
        progressDialog.showProgressDialog();
        try {
            dataService.SaveReceiveInvoice(preference.getInt(PreferenceKey.EnvironmentId), ReceiveInvoiceId, model).enqueue(new Callback<ReceiveModel>() {
                @Override
                public void onResponse(Call<ReceiveModel> call, retrofit2.Response<ReceiveModel> response) {
                    progressDialog.hideProgressDialog();
                    //Response from Authenticate
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Cast
                            receiveModelData = response.body();

                            //ReceiveId
                            ReceiveId = receiveModelData.getId();

                            //Workflow
                            saveWorkFlow(workFlowModel);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(CreateReceiveActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 406) {
                        notification.showHttp406Notification();
                    }
                }

                @Override
                public void onFailure(Call<ReceiveModel> call, Throwable t) {
                    progressDialog.hideProgressDialog();
                    notification.showFailureNotification(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveWorkFlow(final WorkFlowModel model) {
        progressDialog.showProgressDialog();
        try {
            dataService.setReceiveWorkFlow(preference.getInt(PreferenceKey.EnvironmentId), ReceiveInvoiceId, ReceiveId, model).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    progressDialog.hideProgressDialog();
                    //Response from Authenticate
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Notification
                            notification.showSuccessNotification("Successful!");

                            //Route
                            if (workFlowModel.getAction().equals("Submit")) {
                                Intent intent = new Intent(CreateReceiveActivity.this, ConfirmReceiveActivity.class);
                                intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                                intent.putExtra("ReceiveId", receiveModelData.getId());
                                intent.putExtra("ReceiveStatusCode", receiveModelData.getReceiveStatusCode());
                                startActivity(intent);
                            } else if (workFlowModel.getAction().equals("Save")) {
                                Intent intent = new Intent(CreateReceiveActivity.this, ReceiveMenuActivity.class);
                                intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                                startActivity(intent);
                            }
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(CreateReceiveActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 406) {
                        notification.showHttp406Notification();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    progressDialog.hideProgressDialog();
                    notification.showFailureNotification(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", this.getLocalClassName());
        _mTracker.setScreenName("Receipts: Create Receipt");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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
                Intent intent = new Intent(CreateReceiveActivity.this, ReceiveMenuActivity.class);
                intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                startActivity(intent);
                return true;
            case R.id.action_home:
                Intent i = new Intent(CreateReceiveActivity.this, MainNavigationActivity.class);
                i.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    public void onBackPressed() {
        if (hasPreviousReceive) {
            Intent intent = new Intent(this, ReceiveMenuActivity.class);
            intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ShipmentsMenuActivity.class);
            startActivity(intent);
        }
    }
}