package com.jsi.mbrana.Workflow.Reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jsi.mbrana.Workflow.Adapter.Reports.BincardAdapter;
import com.jsi.mbrana.AnalyticsApplication;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.UserActivityCodeHelper;
import com.jsi.mbrana.Modules.UserManagement.MainNavigationActivity;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BincardActivity extends AppCompatActivity implements IDataNotification {
    ArrayList<ItemModel> itemUnitObjs = new ArrayList<>();
    ArrayList<ItemModel> Bincard = new ArrayList<>();
    ArrayList<String> itemUnitList = new ArrayList<>();
    Spinner itemUnitListSpinner;
    ListView bincardContainer;
    ProgressDialog progress;
    int itemIndex = -1;
    String ActivityCode, EnvironmentCode;
    boolean isProgressVisible = false;
    private CheckBox IsCampain;
    private Tracker _mTracker;
    private long start;
    private long il_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Analytics Tracker
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _mTracker = application.getDefaultTracker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bincard);
        setTitle("Bincard");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences("userCredentials", 0);
        getSupportActionBar().setSubtitle(prefs.getString("Environment", ""));

        progress = new ProgressDialog(this);

        ActivityCode = prefs.getString("ActivityCode", "");
        EnvironmentCode = prefs.getString("EnvironmentCode", "");
        itemUnitListSpinner = (Spinner) findViewById(R.id.bincard_itemlistSpinner);
        bincardContainer = (ListView) findViewById(R.id.bincard_tableContainer);

        IsCampain = (CheckBox) findViewById(R.id.isCampinCheckBoxBinCard);
        IsCampain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemIndex == -1) {
                    Toast.makeText(BincardActivity.this, "Please choose item first", Toast.LENGTH_LONG).show();
                } else {
                    LoadBincard(itemIndex, IsCampain.isChecked());
                }
            }
        });
        il_start = System.currentTimeMillis();

        // Load item unit look up
        new DataServiceTask(this, 2, false).execute("Lookup/GetReceivedItemList"
                + "?ActivityCode=" + UserActivityCodeHelper.getDefaultActivityCode()
                + "&EnvironmentCode=" + EnvironmentCode, "");

        itemUnitListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemIndex = position;
                LoadBincard(position, IsCampain.isChecked());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("GA-Screen", "Bin Card");
        _mTracker.setScreenName("Bin Card");
        _mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void LoadBincard(int position, boolean IsCampaign) {
        // Clear previous data
        Bincard = new ArrayList<>();
        BincardAdapter adapter = new BincardAdapter(BincardActivity.this, R.layout.layout_facilitieswithnoreorde, Bincard);
        bincardContainer.setAdapter(adapter);

        // Setting the ActivityCode
        String _ActivityCode;
        _ActivityCode = UserActivityCodeHelper.getActivityCode(IsCampaign);

        start = System.currentTimeMillis();
        new DataServiceTask(this, 3, false).execute("StockStatus/BinCard?"
                + "EnvironmentCode=" + EnvironmentCode
                + "&ItemId=" + itemUnitObjs.get(position).getItemID()
                + "&UnitId=" + itemUnitObjs.get(position).getUnitID()
                + "&IsCampaign=" + IsCampaign
                + "&ActivityCode=" + _ActivityCode, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itemUnitObjs to the action bar if it is present.
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
            Intent intent = new Intent(BincardActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            Intent i = new Intent(this, MainNavigationActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        if (requestCode == 2) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel item = new ItemModel();
                    item.setItemNameSH(jo.getString("FullItemName"));
                    item.setItemCode(jo.getString("ProductCN"));
                    item.setUnit(jo.getString("Unit"));
                    item.setItemID(jo.getInt("ItemID"));
                    item.setUnitID(jo.getInt("UnitID"));
                    itemUnitList.add(item.getItemCode() + " [" + item.getUnit() + "]");
                    itemUnitObjs.add(item);
                }
                ArrayAdapter<String> itemUnitListAdapter = new ArrayAdapter<String>(this, R.layout.layout_environment_list, itemUnitList);
                itemUnitListAdapter.setDropDownViewResource(R.layout.layout_environment_list);
                itemUnitListSpinner.setAdapter(itemUnitListAdapter);

                long stop = System.currentTimeMillis();
                long interval = (stop - il_start) / 1000;

                _mTracker.send(new HitBuilders.TimingBuilder()
                        .setCategory("Loading")
                        .setValue(interval)
                        .setVariable("Bincard")
                        .setLabel("Lookup/GetReceivedItemList")
                        .build());
                Log.d("GA-Timer", "Loading Time for Bincard:(Lookup/GetReceivedItemList) is " + interval + "Seconds");
            } catch (Exception e) {
                e.printStackTrace();
                _mTracker.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(getApplicationContext(), null)
                                .getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build());
            }
        } else if (requestCode == 3) {
            Bincard = new ArrayList<>();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel item = new ItemModel();
                    item.setItemNameSH(jo.getString("FullItemName"));
                    item.setItemCode(jo.getString("ProductCN"));
                    item.setUnit(jo.getString("Unit"));
                    item.setItemID(jo.getInt("ItemID"));
                    item.setUnitID(jo.getInt("UnitID"));
                    item.setReceivedQuantity(jo.getInt("Received"));
                    item.setIssuedQuantity(jo.getInt("Issued"));
                    item.setBalance(jo.getInt("SOHBalance"));
                    item.setSOH(jo.getInt("Balance"));
                    item.setTransactionDate(jo.getString("Date"));
                    item.setEthiopianDate(jo.getString("EthiopianDate"));
                    item.setToFrom(jo.getString("ToFrom"));
                    item.setVoid(jo.getBoolean("Void"));
                    Bincard.add(item);
                }
                BincardAdapter adapter = new BincardAdapter(BincardActivity.this, R.layout.layout_facilitieswithnoreorde, Bincard);
                bincardContainer.setAdapter(adapter);

                long stop = System.currentTimeMillis();
                long interval = (stop - start) / 1000;

                _mTracker.send(new HitBuilders.TimingBuilder()
                        .setCategory("Loading")
                        .setValue(interval)
                        .setVariable("Bincard")
                        .setLabel("StockStatusActivity/BinCard")
                        .build());
                Log.d("GA-Timer", "Loading Time for Bincard:(StockStatusActivity/BinCard) is " + interval + "Seconds");
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

    @Override
    public void handelNotification(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
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
