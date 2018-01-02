package com.jsi.mbrana.Workflow.Adapter.PO;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Models.RRFModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;

public class ShowOrderAdapter extends RecyclerView.Adapter {
    ArrayList<RRFModel> item_model;
    Context context;
    RecyclerView.ViewHolder holders;
    int selected_position = -1;
    Constants cons = new Constants();

    public ShowOrderAdapter(Context leContext, ArrayList<RRFModel> item_model) {
        context = leContext;
        this.item_model = item_model;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_show_vrf, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RRFModel item = item_model.get(position);
        holders = holder;

        ViewHolder itemview = (ViewHolder) holder;
        holder.itemView.setSelected(true);
        itemview.tv_rowno.setText((position + 1) + ".");
        itemview.tv_item.setText(item.getProductCN() + "");
        itemview.tv_unit.setText(item.getUnit() + "");
        itemview.tv_beginning_balance.setText(item.getBeginningBalance() + "");
        itemview.tv_quantity_received.setText(item.getQuantityReceived() + "");
        itemview.tv_ending_balance.setText(item.getEndingBalance() + "");
        itemview.tv_max_stock_quantity.setText(item.getMaxStockQuantity() + "");
        itemview.tv_required_for_next_supply_period.setText(item.getRequiredForNextSupplyPeriod() + "");
        itemview.tv_requested_quantity.setText(item.getRequestedQuantity() + "");

        if ((position % 2) != 0) {
            itemview.itemView.setBackgroundResource(R.color.whitesmoke);
        } else {
            itemview.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return item_model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rowno, tv_item, tv_unit, tv_beginning_balance, tv_quantity_received, tv_ending_balance,
                tv_max_stock_quantity, tv_C,tv_L, tv_required_for_next_supply_period;
        EditText tv_requested_quantity;

        public ViewHolder(View view) {
            super(view);
            tv_rowno = (TextView) view.findViewById(R.id.vrf_rowno);
            tv_item = (TextView) view.findViewById(R.id.vrf_item);
            tv_unit = (TextView) view.findViewById(R.id.vrf_unit);
            tv_beginning_balance = (TextView) view.findViewById(R.id.vrf_beginning_balance);
            tv_quantity_received = (TextView) view.findViewById(R.id.vrf_quantity_received);
            tv_ending_balance = (TextView) view.findViewById(R.id.vrf_ending_balance);
            tv_C = (TextView) view.findViewById(R.id.vrf_consumption);
            tv_L = (TextView) view.findViewById(R.id.vrf_loss);
            tv_required_for_next_supply_period = (TextView) view.findViewById(R.id.vrf_required_for_next_supply_period);
            tv_requested_quantity = (EditText) view.findViewById(R.id.vrf_requested_quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                    cons.hideSoftKeyboard(v, context);
                }
            });
        }
    }
}
