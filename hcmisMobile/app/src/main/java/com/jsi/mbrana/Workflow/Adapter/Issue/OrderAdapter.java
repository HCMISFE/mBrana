package com.jsi.mbrana.Workflow.Adapter.Issue;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class OrderAdapter extends Adapter<OrderAdapter.MyViewHolder> {
    private ArrayList<ItemModel> objects;
    private Context context;
    private int selected_position = -1;
    private Constants cons = new Constants();
    private RecyclerView rView;

    public OrderAdapter(Context leContext, ArrayList<ItemModel> Items, RecyclerView rView) {
        this.objects = Items;
        this.context = leContext;
        this.rView = rView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_neworder, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ItemModel item = objects.get(position);

        holder.rowno.setText((position + 1) + "");
        holder.tvItemColumn.setText((item.getItemCode()));
        holder.unitTV.setText(item.getUnit());
        holder.tvQuantityColumn.setText(item.getQuantity() + "");
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemColumn, unitTV, rowno;
        private EditText cSOHTV, tvQuantityColumn;

        private MyViewHolder(View itemView) {
            super(itemView);
            tvItemColumn = (TextView) itemView.findViewById((R.id.neworder_ItemColumn));
            unitTV = (TextView) itemView.findViewById(R.id.neworder_unitsTextview);
            rowno = (TextView) itemView.findViewById(R.id.neworder_rowno);
            cSOHTV = (EditText) itemView.findViewById(R.id.neworder_cSOH);
            tvQuantityColumn = (EditText) itemView.findViewById(R.id.newissueorder_QtyColumn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                    cons.hideSoftKeyboard(v, context);
                }
            });

            cSOHTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        cSOHTV.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        cSOHTV.setBackgroundResource(R.drawable.inputtextdrawable);
                    }
                }
            });

            tvQuantityColumn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tvQuantityColumn.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        tvQuantityColumn.setBackgroundResource(R.drawable.inputtextdrawable);
                    }
                }
            });

            cSOHTV.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int quantity = (int) ((cSOHTV.getText().toString().length() == 0 ||
                            cSOHTV.getText().toString().equals("")) ? 0 :
                            Double.parseDouble(cSOHTV.getText().toString()));
                    objects.get(getAdapterPosition()).setcStockOnHand(quantity);
                    ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("NewIssueDataSet")).get(getAdapterPosition()).setcStockOnHand(quantity);
                }
            });

            tvQuantityColumn.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int quantity = (int) ((tvQuantityColumn.getText().toString().length() == 0 ||
                            tvQuantityColumn.getText().toString().equals("")) ? 0 :
                            Double.parseDouble(tvQuantityColumn.getText().toString()));
                    objects.get(getAdapterPosition()).setQuantity(quantity);
                    ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("NewIssueDataSet")).get(getAdapterPosition()).setQuantity(quantity);
                }
            });

            cSOHTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            tvQuantityColumn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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