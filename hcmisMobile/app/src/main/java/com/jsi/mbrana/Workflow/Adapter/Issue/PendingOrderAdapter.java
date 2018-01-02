package com.jsi.mbrana.Workflow.Adapter.Issue;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Models.OrderModel;
import com.jsi.mbrana.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class PendingOrderAdapter extends ArrayAdapter {
    ArrayList<OrderModel> objects, master_object;

    public PendingOrderAdapter(Context context, int resource, ArrayList<OrderModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.master_object = new ArrayList<>();
        this.master_object.addAll(objects);
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
            view = inflater.inflate(R.layout.layout_pendingorder, null);
        }

        OrderModel order = objects.get(position);

        if (order != null) {
            TextView tvDate = (TextView) view.findViewById(R.id.pendingOrder_dateColumn);
            TextView tvStatus = (TextView) view.findViewById(R.id.pendingOrder_statusColumn);
            TextView tvFacility = (TextView) view.findViewById(R.id.pendingOrder_facilityColumn);
            TextView tvFirstLetter = (TextView) view.findViewById(R.id.pendingOrder_FirstLetter);
            TextView tv_requested_number = (TextView) view.findViewById(R.id.tv_requested_number);
            TextView tv_processed_number = (TextView) view.findViewById(R.id.tv_processed_number);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            Date cratedDate = null;
            Date ModifiedDate = null;

            try {
                cratedDate = simpleDateFormat.parse(order.getDateCreated().replace("T", " "));
                ModifiedDate = simpleDateFormat.parse(order.getModifiedDate().replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tvDate.setText(Html.fromHtml("" + Helper.getMomentFromNow(ModifiedDate)));
            tvStatus.setText(Html.fromHtml("<b></b>" + (order.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode()) ? Constants.OrderStatus.ORDERFILLED.getOrderStatus() : order.getOrderStatus())));

            if (order.isCampaign()) {
                tvFacility.setText(Html.fromHtml("<b>" + Helper.shortenFacilityName(order.getFacility()) + " [CA]"));
            } else {
                tvFacility.setText(Html.fromHtml("<b>" + Helper.shortenFacilityName(order.getFacility())));
            }

            if (order.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.DRAFT.getOrderStatusCode())) {
                tvFirstLetter.setText("D");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_blue);
            } else if (order.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.ORDERFILLED.getOrderStatusCode())) {
                tvFirstLetter.setText("S");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_orange2);
            } else if (order.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.APPROVED.getOrderStatusCode())) {
                tvFirstLetter.setText("A");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_purple);
            } else if (order.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.PickListed.getOrderStatusCode())) {
                tvFirstLetter.setText("P");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_purple);
            } else if (order.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.ISSUED.getOrderStatusCode())) {
                tvFirstLetter.setText("I");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_green);
            }else if (order.getOrderStatusCode().equalsIgnoreCase(Constants.OrderStatus.VOID.getOrderStatusCode())) {
                tvFirstLetter.setText("V");
                tvFirstLetter.setBackgroundResource(R.drawable.circle_red);
            }

            tv_requested_number.setText(Html.fromHtml("Req: " + order.getRequestedNoOfItems()));
            tv_processed_number.setText(Html.fromHtml("Proc: " + order.getProcessedNoOfItems()));

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

    public void filter(Boolean isZoneToWoreda) {
        objects.clear();
        if (isZoneToWoreda) {
            for (OrderModel ri : master_object) {
                if (ri.getWoredaToZoneSync()) {
                    objects.add(ri);
                }
            }
        } else {
            objects.addAll(master_object);
        }
        notifyDataSetChanged();
    }
}