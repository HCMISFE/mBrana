package com.jsi.mbrana.Modules.Receive.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.CommonModels.VVMModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice.ReceiveInvoiceDetailModel;
import com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice.ReceiveInvoiceModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sololia on 6/17/2016.
 */

public class CreateReceiptInvoiceAdapter extends RecyclerView.Adapter<CreateReceiptInvoiceAdapter.MyViewHolder> {
    private List<VVMModel> vvmModelData;
    private ReceiveInvoiceModel receiveInvoiceModelData;
    private ArrayAdapter<String> dataAdapter;
    private Context context;
    private int selected_position = -1;
    private Constants cons = new Constants();
    private RecyclerView rView;

    public CreateReceiptInvoiceAdapter(Context context, List<VVMModel> vvmModelData, ReceiveInvoiceModel receiveInvoiceModelData, RecyclerView rView) {
        this.receiveInvoiceModelData = receiveInvoiceModelData;
        this.vvmModelData = vvmModelData;
        this.context = context;
        this.rView = rView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_new_receipt_invoice, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final int adapterPosition = position;
        ReceiveInvoiceDetailModel model = receiveInvoiceModelData.getReceiveInvoiceDetail().get(position);

        holder.Rowno.setText((position + 1) + "");

        holder.productCN.setText(model.getItemName());

        holder.Received.setText(Math.max(0, model.getQuantity()) + "");

        holder.Unit.setText(model.getUnitOfIssue());

        holder.Batch.setText(model.getBatchNumber() + "");

        holder.androidProgressBar.setProgress(model.getReceivedQuantity() / model.getQuantity());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date expiry = null;
        try {
            expiry = simpleDateFormat.parse(model.getExpDate().replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.Expiry.setText(new SimpleDateFormat("MM-yyyy").format(expiry));

        holder.ManufactureSH.setText(Helper.ManufacturerCleanup(model.getManufacturer()));

        holder.vvmSpinner.setAdapter(dataAdapter);
        holder.vvmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                // Don't know what the code below is for
                //GlobalVariables.setVVMCode(VVMList.get(index).getCode());
                //GlobalVariables.setVVMID(VVMList.get(index).getId());
                // Assigning selection from the spinner to the model
                //receiveInvoiceModelData.getReceiveInvoiceDetail().get(adapterPosition).setVVMCode(VVMList.get(index).getCode());
                //receiveInvoiceModelData.getReceiveInvoiceDetail().get(adapterPosition).set(VVMList.get(index).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if ((position % 2) != 0) {
            holder.itemView.setBackgroundResource(R.color.whitesmoke);
        } else {
            holder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return receiveInvoiceModelData.getReceiveInvoiceDetail().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Rowno, productCN, Expiry, Unit, ManufactureSH;
        Spinner vvmSpinner;
        EditText Received, Batch;
        ProgressBar androidProgressBar;

        MyViewHolder(View itemView) {
            super(itemView);
            productCN = (TextView) itemView.findViewById(R.id.AddNewRIMaterInput_TableRowProductCN);
            Received = (EditText) itemView.findViewById(R.id.AddNewRIMaterInput_TableRowRecievedQuanitity);
            Batch = (EditText) itemView.findViewById(R.id.inputAddNewRIMaterInput_TableRowBatch);
            Expiry = (TextView) itemView.findViewById(R.id.inputAddNewRIMaterInput_TableRowExpiry);
            Unit = (TextView) itemView.findViewById(R.id.AddNewRIMaterInput_TableRowUnit);
            ManufactureSH = (TextView) itemView.findViewById(R.id.inputAddNewRIMaterInput_TableRowManufacturer);
            vvmSpinner = (Spinner) itemView.findViewById(R.id.vvm_spinner);
            Rowno = (TextView) itemView.findViewById(R.id.AddNewRIMaterInput_TableRowNo);
            androidProgressBar = (ProgressBar) itemView.findViewById(R.id.horizontal_progress_bar);
            ArrayList<String> VVMCode = new ArrayList<>();

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

            Received.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Received.setBackgroundResource(R.drawable.inputtextdrawable_focused);
                        ((EditText) v).selectAll();
                    } else {
                        Received.setBackgroundResource(R.drawable.inputtextdrawable);
                    }
                }
            });

            Received.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            Received.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //
                    int quantity = (int) ((Received.getText().toString().length() == 0 || Received.getText().toString().equals(""))
                            ? 0 : Double.parseDouble(Received.getText().toString()));

                    // Update the model
                    receiveInvoiceModelData.getReceiveInvoiceDetail().get(getAdapterPosition()).setQuantity(quantity);
                }
            });

            Batch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            for (int i = 0; i < vvmModelData.size() ; i++) {
                VVMCode.add(vvmModelData.get(i).getCode());
            }

            dataAdapter = new ArrayAdapter<>(context, R.layout.layout_vvm_list, VVMCode);
            dataAdapter.setDropDownViewResource(R.layout.layout_vvm_list);
        }
    }
}

