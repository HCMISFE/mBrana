package com.jsi.mbrana.Workflow.Adapter.PO;

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
 * Created by pc-6 on 6/18/2016.
 */
public class RequestedPOAdapter extends ArrayAdapter {
    ArrayList<ItemModel> objects;

    public RequestedPOAdapter(Context context, int resource, ArrayList<ItemModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_requestedpo, null);
        }

        ItemModel item = objects.get(position);

        if (item != null) {
            TextView rowno = (TextView) view.findViewById(R.id.requestedPO_rowno);
            TextView tvItemColumn = (TextView) view.findViewById(R.id.requestedPO_ItemColumn);
            TextView requestedPO_DTextview = (TextView) view.findViewById(R.id.requestedPO_DTextview);
            TextView requestedPO_WFTextview = (TextView) view.findViewById(R.id.requestedPO_WFTextview);
            TextView requestedPO_TCTextview = (TextView) view.findViewById(R.id.requestedPO_TCTextview);
            TextView requestedPO_BBTextview = (TextView) view.findViewById(R.id.requestedPO_BBTextview);
            TextView requestedPO_QRTextview = (TextView) view.findViewById(R.id.requestedPO_QRTextview);
            TextView requestedPO_CTextview = (TextView) view.findViewById(R.id.requestedPO_CTextview);
            TextView requestedPO_LTextview = (TextView) view.findViewById(R.id.requestedPO_LTextview);
            TextView requestedPO_EBTextview = (TextView) view.findViewById(R.id.requestedPO_EBTextview);
            TextView requestedPO_QtyColumn = (TextView) view.findViewById(R.id.requestedPO_QtyColumn);
            TextView systemPO_QtyColumn = (TextView) view.findViewById(R.id.systemPO_QtyColumn);


            rowno.setText((position + 1) + "");
            tvItemColumn.setText(item.getItemCode() + "");
            requestedPO_DTextview.setText(item.getDose() + "");
            requestedPO_WFTextview.setText(item.getWasteFactor() + "");
            requestedPO_TCTextview.setText(item.getTargetCoverage() + "");
            requestedPO_BBTextview.setText(item.getBeginningBalance() + "");
            requestedPO_QRTextview.setText(item.getQuantityReceived() + "");
            requestedPO_CTextview.setText(item.getConsumption() + "");
            requestedPO_LTextview.setText(item.getLoss() + "");
            requestedPO_EBTextview.setText(item.getEndingBalance() + "");
            requestedPO_QtyColumn.setText(item.getQuantity() + "");
            systemPO_QtyColumn.setText(item.getSystemQuantity() + "");
        }

        return view;
    }
}