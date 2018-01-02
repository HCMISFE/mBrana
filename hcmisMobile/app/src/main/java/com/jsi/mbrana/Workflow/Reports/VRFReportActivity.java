package com.jsi.mbrana.Workflow.Reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.PO.ShowOrderAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.PeriodModel;
import com.jsi.mbrana.Models.RRFModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.jsi.mbrana.Helpers.Helper.dateFormatter;

public class VRFReportActivity extends AppCompatActivity implements IDataNotification {
    final ArrayList<PeriodModel> periods = new ArrayList<>();
    final ArrayList<RRFModel> rrf_model = new ArrayList<>();
    String EnvironmentCode = GlobalVariables.getSelectedEnvironmentCode();
    String ActivityCode = GlobalVariables.getActivityCode();
    ProgressDialog progress;
    RecyclerView recycler_vrf;
    WebView mWebView;
    Button btn_print;
    boolean isProgressVisible = false;
    private Tracker _mTracker;
    private TextView tv_date_start;
    private TextView tv_date_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        // Setting main layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_vrf);
        setTitle("VRF Report");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences("userCredentials", 0);
        getSupportActionBar().setSubtitle(prefs.getString("Environment", ""));

        progress = new ProgressDialog(this);

        tv_date_start = (TextView) findViewById(R.id.tv_date_start);
        tv_date_next = (TextView) findViewById(R.id.tv_date_next);

        // UI elements
        // VRF Recycler
        recycler_vrf = (RecyclerView) findViewById(R.id.vrf_recycler);

        // Print order
        btn_print = (Button) findViewById(R.id.order_print);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWebViewPrint(rrf_model);
            }
        });

        // Get the Period & Supplier
        new DataServiceTask(this, 1, false).execute("Lookup/GetPeriod?EnvironmentCode=" + EnvironmentCode, "");
    }

    public void GenerateVRF() {
        new DataServiceTask(this, 2, false).execute("PurchaseOrder/GenerateVRF?PeriodId=" + periods.get(0).getPeriodID() + "&PeriodStart=" + periods.get(0).getPeriodStart()
                + "&EnvironmentCode=" + EnvironmentCode + "&ActivityCode=" + ActivityCode, "");
    }

    private void doWebViewPrint(ArrayList<RRFModel> model) {
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
                "        <th colspan=\"5\">Amhara</th>" +
                "        <th colspan=\"2\">Level of cold chain</th>" +
                "        <th colspan=\"1\">Date of request:</th>" +
                "        <th colspan=\"1\">19/05/09</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Name of cold store</th>" +
                "        <th colspan=\"5\">BahirDar HUB</th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"1\">No. of months to supply(S):</th>" +
                "        <th colspan=\"1\">3</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Responsible Person</th>" +
                "        <th colspan=\"5\">Biruh Tesfaye</th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"2\" style=\"font-size: large\">For population catchment served</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Contact Address</th>" +
                "        <th colspan=\"5\">email@gmail.com</th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"1\">Births(BI):</th>" +
                "        <th colspan=\"1\">336,697</th>" +
                "    </tr>" +
                "    <tr>" +
                "        <th colspan=\"1\">Telephone Number(s):</th>" +
                "        <th colspan=\"5\">091111111</th>" +
                "        <th colspan=\"2\" style=\"border:none\"></th>" +
                "        <th colspan=\"1\">Surviving Infants(SI):</th>" +
                "        <th colspan=\"1\">300,697</th>" +
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
                    "<td>" + model.get(i).getProductCN() + "</td>" +
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
        _mTracker.setScreenName("Reports: VRF Report");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent intent = new Intent(VRFReportActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Report")) {
                Intent intent = new Intent(VRFReportActivity.this, ReportListActivity.class);
                startActivity(intent);
                return true;
            } else if (getIntent().getStringExtra("NavigationFrom") != null &&
                    getIntent().getStringExtra("NavigationFrom").equalsIgnoreCase("Dashboard")) {
                Intent i = new Intent(VRFReportActivity.this, MainNavigationActivity.class);
                startActivity(i);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        if (requestCode == 1) {
            try {
                ArrayList<String> period_model = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    PeriodModel period = new PeriodModel();
                    period.setPeriodID(jo.getInt("PeriodID"));
                    period.setNextPeriodStart(jo.getString("NextPeriodStart"));
                    period.setPeriodStart(jo.getString("PeriodStart"));
                    periods.add(period);
                }
                tv_date_start.setText(dateFormatter(periods.get(0).getPeriodStart()) + "");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                Date next_date = simpleDateFormat.parse(periods.get(0).getNextPeriodStart().replace("T", " "));
                tv_date_next.setText(Helper.getMomentFromNow(next_date));

                GenerateVRF();
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
        } else if (requestCode == 2) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                boolean IsFilled = jo.getBoolean("IsFilled");
                JSONArray VrfDetail = jo.getJSONArray("VrfDetail");

                // Empty the list
                rrf_model.clear();
                btn_print.setVisibility(View.VISIBLE);

                // Check if the Order is filled
                if (IsFilled) {
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
                    ShowOrderAdapter adapter = new ShowOrderAdapter(this, rrf_model);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recycler_vrf.setLayoutManager(mLayoutManager);
                    recycler_vrf.setItemAnimator(new DefaultItemAnimator());
                    recycler_vrf.setAdapter(adapter);

                } else {
                    // Clearing the Model
                    rrf_model.clear();
                    recycler_vrf.setAdapter(null);
                    btn_print.setVisibility(View.GONE);
                    // Show the notification
                    handelNotification("Order is not filled for this period.");
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

    public String FormatDate(String _date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(_date.replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(this, null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build());
        }
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    @Override
    public void handelNotification(String message) {
        Snackbar snack;
        snack = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(message), Snackbar.LENGTH_LONG);
        snack.setActionTextColor(getResources().getColor(R.color.white));
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.stockout));
        snack.show();
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
}
