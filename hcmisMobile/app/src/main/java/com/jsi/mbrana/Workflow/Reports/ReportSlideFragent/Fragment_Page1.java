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

public class Fragment_Page1 extends Fragment implements IDataNotification {

    String environment_code = GlobalVariables.getSelectedEnvironmentCode();
    View mView;
    TextView tv_left, tv_right, subtitle_left, subtitle_right, widget_header;
    LineChart issue_receive_linechart;
    PieChart mChart;
    ArrayList<LineDataSet> _dataSets_;
    ArrayList<String> _issue_labels_;
    ArrayList<String> _receipt_labels_;
    LineDataSet _issue_dataset_, _receipt_dataset_;
    ArrayList<Entry> _receipt_entries_, _issue_entries_;
    Typeface custom_font_bold, custom_font_light, custom_font_condensed, custom_font_thin;
    SharedPreferences prefs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_main_dashboard_page1, container, false);
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
        if (requestCode == 11) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                String PendingReceipts = jo.getString("PendingReceipts");
                String lastReceiveDate = jo.getString("LastReceiptDate");
                tv_left.setText(PendingReceipts);
                tv_right.setText(returnMomentFromDate(lastReceiveDate));
            } catch (Exception e) {
                Log.d("mytag", "Error at API 11: " + e.getMessage());
            }
        } else if (requestCode == 121) {
            try {
                _issue_labels_ = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    _issue_entries_.add(new Entry(i, jo.getInt("Quantity")));
                    _issue_labels_.add(i + "");
                }

                if (issue_receive_linechart.getData() != null && issue_receive_linechart.getData().getDataSetCount() > 0) {
                    _issue_dataset_ = (LineDataSet) issue_receive_linechart.getData().getDataSetByIndex(0);
                    _issue_dataset_.setValues(_issue_entries_);
                    _receipt_dataset_ = (LineDataSet) issue_receive_linechart.getData().getDataSetByIndex(1);
                    _receipt_dataset_.setValues(_receipt_entries_);
                    issue_receive_linechart.getData().notifyDataChanged();
                    issue_receive_linechart.notifyDataSetChanged();

                } else {
                    // for issue dataset
                    _issue_dataset_.setValues(_issue_entries_);
                    _issue_dataset_.setLabel("Issue Trend(Quantity)");
                    _issue_dataset_.setHighlightEnabled(true);
                    _issue_dataset_.setHighlightEnabled(true);
                    _issue_dataset_.setDrawHighlightIndicators(true);
                    _issue_dataset_.setDrawValues(false);
                    _issue_dataset_.setColors(new int[]{getResources().getColor(R.color.material_widget_issue)});
                    _issue_dataset_.setDrawFilled(false);
                    _issue_dataset_.setDrawCircleHole(false);
                    _issue_dataset_.setDrawCircles(false);

                    // for issue dataset
                    _receipt_dataset_.setValues(_receipt_entries_);
                    _receipt_dataset_.setLabel("Receive Trend(Quantity)");
                    _receipt_dataset_.setHighlightEnabled(true);
                    _receipt_dataset_.setHighlightEnabled(true);
                    _receipt_dataset_.setDrawHighlightIndicators(true);
                    _receipt_dataset_.setDrawValues(false);
                    _receipt_dataset_.setColors(new int[]{getResources().getColor(R.color.material_widget_receive)});
                    _receipt_dataset_.setDrawFilled(false);
                    _receipt_dataset_.setDrawCircleHole(false);
                    _receipt_dataset_.setDrawCircles(false);

                    LineData lnData = new LineData(_issue_dataset_, _receipt_dataset_);

                    issue_receive_linechart.setData(lnData);
                    issue_receive_linechart.getDescription().setEnabled(false);
                    issue_receive_linechart.setDrawGridBackground(false);
                    issue_receive_linechart.getAxisLeft().setAxisMinValue(0);
                    issue_receive_linechart.setBackgroundColor(Color.TRANSPARENT);
                }
                issue_receive_linechart.animateXY(1000, 3000);

                XAxis xAxis = issue_receive_linechart.getXAxis();
                xAxis.setEnabled(false);

                YAxis yRAxis = issue_receive_linechart.getAxisRight();
                yRAxis.setEnabled(false);

                YAxis yLAxis = issue_receive_linechart.getAxisLeft();
                yLAxis.setTypeface(custom_font_light);
                yLAxis.setDrawGridLines(true);
                yLAxis.setTextColor(getResources().getColor(R.color.white));
                yLAxis.setGridColor(getResources().getColor(R.color.white));

                Legend l = issue_receive_linechart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setTextColor(getResources().getColor(R.color.white));
                l.setTypeface(custom_font_light);

            } catch (Exception e) {
                Log.d("mytag", "Error at API 12: " + e.getMessage());
            }
        } else if (requestCode == 122) {
            try {

                _receipt_labels_ = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    _receipt_entries_.add(new Entry(i, jo.getInt("Quantity")));
                    _receipt_labels_.add(i + "");
                }

                if (issue_receive_linechart.getData() != null && issue_receive_linechart.getData().getDataSetCount() > 0) {
                    _issue_dataset_ = (LineDataSet) issue_receive_linechart.getData().getDataSetByIndex(0);
                    _issue_dataset_.setValues(_issue_entries_);
                    _receipt_dataset_ = (LineDataSet) issue_receive_linechart.getData().getDataSetByIndex(1);
                    _receipt_dataset_.setValues(_receipt_entries_);
                    issue_receive_linechart.getData().notifyDataChanged();
                    issue_receive_linechart.notifyDataSetChanged();

                } else {
                    // for receipt dataset
                    _receipt_dataset_.setValues(_receipt_entries_);
                    _receipt_dataset_.setLabel("Receive Trend(Quantity)");
                    _receipt_dataset_.setDrawValues(false);
                    _receipt_dataset_.setColors(new int[]{getResources().getColor(R.color.material_widget_receive)});
                    _receipt_dataset_.setDrawFilled(true);
                    _receipt_dataset_.setDrawCircleHole(false);
                    _receipt_dataset_.setDrawCircles(false);

                    // for issue dataset
                    _issue_dataset_.setValues(_issue_entries_);
                    _issue_dataset_.setLabel("Issue Trend(Quantity)");
                    _issue_dataset_.setDrawValues(false);
                    _issue_dataset_.setColors(new int[]{getResources().getColor(R.color.material_widget_issue)});
                    _issue_dataset_.setDrawFilled(true);
                    _issue_dataset_.setDrawCircleHole(false);
                    _issue_dataset_.setDrawCircles(false);

                    LineData llnData = new LineData(_issue_dataset_, _receipt_dataset_);

                    issue_receive_linechart.setData(llnData);
                    issue_receive_linechart.getDescription().setEnabled(false);
                    issue_receive_linechart.getAxisLeft().setDrawGridLines(false);
                    issue_receive_linechart.getXAxis().setDrawGridLines(false);
                    issue_receive_linechart.setDrawGridBackground(false);
                    issue_receive_linechart.getAxisLeft().setAxisMinValue(0);
                    issue_receive_linechart.setBackgroundColor(Color.TRANSPARENT);
                }
                issue_receive_linechart.animateXY(1000, 3000);

                XAxis xAxis = issue_receive_linechart.getXAxis();
                xAxis.setEnabled(false);

                YAxis yRAxis = issue_receive_linechart.getAxisRight();
                yRAxis.setEnabled(false);

                YAxis yLAxis = issue_receive_linechart.getAxisLeft();
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
                yLAxis.setTypeface(custom_font);
                yLAxis.setDrawGridLines(true);
                yLAxis.setTextColor(getResources().getColor(R.color.white));
                yLAxis.setGridColor(getResources().getColor(R.color.white));

                Legend l = issue_receive_linechart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setTextColor(getResources().getColor(R.color.white));
                l.setTypeface(custom_font);

            } catch (Exception e) {
                Log.d("mytag", "Error at API 12: " + e.getMessage());
            }
        } else if (requestCode == 13) {
            try {
                JSONObject jo = jsonArray.getJSONObject(0);
                double draft_value = jo.getDouble("Draft");
                double submitted_value = jo.getDouble("Submitted");
                float _multiplier = 1; // this multiplier doesn't make a difference

                ArrayList<PieEntry> entries = new ArrayList<>();
                if (draft_value != 0)
                    entries.add(new PieEntry((float) ((draft_value * _multiplier) + _multiplier / 5), Constants.Draft));
                if (submitted_value != 0)
                    entries.add(new PieEntry((float) ((submitted_value * _multiplier) + _multiplier / 5), Constants.Submitted));

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setSliceSpace(0f);
                dataSet.setSelectionShift(5f);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                colors.add(getResources().getColor(R.color.material_widget_draft));
                colors.add(getResources().getColor(R.color.material_widget_submitted));
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);

                moveOffScreen();
                mChart.setData(data);
                mChart.highlightValues(null);
                mChart.setEntryLabelTypeface(custom_font_light);
                mChart.invalidate();
                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

                Legend l = mChart.getLegend();
                l.setEnabled(false);

                mChart.setEntryLabelColor(Color.WHITE);
                mChart.setEntryLabelTextSize(12f);

            } catch (Exception e) {
                Log.d("mytag", "Error at API 12: " + e.getMessage());
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

    public void CreateReportViews() {
        custom_font_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Bold.ttf");
        custom_font_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        custom_font_condensed = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Condensed.ttf");
        custom_font_thin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");

        _dataSets_ = new ArrayList<>();

        prefs = this.getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);

        tv_left = (TextView) mView.findViewById(R.id.tv_dashboard_receive_tab_left);
        tv_left.setTypeface(custom_font_bold);
        tv_right = (TextView) mView.findViewById(R.id.tv_dashboard_receive_tab_right);
        tv_right.setTypeface(custom_font_bold);

        subtitle_left = (TextView) mView.findViewById(R.id.subtitle_left);
        subtitle_left.setTypeface(custom_font_condensed);
        subtitle_right = (TextView) mView.findViewById(R.id.subtitle_right);
        subtitle_right.setTypeface(custom_font_condensed);

        issue_receive_linechart = (LineChart) mView.findViewById(R.id.dashboard_receive_linechart);
        issue_receive_linechart.setNoDataText("Loading");
        issue_receive_linechart.setBorderColor(getResources().getColor(R.color.white));
        issue_receive_linechart.setGridBackgroundColor(getResources().getColor(R.color.white));

        mChart = (PieChart) mView.findViewById(R.id.dashboard_receive_linechart_piechart);
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

        _receipt_entries_ = new ArrayList<>();
        _issue_entries_ = new ArrayList<>();
        _issue_dataset_ = new LineDataSet(null, null);
        _receipt_dataset_ = new LineDataSet(null, null);

        widget_header = (TextView) mView.findViewById(R.id.widget_header);
        widget_header.setTypeface(custom_font_light);
    }

    public void LoadChartData() {
        //
        int SelectedItemID = prefs.getInt("SelectedItem", 0);

        // Receipt Summary API
        new DataServiceTask(this, 11, false, false).execute("Receipt/GetEnvironmentReceiptSummary?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");

        // Issue Vs Receipt Trend
        if (_dataSets_.size() != 0) // clearing the arraylist
            _dataSets_.clear();
        new DataServiceTask(this, 121, false, false).execute("Issue/IssueTrend?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");
        new DataServiceTask(this, 122, false, false).execute("Receipt/ReceiveTrend?EnvironmentCode=" + environment_code + "&ItemID=" + SelectedItemID, "");

        // Receipt Status
        // => Item filter not added
        new DataServiceTask(this, 13).execute("Receipt/GetReceiptStatus?EnvironmentCode=" + environment_code, "");

    }

    public View getView() {
        return mView;
    }

    public void setView(View v) {
        mView = v;
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
