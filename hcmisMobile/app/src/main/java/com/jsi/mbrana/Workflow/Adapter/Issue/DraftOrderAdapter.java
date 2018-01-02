package com.jsi.mbrana.Workflow.Adapter.Issue;

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
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class DraftOrderAdapter extends RecyclerView.Adapter {
    ArrayList<ItemModel> objects;
    Context context;
    Constants cons = new Constants();
    RecyclerView rView;
    private int selected_position = -1;

    public DraftOrderAdapter(Context context, ArrayList<ItemModel> objects, RecyclerView rView) {
        this.objects = objects;
        this.context = context;
        this.rView = rView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_draftorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemModel item = objects.get(position);
        int index = objects.indexOf(item);

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.rowno.setText(position + 1 + "");
        viewHolder.tvItemColumn.setText(item.getItemCode());
        viewHolder.tvQuantityColumn.setText(item.getQuantity() + "");
        viewHolder.unitTV.setText(item.getUnit());
        viewHolder.cSOHTV.setText(item.getcStockOnHand() + "");

        if ((index % 2) != 0) {
            viewHolder.itemView.setBackgroundResource(R.color.whitesmoke);
        } else {
            viewHolder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemColumn, unitTV, rowno;
        EditText cSOHTV, tvQuantityColumn;

        public ViewHolder(View view) {
            super(view);
            tvItemColumn = (TextView) view.findViewById(R.id.draftOrderItemColumn);
            tvQuantityColumn = (EditText) view.findViewById(R.id.draftOrderQtyColumn);
            unitTV = (TextView) view.findViewById(R.id.draftOrderUnitsTextView);
            rowno = (TextView) view.findViewById(R.id.draftorderRowNo);
            cSOHTV = (EditText) view.findViewById(R.id.draftOrdercSOH);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                    cons.hideSoftKeyboard(v, context);
                }
            });

            tvQuantityColumn.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int quantity = (int) ((tvQuantityColumn.getText().toString().length() == 0 ||
                            tvQuantityColumn.getText().toString().equals("")) ? 0 :
                            Double.parseDouble(tvQuantityColumn.getText().toString()));

                    objects.get(getAdapterPosition()).setQuantity(quantity);
                }
            });

            tvQuantityColumn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tvQuantityColumn.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        tvQuantityColumn.setBackgroundResource(R.drawable.inputtextdrawable);
                    }
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

            cSOHTV.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int quantity = (int) ((cSOHTV.getText().toString().length() == 0 ||
                            cSOHTV.getText().toString().equals("")) ? 0 :
                            Double.parseDouble(cSOHTV.getText().toString()));

                    objects.get(getAdapterPosition()).setcStockOnHand(quantity);
                }
            });

            cSOHTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        cSOHTV.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        cSOHTV.setBackgroundResource(R.drawable.inputtextdrawable);
                    }
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
        }
    }
}