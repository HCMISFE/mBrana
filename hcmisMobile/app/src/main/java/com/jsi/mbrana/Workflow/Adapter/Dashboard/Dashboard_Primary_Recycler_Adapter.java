package com.jsi.mbrana.Workflow.Adapter.Dashboard;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Helpers.Constants;
import com.robinhood.spark.SparkView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by surafel on 11/15/2016.
 */

public class Dashboard_Primary_Recycler_Adapter extends RecyclerView.Adapter {
    ArrayList<ItemModel> objects;
    Context context;
    RecyclerView.ViewHolder holders;
    Typeface custom_font_bold, custom_font_light, custom_font_condensed, custom_font_thin;

    public Dashboard_Primary_Recycler_Adapter(Context leContext, ArrayList<ItemModel> objects) {
        this.objects = objects;
        context = leContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View case1View = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_main_dashboard_primary_recyclerview, parent, false);
        return new Dashboard_Primary_Recycler_Adapter.MyViewHolder(case1View);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemModel item = objects.get(position);
        holders = holder;

        MyViewHolder view_holder = (MyViewHolder) holder;
        view_holder.tv_rowno.setText(position + 1 + "");
        view_holder.tv_item.setText(item.getItemName() + "");
        view_holder.tv_soh.setText(item.getSOH() + "");
        view_holder.tv_wos.setText(item.getWOS() + "");
        view_holder.tv_git.setText(item.getGIT() + "");
        view_holder.tv_ordered.setText(item.getOrderedQuantity() + "");

        String val = item.getStockStatus();
        if (Objects.equals(val, Constants.StockedOut)) {
            view_holder.tv_soh.setBackgroundResource(R.drawable.ss_stockout_rounded);
        } else if (Objects.equals(val, Constants.BelowMin)) {
            view_holder.tv_soh.setBackgroundResource(R.drawable.ss_belowmin_rounded);
        } else if (Objects.equals(val, Constants.BelowEOP)) {
            view_holder.tv_soh.setBackgroundResource(R.drawable.ss_beloweop_rounded);
        } else if (Objects.equals(val, Constants.Normal)) {
            view_holder.tv_soh.setBackgroundResource(R.drawable.ss_normal_rounded);
        } else if (Objects.equals(val, Constants.Excess)) {
            view_holder.tv_soh.setBackgroundResource(R.drawable.ss_excess_rounded);
        } else {
            view_holder.tv_soh.setBackgroundResource(R.drawable.ss_unknown_rounded);
        }

        int index = objects.indexOf(item);
        if ((index % 2) != 0) {
            view_holder.view_row.setBackgroundResource(R.color.material_grid_row_even);
        } else {
            view_holder.view_row.setBackgroundResource(R.color.material_grid_row_odd);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rowno, tv_item, tv_soh, tv_wos, tv_git, tv_ordered;
        LinearLayout view_row;
        SparkView sparkView;

        private MyViewHolder(View view) {
            super(view);
            custom_font_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
            custom_font_light = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            custom_font_condensed = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Condensed.ttf");
            custom_font_thin = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");

            view_row = (LinearLayout) view.findViewById(R.id.rv_view_row);

            tv_rowno = (TextView) view.findViewById(R.id.rv_rowno);
            tv_rowno.setTypeface(custom_font_condensed);
            tv_item = (TextView) view.findViewById(R.id.tv_item);
            tv_item.setTypeface(custom_font_condensed);
            tv_soh = (TextView) view.findViewById(R.id.rv_soh);
            tv_soh.setTypeface(custom_font_condensed);
            tv_wos = (TextView) view.findViewById(R.id.rv_wos);
            tv_wos.setTypeface(custom_font_condensed);
            tv_git = (TextView) view.findViewById(R.id.rv_git);
            tv_git.setTypeface(custom_font_condensed);
            tv_ordered = (TextView) view.findViewById(R.id.rv_ordered);
            tv_ordered.setTypeface(custom_font_condensed);
            sparkView = (SparkView) view.findViewById(R.id.sparkview);
        }
    }
}