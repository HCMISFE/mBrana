package com.jsi.mbrana.Workflow.Adapter.PO;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.Models.POModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Helpers.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pc-6 on 6/18/2016.
 */
public class POAdapter extends ArrayAdapter {
    ArrayList<POModel> objects;

    public POAdapter(Context context, int resource, ArrayList<POModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_polist, null);
        }

        POModel order = objects.get(position);

        if (order != null) {
            TextView tvPONO = (TextView) view.findViewById(R.id.po_ponocolumn);
            TextView tvFirstLetter = (TextView) view.findViewById(R.id.po_FirstLetter);
            TextView tvStatus = (TextView) view.findViewById(R.id.po_postatuscolumn);
            TextView tvSupplier = (TextView) view.findViewById(R.id.po_posuppliercolumn);
            TextView tvitemcount = (TextView) view.findViewById(R.id.po_itemCountColumn);
            TextView tvFillRate = (TextView) view.findViewById(R.id.po_fillrate);
            TextView tvDate = (TextView) view.findViewById(R.id.po_poDatecolumn);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date date = null;

            try {
                date = simpleDateFormat.parse(order.getModifiedDate().replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tvDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
            tvSupplier.setText(order.getSupplier() + "");
            tvPONO.setText(order.getPONumber() + "");
            tvFillRate.setText(order.getFillRate() + "%");

            if (order.getPurchaseOrderStatusCode().equalsIgnoreCase(Constants.PurchaseOrderStatus.Requested.getOrderStatusCode()))
                tvStatus.setText(Constants.PurchaseOrderStatus.Requested.getOrderStatus());
            else tvStatus.setText(order.getPurchaseOrderStatus());

            if (order.getPurchaseOrderStatusCode().equalsIgnoreCase(Constants.PurchaseOrderStatus.Draft.getOrderStatusCode())) {
                tvFirstLetter.setText("D");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_orange2);
            } else if (order.getPurchaseOrderStatusCode().equalsIgnoreCase(Constants.PurchaseOrderStatus.Requested.getOrderStatusCode())) {
                tvFirstLetter.setText("S");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_blue);
            } else if (order.getPurchaseOrderStatusCode().equalsIgnoreCase(Constants.PurchaseOrderStatus.InProcess.getOrderStatusCode())) {
                tvFirstLetter.setText("I");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_purple);
            } else if (order.getPurchaseOrderStatusCode().equalsIgnoreCase(Constants.PurchaseOrderStatus.Processed.getOrderStatusCode())) {
                tvFirstLetter.setText("P");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_green);
            } else if (order.getPurchaseOrderStatusCode().equalsIgnoreCase(Constants.PurchaseOrderStatus.Canceled.getOrderStatusCode())) {
                tvFirstLetter.setText("C");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_red);
            }

            tvitemcount.setText(Html.fromHtml(order.getDetailCount() + " items"));

            //handel row strip
            int index = objects.indexOf(order);
            if ((index % 2) != 0) {
                view.setBackgroundResource(R.color.whitesmoke);
            } else {
                view.setBackgroundResource(R.color.white);
            }
        }

        return view;
    }
}