package com.jsi.mbrana.Modules.Receive.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveDetailModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;
import com.jsi.mbrana.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sololia on 6/17/2016.
 */

public class ConfirmReceiveAdapter extends RecyclerView.Adapter<ConfirmReceiveAdapter.PrintGRNFMyViewHolder> {
    private ReceiveModel receiveModel = null;

    public ConfirmReceiveAdapter(ReceiveModel receiveModel) {
        this.receiveModel = receiveModel;
    }

    @Override
    public PrintGRNFMyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_print_grnf_receitp_invoice, parent, false);
        return new PrintGRNFMyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PrintGRNFMyViewHolder holder, int position) {
        ReceiveDetailModel model = receiveModel.getReceiveDetail().get(position);

        holder.ROWNO.setText((position + 1) + "");
        holder.Invoiced.setText("Invoiced");
        holder.productCN.setText(model.getItemName());
        holder.Received.setText((model.getQuantity()) + "");
        holder.Unit.setText(model.getUnitOfIssue());
        holder.Batch.setText(model.getBatchNumber());
        holder.VVM.setText(model.getVvmId() + "");
        holder.Manufacturer.setText(model.getManufacturerName());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date expiry = null;
        try {
            expiry = simpleDateFormat.parse(model.getExpiryDate().replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.Expiry.setText(new SimpleDateFormat("MM-yyyy").format(expiry));
    }

    @Override
    public int getItemCount() {
        return receiveModel.getReceiveDetail().size();
    }

    class PrintGRNFMyViewHolder extends RecyclerView.ViewHolder {
        TextView productCN, Invoiced, Received, Unit, Batch, Expiry, Manufacturer, VVM, ROWNO;

        PrintGRNFMyViewHolder(View itemView) {
            super(itemView);
            productCN = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowProductCN);
            Invoiced = (TextView) itemView.findViewById(R.id.inputPrintGRNFRIMaterInput_TableRowInvoicedQ);
            Received = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowRecievedQuanitity);
            Unit = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowUnit);
            Batch = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowRecievedBatch);
            Expiry = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowExpiry);
            Manufacturer = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowManufacturer);
            VVM = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowVVM);
            ROWNO = (TextView) itemView.findViewById(R.id.PrintGRNFRIMaterInput_TableRowNo);
        }
    }
}