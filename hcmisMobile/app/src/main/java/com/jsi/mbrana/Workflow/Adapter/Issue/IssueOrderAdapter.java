package com.jsi.mbrana.Workflow.Adapter.Issue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class IssueOrderAdapter extends ArrayAdapter {
    ArrayList<ItemModel> objects;

    public IssueOrderAdapter(Context context, int resource, ArrayList<ItemModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_issueorder, null);
        }

        ItemModel item = objects.get(position);

        if (item != null) {
            TextView tvItemColumn = (TextView) view.findViewById(R.id.IssueOrder_ItemColumn);
            TextView tvRequestedQuantityColumn = (TextView) view.findViewById(R.id.IssueOrder_requestedQtyColumn);
            TextView tvApprovedQuantityColumn = (TextView) view.findViewById(R.id.IssueOrder_approvedQtyColumn);
            TextView tvUnitColumn = (TextView) view.findViewById(R.id.IssueOrder_unitColum);

            tvItemColumn.setText(item.getItemCode());
            tvRequestedQuantityColumn.setText(item.getQuantity() + "");
            tvApprovedQuantityColumn.setText(item.getApprovedQuantity() + "");
            tvUnitColumn.setText(item.getUnit().replace("Vial  of", ""));
        }

        return view;
    }
}