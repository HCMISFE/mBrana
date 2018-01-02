package com.jsi.mbrana.Workflow.Issue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.jsi.mbrana.Workflow.Adapter.Issue.IssueConfirmationAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.EthiopianDateConversion.EthiopianDateConversion;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Models.ApiIssueModel;
import com.jsi.mbrana.Models.ApiVoidIssueModel;
import com.jsi.mbrana.Models.IssueHeaderModel;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PrintIssueActivity extends Activity implements IDataNotification {
    TextView stvNoTV, refNoTV, facilityTV, password_error;
    ListView itemTable;
    OrderModel Header;
    Button printBtn;
    Button voidBtn;
    ProgressDialog progress;
    boolean fromPendingActivity = false;
    ArrayList<ApiIssueModel> issues = new ArrayList<>();
    IssueConfirmationAdapter issueConfirmationAdapter;
    WifiManager wifiManager;
    int actionType = -1;
    WebView mWebView;
    Tracker _mTracker;
    boolean isProgressVisible = false;
    SharedPreferences prefs_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_confirmation);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        setTitle("Issued Invoice");

        prefs_users = getSharedPreferences("userCredentials", 0);

        progress = new ProgressDialog(this);

        stvNoTV = (TextView) findViewById(R.id.issueconf_STVNO);
        refNoTV = (TextView) findViewById(R.id.issueconf_RefNO);
        facilityTV = (TextView) findViewById(R.id.issueconf_facility);
        itemTable = (ListView) findViewById(R.id.issueconf_ItemTable);
        printBtn = (Button) findViewById(R.id.issueconf_printbtn);
        voidBtn = (Button) findViewById(R.id.issueconf_Voidbtn);
        password_error = (TextView) findViewById(R.id.password_footer);
        Header = (OrderModel) getIntent().getSerializableExtra("Order");
        issues = (ArrayList<ApiIssueModel>) getIntent().getSerializableExtra("Issues");

        if (issues != null) {
            fromPendingActivity = false;
            if (issues.size() > 0) {
                stvNoTV.setText(Html.fromHtml("<b>STV No. </b>" + issues.get(0).getIssNo()));
                refNoTV.setText(Html.fromHtml("<b>Ref No. </b>" + issues.get(0).getRefNO()));
                if (Header != null)
                    facilityTV.setText(Html.fromHtml("<b>To </b>" + Header.getFacility()));
                issueConfirmationAdapter = new IssueConfirmationAdapter(PrintIssueActivity.this, R.layout.layout_issueconfirmation, issues);
                itemTable.setAdapter(issueConfirmationAdapter);
            }
        } else {
            fromPendingActivity = true;

            if (getIntent().getIntExtra("IssueID", 0) != 0) {
                new DataServiceTask(this, 6, false).execute("Issue/GetIssueDetail?IssueID=" + getIntent().getIntExtra("IssueID", 0)
                        + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode() + "&ActivityCode=VWC", "");
            } else {
                new DataServiceTask(this, 6, false).execute("Issue/GetIssueDetail?OrderID=" + Header.getID()
                        + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode() + "&ActivityCode=VWC", "");
            }
        }

        printBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Print")
                        .setLabel("Print Issue")
                        .setValue(1)
                        .build());
                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                    startActivityForResult(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 1);
                } else {
                    if (wifi.isConnected()) {
                        doWebViewPrint();
                    } else {
                        startActivityForResult(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 1);
                    }
                }
            }
        });


        voidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("Void")
                        .setLabel("Void Issue")
                        .build());
                ApiVoidIssueModel voidIssueModel = new ApiVoidIssueModel();
                IssueHeaderModel issueHeaderModel = new IssueHeaderModel();

                if (issues != null && issues.size() > 0) {
                    issueHeaderModel.setID(issues.get(0).getSTVID());
                    issueHeaderModel.setEnvironmentID(GlobalVariables.getSelectedEnvironmentID());
                    issueHeaderModel.setEnvironmentCode(GlobalVariables.getSelectedEnvironmentCode());
                    issueHeaderModel.setOrderID(issues.get(0).getOrderID());
                }

                voidIssueModel.setIssue(issueHeaderModel);
                voidIssueModel.setIssueDetail(issues);
                Gson gson = new Gson();
                final String itemstring = gson.toJson(voidIssueModel);

                new AlertDialog.Builder(PrintIssueActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.voidMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final View dialogView;
                                final EditText entered_password;
                                final String the_password = prefs_users.getString("Password", "");

                                final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(PrintIssueActivity.this);
                                LayoutInflater inflater = PrintIssueActivity.this.getLayoutInflater();
                                dialogView = inflater.inflate(R.layout.enter_password, null);
                                dialogBuilder.setView(dialogView);
                                entered_password = (EditText) dialogView.findViewById(R.id.enter_password);
                                dialogBuilder.setTitle("Enter password");
                                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String entered_text = entered_password.getText().toString();
                                        try {
                                            if (the_password.equals(entered_text)) {
                                                handelDataSave(itemstring, "Issue/VoidIssue", 7);
                                                actionType = 3;
                                            } else {
                                                password_error.setText("Wrong Password!");
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        password_error.setText("");
                                                    }
                                                }, 3000);
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
                                });
                                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                });
                                android.support.v7.app.AlertDialog b = dialogBuilder.create();
                                b.show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Issues: Issue Confirmation");
        _mTracker.setScreenName("Issues: Issue Confirmation");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void handelDataSave(String objString, String api, int requestcode) {
        new DataServiceTaskForObjects(this, objString, requestcode).execute(api, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi.isConnected()) {
                doWebViewPrint();
            } else {
                new AlertDialog.Builder(PrintIssueActivity.this)
                        .setMessage(Html.fromHtml(getString(R.string.notConnectedToWifiMessage)))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // startActivityForResult(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 1);
                            }
                        })
                        .show();
            }
        }
    }

    private void doWebViewPrint() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date printedDate = null;

        try {
            printedDate = simpleDateFormat.parse(issues.get(0).getPrintedDate().replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build());
        }

        String printedDateStringValue = "";
        if (printedDate != null)
            printedDateStringValue = EthiopianDateConversion.GregorianDateToEthiopianDate_IssueDateFormat(printedDate);

        // Create a WebView object specifically for printing
        WebView webView = new WebView(PrintIssueActivity.this);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        // Generate an HTML document on the fly:
        // String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
        // "testing, testing...</p></body></html>";
        String htmlDocument = "<div align='center'><img src='file:///android_res/drawable/pfsalogo.png' alt='' /></div>" +
                "<h3 align='center'><span style='text-decoration: underline;'>" + GlobalVariables.getSelectedEnvironment() + " </span></h3>" +
                "<h3 align='center'><span style='text-decoration: underline;'>Issued Articles Or Property Issued  </span></h3>" +
                "<div>" +
                "<div>" +
                "<div style='width:30%;display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;'><label style='    font-size: 10px;font-weight: 600;'> From </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> " + GlobalVariables.getSelectedEnvironment() + " </label></div>" +
                "  " +
                "<div style='display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;'><label style='font-size: 10px;font-weight: 600;'>To </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'>" + Header.getFacility() + "</label></div>" +
                "" +
                "  <div style='float:right;display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;' align='right'><label style='    font-size: 10px;font-weight: 600;'>Issue Date </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> " + printedDateStringValue + "</label></div>" +
                "</div>" +
                "<div>" +
                "  " +
                "<div style='width:30%;display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;'><label style='    font-size: 10px;font-weight: 600;'> STV No. </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> E999 </label></div>" +
                "<div style='display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;'><label style='    font-size: 10px;font-weight: 600;'>Ref No. </label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> 7E2C</label></div>" +
                "  <div style='float:right;display: inline-block; margin-right: 55px; margin-top: 4px; margin-bottom: 4px;' align='right'><label style='    font-size: 10px;font-weight: 600;'>Printed Date</label> <label style='    font-size: 10px;border-bottom: 1px #c0c0c0; border-style: dotted; border-top: none; border-left: none; border-right: none; padding-right: 10px;'> " + EthiopianDateConversion.GregorianDateToEthiopianDate_IssueDateFormat(Calendar.getInstance().getTime()) + "</label></div>" +
                "</div>" +
                "  " +
                "</div>" +
                "<br /><br />" +
                "   <div> <div><table cellspacing='0' class='mce-item-table' style=' border: none; '>" +
                "<tbody style=' border: none; '>" +
                "<tr style=' border: none; background: whitesmoke;'>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9; border-top: none; width: 5%; '>No.</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 30%;'>Detailed Description of Articles or Property</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Manufacturer</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Unit</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Batch No</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Expiry Date</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Qty</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Unit Cost</td>" +
                "<td style=' font-size: 10px;border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>Total Cost</td>" +
                "</tr>";
        String tablebody = "";
        for (int i = 0; i < issues.size(); i++) {

            Date expiry = null;
            try {
                expiry = simpleDateFormat.parse(issues.get(0).getExpireDate().replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }

            tablebody += "<tr style='" +
                    "    border: none;" +
                    "    background: white; '>" +
                    "<td style=' font-size: 9px;border: 1px solid #b9b9b9;border-top: none; width: 5%; '>" + (i + 1) + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9; border-left: none; border-top: none; width: 30%; '>" +
                    issues.get(i).getFullItemName() + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                    issues.get(i).getManufacturer() + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                    issues.get(i).getUnit() + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                    issues.get(i).getBatchNo() + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                    new SimpleDateFormat("MM-yyyy").format(expiry) + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                    NumberFormat.getNumberInstance(Locale.US).format(issues.get(i).getQuantity()) + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                    NumberFormat.getNumberInstance(Locale.US).format(issues.get(i).getUnitCost()) + "</td>" +

                    "<td style=' font-size: 9px; border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                    NumberFormat.getNumberInstance(Locale.US).format(issues.get(i).getTotalCost()) + " </td>" +

                    "</tr>";
        }
        htmlDocument += tablebody;
        String footer = "<div style='font-size: 9px;'>" +
                "  <div style='    display: inline-block;'>  \tI certify that I have received the above mentioned goods in good condition " +
                "    </div>  <div  style='    display: inline-block;'>" +
                "        \tName:..............................................................................  Signature ....................................................................." +
                "        </div>  <div>" +
                "<div> <table> <tbody> <tr>" +
                "<td style='font-size: 9px; width: 10%; border:none;'><strong>Sender</strong></td>" +
                "<td style='font-size: 9px; width: 50%;border:none;'>Name</td>" +
                "<td style='font-size: 9px; width: 50%;border:none;'>Signature</td>" +
                "</tr>" +
                "<tr> <td style='font-size: 9px; width: 15%;border:none;'>Store Head</td>" +
                "<td style='font-size: 9px; width: 50%;border:none;'>........................................................</td>" +
                "<td style='font-size: 9px; width: 50%;border:none;'>........................................................</td>" +
                "</tr> </tbody> </table> </div>";

        htmlDocument += "</tbody></table></div></div> <br><br>" + footer;
        String x = htmlDocument;
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) PrintIssueActivity.this
                .getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";

        //  PrintAttributes.MediaSize mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PrintIssueActivity.this, IssueMenuActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        switch (requestCode) {
            case 6: {
                try {
                    issues = new ArrayList<ApiIssueModel>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        ApiIssueModel Issue = new ApiIssueModel();
                        Issue.setRefNO(jo.getString("RefNo"));
                        Issue.setIssNo(jo.getString("IssNo"));
                        Issue.setFullItemName(jo.getString("FullItemName"));
                        Issue.setProductCN(jo.getString("ProductCN"));
                        Issue.setExpireDate(jo.getString("ExpireDate"));
                        Issue.setBatchNo(jo.getString("BatchNo"));
                        Issue.setQuantity(jo.getInt("Quantity"));
                        Issue.setEnvironmentCode(jo.getString("EnvironmentCode"));
                        Issue.setEnvironmentID(jo.getInt("EnvironmentID"));
                        Issue.setUnit(jo.getString("Unit"));
                        Issue.setVVMID(jo.getInt("VVMID"));
                        Issue.setVVMCode(jo.getString("VVMCode"));
                        Issue.setManufacturer(jo.getString("ManufacturerSH"));
                        Issue.setUnitCost(jo.getInt("UnitCost"));
                        Issue.setTotalCost(jo.getInt("TotalCost"));
                        Issue.setID(jo.getInt("ID"));
                        Issue.setSTVID(jo.getInt("STVID"));
                        Issue.setPrintedDate(jo.getString("PrintedDate"));
                        Issue.setOrderID(jo.getInt("OrderId"));
                        Issue.setIDPrinted(jo.getString("IDPrinted"));
                        if (jo.getInt("IsVoided") == 1)
                            Issue.setVoided(true);
                        else
                            Issue.setVoided(false);
                        issues.add(Issue);
                    }

                    if (issues.get(0).getVoided())
                        voidBtn.setVisibility(View.GONE);

                    stvNoTV.setText(Html.fromHtml("<b>STV No. </b>" + issues.get(0).getIDPrinted()));
                    refNoTV.setText(Html.fromHtml("<b>Ref No. </b>" + issues.get(0).getRefNO()));
                    
                    if (Header != null)
                        facilityTV.setText(Html.fromHtml("<b>To </b>" + Header.getFacility()));

                    issueConfirmationAdapter = new IssueConfirmationAdapter(PrintIssueActivity.this, R.layout.layout_issueconfirmation, issues);
                    itemTable.setAdapter(issueConfirmationAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(getApplicationContext(), null)
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
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
        if (message.equalsIgnoreCase("Change is successful")) {
            if (actionType == 3) {
                Intent intent = new Intent(PrintIssueActivity.this, IssueMenuActivity.class);
                startActivity(intent);
                this.finish();
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
