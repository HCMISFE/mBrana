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
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.CommonModels.VVMModel;
import com.jsi.mbrana.CommonUIComponents.mBranaNotification;
import com.jsi.mbrana.CommonUIComponents.mBranaProgressDialog;
import com.jsi.mbrana.DataAccessLayer.DataServiceAgent;
import com.jsi.mbrana.DataAccessLayer.DataServiceInterface;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Modules.Receive.Adapter.UpdateReceiveAdapter;
import com.jsi.mbrana.Modules.Receive.Helper.WorkFlow.WorkFlowModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;
import com.jsi.mbrana.Modules.UserManagement.LoginActivity;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Preferences.PreferenceKey;
import com.jsi.mbrana.Preferences.PreferenceManager;
import com.jsi.mbrana.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class UpdateReceiveActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button updateDraftButton, submitButton, cancelButton;
    TextView IsCampain;
    Tracker _mTracker;
    PreferenceManager preference;
    DataServiceInterface dataService;
    mBranaNotification notification;
    mBranaProgressDialog progressDialog;
    List<VVMModel> vvmModelData;
    int ReceiveId, ReceiveInvoiceId;
    ReceiveModel receiveModelData;
    WorkFlowModel workFlowModel = new WorkFlowModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatereceive);

        //Toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Update Receive");
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

        //ReceiveId
        ReceiveId = getIntent().getIntExtra("ReceiveId", 0);

        //ReceiveInvoiceId
        ReceiveInvoiceId = getIntent().getIntExtra("ReceiveInvoiceId", 0);

        //RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        //update button
        updateDraftButton = (Button) findViewById(R.id.UpdateDraftRIButton);

        //submit button
        submitButton = (Button) findViewById(R.id.SubmitUpdateRIButton);

        //cancel button
        cancelButton = (Button) findViewById(R.id.UpdateDraftRIButton_Cancel);

        //Campain
        IsCampain = (TextView) findViewById(R.id.isCampinCheckBoxUpdateReceipt);

        //
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Cancel")
                        .setLabel("Delete Receipt")
                        .build());
                //Workflow
                workFlowModel.setAction("Cancel");

                //Receive
                updateReceive(receiveModelData);
            }
        });

        //
        updateDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Update")
                        .setLabel("Update Receipt")
                        .build());
                //Workflow
                workFlowModel.setAction("");

                //Receive
                updateReceive(receiveModelData);
            }
        });

        //
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Submit")
                        .setLabel("Submit Receipt")
                        .build());
                //Workflow
                workFlowModel.setAction("Submit");

                //Receive
                updateReceive(receiveModelData);
            }
        });

        //VVM API
        getVVMS();

        //Receive API
        getReceiveData();
    }

    public void getReceiveData() {
        progressDialog.hideProgressDialog();
        progressDialog.showProgressDialog();
        try {
            dataService.GetReceive(preference.getInt(PreferenceKey.EnvironmentId), ReceiveInvoiceId, ReceiveId).enqueue(new Callback<ReceiveModel>() {
                @Override
                public void onResponse(Call<ReceiveModel> call, retrofit2.Response<ReceiveModel> response) {
                    progressDialog.hideProgressDialog();
                    //Response
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Convert Model
                            receiveModelData = response.body();

                            //Populate
                            populateData(vvmModelData, receiveModelData);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(UpdateReceiveActivity.this, LoginActivity.class);
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

    private void getVVMS() {
        progressDialog.hideProgressDialog();
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

                            //Populate
                            populateData(vvmModelData, receiveModelData);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(UpdateReceiveActivity.this, LoginActivity.class);
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

    private void populateData(List<VVMModel> vvmModel, ReceiveModel receiveModel) {
        if (vvmModel != null && receiveModel != null) {
            UpdateReceiveAdapter adapter = new UpdateReceiveAdapter(this, receiveModel, vvmModel);
            recyclerView.setAdapter(adapter);
        }
    }

    private void updateReceive(final ReceiveModel model) {
        progressDialog.showProgressDialog();
        try {
            dataService.UpdateReceiveInvoice(preference.getInt(PreferenceKey.EnvironmentId), ReceiveInvoiceId, ReceiveId, model).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    progressDialog.hideProgressDialog();
                    //Response from Authenticate
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Workflow
                            saveWorkFlow(workFlowModel);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(UpdateReceiveActivity.this, LoginActivity.class);
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

    private void saveWorkFlow(final WorkFlowModel model) {
        progressDialog.showProgressDialog();
        try {
            dataService.setReceiveWorkFlow(preference.getInt(PreferenceKey.EnvironmentId), ReceiveInvoiceId, ReceiveId, model).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    progressDialog.hideProgressDialog();
                    //Response
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            //Notification
                            notification.showSuccessNotification("Successful");

                            //Route
                            if (workFlowModel.getAction().equals("Submit")) {
                                Intent intent = new Intent(UpdateReceiveActivity.this, ConfirmReceiveActivity.class);
                                intent.putExtra("ReceiveStatusCode", receiveModelData.getReceiveStatusCode());
                                intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                                intent.putExtra("ReceiveId", ReceiveId);
                                startActivity(intent);
                            } else if (workFlowModel.getAction().equals("Cancel")) {
                                Intent intent = new Intent(UpdateReceiveActivity.this, ReceiveMenuActivity.class);
                                intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                                intent.putExtra("ReceiveId", ReceiveId);
                                startActivity(intent);
                            }
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(UpdateReceiveActivity.this, LoginActivity.class);
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
        Log.d("GA-Screen", "Receipts: Edit Receipt");
        _mTracker.setScreenName("Receipts: Edit Receipt");
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
        if (items == null)
            return false;
        switch (items.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(UpdateReceiveActivity.this, ReceiveMenuActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_home:
                Intent i = new Intent(UpdateReceiveActivity.this, MainNavigationActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ReceiveMenuActivity.class);
        intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
        startActivity(intent);
    }
}
