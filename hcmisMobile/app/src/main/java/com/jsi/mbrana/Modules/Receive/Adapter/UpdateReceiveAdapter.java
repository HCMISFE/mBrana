package com.jsi.mbrana.Modules.Receive.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsi.mbrana.CommonModels.VVMModel;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveDetailModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;
import com.jsi.mbrana.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sololia on 6/17/2016.
 */
public class UpdateReceiveAdapter extends RecyclerView.Adapter {
    private ReceiveModel objects;
    private List<VVMModel> VVMList;
    private ArrayAdapter<String> dataAdapter;
    private Context context;
    private int selected_position = -1;

    public UpdateReceiveAdapter(Context leContext, ReceiveModel objects, List<VVMModel> VVMList) {
        this.objects = objects;
        this.VVMList = VVMList;
        this.context = leContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_update_receipt_invoice, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReceiveDetailModel item = objects.getReceiveDetail().get(position);

        ViewHolder itemview = (ViewHolder) holder;
        itemview.Rowno.setText((position + 1) + "");
        itemview.Invoiced.setText("Invoiced");
        itemview.productCN.setText(item.getItemName() + "");
        itemview.Received.setText((item.getQuantity()) + "");
        itemview.Unit.setText(item.getUnitOfIssue() + "");
        itemview.Batch.setText(item.getBatchNumber()+ "");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date expiry = null;
        try {
            expiry = simpleDateFormat.parse(item.getExpiryDate().replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        itemview.Expiry.setText(new SimpleDateFormat("MM-yyyy").format(expiry));

        itemview.Manufacturer.setText(Helper.ManufacturerCleanup(item.getManufacturerName()) + "");

        //vvm spinner
        itemview.vvmSpinner.setAdapter(dataAdapter);
        itemview.vvmSpinner.setSelection(item.getVvmId() - 1);
        itemview.vvmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Position", String.valueOf(position));
                GlobalVariables.setVVMCode(VVMList.get(position).getCode());
                GlobalVariables.setVVMID(VVMList.get(position).getId());
                GlobalVariables.setPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if ((position % 2) != 0) {
            itemview.itemView.setBackgroundResource(R.color.whitesmoke);
        } else {
            itemview.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return objects.getReceiveDetail().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Rowno, productCN, Invoiced, Received, Unit, Batch, Expiry, Manufacturer;
        Spinner vvmSpinner;

        public ViewHolder(View itemView) {
            super(itemView);
            productCN = (TextView) itemView.findViewById(R.id.UpdateRIMaterInput_TableRowProductCN);
            Invoiced = (TextView) itemView.findViewById(R.id.inputUpdateRIMaterInput_TableRowInvoicedQ);
            Received = (TextView) itemView.findViewById(R.id.UpdateReceiptInput_TableRowRecievedQuanitity);
            Unit = (TextView) itemView.findViewById(R.id.UpdateRIMaterInput_TableRowUnit);
            Batch = (TextView) itemView.findViewById(R.id.UpdateRIMaterInput_TableRowRecievedBatch);
            Expiry = (TextView) itemView.findViewById(R.id.inputUpdateRIMaterInput_TableRowExpiry);
            Manufacturer = (TextView) itemView.findViewById(R.id.UpdateRIMaterInput_TableRowManufacturer);
            vvmSpinner = (Spinner) itemView.findViewById(R.id.vvm_spinnerupdate);
            Rowno = (TextView) itemView.findViewById(R.id.UpdateRIMaterInput_TableRowNo);

            ArrayList<String> VVMCode = new ArrayList<>();
            for (int i = 0; i < VVMList.size(); i++) {
                VVMCode.add(VVMList.get(i).getCode());
            }

            dataAdapter = new ArrayAdapter<>(context, R.layout.layout_vvm_list, VVMCode);

            dataAdapter.setDropDownViewResource(R.layout.layout_vvm_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
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
                    int quantity = (int) ((Received.getText().toString().length() == 0 ||
                            Received.getText().toString().equals("")) ? 0 :
                            Double.parseDouble(Received.getText().toString()));
                    //Quantity
                    if (quantity != objects.getReceiveDetail().get(getAdapterPosition()).getQuantity()) {
                        objects.getReceiveDetail().get(getAdapterPosition()).setQuantity(quantity);
                    }
                }
            });
        }
    }
}
