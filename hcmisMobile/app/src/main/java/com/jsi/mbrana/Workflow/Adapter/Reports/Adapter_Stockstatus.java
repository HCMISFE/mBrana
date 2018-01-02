package com.jsi.mbrana.Workflow.Adapter.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsi.mbrana.Models.SoH_Model;
import com.jsi.mbrana.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sololia on 7/4/2016.
 */
public class Adapter_Stockstatus extends ArrayAdapter {
    ArrayList<SoH_Model> objects;
    Context context;

    public Adapter_Stockstatus(Context context, int resource, ArrayList<SoH_Model> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_stock_status_report, null);
        }

        final SoH_Model sohs = objects.get(position);

        if (sohs != null) {
            TextView ProductSN = (TextView) view.findViewById(R.id.ss_TableRowProductCN);
            TextView AWC = (TextView) view.findViewById(R.id.ss_TableRowAWC);
            TextView WOS = (TextView) view.findViewById(R.id.ss_TableRowWOS);
            TextView SOH = (TextView) view.findViewById(R.id.ss_TableRowSOH);
            TextView UsableSOH = (TextView) view.findViewById(R.id.ss_TableRowUsableSOH);
            TextView UNIT = (TextView) view.findViewById(R.id.ss_TableGridUnit);
            TextView VVMExpired = (TextView) view.findViewById(R.id.ss_TableGridVVMExpired);
            TextView expired = (TextView) view.findViewById(R.id.tv_expired);
            TextView near_expiry = (TextView) view.findViewById(R.id.tv_near_expiry);
            LinearLayout StockStatusHeaderDetailId = (LinearLayout) view.findViewById(R.id.stockstatusheaderdetailid);

            try {
                ProductSN.setText(sohs.getProductCN());
                UNIT.setText((sohs.getUnit()));
                AWC.setText(NumberFormat.getNumberInstance(Locale.US).format(sohs.getAWC()) + "");
                WOS.setText(NumberFormat.getNumberInstance(Locale.US).format(sohs.getWOS()) + "");
                VVMExpired.setText(0 + "");
                SOH.setText(NumberFormat.getNumberInstance(Locale.US).format(sohs.getSOH()) + "");
                UsableSOH.setText(NumberFormat.getNumberInstance(Locale.US).format(sohs.getUsableSOH()) + "");
                expired.setText(NumberFormat.getNumberInstance(Locale.US).format(sohs.getExpiredQuantity()) + "");
                near_expiry.setText(NumberFormat.getNumberInstance(Locale.US).format(sohs.getNearExpiredQuantity()) + "");
                if (sohs.getSOH() == 0) {
                    StockStatusHeaderDetailId.setBackgroundColor(view.getResources().getColor(R.color.lightStockOut));
                } else {
                    int index = objects.indexOf(sohs);
                    if ((index % 2) != 0) {
                        StockStatusHeaderDetailId.setBackgroundResource(R.color.whitesmoke);
                    } else {
                        StockStatusHeaderDetailId.setBackgroundResource(R.color.white);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Handel row strip
            int index = objects.indexOf(sohs);
            if ((index % 2) != 0) {
                view.setBackgroundResource(R.color.whitesmoke);
            } else {
                view.setBackgroundResource(R.color.white);
            }
            view.setPadding(0, 6, 0, 6);
        }

        return view;
    }
}
