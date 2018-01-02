package com.jsi.mbrana.Workflow.Reports.ReportSlideFragent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by surafel on 11/8/2016.
 */

public class Fragment_Page2 extends Fragment implements IDataNotification {
    String environment_code = GlobalVariables.getSelectedEnvironmentCode();
    View mView;
    PieChart mChart;
    LineChart linechart;
    TextView issue_header_left, issue_header_right, subtitle_left, subtitle_right, widget_header;
    ArrayList<LineDataSet> _dataSets_;
    ArrayList<String> _issue_labels_, _receipt_labels_;
    LineDataSet _issue_dataset_, _receipt_dataset_;
    ArrayList<Entry> _receipt_entries_, _issue_entries_;
    Typeface custom_font_bold, custom_font_light, custom_font_condensed, custom_font_thin;
    SharedPreferences prefs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_main_dashboard_page2, container, false);
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
        if (requestCode == 21) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                double draft_value = jo.getInt("Draft");
                double submitted_value = jo.getInt("Submitted");
                double picklist_value = jo.getInt("Picklist");
                double issued_value = jo.getInt("Issued");
                float _multiplier = 1; // this multiplier doesn't make a difference

                ArrayList<PieEntry> entries = new ArrayList<>();
                if (draft_value != 0)
                    entries.add(new PieEntry((float) ((draft_value * _multiplier) + _multiplier / 5), Constants.Draft));
                if (submitted_value != 0)
                    entries.add(new PieEntry((float) ((submitted_value * _multiplier) + _multiplier / 5), Constants.Submitted));
                if (picklist_value != 0)
                    entries.add(new PieEntry((float) ((picklist_value * _multiplier) + _multiplier / 5), Constants.Picklist));
                if (issued_value != 0)
                    entries.add(new PieEntry((float) ((issued_value * _multiplier) + _multiplier / 5), Constants.Issued));

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setSliceSpace(0f);
                dataSet.setSelectionShift(5f);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                colors.add(getResources().getColor(R.color.material_widget_draft));
                colors.add(getResources().getColor(R.color.material_widget_submitted));
                colors.add(getResources().getColor(R.color.material_widget_picklist));
                colors.add(getResources().getColor(R.color.material_widget_issue));

                dataSet.setColors(colors);
                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);

                moveOffScreen();
                mChart.setData(data);
                mChart.setEntryLabelTypeface(custom_font_light);
                mChart.invalidate();
                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

                Legend l = mChart.getLegend();
                l.setEnabled(false);

                mChart.setEntryLabelColor(Color.WHITE);
                mChart.setEntryLabelTextSize(12f);

            } catch (Exception e) {
                Log.d("mytag", "Error at API 21: " + e.getMessage());
            }
        } else if (requestCode == 221) {
            try {

                _issue_labels_ = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    _issue_entries_.add(new Entry(i, jo.getInt("Quantity")));
                    _issue_labels_.add(i + "");
                }

                if (linechart.getData() != null && linechart.getData().getDataSetCount() > 0) {
                    _issue_dataset_ = (LineDataSet) linechart.getData().getDataSetByIndex(0);
                    _issue_dataset_.setValues(_issue_entries_);
                    _receipt_dataset_ = (LineDataSet) linechart.getData().getDataSetByIndex(1);
                    _receipt_dataset_.setValues(_receipt_entries_);
                    linechart.getData().notifyDataChanged();
                    linechart.notifyDataSetChanged();

                } else {
                    // for issue dataset
                    _issue_dataset_.setValues(_issue_entries_);
                    _issue_dataset_.setLabel("Issue Trend(Quantity)");
                    _issue_dataset_.setDrawValues(false);
                    _issue_dataset_.setColors(new int[]{getResources().getColor(R.color.material_widget_issue)});
                    _issue_dataset_.setDrawFilled(false);
                    _issue_dataset_.setDrawCircleHole(false);
                    _issue_dataset_.setDrawCircles(false);

                    // for issue dataset
                    _receipt_dataset_.setValues(_receipt_entries_);
                    _receipt_dataset_.setLabel("Receive Trend(Quantity)");
                    _receipt_dataset_.setDrawValues(false);
                    _receipt_dataset_.setColors(new int[]{getResources().getColor(R.color.material_widget_receive)});
                    _receipt_dataset_.setDrawFilled(false);
                    _receipt_dataset_.setDrawCircleHole(false);
                    _receipt_dataset_.setDrawCircles(false);

                    LineData lnData = new LineData(_issue_dataset_, _receipt_dataset_);

                    linechart.setData(lnData);
                    linechart.getDescription().setEnabled(false);
                    linechart.getAxisLeft().setDrawGridLines(false);
                    linechart.getXAxis().setDrawGridLines(false);
                    linechart.setDrawGridBackground(false);
                    linechart.getAxisLeft().setAxisMinValue(0);
                    linechart.setBackgroundColor(Color.TRANSPARENT);
                }
                linechart.animateXY(1000, 3000);

                XAxis xAxis = linechart.getXAxis();
                xAxis.setEnabled(false);

                YAxis yRAxis = linechart.getAxisRight();
                yRAxis.setEnabled(false);

                YAxis yLAxis = linechart.getAxisLeft();
                yLAxis.setTypeface(custom_font_light);
                yLAxis.setDrawGridLines(true);
                yLAxis.setTextColor(getResources().getColor(R.color.white));
                yLAxis.setGridColor(getResources().getColor(R.color.white));

                Legend l = linechart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setTextColor(getResources().getColor(R.color.white));
                l.setTypeface(custom_font_light);

            } catch (Exception e) {
                Log.d("mytag", "Error at API 12: " + e.getMessage());
            }
        } else if (requestCode == 222) {
            try {

                _receipt_labels_ = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    _receipt_entries_.add(new Entry(i, jo.getInt("Quantity")));
                    _receipt_labels_.add(i + "");
                }

                if (linechart.getData() != null && linechart.getData().getDataSetCount() > 0) {
                    _issue_dataset_ = (LineDataSet) linechart.getData().getDataSetByIndex(0);
                    _issue_dataset_.setValues(_issue_entries_);
                    _receipt_dataset_ = (LineDataSet) linechart.getData().getDataSetByIndex(1);
                    _receipt_dataset_.setValues(_receipt_entries_);
                    linechart.getData().notifyDataChanged();
                    linechart.notifyDataSetChanged();

                } else {
                    // for receipt dataset
                    _receipt_dataset_.setValues(_receipt_entries_);
                    _receipt_dataset_.setLabel("Receive Trend(Quantity)");
                    _receipt_dataset_.setDrawValues(false);
                    _receipt_dataset_.setColors(new int[]{getResources().getColor(R.color.material_DarkBrown)});
                    _receipt_dataset_.setFillColor(getResources().getColor(R.color.material_DarkBrown));
                    _receipt_dataset_.setDrawFilled(true);
                    _receipt_dataset_.setDrawCircleHole(false);
                    _receipt_dataset_.setDrawCircles(false);

                    // for issue dataset
                    _issue_dataset_.setValues(_issue_entries_);
                    _issue_dataset_.setLabel("Issue Trend(Quantity)");
                    _issue_dataset_.setDrawValues(false);
                    _issue_dataset_.setColors(new int[]{getResources().getColor(R.color.material_primary)});
                    _issue_dataset_.setFillColor(getResources().getColor(R.color.material_primary));
                    _issue_dataset_.setDrawFilled(true);
                    _issue_dataset_.setDrawCircleHole(false);
                    _issue_dataset_.setDrawCircles(false);

                    LineData llnData = new LineData(_issue_dataset_, _receipt_dataset_);

                    linechart.setData(llnData);
                    linechart.getDescription().setEnabled(false);
                    linechart.getAxisLeft().setDrawGridLines(false);
                    linechart.getXAxis().setDrawGridLines(false);
                    linechart.setDrawGridBackground(false);
                    linechart.getAxisLeft().setAxisMinValue(0);
                    linechart.setBackgroundColor(Color.TRANSPARENT);
                }
                linechart.animateXY(1000, 3000);

                XAxis xAxis = linechart.getXAxis();
                xAxis.setEnabled(false);

                YAxis yRAxis = linechart.getAxisRight();
                yRAxis.setEnabled(false);

                YAxis yLAxis = linechart.getAxisLeft();
                yLAxis.setTypeface(custom_font_light);
                yLAxis.setDrawGridLines(true);
                yLAxis.setTextColor(getResources().getColor(R.color.white));
                yLAxis.setGridColor(getResources().getColor(R.color.white));

                Legend l = linechart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setTextColor(getResources().getColor(R.color.white));
                l.setTypeface(custom_font_light);

            } catch (Exception e) {
                Log.d("mytag", "Error at API 12: " + e.getMessage());
            }
        } else if (requestCode == 23) {
            try {

                JSONObject jo = jsonArray.getJSONObject(0);
                String last_facility = jo.getString("LastIssuedFacility");
                String last_issed = jo.getString("LastIssuedDate");
                issue_header_left.setText(last_facility);
                issue_header_right.setText(returnMomentFromDate(last_issed));

            } catch (Exception e) {
                Log.d("mytag", "Error at API 23: " + e.getMessage());
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

    private void setView(View view) {
        mView = view;
    }

    public void CreateReportViews() {
        custom_font_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        custom_font_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        custom_font_condensed = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Condensed.ttf");
        custom_font_thin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");

        _dataSets_ = new ArrayList<>();

        prefs = this.getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);

        mChart = (PieChart) mView.findViewById(R.id.dashboard_issue_linechart_halfpiechart);
        mChart.setNoDataText("Loading");
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setTransparentCircleColor(getResources().getColor(R.color.material_panel));
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setMaxAngle(180f); // HALF CHART
        mChart.setRotationAngle(180f);
        mChart.setCenterTextOffset(0, -20);

        linechart = (LineChart) mView.findViewById(R.id.dashboard_issue_linechart);
        linechart.setNoDataText("Loading");

        issue_header_left = (TextView) mView.findViewById(R.id.tv_dashboard_issue_tab_left);
        issue_header_left.setTypeface(custom_font_bold);
        issue_header_right = (TextView) mView.findViewById(R.id.tv_dashboard_issue_tab_right);
        issue_header_right.setTypeface(custom_font_bold);

        widget_header = (TextView) mView.findViewById(R.id.widget_header);
        widget_header.setTypeface(custom_font_light);

        subtitle_left = (TextView) mView.findViewById(R.id.subtitle_left);
        subtitle_left.setTypeface(custom_font_condensed);
        subtitle_right = (TextView) mView.findViewById(R.id.subtitle_right);
        subtitle_right.setTypeface(custom_font_condensed);

        _receipt_entries_ = new ArrayList<>();
        _issue_entries_ = new ArrayList<>();

        _issue_dataset_ = new LineDataSet(null, null);
        _receipt_dataset_ = new LineDataSet(null, null);
    }

    public void LoadChartData() {
        //
        int SelectedItemID = prefs.getInt("SelectedItem", 0);
        // Issue Header
        new DataServiceTask(this, 23, false, false).execute("StockStatus/EnvironmentTransactionSummary?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");
        // Issue Vs Receipt Trend
        if (_dataSets_.size() != 0) // clearing the arraylist
            _dataSets_.clear();
        new DataServiceTask(this, 221, false, false).execute("Issue/IssueTrend?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");
        new DataServiceTask(this, 222, false, false).execute("Receipt/ReceiveTrend?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");
        // Issue Status
        new DataServiceTask(this, 21, false, false).execute("Order/GetOrderStatus?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");
    }

    public String returnMomentFromDate(String Date) {
        if (Date.equalsIgnoreCase("0001-01-01T00:00:00")) return " - ";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        java.util.Date date = null;
        try {
            date = simpleDateFormat.parse(Date.replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null)
            return Helper.getMomentFromNow(date).replace("moments ago", "right now").replace("minute", "min").replace("minutes", "min").replace("hours", "hrs").replace("hour", "h");
        else return " - ";
    }

    private void moveOffScreen() {

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated

        int offset = (int) (height * 0.15); /* percent to move */

        LinearLayout.LayoutParams rlParams =
                (LinearLayout.LayoutParams) mChart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        mChart.setLayoutParams(rlParams);
    }
}
