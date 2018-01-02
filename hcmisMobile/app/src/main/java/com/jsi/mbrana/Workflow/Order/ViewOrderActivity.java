package com.jsi.mbrana.Workflow.Order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.PO.RequestedPOAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.POModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewOrderActivity extends AppCompatActivity implements IDataNotification {
    TextView dateTV;
    ArrayList<ItemModel> items = new ArrayList<>();
    private POModel headerPoModel;
    private ProgressDialog progress;
    private ListView itemsTable;
    private Tracker _mTracker;
    Button btn_print;
    WebView mWebView;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);

        btn_print = (Button) findViewById(R.id.order_print);

        dateTV = (TextView) findViewById(R.id.requestedPO_dateTv);
        headerPoModel = (POModel) getIntent().getSerializableExtra("Header");

        if (!headerPoModel.equals("null")) {
            getSupportActionBar().setTitle("VRF " + headerPoModel.getPONumber());
            getSupportActionBar().setSubtitle("Status " + headerPoModel.getPurchaseOrderStatus());

            btn_print.setVisibility(View.VISIBLE);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;

            try {
                date = simpleDateFormat.parse(headerPoModel.getModifiedDate().replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(this, null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }

            dateTV.setText(Html.fromHtml("<b>Date:</b> " + dateFormat.format(date)));

            // Load details
            new DataServiceTask(this, 1, false).execute("PurchaseOrder/GetPurchaseOrderDetail?PurchaseOrderID=" + headerPoModel.getID() + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

            // Initialize item table
            itemsTable = (ListView) findViewById(R.id.requestedPO_itemslistTable);
        }

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWebViewPrint(items);
            }
        });
    }

    private void doWebViewPrint(ArrayList<ItemModel> model) {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(this);
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

        // Generating the header
        String headerDocument = "<style xmlns=\"http://www.w3.org/1999/html\">" +
                "    table {" +
                "        border-collapse: collapse;" +
                "        width: 100%;" +
                "    }" +
                "" +
                "    th, td {" +
                "        border: 1px solid #ccc;" +
                "        padding: 10px;" +
                "    }" +
                "" +
                "    caption {" +
                "        font-size: x-large;" +
                "        font-weight: bold;" +
                "    }" +
                "</style>" +
                "" +
                "<table cellspacing='0'>" +
                "    <caption>Vaccine Request Form</caption>" +
                "    <caption>Federal Ministry of Health</caption>" +
                "" +
                "    <thead>" +
                "    <tr>" +
                "        <th colspan=\"1\">Region/Zone/Woreda</th>" +
                "        <th colspan=\"5\"> </th>" +
                "        <th colspan=\"2\">Level of cold chain</th>" +
                "        <th colspan=\"1\">Date of request:</th>" +
                "        <th colspan=\"1\"> </th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Name of cold store</th>" +
                "        <th colspan=\"5\"> </th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"1\">No. of months to supply(S):</th>" +
                "        <th colspan=\"1\"> </th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Responsible Person</th>" +
                "        <th colspan=\"5\"> </th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"2\" style=\"font-size: large\">For population catchment served</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Contact Address</th>" +
                "        <th colspan=\"5\"> </th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"1\">Births(BI):</th>" +
                "        <th colspan=\"1\"> </th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Telephone Number(s):</th>" +
                "        <th colspan=\"5\"> </th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"1\">Surviving Infants(SI):</th>" +
                "        <th colspan=\"1\"> </th>" +
                "    </tr>" +
                "    </thead>" +
                "" +
                "    <thead style=' border: none;'>" +
                "    <tr style=' border: none; background: whitesmoke;'>" +
                "        <th style='border: 1px solid #b9b9b9; border-top: none; width: 5%; '>" +
                "            #" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 30%;'>" +
                "            Antigen" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                "            Doses" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                "            Waste Factor" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                "            Target Coverage" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%;'>" +
                "            Beginning Balance" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                "            Quantity Received" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                "            Current Balance" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                "            Required for Next Supply Period" +
                "        </th>" +
                "        <th style='border: 1px solid #b9b9b9;border-left: none; border-top: none; width: 10%; '>" +
                "            Quantity Requested" +
                "        </th>" +
                "    </tr>" +
                "    </thead>" +
                "" +
                "    <tbody>";

        // Generating the body
        String bodyDocument = "";
        for (int i = 0; i < model.size(); i++) {
            bodyDocument += "<tr>" +
                    "<td>" + (i + 1) + "." + "</td>" +
                    "<td>" + model.get(i).getItemCode() + "</td>" +
                    "<td>" + model.get(i).getDose() + "</td>" +
                    "<td>" + model.get(i).getWasteFactor() + "</td>" +
                    "<td>" + model.get(i).getTargetCoverage() + "</td>" +
                    "<td>" + model.get(i).getBeginningBalance() + "</td>" +
                    "<td>" + model.get(i).getQuantityReceived() + "</td>" +
                    "<td>" + model.get(i).getEndingBalance() + "</td>" +
                    "<td>" + model.get(i).getRequiredForNextSupplyPeriod() + "</td>" +
                    "<td>" + model.get(i).getRequestedQuantity() + "</td>"
                    + "</tr>";
        }

        // Generating the footer
        String footerDocument = " </tbody>" +
                "</table>" +
                "" +
                "<table cellspacing='0' style=\"width: 50%; margin-top: 25px\">" +
                "    <thead style=' border: none;'>" +
                "    <tr style='background: whitesmoke;'>" +
                "        <th colspan=\"1\"" +
                "            style='border: 1px solid #b9b9b9; width: 10%;'>" +
                "            Equipment Monitoring" +
                "        </th>" +
                "        <th colspan=\"3\"" +
                "            style='border: 1px solid #b9b9b9; width: 10%;'>" +
                "            No. of Units" +
                "        </th>" +
                "        <th colspan=\"2\"" +
                "            style='border: 1px solid #b9b9b9; width: 10%;'>No." +
                "            Temperature Excursions" +
                "        </th>" +
                "    </tr>" +
                "    </thead>" +
                "    <tbody>" +
                "    <tr>" +
                "        <td>Type of Fridge</td>" +
                "        <td>F</td>" +
                "        <td>NF</td>" +
                "        <td>FT*</td>" +
                "        <td>< 0°C</td>" +
                "        <td>> 8°C</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td>Cold Rooms</td>" +
                "        <td>4</td>" +
                "        <td></td>" +
                "        <td>Y</td>" +
                "        <td>0</td>" +
                "        <td>0</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td>Refrigerators</td>" +
                "        <td>2</td>" +
                "        <td></td>" +
                "        <td>Y</td>" +
                "        <td>0</td>" +
                "        <td>0</td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td>Freezers</td>" +
                "        <td>3</td>" +
                "        <td></td>" +
                "        <td></td>" +
                "        <td></td>" +
                "        <td></td>" +
                "    </tr>" +
                "    <tr>" +
                "        <td>*Functional (F); Non-functional (NF); Fridges tag (FT) in use</td>" +
                "        <td></td>" +
                "        <td></td>" +
                "        <td></td>" +
                "        <td></td>" +
                "        <td></td>" +
                "    </tr>" +
                "    </tbody>" +
                "</table>";

        // Merging the header, body and footer then printing
        String print_document = headerDocument + bodyDocument + footerDocument;
        webView.loadDataWithBaseURL(null, print_document, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";

        //  PrintAttributes.MediaSize mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
        PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "VRFs: View VRF");
        _mTracker.setScreenName("VRFs: View VRF");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

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
            Intent i = new Intent(ViewOrderActivity.this, MainNavigationActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                        item.setSystemQuantity(jo.getInt("SystemQuantity"));
                        item.setPurchaseOrderDetailID(jo.getInt("PurchaseOrderDetailID"));
                        item.setBeginningBalance(jo.getInt("BeginningBalance"));
                        item.setQuantityReceived(jo.getInt("QuantityReceived"));
                        item.setLoss(jo.getInt("Loss"));
                        item.setEndingBalance(jo.getInt("EndingBalance"));
                        item.setWasteFactor(jo.getDouble("WasteFactor"));
                        item.setTargetCoverage(jo.getInt("TargetCoverage"));
                        item.setDose(jo.getInt("Dose"));
                        item.setRequiredForNextSupplyPeriod(jo.getInt("RequiredForNextSupplyPeriod"));
                        item.setRequestedQuantity(jo.getInt("RequestedQuantity"));
                        item.setConsumption(jo.getInt("Consumption"));
                        items.add(item);
                    }
                    GlobalVariables.setDraftPOItems(items);
                    RequestedPOAdapter adapter = new RequestedPOAdapter(this, R.layout.layout_requestedpo, items);
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
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewOrderActivity.this, OrderMenuActivity.class);
        startActivity(i);
    }

    @Override
    public void handelNotification(String message) {
        ShowSnackNotification(message, R.color.successMessage);
        if (message.equalsIgnoreCase("Change is successful")) {
            Intent intent = new Intent(ViewOrderActivity.this, OrderMenuActivity.class);
            startActivity(intent);
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
