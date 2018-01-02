package com.jsi.mbrana.Workflow.Adapter.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by pc-6 on 7/11/2016.
 */
public class BincardAdapter extends ArrayAdapter {
    ArrayList<ItemModel> objects;

    public BincardAdapter(Context context, int resource, ArrayList<ItemModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_bincard, null);
        }

        ItemModel item = objects.get(position);
        if (item != null) {
            TextView tvDate = (TextView) view.findViewById(R.id.bincard_dateColumn);
            TextView tvTo = (TextView) view.findViewById(R.id.bincard_ToFrom);
            TextView tvReceive = (TextView) view.findViewById(R.id.bincard_receivedColumn);
            TextView tvIssue = (TextView) view.findViewById(R.id.bincard_issuedColumn);
            TextView tvSOH = (TextView) view.findViewById(R.id.bincard_sohColumn);

            tvDate.setText(item.getEthiopianDate() + "");
            tvTo.setText(item.getToFrom() + "");
            tvReceive.setText(NumberFormat.getNumberInstance(Locale.US).format(item.getReceivedQuantity()) + " ");
            tvIssue.setText(NumberFormat.getNumberInstance(Locale.US).format(item.getIssuedQuantity()) + " ");
            if (!item.getVoid())
                tvSOH.setText(NumberFormat.getNumberInstance(Locale.US).format(item.getSOH()) + " ");
            else
                tvSOH.setText("");

            // Row Strip
            int index = objects.indexOf(item);
            if (item.getVoid()) {
                view.setBackgroundResource(R.drawable.rounded_red_stockout_border);
            } else if ((index % 2) != 0) {
                view.setBackgroundResource(R.color.whitesmoke);
            } else {
                view.setBackgroundResource(R.color.white);
            }
        }
        return view;
    }
}