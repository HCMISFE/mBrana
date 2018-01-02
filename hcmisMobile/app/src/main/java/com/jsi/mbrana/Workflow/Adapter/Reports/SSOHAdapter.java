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
 * Created by surafel on 1/12/2017.
 */
public class SSOHAdapter extends ArrayAdapter {
    ArrayList<ItemModel> objects;

    public SSOHAdapter(Context context, int resource, ArrayList<ItemModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_supplierlist, null);
        }

        ItemModel item = objects.get(position);

        if (item != null) {
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
            TextView tvUnit = (TextView) view.findViewById(R.id.tv_unit);
            TextView tvSupplier = (TextView) view.findViewById(R.id.tv_supplier);
            TextView tvSOH = (TextView) view.findViewById(R.id.tv_soh);

            tvItem.setText(item.getItemNameSH() + "");
            tvUnit.setText(item.getUnit() + "");
            tvSupplier.setText(item.getStockStatus() + "");
            tvSOH.setText(NumberFormat.getNumberInstance(Locale.US).format(item.getSOH()) + "");

            //Handle row strip
            int index = objects.indexOf(item);
            if ((index % 2) != 0) {
                view.setBackgroundResource(R.color.whitesmoke);
            } else {
                view.setBackgroundResource(R.color.white);
            }
        }
        return view;
    }
}