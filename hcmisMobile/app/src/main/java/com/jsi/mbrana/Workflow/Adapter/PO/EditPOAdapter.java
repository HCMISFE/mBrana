package com.jsi.mbrana.Workflow.Adapter.PO;

import android.app.Activity;
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
 * Created by pc-6 on 6/18/2016.
 */
public class EditPOAdapter extends RecyclerView.Adapter {
    ArrayList<ItemModel> objects;
    Context context;
    Constants cons = new Constants();
    RecyclerView rView;
    int selected_position = -1;

    public EditPOAdapter(Context context, ArrayList<ItemModel> objects, RecyclerView rView) {
        this.rView = rView;
        this.objects = objects;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_update_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemModel item = objects.get(position);
        int index = objects.indexOf(item);

        ViewHolder itemview = (ViewHolder) holder;
        itemview.rowno.setText((position + 1) + "");
        itemview.tvItemColumn.setText(item.getItemCode());
        itemview.unitTV.setText(item.getUnitOfIssue());
        itemview.tvQuantityColumn.setText(item.getQuantity() + "");

        if ((index % 2) != 0) {
            itemview.itemView.setBackgroundResource(R.color.whitesmoke);
        } else {
            itemview.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final EditText tvQuantityColumn;
        TextView tvItemColumn;
        TextView unitTV;
        TextView rowno;

        public ViewHolder(View view) {
            super(view);
            rowno = (TextView) view.findViewById(R.id.update_order_rowno);
            tvItemColumn = (TextView) view.findViewById(R.id.update_order_item);
            unitTV = (TextView) view.findViewById(R.id.update_order_unit);
            tvQuantityColumn = (EditText) view.findViewById(R.id.update_order_qty);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                    cons.hideSoftKeyboard(v, context);
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

            tvQuantityColumn.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int quantity = ((tvQuantityColumn.getText().toString().length() == 0 || tvQuantityColumn.getText().toString() == "") ?
                            0 : Integer.parseInt(tvQuantityColumn.getText().toString()));
                    ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("DraftPOItems")).get(getAdapterPosition()).setQuantity(quantity);
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