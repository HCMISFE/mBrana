package com.jsi.mbrana.Workflow.Reports.ReportSlideFragent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jsi.mbrana.Workflow.Adapter.Dashboard.Dashboard_Primary_Recycler_Adapter;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by surafel on 11/8/2016.
 */

public class Fragment_Primary extends Fragment implements IDataNotification {
    ArrayList<String> items_name;
    String environment_code = GlobalVariables.getSelectedEnvironmentCode();
    String ActivityCode = GlobalVariables.getActivityCode();
    View mView;
    BarChart mChart;
    RecyclerView mRecycler;
    ArrayList<ItemModel> item_list = new ArrayList<>();
    TextView tv_header_left, tv_header_right, tv_header_left_h, tv_header_right_h, title_no,
            title_git, title_item, title_spark, title_ordered, title_soh, title_wos;
    Typeface custom_font_bold, custom_font_light, custom_font_condensed, custom_font_thin;
    SharedPreferences prefs;

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_main_dashboard_primary, container, false);
        setView(view);
        CreateReportViews();
        LoadChartData();
        return view;
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {
        if (requestCode == 2) {
            try {
                //float groupSpace = 0.8f;
                //float barSpace = 0.05f; // x4 DataSet
                //float barWidth = 0.5f; // x4 DataSet
                float groupSpace = 0.25f;
                float barSpace = 0.05f; // x4 DataSet
                float barWidth = 0.8125f; // x4 DataSet
                // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

                int groupCount = 5;
                int startPos = 0;
                int counter = 0;

                ArrayList<BarEntry> yVals1 = new ArrayList<>();
                ArrayList<BarEntry> yVals2 = new ArrayList<>();

                items_name = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String productCN = jo.getString("ProductCN");
                    int soh = jo.getInt("SOH");
                    int git = jo.getInt("GIT");
                    int ordered = jo.getInt("Ordered");

                    if (soh != 0) {
                        if (counter < groupCount) {
                            items_name.add(productCN);
                            yVals1.add(new BarEntry(i, (float) soh));
                            items_name.add(productCN);
                            yVals2.add(new BarEntry(i, new float[]{git, ordered}));
                            counter += 1;
                        }
                    }
                }

                BarDataSet set1, set2;

                if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

                    set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                    set2 = (BarDataSet) mChart.getData().getDataSetByIndex(1);
                    set1.setValues(yVals1);
                    set2.setValues(yVals2);
                    mChart.getData().notifyDataChanged();
                    mChart.notifyDataSetChanged();

                } else {

                    set1 = new BarDataSet(yVals1, "SOH");
                    set1.setColor(getResources().getColor(R.color.material_widget_draft));
                    set1.setHighlightEnabled(true);
                    set1.setValueTextColor(getResources().getColor(R.color.white));
                    set1.setValueTextSize(10f);
                    set2 = new BarDataSet(yVals2, "");
                    set2.setValueTextColor(getResources().getColor(R.color.white));
                    set2.setValueTextSize(10f);
                    set2.setColors(getResources().getColor(R.color.material_widget_submitted),
                            getResources().getColor(R.color.material_widget_picklist));
                    set2.setStackLabels(new String[]{"GIT", "Ordered"});
                    set2.setHighlightEnabled(true);
                    BarData data = new BarData(set1, set2);
                    mChart.setData(data);

                }

                mChart.getBarData().setBarWidth(barWidth);
                mChart.getXAxis().setAxisMinimum(startPos);
                mChart.getXAxis().setAxisMaximum(groupCount * 2);
                mChart.groupBars(startPos, groupSpace, barSpace);
                mChart.invalidate();

                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setDrawLabels(false);
                leftAxis.setDrawAxisLine(false);
                leftAxis.setDrawGridLines(false);
                leftAxis.setTextColor(getResources().getColor(R.color.white));
                leftAxis.setTextSize(12f);
                leftAxis.setGranularity(1f);

                XAxis xLabels = mChart.getXAxis();
                xLabels.setDrawLabels(true);
                xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
                xLabels.setDrawAxisLine(true);
                xLabels.setDrawGridLines(false);
                xLabels.setAxisLineColor(getResources().getColor(R.color.white));
                xLabels.setTextColor(getResources().getColor(R.color.white));
                xLabels.setTextSize(12f);
                xLabels.setGranularity(1f);
                xLabels.setTypeface(custom_font_condensed);
                xLabels.setValueFormatter(new MyXAxisValueFormatter(items_name));

                YAxis yr = mChart.getAxisRight();
                yr.setDrawLabels(false);
                yr.setDrawAxisLine(false);
                yr.setDrawGridLines(false);
                yr.setTextColor(getResources().getColor(R.color.white));
                yr.setTextSize(12f);
                yr.setGranularity(1f);

                Legend l = mChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                l.setFormSize(10f);
                l.setTextSize(12f);
                l.setTextColor(getResources().getColor(R.color.white));
                l.setFormToTextSpace(1f);
                l.setTypeface(custom_font_condensed);

                // ---------------------------------------------------------------------------------
                // This part then populate the grid
                // ---------------------------------------------------------------------------------
                DataServiceTask DS_Object;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel item_model = new ItemModel();
                    item_model.setItemName(jo.getString("ProductCN"));
                    item_model.setWOS(jo.getInt("WOS"));
                    item_model.setSOH(jo.getInt("SOH"));
                    item_model.setGIT(jo.getInt("GIT"));
                    item_model.setItemID(jo.getInt("ItemId"));
                    item_model.setUnitID(jo.getInt("UnitId"));
                    item_model.setOrderedQuantity(jo.getInt("Ordered"));
                    item_model.setStockStatus(jo.getString("SS"));
                    item_list.add(item_model);
                    // For the Spark View, clean array if not empty
                    //> new DataServiceTask(this, 3, false, false).execute("StockStatus/WeeklyTrend?EnvironmentCode=" + environment_code + "&ItemId=" + item_model.getItemID() + "&UnitId=" + item_model.getUnitID(), "");
                }

                Dashboard_Primary_Recycler_Adapter adapter = new Dashboard_Primary_Recycler_Adapter(getContext(), item_list);
                mRecycler.setAdapter(adapter);
                mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

            } catch (Exception e) {
                Log.d("mytag", "Error at 02: Error found");
            }
        } else if (requestCode == 1) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                int x1 = jo.getInt("SOH");
                String x2 = jo.getString("SS");
                tv_header_left.setText(doubleToStringNoDecimal(x1) + "");
                if (x2.equals("null")) {
                    tv_header_right.setText("Unknown");
                } else {
                    tv_header_right.setText(x2 + "");
                }
            } catch (Exception e) {
                Log.d("mytag", "Error at 04: Error found: " + e.getMessage());
            }
        } else if (requestCode == 3) {

            try {
                float[] spark_value = new float[jsonArray.length()];
                int index = -1;
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jo = jsonArray.getJSONObject(x);
                    int ItemId = jo.getInt("ItemID");
                    int UnitId = jo.getInt("UnitID");

                    for (int k = 0; k < item_list.size(); k++) {
                        if (item_list.get(k).getItemID() == ItemId && item_list.get(k).getUnitID() == UnitId) {
                            index = k;
                        }
                    }

                    spark_value[x] = jo.getInt("WeeklyBalance");
                }

                View view = mRecycler.getChildAt(index);
                SparkView spark_view = (SparkView) view.findViewById(R.id.sparkview);
                spark_view.setAdapter(new SparkViewAdapter(spark_value));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.d("mytag", "Error on API call: request not Found");
        }
    }

    @Override
    public void handelNotification(String message) {

    }

    @Override
    public void handelProgressDialog(Boolean showProgress, String Message) {

    }

    @Override
    public void readFromDatabase(int requestCode) {

    }

    public void LoadChartData() {
        // Item selected from the dropdown
        int SelectedItemID = prefs.getInt("SelectedItem", 0);

        // Header Value
        new DataServiceTask(this, 1, false, false).execute("StockStatus/GetSOH?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID + "&ActivityCode=" + ActivityCode, "");

        // Stock on Hand
        if (item_list.size() != 0)
            item_list.clear();
        new DataServiceTask(this, 2, false, false).execute("StockStatus/GetSOH?EnvironmentCode=" + environment_code + "&ActivityCode=" + ActivityCode, "");
    }

    public void setView(View v) {
        mView = v;
    }

    private void CreateReportViews() {
        custom_font_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        custom_font_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        custom_font_condensed = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Condensed.ttf");
        custom_font_thin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");

        prefs = this.getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);

        mChart = (BarChart) mView.findViewById(R.id.dashboard_primary_bar);
        mChart.setNoDataText("Loading");
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.setBorderColor(getResources().getColor(R.color.white));
        mChart.setHighlightPerTapEnabled(false);

        mRecycler = (RecyclerView) mView.findViewById(R.id.dashboard_primary_recycler);

        tv_header_left = (TextView) mView.findViewById(R.id.tv_dashboard_primary_tab_left);
        tv_header_left.setTypeface(custom_font_bold);

        tv_header_right = (TextView) mView.findViewById(R.id.tv_dashboard_primary_tab_right);
        tv_header_right.setTypeface(custom_font_bold);

        tv_header_left_h = (TextView) mView.findViewById(R.id.subtitle_left);
        tv_header_left_h.setTypeface(custom_font_condensed);

        tv_header_right_h = (TextView) mView.findViewById(R.id.subtitle_right);
        tv_header_right_h.setTypeface(custom_font_condensed);

        title_no = (TextView) mView.findViewById(R.id.title_no);
        title_no.setTypeface(custom_font_condensed);
        title_git = (TextView) mView.findViewById(R.id.title_git);
        title_git.setTypeface(custom_font_condensed);
        title_item = (TextView) mView.findViewById(R.id.title_item);
        title_item.setTypeface(custom_font_condensed);
        title_spark = (TextView) mView.findViewById(R.id.title_spark);
        title_spark.setTypeface(custom_font_condensed);
        title_ordered = (TextView) mView.findViewById(R.id.title_ordered);
        title_ordered.setTypeface(custom_font_condensed);
        title_soh = (TextView) mView.findViewById(R.id.title_soh);
        title_soh.setTypeface(custom_font_condensed);
        title_wos = (TextView) mView.findViewById(R.id.title_wos);
        title_wos.setTypeface(custom_font_condensed);
    }

    private static class SparkViewAdapter extends SparkAdapter {
        private final float[] yData;

        private SparkViewAdapter(float[] yData) {
            this.yData = yData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return yData.length;
        }

        @Override
        public Object getItem(int index) {
            return yData[index];
        }

        @Override
        public float getY(int index) {
            return yData[index];
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private ArrayList<String> mValues;

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value % 2 == 0) {
                return "";
            } else {
                return mValues.get((int) (value - (value % 2)));
            }
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
}
