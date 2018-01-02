package com.jsi.mbrana.Modules.Receive.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsi.mbrana.Modules.Receive.Helper.Status.StatusHelperModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;
import com.jsi.mbrana.R;

import java.util.List;

/**
 * Created by Sololia on 6/17/2016.
 */

public class ReceiptMenuAdapter extends RecyclerView.Adapter<ReceiptMenuAdapter.MyViewHolder> {
    private List<ReceiveModel> objects;

    public ReceiptMenuAdapter(List<ReceiveModel> objects) {
        this.objects = objects;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_viewreceiptinvoices_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ReceiveModel model = objects.get(position);

        holder.ReceiptID.setText("Ref No " + model.getId());
        holder.tvFirstLetter.setText(StatusHelperModel.ReceiveStatus.getReceiveStatusFirstLetter(model.getReceiveStatusCode()));
        holder.tvFirstLetter.setBackgroundResource(StatusHelperModel.ReceiveStatus.getReceiveStatusBackground(model.getReceiveStatusCode()));
        holder.tv_status.setText(StatusHelperModel.ReceiveStatus.getReceiveStatusName(model.getReceiveStatusCode()) + "");

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
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ReceiptID, tvFirstLetter, tv_status, ReceiptDateDetail, NoOfItemsDetail;

        MyViewHolder(View itemView) {
            super(itemView);
            ReceiptID = (TextView) itemView.findViewById(R.id.ViewRIDetail_TableRowReceiptID);
            tvFirstLetter = (TextView) itemView.findViewById(R.id.receiptStatus_FirstLetter);
            tv_status = (TextView) itemView.findViewById(R.id.ViewRecDetail_TableRowStatus);
            ReceiptDateDetail = (TextView) itemView.findViewById(R.id.ViewRecDetail_TableRowPrintedDate);
            NoOfItemsDetail = (TextView) itemView.findViewById(R.id.ViewRecDetail_TableGridRowsNoOfItems);
        }
    }
}