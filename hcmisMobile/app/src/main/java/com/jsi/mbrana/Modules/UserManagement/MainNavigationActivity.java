package com.jsi.mbrana.Modules.UserManagement;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccessLayer.DataServiceAgent;
import com.jsi.mbrana.DataAccessLayer.DataServiceInterface;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.DeviceInformationHelper;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Helpers.NetworkTypeHelper;
import com.jsi.mbrana.Preferences.PreferenceKey;
import com.jsi.mbrana.Preferences.PreferenceManager;
import com.jsi.mbrana.Models.TransactionSummary;
import com.jsi.mbrana.Models.UserDeviceModel;
import com.jsi.mbrana.Models.VersionModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Issue.IssueMenuActivity;
import com.jsi.mbrana.Workflow.Order.CreateOrderActivity;
import com.jsi.mbrana.Modules.Receive.Activity.ShipmentsMenuActivity;
import com.jsi.mbrana.Workflow.Reports.BincardActivity;
import com.jsi.mbrana.Workflow.Reports.ReportListActivity;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Page1;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Page2;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Page3;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Primary;
import com.jsi.mbrana.Workflow.Reports.StockStatusActivity;
import com.jsi.mbrana.Workflow.Update.UpdateActivity;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    public Menu myMenu;
    protected MainNavigationActivity myActivity;
    Boolean passworddialog;
    String restoredEnvironmentCode;
    int restoredEnvironmentID;
    JSONObject passwordChangeObject = new JSONObject();
    PieChart chart, orderstatuschart;
    ProgressDialog progress;
    LinearLayout MenuID, ChartsMainID;
    CardView Receiptcard_view, POcard_view, Issuecard_view, StockStatuscard_view4, Bincard_view4;
    SwipeRefreshLayout refreshLayout;
    FragmentPagerAdapter adapterViewPager;
    PackageInfo pInfo;
    int restoredUserId;
    TextView tv_main_facilities, tv_main_picklisted, tv_main_neworders, tv_main_processedorders, tv_main_processed, tv_main_confirmed_receipt, tv_outstanding_receipt, tv_main_confirmed_issues;
    private Tracker _mTracker;
    DataServiceInterface dataService;
    PreferenceManager preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        //Content View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        // Set the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(GlobalVariables.getSelectedEnvironment());
        setSupportActionBar(toolbar);

        // Saving the Activity
        setMyActivity(this);

        preference = new PreferenceManager(this);

        progress = new ProgressDialog(this);

        passworddialog = false;

        // Restoring Saved Data
        restoredEnvironmentCode = preference.getString(PreferenceKey.EnvironmentCode);
        restoredEnvironmentID = preference.getInt(PreferenceKey.EnvironmentId);
        restoredUserId = preference.getInt(PreferenceKey.UserId);

        //Retrofit API
        dataService = DataServiceAgent.getDataService(this);

        // Version name and code
        UpdateAppVersion();

        // Layout Declaration
        MenuID = (LinearLayout) findViewById(R.id.MenuID);
        ChartsMainID = (LinearLayout) findViewById(R.id.ChartsMainID);
        Receiptcard_view = (CardView) findViewById(R.id.Receiptcard_view);
        POcard_view = (CardView) findViewById(R.id.POcard_view);
        Issuecard_view = (CardView) findViewById(R.id.Issuecard_view);
        StockStatuscard_view4 = (CardView) findViewById(R.id.StockStatuscard_view4);
        Bincard_view4 = (CardView) findViewById(R.id.Bincard_view4);
        tv_main_facilities = (TextView) findViewById(R.id.tv_main_facilities);
        tv_main_neworders = (TextView) findViewById(R.id.tv_main_neworders);
        tv_main_picklisted = (TextView) findViewById(R.id.tv_main_picklisted);
        tv_main_processed = (TextView) findViewById(R.id.tv_main_processed);
        tv_main_processedorders = (TextView) findViewById(R.id.tv_main_processorders);
        tv_main_confirmed_receipt = (TextView) findViewById(R.id.tv_main_confirmed_receipt);
        tv_outstanding_receipt = (TextView) findViewById(R.id.tv_outstanding_receipt);
        tv_main_confirmed_issues = (TextView) findViewById(R.id.tv_main_confirmed_issues);

        // Intialize refresh layout
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                loadApiCalls();
            }
        });

        // Grabbing the viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        adapterViewPager = new mFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        inkPageIndicator.setViewPager(viewPager);

        // Receive
        Receiptcard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNavigationActivity.this, ShipmentsMenuActivity.class);
                startActivity(intent);
            }
        });

        // Bincard
        Bincard_view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNavigationActivity.this, BincardActivity.class);
                intent.putExtra("NavigationFrom", "MainOptionPage");
                startActivity(intent);
            }
        });

        // StockStatus
        StockStatuscard_view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNavigationActivity.this, StockStatusActivity.class);
                intent.putExtra("NavigationFrom", "MainOptionPage");
                startActivity(intent);
            }
        });

        // Issue
        Issuecard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNavigationActivity.this, IssueMenuActivity.class);
                startActivity(intent);
            }
        });

        // Purchase Order
        POcard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainNavigationActivity.this, CreateOrderActivity.class);
                intent.putExtra("ActivityCode", preference.getInt(PreferenceKey.DefaultActivityId));
                startActivity(intent);
            }
        });

        // Report Chart
        chart = (PieChart) findViewById(R.id.ReceiptStatuschartcontainer);
        orderstatuschart = (PieChart) findViewById(R.id.OrderStatuschartcontainer);

        // Load Data
        loadApiCalls();

        // Side Nav Layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation Layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Site Display Text
        TextView environmentTVonDrawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.environmentTVonDrawer);
        environmentTVonDrawer.setText(GlobalVariables.getSelectedEnvironment());

        //Handling User Group
