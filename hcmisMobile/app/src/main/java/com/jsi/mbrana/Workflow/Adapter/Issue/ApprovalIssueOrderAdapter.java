package com.jsi.mbrana.Workflow.Adapter.Issue;

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
import android.widget.Toast;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class ApprovalIssueOrderAdapter extends RecyclerView.Adapter {
    private static final int editable_row = 0;
    private static final int non_editable_row = 1;
    ArrayList<ItemModel> objects;
    Context context;
    Constants cons = new Constants();
    RecyclerView rView;
    private int selected_position = -1;

    public ApprovalIssueOrderAdapter(Context leContext, ArrayList<ItemModel> objects, RecyclerView rView) {
        this.objects = objects;
        this.context = leContext;
        this.rView = rView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == editable_row) {
            View enabledView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_approvalissueorder_enabled, parent, false);
            return new ViewHolderEnabled(enabledView);
        } else if (viewType == non_editable_row) {
            View disabledView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_approvalissueorder_disabled, parent, false);
            return new ViewHolderDisabled(disabledView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemModel item = objects.get(position);
        int index = objects.indexOf(item);

        if (item.getSOH() == 0) {
            ViewHolderDisabled itemview_d = (ViewHolderDisabled) holder;
            holder.itemView.setSelected(true);
            itemview_d.rowNO_disabled.setText((position + 1) + "");
            itemview_d.tvItemColumn_disabled.setText((item.getItemCode()));
            itemview_d.tvRequestedQuantityColumn_disabled.setText(item.getQuantity() + "");
            itemview_d.tvUnit_disabled.setText(item.getUnit().replace("Vial  of", ""));
            itemview_d.tvSoh_disabled.setText(NumberFormat.getNumberInstance(Locale.US).format(item.getSOH()) + "");
            itemview_d.tvApprovedQuantityColumn_disabled.setText(0 + "");
            itemview_d.tvApprovedQuantityColumn_disabled.setBackground(null);

            if (item.getSOH() == 0) {
                itemview_d.itemView.setBackgroundResource(R.color.lightStockOut);
            } else if ((index % 2) != 0) {
                itemview_d.itemView.setBackgroundResource(R.color.whitesmoke);
            }else{
                itemview_d.itemView.setBackgroundResource(R.color.white);
            }
        } else {
            ViewHolderEnabled itemview_e = (ViewHolderEnabled) holder;
            itemview_e.itemView.setSelected(false);
            itemview_e.rowNO_enabled.setText((position + 1) + "");
            itemview_e.tvItemColumn_enabled.setText((item.getItemCode()));
            itemview_e.tvRequestedQuantityColumn_enabled.setText(item.getQuantity() + "");
            itemview_e.tvUnit_enabled.setText(item.getUnit().replace("Vial  of", ""));
            itemview_e.tvSoh_enabled.setText(NumberFormat.getNumberInstance(Locale.US).format(item.getSOH()) + "");
            itemview_e.tvApprovedQuantityColumn_enabled.setText(item.getApprovedQuantity() + "");

            if (item.getSOH() == 0) {
                itemview_e.itemView.setBackgroundResource(R.color.lightStockOut);
            } else if ((index % 2) != 0) {
                itemview_e.itemView.setBackgroundResource(R.color.whitesmoke);
            }else{
                itemview_e.itemView.setBackgroundResource(R.color.white);
            }
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).getSOH() != 0 ? editable_row : non_editable_row;
    }

    public class ViewHolderDisabled extends RecyclerView.ViewHolder {
        public TextView tvItemColumn_disabled, tvRequestedQuantityColumn_disabled, tvSoh_disabled, tvUnit_disabled, rowNO_disabled;
        public EditText tvApprovedQuantityColumn_disabled;

        public ViewHolderDisabled(View view) {
            super(view);
            tvItemColumn_disabled = (TextView) view.findViewById(R.id.approveIssueOrder_ItemColumn_disabled);
            tvApprovedQuantityColumn_disabled = (EditText) itemView.findViewById(R.id.approveIssueOrder_approvedQtyColumn_enabled);
            tvRequestedQuantityColumn_disabled = (TextView) view.findViewById(R.id.approveIssueOrder_requestedQtyColumn_disabled);
            tvSoh_disabled = (TextView) view.findViewById(R.id.approveIssueOrder_sohColumn_disabled);
            tvUnit_disabled = (TextView) view.findViewById(R.id.approveIssueOrder_Unit_disabled);
            rowNO_disabled = (TextView) view.findViewById(R.id.approveIssueOrder_rowno_disabled);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                }
            });
        }
    }

    public class ViewHolderEnabled extends RecyclerView.ViewHolder {
        public TextView tvItemColumn_enabled, tvRequestedQuantityColumn_enabled, tvSoh_enabled, tvUnit_enabled, rowNO_enabled;
        public EditText tvApprovedQuantityColumn_enabled;

        public ViewHolderEnabled(View view) {
            super(view);
            tvItemColumn_enabled = (TextView) view.findViewById(R.id.approveIssueOrder_ItemColumn_enabled);
            tvApprovedQuantityColumn_enabled = (EditText) itemView.findViewById(R.id.approveIssueOrder_approvedQtyColumn_enabled);
            tvRequestedQuantityColumn_enabled = (TextView) view.findViewById(R.id.approveIssueOrder_requestedQtyColumn_enabled);
            tvSoh_enabled = (TextView) view.findViewById(R.id.approveIssueOrder_sohColumn_enabled);
            tvUnit_enabled = (TextView) view.findViewById(R.id.approveIssueOrder_Unit_enabled);
            rowNO_enabled = (TextView) view.findViewById(R.id.approveIssueOrder_rowno_enabled);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                    // Hide the keyboard
                    cons.hideSoftKeyboard(v, context);
                }
            });

            tvApprovedQuantityColumn_enabled.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        tvApprovedQuantityColumn_enabled.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        tvApprovedQuantityColumn_enabled.setBackgroundResource(R.drawable.inputtextdrawable);
                    }
                }
            });

            tvApprovedQuantityColumn_enabled.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            tvApprovedQuantityColumn_enabled.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    final ItemModel item = objects.get(getAdapterPosition());
                    int quantity = (int) ((tvApprovedQuantityColumn_enabled.getText().toString().length() == 0 ||
                            tvApprovedQuantityColumn_enabled.getText().toString().equals("")) ? 0 :
                            Double.parseDouble(tvApprovedQuantityColumn_enabled.getText().toString()));
                    // check if the quantity is greater than soh
                    if (quantity <= item.getSOH()) {
                        ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("OrdersToBeApporved")).get(getAdapterPosition()).setApprovedQuantity(quantity);
                        objects.get(getAdapterPosition()).setApprovedQuantity(quantity);
                        //ArrayList<ItemModel> str = ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("OrdersToBeApporved"));
                    } else {
                        // check if the requested quantity is greater than soh
                        if (item.getQuantity() <= item.getSOH()) {
                            ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("OrdersToBeApporved")).get(getAdapterPosition()).setApprovedQuantity(objects.get(getAdapterPosition()).getQuantity());
                            objects.get(getAdapterPosition()).setApprovedQuantity(objects.get(getAdapterPosition()).getQuantity());
                            tvApprovedQuantityColumn_enabled.setText(objects.get(getAdapterPosition()).getQuantity() + "");
                        } else {
                            ((ArrayList<ItemModel>) ((Activity) context).getIntent().getSerializableExtra("OrdersToBeApporved")).get(getAdapterPosition()).setApprovedQuantity(objects.get(getAdapterPosition()).getSOH());
                            objects.get(getAdapterPosition()).setApprovedQuantity(objects.get(getAdapterPosition()).getSOH());
                            tvApprovedQuantityColumn_enabled.setText("" + objects.get(getAdapterPosition()).getSOH() + "");
                        }
                        // show error message
                        Toast.makeText(context, "Change error! You can't approve above SOH", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
