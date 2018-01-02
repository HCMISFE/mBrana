package com.jsi.mbrana.Workflow.Adapter.Dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by surafel on 11/11/2016.
 */

public class Dashboard_PO_Recycler_Adapter extends RecyclerView.Adapter {

    public TextView tv_rowno, tv_recycler_item, tv_recycler_unit, tv_recycler_soh;
    ArrayList<ItemModel> objects;
    Context context;
    RecyclerView.ViewHolder holders;
    LinearLayout view_row;

    public Dashboard_PO_Recycler_Adapter(Context leContext, ArrayList<ItemModel> objects) {
        this.objects = objects;
        context = leContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_main_dashboard_page3_recyclerview, parent, false);
        return new Dashboard_PO_Recycler_Adapter.ViewHolderRecycler(View);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemModel item = objects.get(position);
        holders = holder;

//        ViewHolderRecycler itemview = (ViewHolderRecycler) holder;
        holder.itemView.setSelected(true);
        tv_rowno.setText((position + 1) + "");
        tv_recycler_item.setText(item.getItemName() + "");
        tv_recycler_unit.setText(item.getUnit() + "");
        tv_recycler_soh.setText(item.getSOH() + "");

        String val = item.getStockStatus();
        if (Objects.equals(val, Constants.StockedOut)) {
            tv_recycler_soh.setBackgroundResource(R.drawable.ss_stockout_rounded);
        } else if (Objects.equals(val, Constants.BelowMin)) {
            tv_recycler_soh.setBackgroundResource(R.drawable.ss_belowmin_rounded);
        } else if (Objects.equals(val, Constants.BelowEOP)) {
            tv_recycler_soh.setBackgroundResource(R.drawable.ss_beloweop_rounded);
        } else if (Objects.equals(val, Constants.Normal)) {
            tv_recycler_soh.setBackgroundResource(R.drawable.ss_normal_rounded);
        } else if (Objects.equals(val, Constants.Excess)) {
            tv_recycler_soh.setBackgroundResource(R.drawable.ss_excess_rounded);
        } else {
            tv_recycler_soh.setBackgroundResource(R.drawable.ss_unknown_rounded);
        }

        int index = objects.indexOf(item);
        if ((index % 2) != 0) {
            view_row.setBackgroundResource(R.color.whitesmoke);
        } else {
            view_row.setBackgroundResource(R.color.white);
        }

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolderRecycler extends RecyclerView.ViewHolder {

        public ViewHolderRecycler(View view) {
            super(view);
            view_row = (LinearLayout) view.findViewById(R.id.rv_view_row);
            tv_rowno = (TextView) view.findViewById(R.id.rv_rowno);
            tv_recycler_item = (TextView) view.findViewById(R.id.dashboard_soh_recycler_item);
            tv_recycler_unit = (TextView) view.findViewById(R.id.dashboard_soh_recycler_unit);
            tv_recycler_soh = (TextView) view.findViewById(R.id.dashboard_soh_recycler_soh);
        }
    }
}
