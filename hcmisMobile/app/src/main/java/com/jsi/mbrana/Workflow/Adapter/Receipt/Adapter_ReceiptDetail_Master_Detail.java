package com.jsi.mbrana.Workflow.Adapter.Receipt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Models.ReceiveInvoiceMastersDetailModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;

/**
 * Created by Sololia on 6/15/2016.
 */
public class Adapter_ReceiptDetail_Master_Detail extends RecyclerView.Adapter {
    ArrayList<ReceiveInvoiceMastersDetailModel> objects;
    Context context;
    RecyclerView rView;
    Constants cons = new Constants();

    public Adapter_ReceiptDetail_Master_Detail(Context context, ArrayList<ReceiveInvoiceMastersDetailModel> objects, RecyclerView rView) {
        this.rView = rView;
        this.objects = objects;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_receipt_invoice_master_detailvalues, parent, false);
        return new ViewHolder(View);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ReceiveInvoiceMastersDetailModel item = objects.get(position);
        int index = objects.indexOf(item);

        ViewHolder itemview = (ViewHolder) holder;
        itemview.ProductCN.setText(item.getProductCN());
        itemview.BatchNo.setText((item.getBatchNo()));
        itemview.InvoiceQ.setText((item.getInvoicedQuantity()) + "");
        itemview.RecievedQuanitity.setText((item.getQuantity()) + "");
        itemview.Expiry.setText((item.getExpDate()));

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
        TextView ProductCN;
        EditText BatchNo;
        EditText InvoiceQ;
        EditText RecievedQuanitity;
        EditText Expiry;

        public ViewHolder(View view) {
            super(view);
            ProductCN = (TextView) view.findViewById(R.id.ViewRecMaterDetail_TableRowProductCN);
            BatchNo = (EditText) view.findViewById(R.id.ViewRecMaterDetail_TableRowBatchNo);
            InvoiceQ = (EditText) view.findViewById(R.id.ViewRecMaterDetail_TableRowInvoicedQuantity);
            RecievedQuanitity = (EditText) view.findViewById(R.id.ViewRecMaterDetail_TableRowRecievedQuanitity);
            Expiry = (EditText) view.findViewById(R.id.ViewRecMaterDetail_TableRowExpiry);

            BatchNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            InvoiceQ.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            RecievedQuanitity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            Expiry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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