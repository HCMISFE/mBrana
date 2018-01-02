package com.jsi.mbrana.Modules.Receive.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.Modules.Receive.Helper.WorkFlow.WorkFlowModel;
import com.jsi.mbrana.CommonUIComponents.mBranaNotification;
import com.jsi.mbrana.CommonUIComponents.mBranaProgressDialog;
import com.jsi.mbrana.DataAccessLayer.DataServiceAgent;
import com.jsi.mbrana.DataAccessLayer.DataServiceInterface;
import com.jsi.mbrana.Helpers.SimpleDividerItemDecoration;
import com.jsi.mbrana.Modules.Receive.Adapter.ConfirmReceiveAdapter;
import com.jsi.mbrana.Modules.Receive.Helper.Status.StatusHelperModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;
import com.jsi.mbrana.Modules.UserManagement.LoginActivity;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Preferences.PreferenceKey;
import com.jsi.mbrana.Preferences.PreferenceManager;
import com.jsi.mbrana.R;

import retrofit2.Call;
import retrofit2.Callback;

public class ConfirmReceiveActivity extends AppCompatActivity {
    ReceiveModel ReceiveData;
    RecyclerView recyclerView;
    Button btnDraft, btnConfirm, btnVoid, btnPrint;
    WifiManager wifiManager;
    TextView IsCampain, password_error;
    Tracker _mTracker;
    PreferenceManager preference;
    DataServiceInterface dataService;
    mBranaNotification notification;
    mBranaProgressDialog progressDialog;
    int ReceiveId, ReceiveInvoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_receive);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Confirm Receive");
            ab.setSubtitle("PlaceHolder");
        }

        //Preference
        preference = new PreferenceManager(this);

        //DataService
        dataService = DataServiceAgent.getDataService(this);

        //Notification
        notification = new mBranaNotification(this, findViewById(android.R.id.content));

        //ProgressDialog
        progressDialog = new mBranaProgressDialog(this);

        //
        ReceiveId = getIntent().getIntExtra("ReceiveId", 0);

        //
        ReceiveInvoiceId = getIntent().getIntExtra("ReceiveInvoiceId", 0);

        //
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //
        IsCampain = (TextView) findViewById(R.id.IsCampaignText);

        //
        password_error = (TextView) findViewById(R.id.password_footer);

        //
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.PrintGRNFReceiptInvloceListDetail_TableContainer);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //draft button
        btnDraft = (Button) findViewById(R.id.ReturnToDraftGRNFDraftRIButton);

        //void button
        btnVoid = (Button) findViewById(R.id.VoidDraftRIButton);

        //print button
        btnPrint = (Button) findViewById(R.id.SubmitPrintRIButton);

        //submit button
        btnConfirm = (Button) findViewById(R.id.ConfirmPrintGRNFRIButton);

        //handle button visibility
        if (getIntent().getStringExtra("ReceiveStatusCode").equals(StatusHelperModel.ReceiveStatus.CONFIRMED.getStatusCode())) {
            //
            btnVoid.setVisibility(View.VISIBLE);
            btnPrint.setVisibility(View.VISIBLE);

            //
            btnConfirm.setVisibility(View.GONE);
            btnDraft.setVisibility(View.GONE);

            //
            if (ab != null) {
                ab.setTitle("Receipt Confirmed");
            }
        } else if (getIntent().getStringExtra("ReceiveStatusCode").equals(StatusHelperModel.ReceiveStatus.SUBMITTED.getStatusCode())) {
            //
            btnVoid.setVisibility(View.GONE);
            btnPrint.setVisibility(View.GONE);

            //
            btnConfirm.setVisibility(View.VISIBLE);
            btnDraft.setVisibility(View.VISIBLE);

            //
            if (ab != null) {
                ab.setTitle("Receipt Confirmation");
            }
        }

        btnVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Cancel")
                        .setLabel("Delete Receipt")
                        .build());
                WorkFlowModel model = new WorkFlowModel();
                model.setAction("Void");
                setWorkFlow(model);
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Print")
                        .setLabel("Print Receipt")
                        .setValue(1)
                        .build());
