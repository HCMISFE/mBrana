package com.jsi.mbrana.Workflow.Reports.ReportSlideFragent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jsi.mbrana.Workflow.Adapter.Dashboard.Dashboard_Primary_Recycler_Adapter;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by surafel on 11/8/2016.
 */

public class Fragment_Page3 extends Fragment implements IDataNotification {

    String environment_code = GlobalVariables.getSelectedEnvironmentCode();
    View mView;
    TextView tv_fillrate, tv_sitefacility, tv_fillrate_sub, tv_sitefacility_sub;
    HorizontalBarChart mChart;
    ArrayList<ItemModel> item_model_list = new ArrayList<>();
    RecyclerView rv_soh;
    Typeface custom_font_bold, custom_font_light, custom_font_condensed, custom_font_thin;
    PieChart chart_pie_fillrate, chart_pie_site2facility;
    SharedPreferences prefs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_main_dashboard_page3, container, false);
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
        if (requestCode == 32) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                ArrayList<Integer> values = new ArrayList<>();
                values.add(jo.getInt("Draft"));
                values.add(jo.getInt("Submitted"));
                values.add(jo.getInt("Picklist"));
                values.add(jo.getInt("Issued"));

                ArrayList<BarEntry> entries_ = new ArrayList<>();

                for (int i = 0; i < values.size(); i++) {
                    entries_.add(new BarEntry(i, values.get(i)));
                }

                BarDataSet set1;

                if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

                    set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                    set1.setValues(entries_);
                    mChart.getData().notifyDataChanged();
                    mChart.notifyDataSetChanged();

                } else {

                    set1 = new BarDataSet(entries_, "Order Status");
                    set1.setValueFormatter(new MyFloatValueFormatter());
                    set1.setStackLabels(new String[]{"Draft", "Submitted", "Picklist", "Issued"});
                    set1.setValueTypeface(custom_font_light);
                    set1.setValueTextColor(getResources().getColor(R.color.white));

                    ArrayList<Integer> colors = new ArrayList<Integer>();
                    colors.add(getResources().getColor(R.color.material_widget_draft));
                    colors.add(getResources().getColor(R.color.material_widget_submitted));
                    colors.add(getResources().getColor(R.color.material_widget_picklist));
                    colors.add(getResources().getColor(R.color.material_widget_issue));
                    set1.setColors(colors);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);

                    BarData data = new BarData(dataSets);
                    mChart.setData(data);
                }
                mChart.animateY(2500);

                Legend l = mChart.getLegend();
                l.setEnabled(true);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setTypeface(custom_font_condensed);
                l.setTextColor(getResources().getColor(R.color.white));

            } catch (Exception e) {
                Log.d("mytag", "Error at API 32: " + e.getMessage());
            }
        } else if (requestCode == 33) {
            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ItemModel item_model = new ItemModel();
                    item_model.setItemName(jo.getString("ProductCN"));
                    item_model.setSOH(jo.getInt("SOH"));
                    item_model.setGIT(jo.getInt("GIT"));
                    item_model.setOrderedQuantity(jo.getInt("Ordered"));
                    item_model.setStockStatus(jo.getString("SS"));
                    item_model_list.add(item_model);
                }

                Dashboard_Primary_Recycler_Adapter adapter = new Dashboard_Primary_Recycler_Adapter(getContext(), item_model_list);
                rv_soh.setAdapter(adapter);
                rv_soh.setLayoutManager(new LinearLayoutManager(getContext()));

            } catch (Exception e) {
                Log.d("mytag", "Error at API 33: " + e.getMessage());
            }
        } else if (requestCode == 34) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                int SFR_entry = jo.getInt("SFR");
                String SFR_entry_text = Float.toString(SFR_entry);
                int OFR_entry = jo.getInt("OFR");
                String OFR_entry_text = Float.toString(OFR_entry);
                float mult = 5;

                // checking the values
                if (SFR_entry > 100) {
                    SFR_entry = 100;
                }
                if (OFR_entry > 100) {
                    OFR_entry = 100;
                }

                // Chart 1 -------------------------------------------------------------------------
                int SFR_entry_other = 100 - SFR_entry;

                ArrayList<PieEntry> SFR_entries = new ArrayList<PieEntry>();

                float xx = (SFR_entry * mult) + mult / 5;
                float xy = (SFR_entry_other * mult) + mult / 5;

                SFR_entries.add(new PieEntry(xx, ""));
                SFR_entries.add(new PieEntry(xy, ""));

                PieDataSet dataSet = new PieDataSet(SFR_entries, "");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setDrawValues(false);
                dataSet.setHighlightEnabled(false);

                dataSet.setColors(getResources().getColor(R.color.material_progress1),
                        getResources().getColor(R.color.material_progress_transparent));
                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);
                data.setValueTypeface(custom_font_condensed);
                chart_pie_fillrate.setCenterText(SFR_entry_text + "%");
                chart_pie_fillrate.setDrawSliceText(false);
                chart_pie_fillrate.setData(data);
                chart_pie_fillrate.highlightValues(null);
                chart_pie_fillrate.invalidate();

                chart_pie_fillrate.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                Legend SFR_l = chart_pie_fillrate.getLegend();
                SFR_l.setEnabled(false);

                // entry label styling
                chart_pie_fillrate.setEntryLabelColor(Color.WHITE);
                chart_pie_fillrate.setEntryLabelTypeface(custom_font_condensed);
                chart_pie_fillrate.setEntryLabelTextSize(12f);

                // Chart 2 -------------------------------------------------------------------------
                int OFR_entry_other = 100 - OFR_entry;

                ArrayList<PieEntry> OFR_entries = new ArrayList<PieEntry>();

                float OFR_xx = (OFR_entry * mult) + mult / 5;
                float OFR_xy = (OFR_entry_other * mult) + mult / 5;

                OFR_entries.add(new PieEntry(OFR_xx, ""));
                OFR_entries.add(new PieEntry(OFR_xy, ""));

                PieDataSet OFR_dataSet = new PieDataSet(OFR_entries, "");
                OFR_dataSet.setSliceSpace(3f);
                OFR_dataSet.setSelectionShift(5f);
                OFR_dataSet.setDrawValues(false);
                OFR_dataSet.setHighlightEnabled(false);

                OFR_dataSet.setColors(getResources().getColor(R.color.material_progress2),
                        getResources().getColor(R.color.material_progress_transparent));
                PieData OFR_data = new PieData(OFR_dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);
                data.setValueTypeface(custom_font_condensed);
                chart_pie_site2facility.setCenterText(OFR_entry_text + "%");
                chart_pie_site2facility.setDrawSliceText(false);
                chart_pie_site2facility.setData(OFR_data);
                chart_pie_site2facility.highlightValues(null);
                chart_pie_site2facility.invalidate();

                chart_pie_site2facility.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                Legend OFR_l = chart_pie_site2facility.getLegend();
                OFR_l.setEnabled(false);

                // entry label styling
                chart_pie_site2facility.setEntryLabelColor(Color.WHITE);
                chart_pie_site2facility.setEntryLabelTypeface(custom_font_condensed);
                chart_pie_site2facility.setEntryLabelTextSize(12f);

            } catch (Exception e) {
                Log.d("mytag", "Error at API 33: " + e.getMessage());
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

    public View getView() {
        return mView;
    }

    public void setView(View v) {
        mView = v;
    }

    public void CreateReportViews() {
        custom_font_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        custom_font_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        custom_font_condensed = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Condensed.ttf");
        custom_font_thin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");

        prefs = this.getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);

        tv_fillrate = (TextView) mView.findViewById(R.id.page3_fillrate);
        tv_fillrate.setTypeface(custom_font_light);
        tv_sitefacility = (TextView) mView.findViewById(R.id.page3_sitetofacility);
        tv_sitefacility.setTypeface(custom_font_light);
        tv_fillrate_sub = (TextView) mView.findViewById(R.id.page3_fillrate_sub);
        tv_fillrate_sub.setTypeface(custom_font_light);
        tv_sitefacility_sub = (TextView) mView.findViewById(R.id.page3_sitetofacility_sub);
        tv_sitefacility_sub.setTypeface(custom_font_light);

        mChart = (HorizontalBarChart) mView.findViewById(R.id.dashboard_po_issue_hbarchart);
        mChart.setNoDataText("Loading");
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);

        XAxis xl = mChart.getXAxis();
        xl.setDrawLabels(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setTextColor(getResources().getColor(R.color.white));
        xl.setGranularity(1f);
        xl.setTypeface(custom_font_light);
        xl.setValueFormatter(new MyXAxisValueFormatter(Constants.status_list));

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawLabels(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setTextColor(getResources().getColor(R.color.white));
        yl.setTypeface(custom_font_light);
        yl.setAxisMinimum(0f);
        yl.setGranularity(1f);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawLabels(false);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setTypeface(custom_font_light);
        yr.setTextColor(getResources().getColor(R.color.white));
        yr.setAxisMinimum(0f);
        yr.setGranularity(1f);

        // ---  --- //
        chart_pie_fillrate = (PieChart) mView.findViewById(R.id.pie_fillrate);
        chart_pie_fillrate.setUsePercentValues(true);
        chart_pie_fillrate.setNoDataText("Loading");
        chart_pie_fillrate.getDescription().setEnabled(false);
        chart_pie_fillrate.setExtraOffsets(5, 10, 5, 5);
        chart_pie_fillrate.setDragDecelerationFrictionCoef(0.95f);
        chart_pie_fillrate.setCenterTextTypeface(custom_font_condensed);
        chart_pie_fillrate.setCenterTextSize(20);
        chart_pie_fillrate.setCenterTextColor(Color.WHITE);
        chart_pie_fillrate.setDrawHoleEnabled(true);
        chart_pie_fillrate.setHoleColor(getResources().getColor(R.color.material_chart_hole));
        chart_pie_fillrate.setTransparentCircleColor(Color.WHITE);
        chart_pie_fillrate.setTransparentCircleAlpha(110);
        chart_pie_fillrate.setHoleRadius(80f);
        chart_pie_fillrate.setTransparentCircleRadius(80f);
        chart_pie_fillrate.setDrawCenterText(true);
        chart_pie_fillrate.setRotationAngle(270);
        chart_pie_fillrate.setRotationEnabled(false);
        chart_pie_fillrate.setHighlightPerTapEnabled(false);

        // ---  --- //
        chart_pie_site2facility = (PieChart) mView.findViewById(R.id.pie_site2facility);
        chart_pie_site2facility.setUsePercentValues(true);
        chart_pie_site2facility.setNoDataText("Loading");
        chart_pie_site2facility.getDescription().setEnabled(false);
        chart_pie_site2facility.setExtraOffsets(5, 10, 5, 5);
        chart_pie_site2facility.setDragDecelerationFrictionCoef(0.95f);
        chart_pie_site2facility.setCenterTextTypeface(custom_font_condensed);
        chart_pie_site2facility.setCenterTextSize(20);
        chart_pie_site2facility.setCenterTextColor(Color.WHITE);
        chart_pie_site2facility.setDrawHoleEnabled(true);
        chart_pie_site2facility.setHoleColor(getResources().getColor(R.color.material_chart_hole));
        chart_pie_site2facility.setTransparentCircleColor(Color.WHITE);
        chart_pie_site2facility.setTransparentCircleAlpha(110);
        chart_pie_site2facility.setHoleRadius(80f);
        chart_pie_site2facility.setTransparentCircleRadius(80f);
        chart_pie_site2facility.setDrawCenterText(true);
        chart_pie_site2facility.setRotationAngle(270);
        chart_pie_site2facility.setRotationEnabled(false);
        chart_pie_site2facility.setHighlightPerTapEnabled(false);

    }

    public void LoadChartData() {
        //
        int SelectedItemID = prefs.getInt("SelectedItem", 0);

        // Order Status
        new DataServiceTask(this, 32, false, false).execute("Order/GetOrderStatus?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");

        // Stock on Hand
        if (item_model_list.size() != 0)
            item_model_list.clear();
        new DataServiceTask(this, 33, false, false).execute("StockStatus/GetSOH?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");

        //
        new DataServiceTask(this, 34, false, false).execute("/StockStatus/EnvironmentFillRate?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");
    }

    public class MyFloatValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyFloatValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value);
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
}
