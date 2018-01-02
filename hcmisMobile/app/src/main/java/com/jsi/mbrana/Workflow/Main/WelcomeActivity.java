package com.jsi.mbrana.Workflow.Main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTaskForObjects;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Modules.UserManagement.LoginActivity;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Reports.ReportListActivity;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Page1;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Page2;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Page3;
import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Primary;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity implements IDataNotification, NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences prefs_user;
    String EnvironmentCode, Environment;
    FragmentPagerAdapter adapterViewPager;
    Toolbar toolbar;
    JSONObject passwordChangeObject = new JSONObject();
    private Tracker _mTracker;
    private ProgressDialog progress;
    private Boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        progress = new ProgressDialog(this);

        prefs_user = getSharedPreferences("userCredentials", 0);
        EnvironmentCode = prefs_user.getString("EnvironmentCode", "");
        Environment = prefs_user.getString("Environment", "");

//        toolbar = (Toolbar) findViewById(R.id.welcome_toolbar);
//        toolbar.setTitle("mFlow");
//        toolbar.setSubtitle(Environment);
//        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.welcome_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.welcome_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Grabbing the viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.welcome_viewpager);
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.welcome_indicator);
        adapterViewPager = new mFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        inkPageIndicator.setViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _mTracker.setScreenName("WorkFlow-Main: Welcome");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_report) {
            intent = new Intent(WelcomeActivity.this, ReportListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            // clear user account
            prefs_user.edit().putBoolean("Remberme", false).apply();
            prefs_user.edit().putBoolean("IsFromLogout", true).apply();

            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_changepassword) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_hangepassword, null);
            dialogBuilder.setView(dialogView);
            final EditText oldpassword = (EditText) dialogView.findViewById(R.id.oldpassword);
            final EditText newpassword = (EditText) dialogView.findViewById(R.id.newpassword);
            final EditText repeatnewpassword = (EditText) dialogView.findViewById(R.id.repeatnewpassword);
            dialogBuilder.setTitle("Change password");
            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    try {
                        if (newpassword.getText().toString().equals(repeatnewpassword.getText().toString())) {
                            passwordChangeObject.put("UserID", prefs_user.getInt("userid", 0));
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {

    }

    @Override
    public void handelNotification(String message) {

    }

    public void handelPasswordChange(String objString) {
        new DataServiceTaskForObjects(this, objString).execute("Account/ChangePassword", "");
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

    public class mFragmentPagerAdapter extends FragmentPagerAdapter {
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
