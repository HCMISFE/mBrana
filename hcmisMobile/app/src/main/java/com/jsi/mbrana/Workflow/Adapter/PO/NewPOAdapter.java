package com.jsi.mbrana.Workflow.Adapter.PO;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Helpers.Constants;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/18/2016.
 */
public class NewPOAdapter extends ArrayAdapter {
    ArrayList<ItemModel> objects;
    Context context;
    TextView tvItemColumn;
    EditText tvQuantityColumn;
    TextView unitTV;
    TextView rowno;
    Constants cons = new Constants();

    public NewPOAdapter(Context context, int resource, ArrayList<ItemModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_create_vrf, null);
        }

        ItemModel item = objects.get(position);

        if (item != null) {
            tvItemColumn = (TextView) view.findViewById(R.id.neworder_ItemColumn_);
            tvQuantityColumn = (EditText) view.findViewById(R.id.order_requested);
            unitTV = (TextView) view.findViewById(R.id.neworder_unitsTextview_);
            rowno = (TextView) view.findViewById(R.id.neworder_rowno_);

            // Setting value
            rowno.setText((position + 1) + "");
            tvItemColumn.setText(item.getItemCode());
            unitTV.setText(item.getUnitOfIssue());

            //
            if (item.getQuantity() != 0)
                tvQuantityColumn.setText(item.getQuantity() + "");

            tvQuantityColumn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tvQuantityColumn.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        tvQuantityColumn.setBackgroundResource(R.drawable.inputtextdrawable);
                        cons.hideSoftKeyboard(v, context);
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
                    int quantity = ((tvQuantityColumn.getText().toString().length() == 0 || tvQuantityColumn.getText().toString().equals("")) ? 0 : Integer.parseInt(tvQuantityColumn.getText().toString()));
                    ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("DraftPOItems")).get(position).setQuantity(quantity);
                }
            });
        }
        return view;
    }
}