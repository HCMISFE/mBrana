package com.jsi.mbrana.Workflow.Issue;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.Issue.FacilityAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.InstitutionModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FacilitySelectionActivity extends AppCompatActivity implements IDataNotification {
    public static View.OnClickListener myOnClickListener;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    final ArrayList<InstitutionModel> institutions = new ArrayList<InstitutionModel>();
    android.support.v7.app.ActionBar ab;
    ProgressDialog progress;
    private RecyclerView.LayoutManager layoutManager;
    private Tracker _mTracker;
    boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_cardlistview);
        ab = getSupportActionBar();
        ab.setSubtitle("Select facility");
        myOnClickListener = new MyOnClickListener(this);

        progress = new ProgressDialog(this);

        //Load lookups
        new DataServiceTask(this, 2, false).execute("LookUp/GetInstitution?EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Issues:Facility Menu(Create Issue)");
        _mTracker.setScreenName("Issues: Facility Menu(Create Issue)");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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
            Intent intent = new Intent(FacilitySelectionActivity.this, MainNavigationActivity.class);
            intent.putExtra("ActivityCode", getIntent().getStringExtra("ActivityCode"));
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(FacilitySelectionActivity.this, IssueMenuActivity.class);
            intent.putExtra("ActivityCode", getIntent().getStringExtra("ActivityCode"));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        try {
            ArrayList<String> institutionNames = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                InstitutionModel institutionModel = new InstitutionModel();
                institutionModel.setInstitutionName(jo.getString("Institution"));
                institutionModel.setInstitutionID(jo.getInt("InstitutionID"));
                institutionModel.setIssueDate(jo.getString("IssueDate"));

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                Date issuedate = null;
                try {
                    issuedate = simpleDateFormat.parse(institutionModel.getIssueDate().replace("T", " "));
                } catch (ParseException e) {
                    e.printStackTrace();
                    _mTracker.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                    .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build());
                }

                if (institutionModel.getIssueDate().equalsIgnoreCase("0001-01-01T00:00:00"))
                    institutionNames.add(institutionModel.getInstitutionName() + " - NA");
                else
                    institutionNames.add(institutionModel.getInstitutionName() + " - " + Helper.getMomentFromNow(issuedate));

                institutions.add(institutionModel);
            }

            adapter = new FacilityAdapter(institutions);
            recyclerView.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
            _mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                            .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build());
        }
    }

    @Override
    public void handelNotification(String message) {

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

    private class MyOnClickListener implements View.OnClickListener {
        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (recyclerView == null)
                return;
            int selectedItemPosition = recyclerView.getChildPosition(v);

            _mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("UX")
                    .setAction("Facility Selection")
                    .setLabel("Selected Facility :(" + institutions.get(selectedItemPosition).getInstitutionID() + ")")
                    .build());

            Intent intent = new Intent(FacilitySelectionActivity.this, CreateIssueActivity.class);
            intent.putExtra("InstitutionID", institutions.get(selectedItemPosition).getInstitutionID());
            intent.putExtra("InstitutionName", institutions.get(selectedItemPosition).getInstitutionName());
            intent.putExtra("ActivityCode", getIntent().getStringExtra("ActivityCode"));
            startActivity(intent);

        }
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
