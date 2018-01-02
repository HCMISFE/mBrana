package com.jsi.mbrana.Workflow.Adapter.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.R;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class ReportsListAdapter extends ArrayAdapter {
    String[] objects;

    public ReportsListAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        int[] circles = {R.drawable.circle_blue, R.drawable.circle_orange, R.drawable.circle_purple, R.drawable.circle_orange2,
                R.drawable.circle_green, R.drawable.circle_yellow, R.drawable.circle_blue, R.drawable.circle_orange,
                R.drawable.circle_purple, R.drawable.circle_orange2, R.drawable.circle_green, R.drawable.circle_yellow,
                R.drawable.circle_blue, R.drawable.circle_orange, R.drawable.circle_purple, R.drawable.circle_orange2,
                R.drawable.circle_green, R.drawable.circle_yellow, R.drawable.circle_blue, R.drawable.circle_orange,
                R.drawable.circle_purple, R.drawable.circle_orange2, R.drawable.circle_green, R.drawable.circle_yellow,
                R.drawable.circle_blue, R.drawable.circle_orange, R.drawable.circle_purple, R.drawable.circle_orange2,
                R.drawable.circle_green, R.drawable.circle_yellow, R.drawable.circle_blue, R.drawable.circle_orange,
                R.drawable.circle_purple, R.drawable.circle_orange2, R.drawable.circle_green, R.drawable.circle_yellow,
                R.drawable.circle_blue, R.drawable.circle_orange, R.drawable.circle_purple, R.drawable.circle_orange2,
                R.drawable.circle_green, R.drawable.circle_yellow, R.drawable.circle_blue, R.drawable.circle_orange,
                R.drawable.circle_purple, R.drawable.circle_orange2, R.drawable.circle_green, R.drawable.circle_yellow};

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_reportlistview, null);
        }


        String report = objects[position];

        if (report != null) {

            TextView tvFirstLetter = (TextView) view.findViewById(R.id.report_FirstLetter);
            TextView tvReportName = (TextView) view.findViewById(R.id.report_ReportName);

            tvFirstLetter.setText(report.charAt(0)+"");
            tvReportName.setText(report);
            tvFirstLetter.setBackgroundResource(circles[position]);

            //handel row strip
//            int index = objects.indexOf(report);
//            if ((index % 2) != 0) {
//                view.setBackgroundResource(R.color.whitesmoke);
//            } else {
//                view.setBackgroundResource(R.color.white);
//            }

        }

        return view;
    }
}