//                if (item == null)
//                    return;
//                //Print
//                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//                NetworkInfo wifi = null;
//                if (cm != null) {
//                    wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//                }
//
//                if (!wifiManager.isWifiEnabled()) {
//                    wifiManager.setWifiEnabled(true);
//                    startActivityForResult(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 1);
//                } else {
//                    if (wifi != null) {
//                        if (wifi.isConnected()) {
//                            // doWebViewPrint();
//                            Intent intent = new Intent(ConfirmReceiveActivity.this, ReceiveMenuActivity.class);
//                            intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
//                            startActivity(intent);
//                        } else {
//                            startActivityForResult(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 1);
//                        }
//                    }
//                }
            }
        });

        btnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Return")
                        .setLabel("Return Receipt to Draft")
                        .build());
                WorkFlowModel model = new WorkFlowModel();
                model.setAction("ReturnToDraft");
                setWorkFlow(model);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Confirm")
                        .setLabel("Confirm Receipt")
                        .build());
                WorkFlowModel model = new WorkFlowModel();
                model.setAction("Confirm");
                setWorkFlow(model);
            }
        });

        GetReceive();
    }

    public void GetReceive() {
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
                            ReceiveData = response.body();

                            ConfirmReceiveAdapter adapter = new ConfirmReceiveAdapter(ReceiveData);
                            recyclerView.setAdapter(adapter);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(ConfirmReceiveActivity.this, LoginActivity.class);
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

    private void setWorkFlow(final WorkFlowModel model){
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
                            Intent intent = new Intent(ConfirmReceiveActivity.this, ReceiveMenuActivity.class);
                            intent.putExtra("ReceiveInvoiceId", ReceiveInvoiceId);
                            startActivity(intent);
                        }
                    } else if (response.code() == 400) {
                        notification.showHttp400Notification();
                    } else if (response.code() == 401) {
                        notification.showHttp401Notification();
                        Intent intent = new Intent(ConfirmReceiveActivity.this, LoginActivity.class);
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
        Log.d("GA-Screen", "Receipts: Receipt Confirmed");
        _mTracker.setScreenName("Receipts: Receipt Confirmed");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        if (items == null)
            return false;
        switch (items.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(ConfirmReceiveActivity.this, ReceiveMenuActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_home:
                Intent i = new Intent(ConfirmReceiveActivity.this, MainNavigationActivity.class);
                startActivity(i);
                return true;

        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi.isConnected()) {
                // doWebViewPrint();
            } else {
                new android.app.AlertDialog.Builder(ConfirmReceiveActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.notConnectedToWifiMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        }
    }

//    private void doWebViewPrint() {
//        // Create a WebView object specifically for printing
//        WebView webView = new WebView(ConfirmReceiveActivity.this);
//        webView.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                createWebPrintJob(view);
//                mWebView = null;
//            }
//        });
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
//        Date ModifiedDate = null;
//
//        try {
//            ModifiedDate = simpleDateFormat.parse(ReceiptInvoicesMaster.get(0).getModifiedDate().replace("T", " "));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            _mTracker.send(new HitBuilders.ExceptionBuilder()
//                    .setDescription(new StandardExceptionParser(getApplicationContext(), null)
//                            .getDescription(Thread.currentThread().getName(), e))
//                    .setFatal(false)
//                    .build());
//        }
//
//        String ModifiedDateStringValue = "";
//        if (ModifiedDate != null)
//            ModifiedDateStringValue = EthiopianDateConversion.GregorianDateToEthiopianDate_IssueDateFormat(ModifiedDate);
//
//        String htmlDocument = "<div align='center'><img src='file:///android_res/drawable/pfsalogo.png' alt='' /></div>" +
//                "<h3 align='center'><span style='text-decoration: underline;'>" + GlobalVariables.getSelectedEnvironment() + " </span></h3>" +
//                "<h3 align='center'><span style='text-decoration: underline;'> Goods Receiving Notification Form  </span></h3>" +
//                "<div>" +
//                "<div>" +
//                "<div style='width:30%;display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;'><label style='    font-size: 10px;font-weight: 600;'> Supplier </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> " + ReceiptInvoicesMaster.get(0).getSupplier() + " </label></div>" +
//                "  " +
//                "<div style='display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;'><label style='font-size: 10px;font-weight: 600;'>GRNF No. </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> " + ReceiptInvoicesMaster.get(0).getGRNF() + " </label></div>" +
//                "" +
//                "  <div style='float:right;display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;' align='right'><label style='    font-size: 10px;font-weight: 600;'>Receive Date </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> " + ModifiedDateStringValue + " </label></div>" +
//                "</div>" +
//                "<div>" +
//                "  " +
//                "<div style='width:30%;display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;'><label style='    font-size: 10px;font-weight: 600;'> Printed Date </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> " + EthiopianDateConversion.GregorianDateToEthiopianDate_IssueDateFormat(Calendar.getInstance().getTime()) + " </label></div>" +
//                "</div>" +
//                "  " +
//                "</div>" +
//                "<br /><br />" +
//                "   <div> <div><table cellspacing='0' class='mce-item-table' style=' border: none; '>" +
//                "<tbody style=' border: none; '>" +
//                "<tr style=' border: none; background: whitesmoke;'>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9; border-top: none; width: 5%; '>No.</td>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 30%;'> Item Name</td>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Unit</td>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Manufacturer</td>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Invoiced Qty</td>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Received Qty</td>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Batch No</td>" +
//                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Expiry Date</td>" +
//                "</tr>";
//        String tablebody = "";
//        for (int i = 0; i < ReceiptInvoices.size(); i++) {
//            Date expiry = null;
//            try {
//                expiry = simpleDateFormat.parse(ReceiptInvoices.get(0).getExpDate().replace("T", " "));
//            } catch (ParseException e) {
//                e.printStackTrace();
//                _mTracker.send(new HitBuilders.ExceptionBuilder()
//                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
//                                .getDescription(Thread.currentThread().getName(), e))
//                        .setFatal(false)
//                        .build());
//            }
//
//            tablebody += "<tr style='" +
//                    "    border: none;" +
//                    "    background: white; '>" +
//                    "<td style=' font-size: 9px;border: 1px solid #b9b9b9;border-top: none; width: 5%; '>" + (i + 1) + "</td>" +
//
//                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9; border-left: none; border-top: none; width: 30%; '>" +
//                    ReceiptInvoices.get(i).getFullItemName() + "</td>" +
//
//                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
//                    ReceiptInvoices.get(i).getUnit() + "</td>" +
//
//                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
//                    ReceiptInvoices.get(i).getManufacturer() + "</td>" +
//
//                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
//                    NumberFormat.getNumberInstance(Locale.US).format(ReceiptInvoices.get(i).getInvoicedNoOfPack()) + "</td>" +
//
//                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
//                    NumberFormat.getNumberInstance(Locale.US).format(ReceiptInvoices.get(i).getQuantity()) + "</td>" +
//
//                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
//                    ReceiptInvoices.get(i).getBatchNo() + "</td>" +
//
//                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
//                    new SimpleDateFormat("MM-yyyy").format(expiry) + "</td>" +
//                    "</tr>";
//        }
//        htmlDocument += tablebody;
//        String footer = "<div style='font-size: 9px;'>" +
//                "<div> " +
//                "<table> <tbody>" +
//                " <tr>" +
//                "<td style='font-size: 9px; width: 25%; border:none;'>Prepared By: .......................................</td>" +
//                "<td style='font-size: 9px; width: 25%;border:none;'>Deliverd By: .......................................</td>" +
//                "<td style='font-size: 9px; width: 25%;border:none;'>Confirmed By: .......................................</td>" +
//                "<td style='font-size: 9px; width: 25%;border:none;'>Received By: .......................................</td>" +
//                "</tr>" +
//                "<tr> <td style='font-size: 9px; width: 25%;border:none;'>Signature: ..............................................</td>" +
//                "<td style='font-size: 9px; width: 25%;border:none;'> .............................................................</td>" +
//                "<td style='font-size: 9px; width: 25%;border:none;'> .............................................................</td>" +
//                "<td style='font-size: 9px; width: 25%;border:none;'> .............................................................</td>" +
//                "</tr>" +
//                " </tbody> </table> </div>";
//
//        htmlDocument += "</tbody></table></div></div> <br><br>" + footer;
//        String x = htmlDocument;
//        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
//
//        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
//        // to the PrintManager
//        mWebView = webView;
//    }

    private void createWebPrintJob(WebView webView) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) ConfirmReceiveActivity.this
                .getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";

        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
}