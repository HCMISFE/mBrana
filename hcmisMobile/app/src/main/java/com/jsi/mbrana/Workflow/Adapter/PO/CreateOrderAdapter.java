package com.jsi.mbrana.Workflow.Adapter.PO;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Models.RRFModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;

public class CreateOrderAdapter extends RecyclerView.Adapter {
    ArrayList<RRFModel> item_model;
    Context context;
    RecyclerView.ViewHolder holders;
    int selected_position = -1;
    String ViewType;
    Constants cons = new Constants();
    RecyclerView rView;

    public CreateOrderAdapter(Context leContext, ArrayList<RRFModel> item_model, String ViewType, RecyclerView rView) {
        this.item_model = item_model;
        context = leContext;
        this.ViewType = ViewType;
        this.rView = rView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View enabledView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_create_vrf, parent, false);
        return new ViewHolder(enabledView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RRFModel item = item_model.get(position);
        holders = holder;

        ViewHolder itemview = (ViewHolder) holder;
        holder.itemView.setSelected(true);
        itemview.rowno.setText((position + 1) + "");
        itemview.tvItemColumn.setText(item.getProductCN() + "");
        itemview.unitTV.setText(item.getUnit() + "");
        itemview.tv_required_qty.setText(item.getRequiredForNextSupplyPeriod() + "");
        itemview.tv_requested_qty.setText(item.getRequestedQuantity() + "");

        if (ViewType.equals("Non-Editable")) {
            itemview.tv_requested_qty.setEnabled(false);
        } else if (ViewType.equals("Editable")) {
            itemview.tv_requested_qty.setEnabled(true);
        } else if (ViewType.equals("Emergency")) {
            itemview.tv_requested_qty.setEnabled(true);
            itemview.tv_required_qty.setVisibility(View.GONE);
        }

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
        TextView tvItemColumn, unitTV, rowno, tv_required_qty;
        EditText tv_requested_qty;

        public ViewHolder(View view) {
            super(view);
            rowno = (TextView) view.findViewById(R.id.neworder_rowno_);
            tvItemColumn = (TextView) view.findViewById(R.id.neworder_ItemColumn_);
            unitTV = (TextView) view.findViewById(R.id.neworder_unitsTextview_);
            tv_requested_qty = (EditText) view.findViewById(R.id.order_requested);
            tv_required_qty = (TextView) view.findViewById(R.id.order_required);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                    cons.hideSoftKeyboard(v, context);
                }
            });

            tv_requested_qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tv_requested_qty.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        tv_requested_qty.setBackgroundResource(R.drawable.inputtextdrawable);
                    }
                }
            });

            tv_requested_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int quantity = (int) ((tv_requested_qty.getText().toString().length() == 0 ||
                            tv_requested_qty.getText().toString().equals("")) ? 0 :
                            Double.parseDouble(tv_requested_qty.getText().toString()));
                    item_model.get(getAdapterPosition()).setRequestedQuantity(quantity);
                }
            });

            tv_requested_qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    int position = getLayoutPosition();
                    rView.scrollToPosition(position + 1);
                    RecyclerView.ViewHolder viewHolder = rView.findViewHolderForLayoutPosition(position + 1);
                    if (viewHolder == null) {
                        rView.scrollToPosition(position + 1);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