//        if (UserActivityCodeHelper.getAssignedTo().equals("Malaria")) {
//            POcard_view.setVisibility(View.GONE);al false);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Main Menu");
        _mTracker.setScreenName("Main Menu");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    private void doPermissionGranted_PhoneState() {
        // Register device for pushNotification
        handelPushNotification();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doPermissionGranted_PhoneState();
            } else {
                Log.d("mytag", "Permission not granted!");
            }
        }
    }

    private void handelPushNotification() {
        // register the device and token
        DeviceInformationHelper deviceInformationHelper = new DeviceInformationHelper(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            UserDeviceModel userDevice = new UserDeviceModel();
            userDevice.setUserID(preference.getInt(PreferenceKey.UserId));
            userDevice.setDeviceIdentifier(deviceInformationHelper.getUniqueDeviceID());
            userDevice.setDeviceType("Mobile");
            userDevice.setPushNotificationIdentifier(refreshedToken);
            userDevice.setOtherInfo(deviceInformationHelper.getDeviceName());

            Gson gson = new Gson();
            final String userDeviceString = gson.toJson(userDevice);
        }
    }

    private void loadApiCalls() {
        if (Helper.isNetworkAvailable(this)) {
        } else {
            Log.d("Network", "Notavailble");
            handelNotification(getResources().getString(R.string.connectionErrorMessage));
        }
    }

    public void setMyActivity(MainNavigationActivity activity) {
        myActivity = activity;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            // Clear user account
            preference.put(PreferenceKey.RememberMe, false);
            preference.put(PreferenceKey.IsFromLogout, true);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_report) {
            intent = new Intent(MainNavigationActivity.this, ReportListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_emergency_vrf) {
            intent = new Intent(MainNavigationActivity.this, CreateOrderActivity.class);
            intent.putExtra("isEmergency", true);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            intent = new Intent(this, LoginActivity.class);
            // clear user account
            preference.put(PreferenceKey.RememberMe, false);
            preference.put(PreferenceKey.IsFromLogout, true);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_changepassword) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_hangepassword, null);
            dialogBuilder.setView(dialogView);
            passworddialog = true;
            final EditText oldpassword = (EditText) dialogView.findViewById(R.id.oldpassword);
            final EditText newpassword = (EditText) dialogView.findViewById(R.id.newpassword);
            final EditText repeatnewpassword = (EditText) dialogView.findViewById(R.id.repeatnewpassword);
            dialogBuilder.setTitle("Change password");
            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    try {
                        if (newpassword.getText().toString().equals(repeatnewpassword.getText().toString())) {
                            passwordChangeObject.put("UserID", preference.getString(PreferenceKey.UserId));
                            passwordChangeObject.put("OldPassword", oldpassword.getText().toString());
                            passwordChangeObject.put("NewPassword", newpassword.getText().toString());
                            handelPasswordChange(passwordChangeObject.toString());
                        } else {
                            handelNotification(getResources().getString(R.string.passwordDoesntMatch));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Pass
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        } else if (id == R.id.nav_update) {
            CheckAppUpdate();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CheckAppUpdate() {
        if (NetworkTypeHelper.isConnectedWifi(this) || NetworkTypeHelper.isConnectedMobile(this)) {
            DownloadJSON jd = new DownloadJSON();
            jd.execute();
        } else {
            handelNotification(Constants.error_ApiFail);
        }
    }

    public void handelPasswordChange(String objString) {
        //new DataServiceTaskForObjects(this, objString).execute("Account/ChangePassword", "");
    }

    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {
        if (requestCode == 16) {

        }
    }

    public void dataReceived(JSONArray jsonArray, int requestCode) {
        if (requestCode == 53) {
            try {
                if (jsonArray.length() > 0) {
                    TransactionSummary tm = new TransactionSummary();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        tm.setIssuedFacilities(jo.getInt("IssuedFacilities"));
                        tm.setNoOfItems(jo.getInt("NoOfItems"));
                        tm.setNoConfirmedReceipts(jo.getInt("NoConfirmedReceipts"));
                        tm.setOutStandingReceipts(jo.getInt("OutStandingReceipts"));
                        tm.setNoIssuesConfirmed(jo.getInt("NoIssuesConfirmed"));
                        tm.setLastReceivedDate(jo.getString("LastReceivedDate"));
                        tm.setLastIssuedDate(jo.getString("LastIssuedDate"));
                        tm.setLastOrderedDate(jo.getString("LastOrderedDate"));
                    }
                    tv_main_facilities.setText(tm.getIssuedFacilities() + " Facilities");
                    tv_main_confirmed_receipt.setText(tm.getNoConfirmedReceipts() + " Confirmed");
                    tv_outstanding_receipt.setText(tm.getOutStandingReceipts() + " Outstanding");
                    tv_main_confirmed_issues.setText(tm.getNoIssuesConfirmed() + " Confirmed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 66) {
            try {
                if (jsonArray.length() > 0) {
                    JSONObject jo = jsonArray.getJSONObject(0);
                    int draft_Count = jo.getInt("Draft");
                    int requested_Count = jo.getInt("Requested");
                    int inProcess_Count = jo.getInt("InProcess");
                    int processed_Count = jo.getInt("Processed");
                    tv_main_neworders.setText(requested_Count + " New orders");
                    tv_main_processedorders.setText(inProcess_Count + " In-process orders");
                    tv_main_processed.setText(processed_Count + " Processed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 77) {
            try {
                if (jsonArray.length() > 0) {
                    JSONObject jo = jsonArray.getJSONObject(0);
                    int draft_Count = jo.getInt("Draft");
                    int Submitted_Count = jo.getInt("Submitted");
                    int Picklist_Count = jo.getInt("Picklist");
                    int Issued_Count = jo.getInt("Issued");
                    tv_main_picklisted.setText(Picklist_Count + " Picklisted");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdateAppVersion() {
        // Version name and code
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;
            int versionCode = pInfo.versionCode;
            String version = versionName + "(" + versionCode + ")";

            // Call API
            VersionModel version_model = new VersionModel();
            version_model.setEnvironmentID(restoredEnvironmentID);
            version_model.setEnvironmentCode(restoredEnvironmentCode);
            version_model.setVersionName(versionName);
            version_model.setVersionCode(versionCode);
            version_model.setVersion(version);
            version_model.setUserId(restoredUserId);

            // Json conversion
            Gson gson = new Gson();
            final String itemstring = gson.toJson(version_model);

            //handelDataSave(itemstring, "Account/SaveAppVersion", 16);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handelNotification(String message) {
        if (message.equals("Change is successful")) {
            if (passworddialog) {
                Toast passwordtoast = Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT);
                passwordtoast.show();
            }
        } else {
            if (passworddialog) {
                Toast passwordtoast = Toast.makeText(this, "Password Change failed", Toast.LENGTH_SHORT);
                passwordtoast.show();
            }
        }
    }

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

    private PackageInfo getCurrentPackageInfo() {
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void showUpdateMessage() {
        Snackbar _snack = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml("No new updates."), Snackbar.LENGTH_LONG);
        _snack.setActionTextColor(getResources().getColor(R.color.white));
        View view = _snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.stockout));
        _snack.show();
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
        String apk_update_url, apk_latest_version_name;
        int apk_latest_version_code, current_apk_version_code;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONDownloader jd = new JSONDownloader();
            // Making a request to url and getting response
            String jsonStr = jd.makeServiceCall(Constants.JSONUrl);
            //
            if (jsonStr != null) {
                try {
                    // Parse the JSONObject
                    JSONObject jo = new JSONObject(jsonStr);

                    String packageName = getApplication().getApplicationInfo().packageName;
                    if (packageName.contains(".dev")) {
                        //then do dev stuff
                        apk_update_url = jo.getString("UPDATE_URL_DEV");
                        apk_latest_version_code = jo.getInt("LATEST_VERSION_CODE_DEV");
                        apk_latest_version_name = jo.getString("LATEST_VERSION_NAME_DEV");
                    } else {
                        //we are on the master/release branch
                        apk_update_url = jo.getString("UPDATE_URL");
                        apk_latest_version_code = jo.getInt("LATEST_VERSION_CODE");
                        apk_latest_version_name = jo.getString("LATEST_VERSION_NAME");
                    }

                    // Get the Current Apk detail
                    pInfo = getCurrentPackageInfo();
                    current_apk_version_code = pInfo.versionCode;

                    // Compare the two version codes
                    if (current_apk_version_code < apk_latest_version_code) {
                        Intent intent = new Intent(MainNavigationActivity.this, UpdateActivity.class);
                        intent.putExtra("APK_UPDATE_URL", apk_update_url);
                        intent.putExtra("APK_LATEST_VERSION_NAME", apk_latest_version_name);
                        intent.putExtra("APK_LATEST_VERSION_CODE", apk_latest_version_code);
                        startActivity(intent);
                        finish();
                    } else {
                        showUpdateMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class JSONDownloader {
        private String makeServiceCall(String reqUrl) {
            String result = "";
            try {
                URL url = new URL(reqUrl);

                Log.d("URL: ", url.toString());

                MyNetworkInterceptor interceptor = new MyNetworkInterceptor();

                OkHttpClient client = new OkHttpClient
                        .Builder()
                        .addInterceptor(interceptor)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();

                Response response = client.newCall(request).execute();

                int requestStatusCode = response.code();

                Log.d("Response Code: ", String.valueOf(requestStatusCode));

                result = response.body().string();

                Log.d("Result: ", String.valueOf(result));
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
            return result;
        }
    }

    private class MyNetworkInterceptor implements Interceptor {
        private final String TAG = getClass().getSimpleName();

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.code() != 200) {
                Response r = null;
                try {
                    r = makeTokenRefreshCall(request, chain);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return r;
            }
            Log.d(TAG, "INTERCEPTED:$ " + response.toString());
            return response;
        }

        private Response makeTokenRefreshCall(Request req, Chain chain) throws JSONException, IOException {
            Log.d(TAG, "Retrying new request");
            /* make a new request which is same as the original one, except that its headers now contain a refreshed token */
            Request newRequest;
            newRequest = req.newBuilder().build();
            Response another = chain.proceed(newRequest);
            while (another.code() != 200) {
                makeTokenRefreshCall(newRequest, chain);
            }
            return another;
        }
    }

    private class mFragmentPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 4;

        public mFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment_Primary();
                case 1:
                    return new Fragment_Page1();
                case 2:
                    return new Fragment_Page2();
                case 3:
                    return new Fragment_Page3();
                default:
                    return null;
            }
        }
    }
}